package pl.marek.knx;

import pl.marek.knx.database.SubLayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SubLayerFragment extends Fragment{
	
	private SubLayer subLayer;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	
        View rootView = inflater.inflate(R.layout.sublayer, container, false);
        
        subLayer = getArguments().getParcelable(SubLayer.SUBLAYER);
        
        ((TextView) rootView.findViewById(R.id.test_text)).setText(subLayer.getDescription());
        return rootView;
    }
}
