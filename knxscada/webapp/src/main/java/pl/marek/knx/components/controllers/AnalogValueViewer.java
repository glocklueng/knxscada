package pl.marek.knx.components.controllers;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.telegram.Telegram;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;

@HtmlFile("components/controllers/valueviewer.html")
public class AnalogValueViewer extends ValueViewer{

	private static final long serialVersionUID = 1L;

	public AnalogValueViewer(String id, Element element, ControllerType type) {
		super(id, element, type);
		if(getElement() == null){
			setValue("255");
		}
	}
	
	public void telegramReceived(Telegram telegram) {
		if(element != null){
			for(ElementGroupAddress address: element.getGroupAddresses()){
				if(address.getAddress().equals(telegram.getDestinationAddress())){
					if(!telegram.getType().equals("Read")){
						byte[] rawValue = telegram.getRawdata();				
						int rawVal = rawValue[rawValue.length - 1] & 0xff;
						
						updateValue(String.valueOf(rawVal), "value-viewer");
					}
				}
			}
		}
	}
	
	public void readTelegram() {
		if(element != null){
			for(ElementGroupAddress address: element.getGroupAddresses()){
				transferTelegram(KNXDataTransceiver.READ_DATA, address.getAddress(), DPTXlator8BitUnsigned.DPT_PERCENT_U8, null);
			}
		}
	}
}