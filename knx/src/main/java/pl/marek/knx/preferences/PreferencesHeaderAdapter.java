package pl.marek.knx.preferences;
import java.util.List;

import pl.marek.knx.R;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity.Header;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class PreferencesHeaderAdapter extends ArrayAdapter<Header>{
	
	static final int HEADER_TYPE_CATEGORY = 0;
	static final int HEADER_TYPE_NORMAL = 1;
	static final int HEADER_TYPE_SWITCH = 2;

	private LayoutInflater mInflater;

	public PreferencesHeaderAdapter(Context context, List<Header> objects) {
		super(context, 0, objects);

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Header header = getItem(position);
		int headerType = getHeaderType(header);
		View view = null;

		switch (headerType) {
		case HEADER_TYPE_CATEGORY:
			view = mInflater.inflate(R.layout.preference_category, parent, false);
			
			TextView catTitle = ((TextView) view.findViewById(android.R.id.title));
			catTitle.setText(header.getTitle(getContext().getResources()));
			
			break;

		case HEADER_TYPE_SWITCH:
			view = mInflater.inflate(R.layout.preference_header_switch_item, parent, false);

			ImageView imageView = ((ImageView) view.findViewById(android.R.id.icon));
			imageView.setImageResource(header.iconRes);
			
			TextView switchTitle = ((TextView) view.findViewById(android.R.id.title));
			switchTitle.setText(header.getTitle(getContext().getResources()));
			
			CharSequence summary = header.getSummary(getContext().getResources());
			TextView summaryView = ((TextView) view.findViewById(android.R.id.summary));
			summaryView.setText(summary);
			if(summary == null){
				summaryView.setVisibility(View.GONE);
			}
			
			Bundle arguments = header.fragmentArguments;
			Switch switchView = (Switch)view.findViewById(android.R.id.toggle);
			switchView.setTextOn(arguments.getString("switchTextOn"));
			switchView.setTextOff(arguments.getString("switchTextOff"));
			
			break;

		case HEADER_TYPE_NORMAL:
			view = mInflater.inflate(R.layout.preference_header_item, parent, false);
			
			ImageView headerImage = ((ImageView) view.findViewById(android.R.id.icon));
			headerImage.setImageResource(header.iconRes);
			
			TextView headerTitle = ((TextView) view.findViewById(android.R.id.title));
			headerTitle.setText(header.getTitle(getContext().getResources()));
			
			CharSequence headerSummary = header.getSummary(getContext().getResources());
			TextView headerSummaryView = ((TextView) view.findViewById(android.R.id.summary));
			headerSummaryView.setText(headerSummary);
			if(headerSummary == null){
				headerSummaryView.setVisibility(View.GONE);
			}
			break;
		}

		return view;
	}

	public static int getHeaderType(Header header) {
		if ((header.fragment == null) && (header.intent == null)) {
			return HEADER_TYPE_CATEGORY;
		} else if (header.id == android.R.id.toggle) {
			return HEADER_TYPE_SWITCH;
		} else {
			return HEADER_TYPE_NORMAL;
		}
	}
}