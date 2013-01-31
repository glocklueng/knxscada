package pl.marek.knx.preferences;

import java.util.ArrayList;
import java.util.List;

import pl.marek.knx.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.ListAdapter;

public class SettingsActivity extends PreferenceActivity{
    
	private List<Header> headers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
//	protected void onResume() {
//		super.onResume();
//				
//		if (getListAdapter() instanceof PreferencesHeaderAdapter)
//			((PreferencesHeaderAdapter) getListAdapter()).resume();
//	}
//
//	protected void onPause() {
//		super.onPause();
//		if (getListAdapter() instanceof PreferencesHeaderAdapter)
//			((PreferencesHeaderAdapter) getListAdapter()).pause();
//	}
	
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preferences_headers, target);
        headers = target;
    }
    
	public void setListAdapter(ListAdapter adapter) {
		if (headers == null) {
			headers = new ArrayList<Header>();
			for (int i = 0; i < adapter.getCount(); ++i)
				headers.add((Header) adapter.getItem(i));
		}
		getActionBar().setTitle("Ustawienia");
		super.setListAdapter(new PreferencesHeaderAdapter(this, headers));
	}
}