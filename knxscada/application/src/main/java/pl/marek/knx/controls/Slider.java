package pl.marek.knx.controls;

import pl.marek.knx.R;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.telegram.Telegram;
import pl.marek.knx.utils.ColorUtils;
import tuwien.auto.calimero.dptxlator.DPTXlator8BitUnsigned;
import android.content.Context;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class Slider extends Controller implements OnSeekBarChangeListener{

	private TextView nameView;
	private TextView descriptionView;
	private SeekBar sliderView;
	private TextView sliderValue;
	private ImageView sliderImageValue;
	
	private int startColor;
	private int stopColor;
	
	private double maxValue = 255.0;
	
	public Slider(Context context, Element element) {
		this(context, ControllerType.SLIDER, element);
	}

	public Slider(Context context, ControllerType type, Element element) {
		super(context, element, type, type.getLayout());
		initViews();
		initValues();
		
		if(element != null){
			setMaxValue(element.getMaxValue());
		}
		//TODO
		readTelegram();
	}
		
	public void initViews(){
		
		nameView = (TextView)getMainView().findViewById(android.R.id.title);
		descriptionView = (TextView)getMainView().findViewById(android.R.id.summary);
		sliderView = (SeekBar)getMainView().findViewById(R.id.seekbar);
		sliderView.setOnSeekBarChangeListener(this);
		
		if(getType().equals(ControllerType.LIGHT_SLIDER)){
			sliderImageValue = (ImageView)getMainView().findViewById(R.id.slider_image_value);
		}else{
			sliderValue = (TextView)getMainView().findViewById(R.id.slider_value);
		}
		startColor = getResources().getColor(R.color.control_slider_low_color);
		stopColor = getResources().getColor(R.color.control_slider_high_color);
		
		setBackgroundColorBySliderPosition(sliderView.getProgress());
		setSliderValue(sliderView.getProgress());
	}
	
	public void initValues(){
		nameView.setText(element.getName());
		descriptionView.setText(element.getDescription());
	}

	public void setName(String name){
		nameView.setText(name);
	}
	
	public String getName(){
		return nameView.getText().toString();
	}
	
	public void setDescription(String description){
		descriptionView.setText(description);
	}
	
	public String getDescription(){
		return descriptionView.getText().toString();
	}
	
	public void setMinValue(double minValue){}
	
	public void setMaxValue(double maxValue){
		this.maxValue = maxValue;
		if(maxValue == 0){
			this.maxValue = 255.0;
		}	
	}
	
	private void setBackgroundColorBySliderPosition(int position){
		int color = ColorUtils.interpolate(startColor, stopColor, position/100.0f);
		setBackgroundColor(color);
	}
	
	public void setStartColor(int color){
		startColor = color;
	}
	
	public void setStopColor(int color){
		stopColor = color;
	}
	
	private void setSliderValue(int value){
		if(getType().equals(ControllerType.LIGHT_SLIDER)){
			if(sliderImageValue != null){
				int imgRes = 0;
				if(value == 0){
					imgRes = R.drawable.element_bulb_off;
				}else if(value > 0 && value < 10){
					imgRes = R.drawable.element_bulb_on_1;
				}else if(value >= 10 && value < 20){
					imgRes = R.drawable.element_bulb_on_2;
				}else if(value >= 20 && value < 30){
					imgRes = R.drawable.element_bulb_on_3;
				}else if(value >= 30 && value < 40){
					imgRes = R.drawable.element_bulb_on_4;
				}else if(value >= 40 && value < 50){
					imgRes = R.drawable.element_bulb_on_5;
				}else if(value >= 50 && value < 60){
					imgRes = R.drawable.element_bulb_on_6;
				}else if(value >= 60 && value < 70){
					imgRes = R.drawable.element_bulb_on_7;
				}else if(value >= 70 && value < 80){
					imgRes = R.drawable.element_bulb_on_8;
				}else if(value >= 80 && value < 90){
					imgRes = R.drawable.element_bulb_on_9;
				}else if(value >= 90){
					imgRes = R.drawable.element_bulb_on_10;
				}
				sliderImageValue.setImageResource(imgRes);
			}
		}else{
			String val = String.format("%d %%", value);
			if(sliderValue != null){
				sliderValue.setText(val);
			}
		}
	}
	
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		setSliderValue(progress);
		setBackgroundColorBySliderPosition(progress);
	}
	
	public void onStartTrackingTouch(SeekBar seekBar) {}

	public void onStopTrackingTouch(SeekBar seekBar) {
		int value = seekBar.getProgress();
		double v = (value/100.0)*maxValue;
		writeTelegram(String.valueOf((int)v));
	}

	public void telegramReceived(Telegram telegram) {

		for(ElementGroupAddress address: element.getGroupAddresses()){
			if(address.getAddress().equals(telegram.getDestinationAddress())){
				if(!telegram.getType().equals("Read")){
					byte[] rawValue = telegram.getRawdata();				
					int rawVal = rawValue[rawValue.length - 1] & 0xff;
					
					double v = (rawVal/maxValue)*100.0;
					int val = (int)v;
					
					sliderView.setProgress(val);			
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
			transferTelegram(KNXDataTransceiver.READ_DATA, address.getAddress(), DPTXlator8BitUnsigned.DPT_PERCENT_U8, null);
		}
	}
}