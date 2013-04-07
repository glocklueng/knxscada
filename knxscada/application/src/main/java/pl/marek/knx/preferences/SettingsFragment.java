package pl.marek.knx.preferences;

import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Gravity;
import android.widget.Switch;

public class SettingsFragment extends PreferenceFragment{
	
	protected SwitchStateListener switchListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	protected void addActionBar(){
		String title = getArguments().getString("title");
		if(title != null){
			ActionBar actionbar = getActivity().getActionBar();
			actionbar.setTitle(title);
			actionbar.setDisplayOptions( ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_CUSTOM, 
					ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_CUSTOM);
		}
	}
	
	protected void addSwitchToActionBar(){
		Switch actionBarSwitch = new Switch(getActivity());
		switchListener = new SwitchStateListener(getActivity(), actionBarSwitch, null, getArguments());
		actionBarSwitch.setTextOn(getArguments().getString("switchTextOn"));
		actionBarSwitch.setTextOff(getArguments().getString("switchTextOff"));

		getActivity().getActionBar().setCustomView(actionBarSwitch, 
							    new ActionBar.LayoutParams(
											ActionBar.LayoutParams.WRAP_CONTENT,
											ActionBar.LayoutParams.WRAP_CONTENT, 
											Gravity.CENTER_VERTICAL | Gravity.RIGHT));
	}
	
	protected void updateScreenPreferenceEnabled(){
		getPreferenceScreen().setEnabled(!switchListener.isSwitchOn());
	}
}