package pl.marek.knx.preferences;

import pl.marek.knx.R;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity.Header;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class HeaderBuilder {
	
	private View view;
	private Context context;
	private Header header;
	private HeaderView headerResource;
	
	public HeaderBuilder(Context context, Header header, ViewGroup parent){
		this.context = context;
		this.header = header;
	    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    headerResource = getHeaderType(header);
	    view = inflater.inflate(headerResource.getResourceId(), parent, false);
	}
	
	public View getView(){
		headerResource.createView(this);
		return view;
	}
	
	public void createTitleView(){
		getTitleView().setText(header.getTitle(context.getResources()));
	}
	
	public TextView getTitleView(){
		TextView title = ((TextView) view.findViewById(android.R.id.title));
		return title;
	}
	
	public void createIconView(){
		getIconView().setImageResource(header.iconRes);
	}
	
	public ImageView getIconView(){
		ImageView icon = ((ImageView) view.findViewById(android.R.id.icon));
		return icon;
	}
	
	public void createSummaryView(){
		CharSequence summary = header.getSummary(context.getResources());
		if(summary == null){
			getSummaryView().setVisibility(View.GONE);
		}
		getSummaryView().setText(summary);
	}
	
	public TextView getSummaryView(){
		TextView summaryView = ((TextView) view.findViewById(android.R.id.summary));
		return summaryView;
	}
	
	public void createSwitchView(){
		Bundle arguments = header.fragmentArguments;
		getSwitchView().setTextOn(arguments.getString("switchTextOn"));
		getSwitchView().setTextOff(arguments.getString("switchTextOff"));
	}
	
	public Switch getSwitchView(){
		Switch switchView = (Switch)view.findViewById(android.R.id.toggle);
		return switchView;
	}
	
	public static HeaderView getHeaderType(Header header) {
		if (header.id == R.id.header_category) {
			return HeaderView.CATEGORY;
		} else if (header.id == android.R.id.toggle) {
			return HeaderView.SWITCH;
		} else {
			return HeaderView.ITEM;
		}
	}
}
