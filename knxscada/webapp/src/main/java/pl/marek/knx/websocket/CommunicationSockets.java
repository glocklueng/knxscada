package pl.marek.knx.websocket;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.json.simple.JSONObject;

import pl.marek.knx.websocket.CommunicationSocket.CommunicationSocketListener;

public class CommunicationSockets implements CommunicationSocketListener{
	
	public static final String COMMUNICATION_SOCKETS = "communication_sockets";
	
	private Set<CommunicationSocket> sockets;
	
	public CommunicationSockets(){
		sockets = new CopyOnWriteArraySet<CommunicationSocket>();
	}

	@Override
	public void onOpen(CommunicationSocket socket) {
		sockets.add(socket);
	}

	@Override
	public void onClose(CommunicationSocket socket) {
		sockets.remove(socket);
	}

	@Override
	public void onMessage(String message) {
		sendMessage(message);
	}
	
	public void sendMessage(String message){
		for (CommunicationSocket socket : sockets) {
			try{
				socket.getConnection().sendMessage(message);
			}catch(Exception ex){
				
			}
		}
	}
	
	public void sendJSONMessage(JSONObject object){
		sendMessage(object.toJSONString());
	}
}