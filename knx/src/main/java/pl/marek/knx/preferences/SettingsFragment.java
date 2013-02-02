package pl.marek.knx.preferences;

import pl.marek.knx.MainApplication;
import pl.marek.knx.R;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.Gravity;
import android.widget.Switch;

public class SettingsFragment extends PreferenceFragment{
	
	private MainApplication application;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (MainApplication)getActivity().getApplicationContext();
		
		addActionBar();
		
        String settings = getArguments().getString("type");
        if (getString(R.string.knx_connection_toggle).equals(settings)) {
            addPreferencesFromResource(R.xml.connection_prefs);
        } else if (getString(R.string.webserver_toggle).equals(settings)) {
            addPreferencesFromResource(R.xml.webserver_prefs);
        }
	}
	
	private void addActionBar(){
		
		Activity activity = getActivity();
		ActionBar actionbar = activity.getActionBar();
		Switch actionBarSwitch = new Switch(activity);
		
		new SwitchStateListener(getActivity(), actionBarSwitch, null, getArguments());
		
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