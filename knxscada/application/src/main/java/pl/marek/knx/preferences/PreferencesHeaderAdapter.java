package pl.marek.knx.preferences;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity.Header;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class PreferencesHeaderAdapter extends ArrayAdapter<Header>{
	
	private ArrayList<SwitchStateListener> switchListeners;
	
	public PreferencesHeaderAdapter(Context context, List<Header> objects) {
		super(context, 0, objects);
		switchListeners = new ArrayList<SwitchStateListener>();
	}
	
	

	public View getView(int position, View convertView, ViewGroup parent) {
		Header header = getItem(position);
		HeaderBuilder builder = new HeaderBuilder(getContext(), header, parent);
		View view = builder.getView();
		
		Bundle bundle = header.fragmentArguments;
		if((header.id == android.R.id.toggle) && (bundle != null)){
			SwitchStateListener switchListener = new SwitchStateListener(getContext(), 
													 builder.getSwitchView(), 
													 builder.getSummaryView(), 
													 bundle);
			switchListeners.add(switchListener);
		}
		return view;
	}
	
	public void resume(){
		for(SwitchStateListener listener: switchListeners){
			listener.resume();
		}
	}
	
	public void pause(){
		for(SwitchStateListener listener: switchListeners){
			listener.pause();
		}
	}
}