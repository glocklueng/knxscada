package pl.marek.knx.preferences;

import pl.marek.knx.R;
import android.os.Bundle;

public class WebServerSettingsFragment extends SettingsFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.webserver_prefs);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		addActionBar();
		addSwitchToActionBar();
		updateScreenPreferenceEnabled();
		switchListener.setPreferenceScreen(getPreferenceScreen());
	}

}
