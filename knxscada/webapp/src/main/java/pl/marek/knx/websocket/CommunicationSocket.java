package pl.marek.knx.websocket;

import org.eclipse.jetty.websocket.WebSocket.OnTextMessage;

public class CommunicationSocket implements OnTextMessage{
	
	private Connection connection;
	private CommunicationSocketListener listener;
	
	public CommunicationSocket(CommunicationSocketListener listener){
		this.listener = listener;
	}

	public Connection getConnection(){
		return connection;
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
	
	public interface CommunicationSocketListener{
		public void onOpen(CommunicationSocket socket);
		public void onClose(CommunicationSocket socket);
		public void onMessage(String message);
	}
}
