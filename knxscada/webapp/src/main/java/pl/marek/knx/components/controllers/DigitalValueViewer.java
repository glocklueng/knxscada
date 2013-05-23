package pl.marek.knx.components.controllers;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.telegram.Telegram;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;

@HtmlFile("components/controllers/valueviewer.html")
public class DigitalValueViewer extends ValueViewer{

	private static final long serialVersionUID = 1L;

	public DigitalValueViewer(String id, Element element, ControllerType type) {
		super(id, element, type);
	}
	
	public void telegramReceived(Telegram telegram) {

		for(ElementGroupAddress address: element.getGroupAddresses()){
			if(address.getAddress().equals(telegram.getDestinationAddress())){
				if(!telegram.getType().equals("Read")){
					String value = telegram.getData();
					String lastChar = value.substring(value.length()-1).trim();
					
					if(value.equals("on") || lastChar.equals("1")){
						updateValue("1", "value-viewer");
					}else if(value.equals("off") || lastChar.equals("0")){
						updateValue("0", "value-viewer");
					}
				}
			}
		}
	}
	
	public void readTelegram() {
		for(ElementGroupAddress address: element.getGroupAddresses()){
			transferTelegram(KNXDataTransceiver.READ_DATA, address.getAddress(), DPTXlatorBoolean.DPT_SWITCH, null);
		}
	}
}
