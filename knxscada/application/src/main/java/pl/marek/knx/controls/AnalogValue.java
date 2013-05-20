package pl.marek.knx.controls;

import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.telegram.Telegram;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import android.content.Context;

public class AnalogValue extends DigitalValue{

	public AnalogValue(Context context, ControllerType type, Element element) {
		super(context, type, element);
	}
	
	public void telegramReceived(Telegram telegram) {

		for(ElementGroupAddress address: element.getGroupAddresses()){
			if(address.getAddress().equals(telegram.getDestinationAddress())){
				if(!telegram.getType().equals("Read")){
					byte[] rawValue = telegram.getRawdata();				
					int rawVal = rawValue[rawValue.length - 1] & 0xff;
					
					setValue(String.valueOf(rawVal));
				}
			}
		}
	}
	
	public void readTelegram() {
		for(ElementGroupAddress address: element.getGroupAddresses()){
			transferTelegram(KNXDataTransceiver.READ_DATA, address.getAddress(), DPTXlator8BitUnsigned.DPT_PERCENT_U8, null);
		}
	}
}