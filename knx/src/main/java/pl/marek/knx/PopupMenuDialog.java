package pl.marek.knx;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class PopupMenuDialog extends BaseDialog implements OnItemClickListener, View.OnClickListener{
	
	private PopupMenuListAdapter adapter;
	private PopupMenuItemListener listener;
	private ImageView iconView;
	private TextView titleView;
	private ListView listView;
	private Button cancelButton;
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
		
		iconView = (ImageView)findViewById(R.id.dialog_title_icon);
		titleView = (TextView)findViewById(R.id.dialog_title_text);
		setTitle(title);
		listView = (ListView)findViewById(android.R.id.list);
		cancelButton = (Button)findViewById(R.id.popup_menu_cancel_button);
		cancelButton.setOnClickListener(this);
		
		adapter = new PopupMenuListAdapter(getContext(), items);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
		
	public void setTitle(String title){
		titleView.setText(title);
	}
	
	public void setIcon(int iconRes){
		iconView.setImageResource(iconRes);
	}
	
	public void setPopupMenuItemListener(PopupMenuItemListener listener){
		this.listener = listener;
	}
	
	@Override
	public void onClick(View v) {
		if(v.equals(cancelButton)){
			cancel();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> v, View view, int position, long id) {
		if(listener != null)
			listener.onPopupMenuItemClick(position, adapter.getItem(position));
		this.dismiss();
	}
	
	public void showCancelButton(boolean show){
		if(show){
			cancelButton.setVisibility(View.VISIBLE);
		}else{
			cancelButton.setVisibility(View.GONE);
		}
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
			int icon = item.getIcon();
			if(icon != -1){
				iconView.setImageResource(icon);
			}
			return view;
		}
	}
}

class PopupMenuItem{
	
	private int id;
	private String name;
	private int icon = -1;
	
	public PopupMenuItem() {}
	
	public PopupMenuItem(int id, String name, int icon) {
		this.id = id;
		this.name = name;
		this.icon = icon;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}
}