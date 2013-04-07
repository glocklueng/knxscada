package pl.marek.knx;

import java.util.List;

import pl.marek.knx.controls.Controller;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ControllerAdapter extends ArrayAdapter<Controller>{

	public ControllerAdapter(Context context, List<Controller> elements) {
		super(context, android.R.layout.simple_list_item_single_choice, elements);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getItem(position);
	}

}
