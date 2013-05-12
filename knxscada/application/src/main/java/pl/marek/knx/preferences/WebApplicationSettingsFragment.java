package pl.marek.knx.preferences;

import pl.marek.knx.R;
import android.os.Bundle;

public class WebApplicationSettingsFragment extends SettingsFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.webapp_prefs);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		addActionBar();
	}
	
	@Override
	public void onPause() {
		super.onPause();

	}

}
