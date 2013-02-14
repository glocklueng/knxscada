package pl.marek.knx.preferences;

import tuwien.auto.calimero.knxnetip.servicetype.SearchResponse;
import pl.marek.knx.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class KNXInterfacesAdapter extends ArrayAdapter<SearchResponse>{
	
	private LayoutInflater inflater;

	public KNXInterfacesAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_single_choice);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		SearchResponse response = getItem(position);
		
		View view = inflater.inflate(R.layout.knx_interface_item, parent, false);
		TextView nameView = (TextView) view.findViewById(R.id.knx_interface_item_name);
		TextView addressView = (TextView) view.findViewById(R.id.knx_interface_item_address);
		
		nameView.setText(response.getDevice().getName());
		addressView.setText(response.getControlEndpoint().getAddress().getHostAddress());
		
		return view;
	}
}
