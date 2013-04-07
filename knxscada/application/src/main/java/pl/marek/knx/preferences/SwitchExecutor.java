package pl.marek.knx.preferences;

import pl.marek.knx.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public abstract class SwitchExecutor {
	
	protected Context context;
	protected SharedPreferences preferences;
	protected SwitchStateListener listener;
	
	public SwitchExecutor(Context context, SwitchStateListener listener){
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.listener = listener;
	}
	
	public abstract void start();
	public abstract void stop();
	public abstract void registerState();
	public abstract void unregisterState();
	public abstract SwitchState getSwitchState();
	
	public static SwitchExecutor create(Context context, String type, SwitchStateListener listener){
        SwitchExecutor executor = null;
		if (context.getString(R.string.knx_connection_toggle).equals(type)) {
			executor = new KNXConnectionSwitchExecutor(context, listener);
        } else if (context.getString(R.string.webserver_toggle).equals(type)) {
        	executor = new WebServerSwitchExecutor(context, listener);
        }
		return executor;
	}
	
	protected void setState(String switchKey, String value){
		Editor editor = preferences.edit();
		editor.putString(switchKey, value);
		editor.commit();
	}
}