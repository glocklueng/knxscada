package pl.marek.knx.receivers;

import pl.marek.knx.connection.ConnectionState;
import pl.marek.knx.interfaces.KNXConnectionStateListener;
import pl.marek.knx.interfaces.StateSender;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KNXConnectionStateBroadcastReceiver extends BroadcastReceiver{
	
	private KNXConnectionStateListener connectionStateReceiver;
	
	public KNXConnectionStateBroadcastReceiver(KNXConnectionStateListener connectionStateReceiver){
		this.connectionStateReceiver = connectionStateReceiver;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(StateSender.CONNECTION_STATE_CHANGED)){
			connectionStateReceiver.connectionStateChanged((ConnectionState)intent.getSerializableExtra(StateSender.CONNECTION_STATE));
		}
	}
}
