package pl.marek.knx.controls;

import pl.marek.knx.R;
import android.content.Context;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class OnOffSwitch extends Controller implements OnCheckedChangeListener{
		
	private TextView nameView;
	private TextView descriptionView;
	private ToggleButton switchView;
	
	private int onBackgroundColor;
	private int offBackgroundColor;

	public OnOffSwitch(Context context) {
		super(context, R.layout.control_onoffswitch);
		init();
	}
		
	public void init(){

		nameView = (TextView)getMainView().findViewById(android.R.id.title);
		descriptionView = (TextView)getMainView().findViewById(android.R.id.summary);
		switchView = (ToggleButton)getMainView().findViewById(android.R.id.toggle);
		switchView.setOnCheckedChangeListener(this);
		
		onBackgroundColor = getResources().getColor(R.color.control_on_background);
		offBackgroundColor = getResources().getColor(R.color.control_off_background);
		setBackgroundColorByState(switchView.isChecked());
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
	
	public void setSwitchOn(){
		switchView.setChecked(true);
	}
	
	public void setSwitchOff(){
		switchView.setChecked(false);
	}
	
	private void setBackgroundColorByState(boolean state){
		if(state){
			setBackgroundColor(onBackgroundColor);
		}else{
			setBackgroundColor(offBackgroundColor);
		}
		getMainView().setPadding(10,10,10,10);
	}
		
	public void setOnBackgroundColor(int color){
		onBackgroundColor = color;
		setBackgroundColorByState(switchView.isChecked());
	}
	
	public void setOffBackgroundColor(int color){
		offBackgroundColor = color;
		setBackgroundColorByState(switchView.isChecked());
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		setBackgroundColorByState(isChecked);
	}
	

}
