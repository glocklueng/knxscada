package pl.marek.knx.components.controllers;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.json.simple.JSONObject;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.pages.AjaxBehavior;
import pl.marek.knx.telegram.Telegram;
import pl.marek.knx.utils.JSONUtil;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;

@HtmlFile("components/controllers/slider.html")
public class Slider extends Controller{

	private static final long serialVersionUID = 1L;
	
	private Label nameView;
	private Label descriptionView;
	private Label sliderView;
	
	private double maxValue = 255.0;
	
	private OnSliderChangeBehavior onSliderChangeBehavior;

	public Slider(String id, Element element, ControllerType type) {
		super(id, element, type);
		loadComponents();
	}
	
	private void loadComponents(){
		nameView = new Label("controller-name", Model.of(""));
		descriptionView = new Label("controller-description", Model.of(""));
		sliderView = new Label("controller-slider", Model.of("")){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("value", "0");
				tag.put("callback", onSliderChangeBehavior.getCallbackUrl().toString());
				super.onComponentTag(tag);
			}
		};
		
		if(getElement() != null){
			nameView.setDefaultModel(new Model<String>(getElement().getName()));
			descriptionView.setDefaultModel(new Model<String>(getElement().getDescription()));
			setMaxValue(getElement().getMaxValue());
		}
		
		onSliderChangeBehavior = new OnSliderChangeBehavior();
				
		add(nameView);
		add(descriptionView);
		add(sliderView);
		add(onSliderChangeBehavior);
	}
	
	@Override
	public void setName(String name) {
		nameView.setDefaultModelObject(name);
	}

	@Override
	public void setDescription(String description) {
		descriptionView.setDefaultModelObject(description);
	}
	
	public void setMinValue(double minValue){}
	
	public void setMaxValue(double maxValue){
		this.maxValue = maxValue;
		if(maxValue == 0){
			this.maxValue = 255.0;
		}	
	}
	
	protected class OnSliderChangeBehavior extends AjaxBehavior{

		private static final long serialVersionUID = 1L;

		@Override
		protected void respond(AjaxRequestTarget target) {
			String msg = getRequestMessage();
			
			JSONObject obj = JSONUtil.convertStringToObject(msg);
			int value = JSONUtil.convertJSONValueToInt(obj, "value");
			double v = (value/100.0)*maxValue;			
			writeTelegram(String.valueOf((int)v));
		}
	}

	public void telegramReceived(Telegram telegram) {

		for(ElementGroupAddress address: element.getGroupAddresses()){
			if(address.getAddress().equals(telegram.getDestinationAddress())){
				if(!telegram.getType().equals("Read")){
					byte[] rawValue = telegram.getRawdata();				
					int rawVal = rawValue[rawValue.length - 1] & 0xff;
					
					double v = (rawVal/maxValue)*100.0;
					int val = (int)v;					
					updateValue(String.valueOf(val), "slider");
				}
			}
		}		
	}
	
	private void writeTelegram(String value) {
		for(ElementGroupAddress address: element.getGroupAddresses()){
			transferTelegram(KNXDataTransceiver.WRITE_DATA, address.getAddress(), DPTXlator8BitUnsigned.DPT_PERCENT_U8, value);
		}
	}
	
	public void readTelegram() {
		for(ElementGroupAddress address: element.getGroupAddresses()){
			ElementGroupAddressType type = ElementGroupAddressType.valueOf(address.getType());
			if(type.equals(ElementGroupAddressType.STATUS)){
				transferTelegram(KNXDataTransceiver.READ_DATA, address.getAddress(), DPTXlator8BitUnsigned.DPT_PERCENT_U8, null);
			}
		}
	}
}
