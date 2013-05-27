package pl.marek.knx.controls;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import pl.marek.knx.R;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.telegram.Telegram;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;

public class DigitalValue extends Controller implements OnClickListener{
	
	private TextView nameView;
	private TextView descriptionView;
	private TextView valueView;
	
	private int onBackgroundColor;
	private int offBackgroundColor;
	
	public DigitalValue(Context context, Element element) {
		this(context, ControllerType.DIGITAL_VALUE, element);
	}

	public DigitalValue(Context context, ControllerType type, Element element) {
		super(context, element, type, type.getLayout());

		initViews();
		initValues();
		
		readTelegram();
	}
	
	private void initViews(){

		nameView = (TextView)getMainView().findViewById(android.R.id.title);
		descriptionView = (TextView)getMainView().findViewById(android.R.id.summary);
		valueView = (TextView)getMainView().findViewById(R.id.controller_digital_value);
		setOnClickListener(this);
		
		onBackgroundColor = getResources().getColor(R.color.control_on_background);
		offBackgroundColor = getResources().getColor(R.color.control_off_background);
	}
	
	public void initValues(){
		if(element != null){
			nameView.setText(element.getName());
			if(element.getDescription().isEmpty()){
				descriptionView.setVisibility(View.GONE);
			}
			descriptionView.setText(element.getDescription());
		}
	}

	public void setName(String name){
		nameView.setText(name);
	}
	
	public String getName(){
		return nameView.getText().toString();
	}
	
	public void setDescription(String description){
		if(element.getDescription().isEmpty()){
			descriptionView.setVisibility(View.GONE);
		} else{
			descriptionView.setVisibility(View.VISIBLE);
		}
		descriptionView.setText(description);
	}
	
	public String getDescription(){
		return descriptionView.getText().toString();
	}
	
	public void setValue(String value){
		valueView.setText(value);
	}
	
	public String getValue(){
		return valueView.getText().toString();
	}
	
	public void onClick(View v) {
		readTelegram();
	}

	private void setBackgroundColorByState(boolean state){
		if(state){
			setBackgroundColor(onBackgroundColor);
		}else{
			setBackgroundColor(offBackgroundColor);
		}
		getMainView().setPadding(10,10,10,10);
	}

	@Override
	public void setMinValue(double minValue) {}

	@Override
	public void setMaxValue(double maxValue) {}
		
	public void telegramReceived(Telegram telegram) {

		for(ElementGroupAddress address: element.getGroupAddresses()){
			if(address.getAddress().equals(telegram.getDestinationAddress())){
				if(!telegram.getType().equals("Read")){
					String value = telegram.getData();
					String lastChar = value.substring(value.length()-1).trim();
					
					if(value.equals("on") || lastChar.equals("1")){
						setValue("1");
						setBackgroundColorByState(true);
					}else if(value.equals("off") || lastChar.equals("0")){
						setValue("0");
						setBackgroundColorByState(false);
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
