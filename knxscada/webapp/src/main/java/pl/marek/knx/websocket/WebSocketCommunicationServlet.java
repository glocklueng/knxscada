package pl.marek.knx.websocket;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketServlet;

public class WebSocketCommunicationServlet extends WebSocketServlet{

	private static final long serialVersionUID = 1L;
	
	private CommunicationSockets communicationSockets;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		if(communicationSockets == null){
			communicationSockets = new CommunicationSockets();
		}
		getServletContext().setAttribute(CommunicationSockets.COMMUNICATION_SOCKETS, communicationSockets);
	}

	@Override
	public WebSocket doWebSocketConnect(HttpServletRequest arg0, String arg1) {
		return new CommunicationSocket(communicationSockets);
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().getNamedDispatcher("default").forward(request, response);
	}
}