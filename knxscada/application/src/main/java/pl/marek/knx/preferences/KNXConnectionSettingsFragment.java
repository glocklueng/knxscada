package pl.marek.knx.preferences;

import pl.marek.knx.R;
import pl.marek.knx.utils.NetworkInformator;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;

public class KNXConnectionSettingsFragment extends SettingsFragment implements OnPreferenceClickListener, OnPreferenceChangeListener{
	
	private Preference localIPPreference;
	private Preference remoteIPPreference;
//	private Preference localPortPreference;
//	private Preference remotePortPreference;
//	
//	private KNXConnectionSettings settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.connection_prefs);
        loadPreferencesObjects();
//        settings = new KNXConnectionSettings(getActivity());
	}
	
	private void loadPreferencesObjects(){
        localIPPreference = getPreferenceScreen().findPreference(getString(R.string.local_ip_key));
        remoteIPPreference = getPreferenceScreen().findPreference(getString(R.string.remote_ip_key));
//        localPortPreference = getPreferenceScreen().findPreference(getString(R.string.local_port_key));
//        remotePortPreference = getPreferenceScreen().findPreference(getString(R.string.remote_port_key));
	}
	
	private void addPreferencesListeners(){
		localIPPreference.setOnPreferenceClickListener(this);
		remoteIPPreference.setOnPreferenceChangeListener(this);
	}
	
	private void loadInitialValues(){

        
		setLocalIP();
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		addActionBar();
		addSwitchToActionBar();
		updateScreenPreferenceEnabled();
		switchListener.setPreferenceScreen(getPreferenceScreen());
		addPreferencesListeners();
		loadInitialValues();
		switchListener.resume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		switchListener.pause();
	}
	
	private void setLocalIP(){
		localIPPreference.setSummary(NetworkInformator.getIPAddress());
	}
	
	private void setSummary(Preference preference, String value){
		preference.setSummary(value);
	}

	public boolean onPreferenceClick(Preference preference) {
		if(preference.equals(localIPPreference)){
			setLocalIP();
		}
		return false;
	}

	public boolean onPreferenceChange(Preference preference, Object newValue) {
		setSummary(preference, (String)newValue);
		return false;
	}

}
