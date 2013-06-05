package pl.marek.knx.websocket;

import java.io.IOException;

import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;

import android.util.Log;

public class CommunicationSocket implements OnTextMessage{
	
	private Connection connection;
	private CommunicationSocketListener listener;
	
	private String ip;
	private int port;
	
	public CommunicationSocket(CommunicationSocketListener listener, String ip, int port){
		this.listener = listener;
		this.ip = ip;
		this.port = port;
	}

	public Connection getConnection(){
		return connection;
	}
	
	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	@Override
	public void onOpen(Connection connection) {
		this.connection = connection;
		connection.setMaxIdleTime(3600000);
		if(listener != null){
			listener.onOpen(this);
		}
	}
	
	@Override
	public void onClose(int closeCode, String message) {
		if(listener != null){
			listener.onClose(this);
		}
	}

	@Override
	public void onMessage(String data) {
		if(listener != null){
			listener.onMessage(data);
		}
	}
	
	public void sendMessage(String message){
		try {
			getConnection().sendMessage(message);
		} catch (IOException e) {
			Log.w("WebSocket", "Problem with send message via WebSocket!");
		}
		
	}
	
	public interface CommunicationSocketListener{
		public void onOpen(CommunicationSocket socket);
		public void onClose(CommunicationSocket socket);
		public void onMessage(String message);
	}
}
