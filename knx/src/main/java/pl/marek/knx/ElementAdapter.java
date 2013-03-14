package pl.marek.knx;

import java.util.List;

import pl.marek.knx.database.Element;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ElementAdapter extends ArrayAdapter<Element>{
	
	private Context context;
	private LayoutInflater inflater;

	public ElementAdapter(Context context, List<Element> elements) {
		super(context, android.R.layout.simple_list_item_single_choice, elements);
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			
		
		
		return super.getView(position, convertView, parent);
	}

}
