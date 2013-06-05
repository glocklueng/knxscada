package pl.marek.knx.components.controllers;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.json.simple.JSONObject;

import android.content.Intent;

import pl.marek.knx.KNXWebApplication;
import pl.marek.knx.components.PopupMenu;
import pl.marek.knx.database.Element;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.interfaces.KNXWebTelegramListener;
import pl.marek.knx.pages.ElementAjaxOperations;
import pl.marek.knx.websocket.CommunicationSocket;
import pl.marek.knx.websocket.CommunicationSockets;
import tuwien.auto.calimero.dptxlator.DPT;

public abstract class Controller extends Panel implements KNXWebTelegramListener{

	private static final long serialVersionUID = 1L;
	
	private PopupMenu menu;
	
	private KNXWebApplication knxWebApplication;
	private ElementAjaxOperations elementAjaxOperations;
	protected String markupId;
	
	protected Element element;
	protected ControllerType type;
	
	private String clientIPAddress = "";
	
	public Controller(String id, Element element, ControllerType type) {
		super(id);
		knxWebApplication = (KNXWebApplication)getApplication();
		this.element = element;
		this.type = type;
		
		if(element != null){
			setMinValue(element.getMinValue());
			setMaxValue(element.getMaxValue());
		}
		
		setOutputMarkupId(true);
		loadComponents();
		registerTelegramListener();
		
		
		clientIPAddress = getIpAddress();
	}
	
	public String getClientIpAddress(){
		return clientIPAddress;
	}
	
	private String getIpAddress(){
		 WebRequest req = (WebRequest) RequestCycle.get().getRequest();
		 HttpServletRequest httpReq = (HttpServletRequest) req.getContainerRequest();
		 return httpReq.getRemoteHost();
	}
	
	@Override
	protected void onRemove() {		
		unregisterTelegramListener();
		super.onRemove();
	}

	private void loadComponents(){
		menu = new PopupMenu("popup-menu");
		menu.setOutputMarkupId(true);
		
		add(menu);
	}
	
	@Override
	protected void onRender() {
		super.onRender();
		markupId = getMarkupId();
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		String htmlClass = tag.getAttribute("class");
		if(htmlClass.contains("popup-menu-trigger")){
			tag.put("popupMenuId", menu.getMarkupId());
		}
		if(elementAjaxOperations != null){
			tag.put("callback", elementAjaxOperations.getCallbackUrl());
		}
		if(element != null){
			tag.put("elementid", element.getId());
		}
		
		if(isVisualisationElement()){
			tag.put("draggable", false);
			if(htmlClass.contains("visualisation-element")){
				tag.put("style", String.format("position: absolute; top: %dpx; left: %dpx;",element.getY(),element.getX()));
			}
		}
		super.onComponentTag(tag);
	}

	public PopupMenu getPopupMenu(){
		return menu;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
		if(element != null){
			setMaxValue(element.getMaxValue());
			setMinValue(element.getMinValue());
		}
	}

	public ControllerType getType() {
		return type;
	}

	public void setType(ControllerType type) {
		this.type = type;
	}

	public void setElementAjaxOperations(ElementAjaxOperations elementAjaxOperations) {
		this.elementAjaxOperations = elementAjaxOperations;
	}

	
	public boolean isVisualisationElement(){
		if(element == null){
			return false;
		}
		return element.isVisualisationElement();
	}
	
	protected synchronized KNXWebApplication getKNXWebApplication(){
		return knxWebApplication;
	}
	
	protected synchronized ServletContext getServletContext(){
		return getKNXWebApplication().getServletContext();
	}
	
	protected synchronized CommunicationSockets getCommunicationSockets(){
		return (CommunicationSockets)getServletContext().getAttribute(CommunicationSockets.COMMUNICATION_SOCKETS);
	}
	
	protected synchronized CommunicationSocket getCommunicationSocket(){
		CommunicationSocket socket = null;
		CommunicationSockets sockets = getCommunicationSockets();
		if(sockets != null){
			socket = sockets.getSocketByAddress(clientIPAddress);
		}
		return socket;
	}
	
	protected void registerTelegramListener(){
		getKNXWebApplication().addTelegramListener(this);
	}
	
	protected void unregisterTelegramListener(){
		getKNXWebApplication().removeTelegramListener(this);
	}
	
	protected void transferTelegram(String type,String address, DPT dpt, String value){
  		Intent dataIntent = new Intent(type);
  		dataIntent.putExtra(KNXDataTransceiver.GROUP_ADDRESS, address);
  		dataIntent.putExtra(KNXDataTransceiver.DPT_ID, dpt.getID());
  		if(value != null){
  			dataIntent.putExtra(KNXDataTransceiver.VALUE, value);
  		}
  		getKNXWebApplication().getAndroidContext().sendBroadcast(dataIntent);
	}
	
	protected void updateValue(String value, String type){
				
		if(markupId != null){
			new UpdateWebElementThread(markupId, value, type).start();
		}
	}
	
	protected class UpdateWebElementThread extends Thread{
		
		private String markupId;
		private String value;
		private String type;
		
		public UpdateWebElementThread(String markupId, String value, String type){
			this.markupId = markupId;
			this.value = value;
			this.type = type;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			JSONObject object = new JSONObject();
			object.put("id", markupId);
			object.put("value", value);
			object.put("type", type);
			object.put("operation", "update");
			CommunicationSocket socket = getCommunicationSocket();
			if(socket != null){
				socket.sendMessage(object.toJSONString());
			}
		}
	}
	
	public abstract void setName(String name);
	public abstract void setDescription(String description);
	public abstract void setMinValue(double minValue);
	public abstract void setMaxValue(double maxValue);
	

}
