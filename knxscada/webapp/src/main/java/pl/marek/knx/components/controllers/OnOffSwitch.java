package pl.marek.knx.components.controllers;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.components.CompoundButton;
import pl.marek.knx.components.CompoundButton.OnChangeListener;
import pl.marek.knx.components.LightImageButton;
import pl.marek.knx.components.Switch;
import pl.marek.knx.components.ToggleButton;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.telegram.Telegram;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;

@HtmlFile("components/controllers/onoffswitch.html")
public class OnOffSwitch extends Controller implements OnChangeListener{

	private static final long serialVersionUID = 1L;
	
	private Label nameView;
	private Label descriptionView;
	private CompoundButton buttonView;
	
	public OnOffSwitch(String id, ControllerType type, Element element) {
		super(id, element, type);
		loadComponents();
	}
	
	private void loadComponents(){
		nameView = new Label("controller-name", Model.of(""));
		descriptionView = new Label("controller-description", Model.of(""));
		
		if(getElement() != null){
			nameView.setDefaultModel(new Model<String>(getElement().getName()));
			descriptionView.setDefaultModel(new Model<String>(getElement().getDescription()));
		}
		
		switch(getType()){
			case ON_OFF_SWITCH:
				buttonView = new Switch("controller-button");
				break;
			case ON_OFF_TOGGLE:
				buttonView = new ToggleButton("controller-button");
				break;
			case LIGHT_ON_OFF_SWITCH:
				buttonView = new LightImageButton("controller-button");
				break;
			default:
				break;
		}
		buttonView.setOnChangeListener(this);
		
		add(nameView);
		add(descriptionView);
		add(buttonView);
	}
	
	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		readTelegram();
	}

	@Override
	public void setName(String name) {
		nameView.setDefaultModelObject(name);
	}

	@Override
	public void setDescription(String description) {
		descriptionView.setDefaultModelObject(description);
	}
	
	@Override
	public void setMinValue(double minValue) {}

	@Override
	public void setMaxValue(double maxValue) {}

	@Override
	public void onChange(boolean state) {
		String value = "";
		if(state){
			value = "on";
		}else{
			value = "off";
		}
		writeTelegram(value);
	}
	
	@Override
	public void telegramReceived(Telegram telegram) {
		if(element != null){
			for(ElementGroupAddress address: element.getGroupAddresses()){
				if(address.getAddress().equals(telegram.getDestinationAddress())){
					if(!telegram.getType().equals("Read")){
						String value = telegram.getData();
						String lastChar = value.substring(value.length()-1).trim();
						
						if(value.equals("on") || lastChar.equals("1")){
							updateValue("on", "compound-button");
						}else if(value.equals("off") || lastChar.equals("0")){
							updateValue("off", "compound-button");
						}
					}
				}
			}
		}
	}
	
	private void writeTelegram(String value) {
		for(ElementGroupAddress address: element.getGroupAddresses()){
			ElementGroupAddressType type = ElementGroupAddressType.valueOf(address.getType());
			if(!type.equals(ElementGroupAddressType.STATUS)){
				transferTelegram(KNXDataTransceiver.WRITE_DATA, address.getAddress(), DPTXlatorBoolean.DPT_SWITCH, value);
			}
		}
	}
	
	public void readTelegram() {
		for(ElementGroupAddress address: element.getGroupAddresses()){
			ElementGroupAddressType type = ElementGroupAddressType.valueOf(address.getType());
			if(type.equals(ElementGroupAddressType.STATUS)){
				transferTelegram(KNXDataTransceiver.READ_DATA, address.getAddress(), DPTXlatorBoolean.DPT_SWITCH, null);
			}
		}
	}
}