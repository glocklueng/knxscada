package pl.marek.knx;

import java.util.ArrayList;

import pl.marek.knx.controls.ControlType;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.SubLayer;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        		
        ArrayList<Element> elements = new ArrayList<Element>();
        for(int i=0;i<4;i++){
        	Element elem = new Element();
        	elem.setId(i);
        	elem.setName(String.format("Element %d", i));
        	elem.setDescription("Test description! Bla bla bla");
        	
        	if(i == 0){
        		elem.setType(ControlType.ON_OFF_SWITCH);
        	}else if(i == 1){
        		elem.setType(ControlType.SLIDER);
        	}else if(i == 2){
        		elem.setType(ControlType.LIGHT_ON_OFF_SWITCH);
        	}else if(i == 3){
        		elem.setType(ControlType.LIGHT_SLIDER);
        	}
        	elements.add(elem);
        	
        	
        }
        
        ElementAdapter adapter = new ElementAdapter(getActivity(), elements);
        elementsListView.setAdapter(adapter);
        
        subLayerDescriptionTextView.setText(subLayer.getDescription());
        return rootView;
    }

    
}
