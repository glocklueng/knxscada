package pl.marek.knx.preferences;

import pl.marek.knx.MainApplication;
import pl.marek.knx.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public abstract class SwitchExecutor {
	
	protected Context context;
	protected MainApplication application;
	protected SharedPreferences preferences;
	
	public SwitchExecutor(Context context){
		this.context = context;
		application = (MainApplication)context.getApplicationContext();
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public abstract void start();
	public abstract void stop();
	public abstract boolean getSwitchState();
	public abstract void updateSwitchState(boolean state);
	
	public static SwitchExecutor create(Context context, String type){
        SwitchExecutor executor = null;
		if (context.getString(R.string.knx_connection_toggle).equals(type)) {
			executor = new KNXConnectionSwitchExecutor(context);
        } else if (context.getString(R.string.webserver_toggle).equals(type)) {
        	executor = new WebServerSwitchExecutor(context);
        }
		return executor;
	}
	
	protected void setSwitchState(String name, boolean value){
		Editor editor = preferences.edit();
		editor.putBoolean(name, value);
		editor.commit();
	}
}