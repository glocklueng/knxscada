package pl.marek.knx.preferences;

import java.net.UnknownHostException;

import pl.marek.knx.R;
import pl.marek.knx.connection.KNXConnectionSettings;
import pl.marek.knx.connection.KNXDiscoverer;
import pl.marek.knx.log.LogTags;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.knxnetip.servicetype.SearchResponse;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DiscovererFragment extends SettingsFragment implements OnPreferenceClickListener, OnItemClickListener{
	
	private Preference discoverButton;
	private DiscoverTask discoverTask;
	private TextView discovererText;
	private ProgressBar discovererProgressBar;
	private KNXInterfacesAdapter interfacesAdapter;
	private AlertDialog discoverDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.discoverer_prefs);
		loadPreferencesObjects();
	}
	
	private void loadPreferencesObjects(){
        discoverButton = getPreferenceScreen().findPreference(getString(R.string.discover_search_key));
        discoverButton.setOnPreferenceClickListener(this);
        
    }
	
	@Override
	public void onResume() {
		super.onResume();
		addActionBar();
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		interfacesAdapter = new KNXInterfacesAdapter(getActivity());
		createDiscovererDialog();
		search();
		return false;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		
		SearchResponse response = interfacesAdapter.getItem(index);
		
		KNXConnectionSettings settings = new KNXConnectionSettings(getActivity());
		try {
			settings.setRemoteEndPoint(response.getControlEndpoint().getAddress().getHostAddress(),
									   response.getControlEndpoint().getPort());
			
			((CustomEditTextPreference)getPreferenceScreen().findPreference(getString(R.string.remote_ip_key))).setSummary(settings.getRemoteEndPoint().getAddress().getHostAddress());
			
		} catch (UnknownHostException e) {
			Log.w(LogTags.KNX_CONNECTION, e.getMessage());
		}
		
		discoverTask.cancel(true);
		discoverDialog.cancel();
		
	}
	
	private void createDiscovererDialog(){
		 
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.discoverer_item_title));

		LayoutInflater factory = LayoutInflater.from(getActivity());
		View content = factory.inflate(R.layout.discover_dialog, null);

		ListView lv = (ListView) content.findViewById(R.id.discoverer_list);
		lv.setAdapter(interfacesAdapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setOnItemClickListener(this);
		
		discovererText = (TextView) content.findViewById(R.id.discoverer_text);
		discovererProgressBar = (ProgressBar) content.findViewById(R.id.discoverer_progress);
		
		builder.setView(content);
		builder.setCancelable(false);
		builder.setNegativeButton(android.R.string.cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				discoverTask.cancel(true);
			}
		});
		discoverDialog = builder.create();
		discoverDialog.show();
	}
	
	private void addInterface(SearchResponse s){
		interfacesAdapter.add(s);
		interfacesAdapter.notifyDataSetChanged();
	}
	
	private void search(){
		discoverTask = new DiscoverTask();
		discoverTask.execute();
	}
	
	private class DiscoverTask extends AsyncTask<Void, SearchResponse, Void>{

		private KNXDiscoverer discoverer;
		private KNXConnectionSettings settings;
		private boolean found = false;
		
		@Override
		protected void onPreExecute() {
			try {
				settings = new KNXConnectionSettings(getActivity());
				discoverer = new KNXDiscoverer(settings);
				
				discovererText.setText(getString(R.string.discoverer_dialog_text_searching));
			} catch (Exception e) {
				Log.w(LogTags.APPLICATION, e.getMessage());
			}
		}
		
		@Override
		protected void onCancelled() {
			if(discoverer.isSearching())
				discoverer.stopSearch();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				discoverer.startSearch(settings.getDiscoverTimeout());
				while(discoverer.isSearching()){
					SearchResponse[] s = discoverer.getSearchResponses();
					if(s.length > 0){
						found = true;
						publishProgress(s);
						discoverer.clearSearchResponses();
					}
					Thread.sleep(500);
				}
			} catch (KNXException e) {
				Log.w(LogTags.APPLICATION, e.getMessage());
			} catch(InterruptedException e){
				Log.w(LogTags.APPLICATION, e.getMessage());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			discovererProgressBar.setIndeterminate(false);

			if(!found)
				discovererText.setText(getString(R.string.discoverer_dialog_text_not_found));
			else
				discovererText.setText(getString(R.string.discoverer_dialog_text_finished));
		}
		
		@Override
		protected void onProgressUpdate(SearchResponse... values) {
			addInterface(values[0]);
		}
	}
}