package pl.marek.knx.receivers;

import pl.marek.knx.interfaces.WebServerController;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WebServerControlReceiver extends BroadcastReceiver{
	
	private WebServerController controller;
	
	public WebServerControlReceiver(WebServerController controller){
		this.controller = controller;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(WebServerController.START_WEBSERVER)){
			controller.start();	
		} else if(intent.getAction().equals(WebServerController.STOP_WEBSERVER)){
			controller.stop();
		} else if(intent.getAction().equals(WebServerController.DEPLOY_WEBAPP)){
			controller.deployWebApp();
		} else if(intent.getAction().equals(WebServerController.UNDEPLOY_WEBAPP)){
			controller.undeployWebApp();
		}
	}
}
