package pl.marek.knx.web;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

import dalvik.system.DexClassLoader;

import pl.marek.knx.R;
import pl.marek.knx.log.LogTags;
import pl.marek.knx.utils.FileUtils;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class WebServer {
	
	public static final String WEBAPP_DIRECTORY = "webapp";
	public static final String WEBAPP_TMP_DIRECTORY = "webapp_tmp";
	public static final String WEBAPP_DESCRIPTOR = "/WEB-INF/web.xml";
	public static final String DEX_JAR_PATH = "/WEB-INF/lib/classes.jar";
	public static final String WEBAPP_LIB_DIRECTORY = "/WEB-INF/lib/";
	public static final String WEBAPP_CONTEXT_PATH = "/";
	public static final String KEYSTORE_FILE="/WEB-INF/etc/keystore";
	
	public static final int SERVER_SHUTDOWN_TIMEOUT = 500; // In miliseconds
	
	private WebServerSettings settings;
	private Server webServer;
	private Context appContext;
	private LifeCycle.Listener stateListener;
	
	public WebServer(WebServerSettings settings, Context appContext, LifeCycle.Listener stateListener){
		this.settings = settings;
		this.appContext = appContext;
		this.stateListener = stateListener;
	}
	
	public void start() throws Exception{
		
	      webServer = new Server(settings.getPort());
	      webServer.setHandler(createHandlers());
	      webServer.addLifeCycleListener(stateListener);
	      if(settings.isUseSSL()){
	    	  webServer.addConnector(createSSLConnector());
	      }
	      webServer.start();
	}
	
	public void stop() throws Exception{
		webServer.setGracefulShutdown(SERVER_SHUTDOWN_TIMEOUT);
		for(Connector connector: webServer.getConnectors()){
			connector.close();
		}
	}
	
	private Handler createHandlers(){
		HandlerList handlers = new HandlerList();
	    handlers.setHandlers(new Handler[]{createWebAppContext(), createResourceHandler(), new DefaultHandler()});
	    return handlers;
	}
	
	private WebAppContext createWebAppContext(){
				
      	WebAppContext webContext = new WebAppContext();
        webContext.setContextPath(WEBAPP_CONTEXT_PATH);
        webContext.setTempDirectory(getWebAppTmpDirectory());
        webContext.setClassLoader(getDexClassLoader());
        webContext.setParentLoaderPriority(true);
        webContext.setDescriptor(new File(getWebAppDirectoryPath(), WEBAPP_DESCRIPTOR).getAbsolutePath());
        webContext.setResourceBase(getWebAppDirectoryPath());
		return webContext;
	}
	
	private ResourceHandler createResourceHandler(){
		ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        resourceHandler.setResourceBase(".");
        return resourceHandler;
	}
	
	private Connector createSSLConnector(){
		
	    SslContextFactory cf = new SslContextFactory(getKeystoreFile().getAbsolutePath());

	    try {
	    	cf.setKeyStoreType("BKS");
	    	cf.setKeyStorePassword(appContext.getString(R.string.jetty_keystore_password));
		    
		    cf.setNeedClientAuth(false);
		    cf.setTrustStore(getKeystoreFile().getAbsolutePath());
		    cf.setTrustStoreType("BKS");
		    cf.setKeyManagerPassword(appContext.getString(R.string.jetty_keymanager_password));
		    cf.setTrustStorePassword(appContext.getString(R.string.jetty_truststore_password));
		    
		} catch (Exception e) {
			Log.d(LogTags.WEB_SERVER, e.getMessage());
		}
	    SslSelectChannelConnector sslConnector = new SslSelectChannelConnector(cf);
	    sslConnector.setPort(settings.getSSLPort());
	    sslConnector.setConfidentialPort(settings.getSSLPort());

		return sslConnector;
	}
	
	private DexClassLoader getDexClassLoader(){
		DexClassLoader loader = new DexClassLoader(
				new File(getWebAppDirectoryPath(), DEX_JAR_PATH).getAbsolutePath(),
				getWebAppTmpDirectory().getAbsolutePath(), 
				new File(getWebAppDirectoryPath(), WEBAPP_LIB_DIRECTORY).getAbsolutePath(), 
				getClass().getClassLoader());
		return loader;
	}
	
	public void deployWebApp() throws IOException{
		AssetManager assets = appContext.getAssets();
		FileUtils.copy(assets, "webapp", appContext.getFilesDir().getAbsolutePath());
	}
	
	public void undeployWebApp(){
		FileUtils.delete(getWebAppDirectoryPath());
	}
	
	public boolean isDeployed(){
		File webapp = getWebAppDirectory();
		return webapp.exists();
	}
		
	private File getWebAppDirectory(){
		return new File(appContext.getFilesDir().getAbsoluteFile(), WEBAPP_DIRECTORY);
	}
	
	private String getWebAppDirectoryPath(){
		return getWebAppDirectory().getAbsolutePath();
	}
	
	private File getWebAppTmpDirectory(){
		File tmpDir = new File(appContext.getCacheDir(), WEBAPP_TMP_DIRECTORY);
		if(!tmpDir.exists())
			tmpDir.mkdirs();
		return tmpDir;
	}
	
	private File getKeystoreFile(){
		return new File(getWebAppDirectoryPath(),KEYSTORE_FILE);
	}
}