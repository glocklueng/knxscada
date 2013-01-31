package pl.marek.knx.preferences;

import pl.marek.knx.R;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Gravity;
import android.widget.Switch;

public class SettingsFragment extends PreferenceFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addActionBar();
		
        String settings = getArguments().getString("type");
        if ("knxConnection".equals(settings)) {
            addPreferencesFromResource(R.xml.connection_prefs);
        } else if ("webServer".equals(settings)) {
            addPreferencesFromResource(R.xml.webserver_prefs);
        }
	}
	
	private void addActionBar(){
		
		Activity activity = getActivity();
		ActionBar actionbar = activity.getActionBar();
		Switch actionBarSwitch = new Switch(activity);
		
		actionBarSwitch.setTextOn(getArguments().getString("switchTextOn"));
		actionBarSwitch.setTextOff(getArguments().getString("switchTextOff"));

		actionbar.setDisplayOptions( ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_CUSTOM, 
									 ActionBar.DISPLAY_SHOW_TITLE|ActionBar.DISPLAY_SHOW_CUSTOM);
		actionbar.setCustomView(actionBarSwitch, 
							    new ActionBar.LayoutParams(
											ActionBar.LayoutParams.WRAP_CONTENT,
											ActionBar.LayoutParams.WRAP_CONTENT, 
											Gravity.CENTER_VERTICAL | Gravity.RIGHT));
		
		actionbar.setTitle(getArguments().getString("title"));
	}
}