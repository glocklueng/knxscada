package pl.marek.knx.preferences;

import pl.marek.knx.R;
import android.os.Bundle;

public class DiscovererFragment extends SettingsFragment{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.discoverer_prefs);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		addActionBar();
	}
	
	
	
}
