package pl.marek.knx.preferences;

import java.util.ArrayList;
import java.util.List;

import pl.marek.knx.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.ListAdapter;

public class SettingsActivity extends PreferenceActivity{
    
	private List<Header> headers;
	private PreferencesHeaderAdapter headerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	protected void onResume() {
		super.onResume();
		getActionBar().setTitle(getString(R.string.settings));
		if(headerAdapter != null){
			headerAdapter.resume();
		}		
	}

	protected void onPause() {
		super.onPause();
		if(headerAdapter != null){
			headerAdapter.pause();
		}	
	}
	
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
		headerAdapter = new PreferencesHeaderAdapter(this, headers);
		super.setListAdapter(headerAdapter);
	}
}