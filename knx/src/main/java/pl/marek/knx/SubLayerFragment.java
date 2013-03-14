package pl.marek.knx;

import pl.marek.knx.database.SubLayer;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SubLayerFragment extends ListFragment{
	
	private SubLayer subLayer;
	
	private TextView subLayerDescriptionTextView;
	private ListView elementsListView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.sublayer, container, false);
        
        subLayer = getArguments().getParcelable(SubLayer.SUBLAYER);
        subLayerDescriptionTextView = (TextView) rootView.findViewById(R.id.sublayer_description_textview);
        elementsListView = (ListView) rootView.findViewById(android.R.id.list);
        
//        //TODO
//        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
//        		  "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//        		  "Linux", "OS/2" };
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
//		elementsListView.setAdapter(adapter);
		
        
        subLayerDescriptionTextView.setText(subLayer.getDescription());
        return rootView;
    }

    
}
