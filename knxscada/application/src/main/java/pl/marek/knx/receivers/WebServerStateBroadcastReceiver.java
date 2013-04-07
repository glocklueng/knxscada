package pl.marek.knx.receivers;

import pl.marek.knx.interfaces.StateSender;
import pl.marek.knx.interfaces.WebServerStateListener;
import pl.marek.knx.web.WebServerState;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WebServerStateBroadcastReceiver extends BroadcastReceiver{
	private WebServerStateListener webServerStateReceiver;
	
	public WebServerStateBroadcastReceiver(WebServerStateListener webServerStateReceiver){
		this.webServerStateReceiver = webServerStateReceiver;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(StateSender.WEBSERVER_STATE_CHANGED)){
			webServerStateReceiver.webServerStateChanged((WebServerState)intent.getSerializableExtra(StateSender.WEBSERVER_STATE));
		}
	}
}
