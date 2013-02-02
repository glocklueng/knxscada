package pl.marek.knx.preferences;

import pl.marek.knx.MainApplication;
import pl.marek.knx.R;
import android.content.Context;

public abstract class SwitchExecutor {
	
	protected Context context;
	protected MainApplication application;
	
	public SwitchExecutor(Context context){
		this.context = context;
		application = (MainApplication)context.getApplicationContext();
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
}