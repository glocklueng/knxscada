package pl.marek.knx.interfaces;

import pl.marek.knx.connection.ConnectionState;

public interface KNXConnectionStateListener{
	public void connectionStateChanged(ConnectionState state);
}
