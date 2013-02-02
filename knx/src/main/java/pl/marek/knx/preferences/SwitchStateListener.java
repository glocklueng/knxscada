package pl.marek.knx.preferences;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class SwitchStateListener implements OnCheckedChangeListener{
	
	private Switch controlSwitch;
	private TextView summary;
	private Bundle bundle;
	private Context context;
	private SwitchExecutor executor;
	private PreferenceScreen preferenceScreen;
	
	public SwitchStateListener(Context context, Switch controlSwitch, TextView summary, Bundle bundle){
		this.summary = summary;
		this.bundle = bundle;
		this.context = context;
		createExecutor();
		setSwitch(controlSwitch);
	}
	
	private void createExecutor(){
		if(bundle != null){
			executor = SwitchExecutor.create(context, bundle.getString("type"));
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		execute(isChecked);
		setSummary(isChecked);
		updateSwitchState(isChecked);
	}
	
	public void setSwitch(Switch controlSwitch){
		
		if (this.controlSwitch == controlSwitch)
			return;

		if (controlSwitch != null){
			controlSwitch.setOnCheckedChangeListener(this);
			controlSwitch.setChecked(isSwitchOn());
			setSummary(isSwitchOn());
		}
		
	    this.controlSwitch = controlSwitch;
		
	}
	
	public boolean isSwitchOn(){
		return executor.getSwitchState();
	}
	
	private void setSummary(boolean isChecked){
		if(summary != null && bundle != null){
			summary.setVisibility(View.VISIBLE);
			if(isChecked){
				summary.setText(bundle.getString("summaryOn"));
			} else{
				summary.setText(bundle.getString("summaryOff"));	
			}
		}
	}
	
	private void updateSwitchState(boolean state){
		executor.updateSwitchState(state);
		if(preferenceScreen != null){
			preferenceScreen.setEnabled(!state);
		}
	}
	
	private void execute(boolean state){
		if (executor != null){
			if(state){
				if(!executor.getSwitchState()){
					executor.start();
				}
			} else{
				if(executor.getSwitchState()){
					executor.stop();
				}
			}
		}
	}
	
	public void setPreferenceScreen(PreferenceScreen preferenceScreen){
		this.preferenceScreen = preferenceScreen;
	}
	
	public void resume(){
		controlSwitch.setOnCheckedChangeListener(this);
		controlSwitch.setChecked(isSwitchOn());
	}
	
	public void pause(){
		controlSwitch.setOnCheckedChangeListener(null);
	}
}