package pl.marek.knx.interfaces;

public interface StateSender {
	public static final String GET_CONNECTION_STATE = "pl.marek.knx.get_connection_state";
	public static final String CONNECTION_STATE = "pl.marek.knx.connection_state";
	public static final String CONNECTION_STATE_CHANGED = "pl.marek.knx.connection_state_changed";
	public static final String GET_WEBSERVER_STATE = "pl.marek.knx.get_webserver_state";
	public static final String WEBSERVER_STATE = "pl.marek.knx.webserver_state";
	public static final String WEBSERVER_STATE_CHANGED = "pl.marek.knx.webserver_state_changed";

	public void sendState();
}
