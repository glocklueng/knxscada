package pl.marek.knx;

import java.util.ArrayList;

import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.interfaces.DatabaseManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SubLayerFragment extends ListFragment{
	
	private SubLayer subLayer;
	
	private DatabaseManager dbManager;
	
	private TextView subLayerDescriptionTextView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	
        View rootView = inflater.inflate(R.layout.sublayer, container, false);
        dbManager = new DatabaseManagerImpl(getActivity());
        
        subLayer = getArguments().getParcelable(SubLayer.SUBLAYER);
        subLayer = dbManager.getSubLayerByIdWithDependencies(subLayer.getId());
        
        subLayerDescriptionTextView = (TextView) rootView.findViewById(R.id.sublayer_description_textview);
        subLayerDescriptionTextView.setText(subLayer.getDescription());
        return rootView;
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);

        ArrayList<Element> elements = subLayer.getElements();
        ElementAdapter adapter = new ElementAdapter(getActivity(), elements);
        setListAdapter(adapter);
        
        getListView().setOnItemLongClickListener((ProjectActivity)getActivity());     
    }
	
	@Override
	public void onResume() {
		super.onResume();
		if(dbManager != null && !dbManager.isOpen()){
			dbManager.open();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(dbManager != null && dbManager.isOpen())
			dbManager.close();
	}
}
