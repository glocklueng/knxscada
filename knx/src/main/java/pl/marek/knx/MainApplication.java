package pl.marek.knx;

import pl.marek.knx.connection.ConnectionState;
import pl.marek.knx.web.WebServerState;
import android.app.Application;

public class MainApplication extends Application{
	
	private ConnectionState knxConnectionState;
	private WebServerState webServerState;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		knxConnectionState = ConnectionState.UNKNOWN;
		webServerState = WebServerState.UNKNOWN;
	}

	public ConnectionState getKNXConnectionState(){
		return knxConnectionState;
	}
	
	public void setKNXConnectionState(ConnectionState state){
		knxConnectionState = state;
	}

	public WebServerState getWebServerState(){
		return webServerState;
	}
	
	public void setWebServerState(WebServerState state){
		webServerState = state;
	}
}
