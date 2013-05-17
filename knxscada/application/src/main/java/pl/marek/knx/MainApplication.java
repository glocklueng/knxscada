package pl.marek.knx;

import pl.marek.knx.connection.ConnectionState;
import pl.marek.knx.interfaces.KNXConnectionStateListener;
import pl.marek.knx.interfaces.StateSender;
import pl.marek.knx.interfaces.WebServerStateListener;
import pl.marek.knx.receivers.KNXConnectionStateBroadcastReceiver;
import pl.marek.knx.receivers.WebServerStateBroadcastReceiver;
import pl.marek.knx.web.WebServerState;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

public class MainApplication extends Application implements KNXConnectionStateListener, WebServerStateListener{
	
	@Override
	public void onCreate() {
		super.onCreate();
				
		registerReceiver(new WebServerStateBroadcastReceiver(this), new IntentFilter(StateSender.WEBSERVER_STATE_CHANGED));
		registerReceiver(new KNXConnectionStateBroadcastReceiver(this), new IntentFilter(StateSender.CONNECTION_STATE_CHANGED));
		
		setWebServerState(WebServerState.UNKNOWN);
		setKNXConnectionState(ConnectionState.UNKNOWN);
		
		sendBroadcast(new Intent(StateSender.GET_WEBSERVER_STATE));
		sendBroadcast(new Intent(StateSender.GET_CONNECTION_STATE));
	}

	public ConnectionState getKNXConnectionState(){
		return ConnectionState.valueOf(getPreferences().getString(getApplicationContext().getString(R.string.connection_state_key), ConnectionState.UNKNOWN.name()));
	}
	
	public void setKNXConnectionState(ConnectionState state){
		setStateInPreferences(getApplicationContext().getString(R.string.connection_state_key), state.name());
	}

	public WebServerState getWebServerState(){
		 return WebServerState.valueOf(getPreferences().getString(getApplicationContext().getString(R.string.webserver_state_key), WebServerState.UNKNOWN.name()));
	}
	
	public void setWebServerState(WebServerState state){
		setStateInPreferences(getApplicationContext().getString(R.string.webserver_state_key), state.name());
	}
	
	public SharedPreferences getPreferences(){
		return PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	}
	
	public void setStateInPreferences(String switchKey, String value){
		Editor editor = getPreferences().edit();
		editor.putString(switchKey, value);
		editor.commit(); 
	}
	
	public void webServerStateChanged(WebServerState state) {
		setWebServerState(state);
	}

	public void connectionStateChanged(ConnectionState state) {
		setKNXConnectionState(state);
	}
	
	public void restartApplication(){
		Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
	}
	
	public int getScreenSize(){
		int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		return screenSize;
	}
	
}