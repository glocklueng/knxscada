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
			executor = SwitchExecutor.create(context, bundle.getString("type"), this);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		execute(isChecked);
		updatePreferenceScreen(isChecked);
	}
	
	public boolean isSwitchOn(){
		if(executor.getSwitchState().equals(SwitchState.ON))
			return true;
		return false;
	}
	
	public void setSwitch(Switch controlSwitch){
		
		if (this.controlSwitch == controlSwitch)
			return;
		
		this.controlSwitch = controlSwitch;
		
		if (controlSwitch != null){
			controlSwitch.setOnCheckedChangeListener(this);
			setSwitchState(executor.getSwitchState());
			executor.registerState();
		}
	}
		
	private void updatePreferenceScreen(boolean state){
		if(preferenceScreen != null){
			preferenceScreen.setEnabled(!state);
		}
	}
	
	private void execute(boolean state){
		if (executor != null){
			if(state){
				if(executor.getSwitchState().equals(SwitchState.OFF)){
					executor.start();
				}
			} else{
				if(executor.getSwitchState().equals(SwitchState.ON)){
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
		setSwitchState(executor.getSwitchState());
		executor.registerState();
	}
	
	public void pause(){
		controlSwitch.setOnCheckedChangeListener(null);
		executor.unregisterState();
		
	}
	
	public void setSwitchState(SwitchState state){
		state.setSwitchMode(controlSwitch);
		setSummary(state);
		if(state.equals(SwitchState.OFF)){
			updatePreferenceScreen(false);
		}else{
			updatePreferenceScreen(true);	
		}
	}
	
	private void setSummary(SwitchState state){
		if(summary != null && bundle != null){
			summary.setVisibility(View.VISIBLE);
			switch(state){
			case ON:
				summary.setText(bundle.getString("summaryOn"));
				break;
			case ON_OFF_CHANGE:
				summary.setText(bundle.getString("summaryOnOff"));
				break;
			case OFF:
				summary.setText(bundle.getString("summaryOff"));
				break;
			case OFF_ON_CHANGE:
				summary.setText(bundle.getString("summaryOffOn"));
				break;
			}
		}
	}
}