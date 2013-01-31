package pl.marek.knx.interfaces;

import pl.marek.knx.web.WebServerState;

public interface WebServerStateListener {
	public void webServerStateChanged(WebServerState state);
}
