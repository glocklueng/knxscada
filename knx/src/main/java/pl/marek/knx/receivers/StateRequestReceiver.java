package pl.marek.knx.receivers;

import pl.marek.knx.interfaces.StateSender;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StateRequestReceiver extends BroadcastReceiver{
	
	private StateSender sender;
	
	public StateRequestReceiver(StateSender sender){
		this.sender = sender;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(StateSender.GET_CONNECTION_STATE) ||
		   intent.getAction().equals(StateSender.GET_WEBSERVER_STATE)){
			sender.sendState();
		}
	}
}