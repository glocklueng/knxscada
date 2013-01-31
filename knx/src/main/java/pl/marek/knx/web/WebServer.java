package pl.marek.knx.web;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.ssl.SslSelectChannelConnector;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

import dalvik.system.DexClassLoader;

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
	    handlers.setHandlers(new Handler[]{createWebAppContext(), createResourceHandler()});
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
		SslSelectChannelConnector sslConnector = new SslSelectChannelConnector();
	    sslConnector.setPort(settings.getSSLPort());
	    SslContextFactory cf = sslConnector.getSslContextFactory();
	    
	    //TODO Implement usage of keystore etc.

//		  Kod z aplikacji i-Jetty	    
//        SslContextFactory sslContextFactory = new SslContextFactory();
//        sslContextFactory.setKeyStore(_keystoreFile);
//        sslContextFactory.setTrustStore(_truststoreFile);
//        sslContextFactory.setKeyStorePassword(_keystorePassword);
//        sslContextFactory.setKeyManagerPassword(_keymgrPassword);
//        sslContextFactory.setKeyStoreType("bks");
//        sslContextFactory.setTrustStorePassword(_truststorePassword);
//        sslContextFactory.setTrustStoreType("bks");
	    
	    try {
	    	KeyStore keyStore = KeyStore.getInstance("JKS");
	    	
			cf.setKeyStore(keyStore);
		    cf.setKeyStorePassword("OBF:1vny1zlo1x8e1vnw1vn61x8g1zlu1vn4");
		    cf.setKeyManagerPassword("OBF:1u2u1wml1z7s1z7a1wnl1u2g");
		    
		} catch (KeyStoreException e) {
			Log.d(LogTags.WEB_SERVER, e.getMessage());
		}

		
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
}