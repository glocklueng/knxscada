package pl.marek.knx;

import java.io.IOException;

import org.eclipse.jetty.util.component.LifeCycle;
import pl.marek.knx.interfaces.StateSender;
import pl.marek.knx.interfaces.WebServerController;
import pl.marek.knx.receivers.StateRequestReceiver;
import pl.marek.knx.receivers.WebServerControlReceiver;
import pl.marek.knx.log.JettyLogger;
import pl.marek.knx.log.LogTags;
import pl.marek.knx.web.WebServer;
import pl.marek.knx.web.WebServerSettings;
import pl.marek.knx.web.WebServerState;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class WebServerService extends Service implements StateSender, WebServerController, LifeCycle.Listener{
	
	public static final int SERVICE_ID = 10001;
	
	private NotificationManager notificationManager;
	private PowerManager.WakeLock wakeLock;
	
	private WebServer server;
	private WebServerState state;
	private WebServerSettings settings;
	private StateRequestReceiver stateRequestReceiver;
	private WebServerControlReceiver controlReceiver;
	
    static {
        System.setProperty("org.eclipse.jetty.xml.XmlParser.Validating","false");
        System.setProperty("org.eclipse.jetty.util.log.class","pl.marek.knx.log.JettyLogger");
        org.eclipse.jetty.util.log.Log.setLog(new JettyLogger());
    }
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Notification notification = notificationManager.createWebServerServiceNotification();
		startForeground(SERVICE_ID, notification);
		startWebServer();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void startWebServer(){
		server = new WebServer(settings, this, this);
		try{
			//TODO Remove
			server.undeployWebApp();
			
			if(!server.isDeployed()){
				setState(WebServerState.DEPLOYING);
				server.deployWebApp();
			}
			server.start();
		} catch(Exception ex){
			Log.w(LogTags.WEB_SERVER, ex.getMessage());
			stopSelf();
		}	
	}
	
	private void stopWebServer(){
		setState(WebServerState.STOPPING);
		try {
			server.stop();
			setState(WebServerState.STOPPED);
		} catch (Exception e) {
			Log.w(LogTags.WEB_SERVER, e.getMessage());
		}
	}
	
	private void setWakeLock(){
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getString(R.string.application_name));
        wakeLock.acquire();
	}
	
	private void clearWakeLock(){
		wakeLock.release();
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		settings = new WebServerSettings(this);
		notificationManager = new NotificationManager(this);
		
		stateRequestReceiver = new StateRequestReceiver(this);
		registerReceiver(stateRequestReceiver, new IntentFilter(GET_WEBSERVER_STATE));
		
		controlReceiver = new WebServerControlReceiver(this);
		IntentFilter controlFilter = new IntentFilter();
		controlFilter.addAction(WebServerController.START_WEBSERVER);
		controlFilter.addAction(WebServerController.STOP_WEBSERVER);
		controlFilter.addAction(WebServerController.DEPLOY_WEBAPP);
		controlFilter.addAction(WebServerController.UNDEPLOY_WEBAPP);
		registerReceiver(controlReceiver, controlFilter);
		
		setWakeLock();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(stateRequestReceiver);
		unregisterReceiver(controlReceiver);
		stopWebServer();
		clearWakeLock();
		setStoppedStateInPreferences();
		stopForeground(true);		
	}
	
	private void setStoppedStateInPreferences(){
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = p.edit();
		editor.putString(getString(R.string.webserver_state_key), WebServerState.STOPPED.name());
		editor.commit();
	}
	
	private void setState(WebServerState state){		
		this.state = state;
		state.showToast(this);
		sendState();
	}
	
	// StateSender implemented method
	public void sendState() {
		Intent intent = new Intent(StateSender.WEBSERVER_STATE_CHANGED);
		intent.putExtra(WEBSERVER_STATE, state);
		sendBroadcast(intent);	
	}
	
	// Jetty Server state listener methods
	public void lifeCycleStarting(LifeCycle event) {
		Log.d(LogTags.WEB_SERVER, getString(R.string.webserver_service_starting));
		setState(WebServerState.STARTING);
	}

	public void lifeCycleStarted(LifeCycle event) {
		Log.d(LogTags.WEB_SERVER, getString(R.string.webserver_service_started));
		setState(WebServerState.STARTED);
	}

	public void lifeCycleFailure(LifeCycle event, Throwable cause) {
		Log.e(LogTags.WEB_SERVER, getString(R.string.webserver_service_failed));
		Log.e(LogTags.WEB_SERVER, cause.getMessage());
		setState(WebServerState.FAILED);
	}

	public void lifeCycleStopping(LifeCycle event) {
		Log.d(LogTags.WEB_SERVER, getString(R.string.webserver_service_stopping));
		setState(WebServerState.STOPPING);
	}

	public void lifeCycleStopped(LifeCycle event) {
		Log.d(LogTags.WEB_SERVER, getString(R.string.webserver_service_stopped));
		setState(WebServerState.STOPPED);
	}
	
	
	//WebServer Controller implemented methods
	public void start() {
		if(state != WebServerState.STARTED){
			startWebServer();
		}
	}

	public void stop() {
		if(state != WebServerState.STOPPED){
			stopWebServer();
		}
	}

	public void deployWebApp() {
		if(!server.isDeployed()){
			try {
				server.deployWebApp();
			} catch (IOException e) {
				Log.d(LogTags.WEB_SERVER, e.getMessage());
			}
		}
	}

	public void undeployWebApp() {
		if(state != WebServerState.STARTED){
			server.undeployWebApp();
		}
	}
}
