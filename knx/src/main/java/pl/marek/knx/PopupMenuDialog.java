package pl.marek.knx;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class PopupMenuDialog extends Dialog implements OnItemClickListener{
	
	private PopupMenuListAdapter adapter;
	private PopupMenuItemListener listener;
	private TextView titleView;
	private ListView listView;
	private ArrayList<PopupMenuItem> items;
	private String title;

	public PopupMenuDialog(Context context, String title, ArrayList<PopupMenuItem> items) {
		super(context, R.style.dialogTheme);
		this.items = items;
		this.title = title;
	}
	
	public PopupMenuDialog(Context context, ArrayList<PopupMenuItem> items) {
		this(context, "", items);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_menu);
		setDialogSize();
		
		titleView = (TextView)findViewById(R.id.dialog_title_text);
		setTitle(title);
		listView = (ListView)findViewById(android.R.id.list);
		
		adapter = new PopupMenuListAdapter(getContext(), items);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	
	private void setDialogSize(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = (int)(metrics.widthPixels * 0.95f);
		getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
	}
	
	public void setTitle(String title){
		titleView.setText(title);
	}
	
	public void setPopupMenuItemListener(PopupMenuItemListener listener){
		this.listener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> v, View view, int position, long id) {
		if(listener != null)
			listener.onPopupMenuItemClick(position, adapter.getItem(position));
		this.dismiss();
	}
	
	public interface PopupMenuItemListener{
		public void onPopupMenuItemClick(int position, PopupMenuItem item);
	}
	
	public class PopupMenuListAdapter extends ArrayAdapter<PopupMenuItem>{
		
		private LayoutInflater inflater;
		
		public PopupMenuListAdapter(Context context, ArrayList<PopupMenuItem> items) {
			super(context, android.R.layout.simple_list_item_single_choice, items);
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			PopupMenuItem item = getItem(position);
			
			View view = inflater.inflate(R.layout.popup_menu_item, parent, false);
			
			ImageView iconView = (ImageView)view.findViewById(R.id.popup_menu_item_icon);
			TextView nameView = (TextView)view.findViewById(R.id.popup_menu_item_name);
			
			nameView.setText(item.getName());
			Drawable icon = item.getIcon();
			if(icon != null){
				iconView.setImageDrawable(icon);
			}
			return view;
		}
	}
}

class PopupMenuItem{
	
	private String name;
	private Drawable icon;
	
	public PopupMenuItem() {}
	
	public PopupMenuItem(String name, Drawable icon) {
		this.name = name;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
}