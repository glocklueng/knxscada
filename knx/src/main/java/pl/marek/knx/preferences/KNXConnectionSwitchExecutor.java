package pl.marek.knx.preferences;

import pl.marek.knx.KNXConnectionService;
import pl.marek.knx.R;
import pl.marek.knx.connection.ConnectionState;
import pl.marek.knx.interfaces.KNXConnectionStateListener;
import pl.marek.knx.interfaces.StateSender;
import pl.marek.knx.log.LogTags;
import pl.marek.knx.receivers.KNXConnectionStateBroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class KNXConnectionSwitchExecutor extends SwitchExecutor implements KNXConnectionStateListener{
	
	private KNXConnectionStateBroadcastReceiver connectionStateReceiver;
	private boolean receiverRegistered = false;
	
	public KNXConnectionSwitchExecutor(Context context, SwitchStateListener listener) {
		super(context, listener);
		connectionStateReceiver = new KNXConnectionStateBroadcastReceiver(this);
	}

	@Override
	public void start() {
		Log.d(LogTags.KNX_CONNECTION, "Starting KNX");
		context.startService(new Intent(context, KNXConnectionService.class));
	}

	@Override
	public void stop() {
		Log.d(LogTags.KNX_CONNECTION, "Stopping KNX");
		context.stopService(new Intent(context, KNXConnectionService.class));
	}
	
	@Override
	public void registerState() {
		if(!receiverRegistered)
			context.registerReceiver(connectionStateReceiver, new IntentFilter(StateSender.CONNECTION_STATE_CHANGED));
		receiverRegistered = true;
	}

	@Override
	public void unregisterState() {
		if(receiverRegistered)
			context.unregisterReceiver(connectionStateReceiver);
		receiverRegistered = false;
	}

	@Override
	public SwitchState getSwitchState() {
		ConnectionState state = ConnectionState.valueOf(preferences.getString(context.getString(R.string.connection_state_key), ConnectionState.UNKNOWN.name()));
		return getSwitchState(state);
	}
	
	private SwitchState getSwitchState(ConnectionState state){
		switch(state){
			case CONNECTED:
				return SwitchState.ON;
			case CONNECTING:
				return SwitchState.OFF_ON_CHANGE;
			case DISCONNECTING:
				return SwitchState.ON_OFF_CHANGE;
			default:
				return SwitchState.OFF;
		}
	}
	
	@Override
	public void connectionStateChanged(ConnectionState state) {
		setState(context.getString(R.string.connection_state_key), state.name());
		listener.setSwitchState(getSwitchState(state));
	}
}
