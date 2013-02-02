package pl.marek.knx.preferences;

import android.content.Context;

public class WebServerSwitchExecutor extends SwitchExecutor{

	public WebServerSwitchExecutor(Context context) {
		super(context);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSwitchState(boolean state) {
		application.setWebServerStarted(state);
	}

	@Override
	public boolean getSwitchState() {
		return application.isWebServerStarted();
	}

}
