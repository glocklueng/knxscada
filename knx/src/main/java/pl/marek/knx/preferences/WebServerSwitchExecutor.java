package pl.marek.knx.preferences;

import pl.marek.knx.R;
import pl.marek.knx.WebServerService;
import pl.marek.knx.interfaces.StateSender;
import pl.marek.knx.interfaces.WebServerStateListener;
import pl.marek.knx.log.LogTags;
import pl.marek.knx.receivers.WebServerStateBroadcastReceiver;
import pl.marek.knx.web.WebServerState;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class WebServerSwitchExecutor extends SwitchExecutor implements WebServerStateListener{
	
	private WebServerStateBroadcastReceiver webServerStateReceiver;
	private boolean receiverRegistered = false;

	public WebServerSwitchExecutor(Context context, SwitchStateListener listener) {
		super(context, listener);
		webServerStateReceiver = new WebServerStateBroadcastReceiver(this);
	}

	@Override
	public void start() {
		Log.d(LogTags.WEB_SERVER, "Starting WWW");
		context.startService(new Intent(context, WebServerService.class));
	}

	@Override
	public void stop() {
		Log.d(LogTags.WEB_SERVER, "Stopping WWW");
		context.stopService(new Intent(context, WebServerService.class));
	}

	@Override
	public void registerState() {
		if(!receiverRegistered){
			context.registerReceiver(webServerStateReceiver, new IntentFilter(StateSender.WEBSERVER_STATE_CHANGED));
		}
		receiverRegistered = true;
	}

	@Override
	public void unregisterState() {
		if(receiverRegistered)
			context.unregisterReceiver(webServerStateReceiver);
		receiverRegistered = false;
	}
	
	@Override
	public SwitchState getSwitchState() {
		WebServerState state = WebServerState.valueOf(preferences.getString(context.getString(R.string.webserver_state_key), WebServerState.UNKNOWN.name()));
		return getSwitchState(state);
	}
	
	private SwitchState getSwitchState(WebServerState state){
		switch(state){
			case STARTED:
				return SwitchState.ON;
			case STARTING:
			case DEPLOYING:
				return SwitchState.OFF_ON_CHANGE;
			case STOPPING:
				return SwitchState.ON_OFF_CHANGE;
			default:
				return SwitchState.OFF;
		}
	}
	
	@Override
	public void webServerStateChanged(WebServerState state) {
		setState(context.getString(R.string.webserver_state_key), state.name());
		listener.setSwitchState(getSwitchState(state));
	}
}
