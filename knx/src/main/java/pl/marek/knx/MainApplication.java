package pl.marek.knx;

import android.app.Application;

public class MainApplication extends Application{
	
	private boolean knxConnected;
	private boolean webServerStarted;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		knxConnected = false;
		webServerStarted = false;
	}

	public boolean isKnxConnected() {
		return knxConnected;
	}

	public void setKnxConnected(boolean knxConnected) {
		this.knxConnected = knxConnected;
	}

	public boolean isWebServerStarted() {
		return webServerStarted;
	}

	public void setWebServerStarted(boolean webServerStarted) {
		this.webServerStarted = webServerStarted;
	}
}
