package pl.marek.knx;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.protocol.https.HttpsMapper;
import org.apache.wicket.protocol.https.Scheme;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.component.IRequestablePage;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.interfaces.AuthenticatedWebPage;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.interfaces.KNXTelegramListener;
import pl.marek.knx.pages.*;

public class KNXWebApplication extends WebApplication{
	
    private static final String CONTENT_RESOLVER_ATTRIBUTE = "pl.marek.knx.contentResolver";
    private static final String ANDROID_CONTEXT_ATTRIBUTE = "pl.marek.knx.context";
    private static final String DEF_PREFERENCE_FILE_ATTRIBUTE = "pl.marek.knx.defPreferenceFile";
    
    private DatabaseManager dbManager;
    private TelegramBroadcastReceiver telegramReceiver;
	
	@Override
	public Class<? extends Page> getHomePage() {
		return Index.class;
	}

	@Override
	protected void init() {
		super.init();
		setWebPagesFinder();
		setAuthorizationStrategy();
		mountPages();
		mountResources();
		
		if(getAndroidContext() != null){
			configureRootRequestMapper();
			registerTelegramReceiver();
			dbManager = new DatabaseManagerImpl(getAndroidContext());
		} else{
			dbManager = new DBManager();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		if(getAndroidContext() != null){
			unregisterTelegramReceiver();
		}
		if(dbManager != null){
			dbManager.close();
		}
		super.onDestroy();
	}
	
	public DatabaseManager getDatabaseManager(){
		return dbManager;
	}
	
	public ContentResolver getAndroidContentResolver(){
		ContentResolver resolver = (ContentResolver)getServletContext().getAttribute(CONTENT_RESOLVER_ATTRIBUTE);
		return resolver;
	}
	
	public Context getAndroidContext(){
	    Context context  = (Context)getServletContext().getAttribute(ANDROID_CONTEXT_ATTRIBUTE);
	    return context;
	}
	
	public String getDefaultPreferenceFile(){
		String prefFile = (String)getServletContext().getAttribute(DEF_PREFERENCE_FILE_ATTRIBUTE);
		return prefFile;
	}
	
	private int getPort(){
		SharedPreferences preferences = getAndroidContext().getSharedPreferences(getDefaultPreferenceFile(), Context.MODE_MULTI_PROCESS);
	    return preferences.getInt("webserver_port", 8080);
	}
	
	private int getSSLPort(){
		SharedPreferences preferences = getAndroidContext().getSharedPreferences(getDefaultPreferenceFile(), Context.MODE_MULTI_PROCESS);
	    return preferences.getInt("webserver_ssl_port", 8443);
	}
	
	private boolean isUseSsl(){
		SharedPreferences preferences = getAndroidContext().getSharedPreferences(getDefaultPreferenceFile(), Context.MODE_MULTI_PROCESS);
	    return preferences.getBoolean("use_ssl", false);
	}
	
	private void configureRootRequestMapper(){
		setRootRequestMapper(new HttpsMapper(getRootRequestMapper(), new HttpsConfig(getPort(), getSSLPort())){
			@Override
			protected Scheme getDesiredSchemeFor(Class<? extends IRequestablePage> pageClass) {
				if(isUseSsl()){
					return super.getDesiredSchemeFor(pageClass);
				}else{
					return Scheme.HTTP;
				}
			}
		});
	}
	
	private void registerTelegramReceiver(){
		telegramReceiver = new TelegramBroadcastReceiver();
		getAndroidContext().registerReceiver(telegramReceiver, new IntentFilter(KNXTelegramListener.TELEGRAM_RECEIVED));	
	}
	
	private void unregisterTelegramReceiver(){
		getAndroidContext().unregisterReceiver(telegramReceiver);	
	}
	
	public void addTelegramListener(KNXTelegramListener listener){
		if(telegramReceiver != null){
			telegramReceiver.addTelegramListener(listener);
		}
	}
	
	public void removeTelegramListener(KNXTelegramListener listener){
		if(telegramReceiver != null){
			telegramReceiver.removeTelegramListener(listener);
		}
	}
	
	public void removeAllTelegramListeners(){
		if(telegramReceiver != null){
			telegramReceiver.removeAllListeners();
		}
	}
	
	private void setWebPagesFinder() {
		WebPagesFinder finder = new WebPagesFinder(getServletContext());
		getResourceSettings().getResourceFinders().add(finder);
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new SignInSession(request);
	}

	public void setAuthorizationStrategy() {
		getSecuritySettings().setAuthorizationStrategy(new AuthorizationStrategy());
	}

	private void mountPages() {
		mountPage("signin", SignIn.class);
		mountPage("signout", SignOut.class);

	}
	
	private void mountResources(){
//		 getSharedResources().add("image", new ExternalImageResource("/home/marek/Magisterka/Grafika/WWW/logo.png"));
//	     mountResource("image", new SharedResourceReference("downloads"));
	   
	}
	
	private class AuthorizationStrategy implements IAuthorizationStrategy {

		public boolean isActionAuthorized(Component component, Action action) {
			return true;
		}

		public <T extends IRequestableComponent> boolean isInstantiationAuthorized(Class<T> componentClass) {

			if (AuthenticatedWebPage.class.isAssignableFrom(componentClass)) {
				if (((SignInSession) Session.get()).isSignedIn()) {
					return true;
				}
				throw new RestartResponseAtInterceptPageException(SignIn.class);
			}
			return true;
		}
	}
}
