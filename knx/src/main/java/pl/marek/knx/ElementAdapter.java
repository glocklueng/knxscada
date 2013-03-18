package pl.marek.knx;

import java.util.List;

import pl.marek.knx.controls.Controller;
import pl.marek.knx.database.Element;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ElementAdapter extends ArrayAdapter<Element>{
	
	private Context context;

	public ElementAdapter(Context context, List<Element> elements) {
		super(context, android.R.layout.simple_list_item_single_choice, elements);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			
		Element element = getItem(position);
		Controller c = element.getType().createView(context, element);
		return c;
	}

}
