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
		
//		System.out.println(data);
//		String m = "";
//		
//		  JSONParser parser = new JSONParser();
//		  ContainerFactory containerFactory = new ContainerFactory(){
//		    public List creatArrayContainer() {
//		      return new LinkedList();
//		    }
//
//		    public Map createObjectContainer() {
//		      return new LinkedHashMap();
//		    }
//		                        
//		  };
//		                
//		  try{
//		    Map json = (Map)parser.parse(data, containerFactory);
//		    Iterator iter = json.entrySet().iterator();
//		    System.out.println("==iterate result==");
//		    while(iter.hasNext()){
//		      Map.Entry entry = (Map.Entry)iter.next();
//		      System.out.println(entry.getKey() + "=>" + entry.getValue());
//		      m = entry.getValue().toString();
//		    }
//		                        
//		    System.out.println("==toJSONString()==");
//		    System.out.println(JSONValue.toJSONString(json));
//		  }
//		  catch(ParseException pe){
//		    System.out.println(pe);
//		  }
//
//		JSONObject obj = new JSONObject();
//		 obj.put("msg",m);
//		
//		 for (CommunicationSocket socket : sockets) {
//			try{
//				socket.getConnection().sendMessage(obj.toJSONString());
//			
//			}catch(Exception ex){
//				
//			}
//		}
	}
	
	public interface CommunicationSocketListener{
		public void onOpen(CommunicationSocket socket);
		public void onClose(CommunicationSocket socket);
		public void onMessage(String message);
	}
}
