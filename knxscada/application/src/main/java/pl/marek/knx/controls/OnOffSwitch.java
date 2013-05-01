package pl.marek.knx.controls;

import pl.marek.knx.R;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.telegram.Telegram;
import tuwien.auto.calimero.dptxlator.DPTXlatorBoolean;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

@SuppressLint("ViewConstructor")
public class OnOffSwitch extends Controller implements OnCheckedChangeListener{
		
	private TextView nameView;
	private TextView descriptionView;
	private ToggleButton switchView;
	
	private int onBackgroundColor;
	private int offBackgroundColor;
	
	private boolean changeBackgroundColorByState;
	private boolean reactOnSwitchStateChange;
	
	public OnOffSwitch(Context context, Element element) {
		this(context, ControllerType.ON_OFF_SWITCH, element);
	}

	public OnOffSwitch(Context context, ControllerType type, Element element) {
		super(context, element, type, R.layout.control_onoffswitch);
		changeBackgroundColorByState = false;
		reactOnSwitchStateChange = true;
		initViews();
		initValues();
	}
		
	private void initViews(){

		nameView = (TextView)getMainView().findViewById(android.R.id.title);
		descriptionView = (TextView)getMainView().findViewById(android.R.id.summary);
		switchView = (ToggleButton)getMainView().findViewById(android.R.id.toggle);
		switchView.setOnCheckedChangeListener(this);
		
		onBackgroundColor = getResources().getColor(R.color.control_on_background);
		offBackgroundColor = getResources().getColor(R.color.control_off_background);
		setBackgroundColorByState(switchView.isChecked());
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
	
	public void setSwitchOn(){
		switchView.setChecked(true);
	}
	
	public void setSwitchOff(){
		switchView.setChecked(false);
	}
	
	public void setChangeBackgroundColorByState(boolean changeBackgroundColorByState) {
		this.changeBackgroundColorByState = changeBackgroundColorByState;
	}

	private void setBackgroundColorByState(boolean state){
		if(changeBackgroundColorByState){
			if(state){
				setBackgroundColor(onBackgroundColor);
			}else{
				setBackgroundColor(offBackgroundColor);
			}
			getMainView().setPadding(10,10,10,10);
		}
	}
		
	public void setOnBackgroundColor(int color){
		onBackgroundColor = color;
		setBackgroundColorByState(switchView.isChecked());
	}
	
	public void setOffBackgroundColor(int color){
		offBackgroundColor = color;
		setBackgroundColorByState(switchView.isChecked());
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		setBackgroundColorByState(isChecked);
		if(reactOnSwitchStateChange){
			String value = "";
			if(isChecked){
				value = "on";
			} else{
				value = "off";
			}
			writeTelegram(value);
		}
	}

	public void telegramReceived(Telegram telegram) {

		for(ElementGroupAddress address: element.getGroupAddresses()){
			if(address.getAddress().equals(telegram.getDestinationAddress())){
				String value = telegram.getData();
				String lastChar = value.substring(value.length()-1).trim();
				
				if(value.equals("on") || lastChar.equals("1")){
					reactOnSwitchStateChange = false;
					setSwitchOn();
					reactOnSwitchStateChange = true;
				}else if(value.equals("off") || lastChar.equals("0")){
					reactOnSwitchStateChange = false;
					setSwitchOff();
					reactOnSwitchStateChange = true;
				}
			}
		}
		
	}
	
	private void writeTelegram(String value) {
		for(ElementGroupAddress address: element.getGroupAddresses()){
			transferTelegram(KNXDataTransceiver.WRITE_DATA, address.getAddress(), DPTXlatorBoolean.DPT_SWITCH, value);
		}
	}
	
//	public void readTelegram(boolean value) {
//		transferData(KNXDataTransceiver.WRITE_DATA, address, dpt, value);
//	}
	
}
