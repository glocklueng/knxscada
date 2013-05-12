package pl.marek.knx;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class IconPickerDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener{
	
	private GridView iconsGridView;
	private TextView titleView;
	private Button cancelButton;
	private OnIconPickListener listener;
	
	private ArrayList<Integer> icons;
	
	public IconPickerDialog(Context context) {
		super(context, R.style.dialogTheme);
		icons = new ArrayList<Integer>();
	}
	
	public IconPickerDialog(Context context, ArrayList<Integer> iconsRes) {
		super(context, R.style.dialogTheme);
		icons = iconsRes;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.icon_picker);

	    iconsGridView = (GridView) findViewById(R.id.icon_gridview);
	    iconsGridView.setAdapter(new IconAdapter(getContext(),icons));
	    iconsGridView.setOnItemClickListener(this);
	    
	    titleView = (TextView)findViewById(R.id.dialog_title_text);
	    titleView.setText(R.string.icon_picker_title);
	    
	    cancelButton = (Button)findViewById(R.id.icon_picker_cancel_button);
	    cancelButton.setOnClickListener(this);
	  
	}
	
	public void setIcons(ArrayList<Integer> iconsRes){
		this.icons = iconsRes;
	}
	
	public ArrayList<Integer> getIcons(){
		return icons;
	}
	
	public void setOnIconPickListener(OnIconPickListener listener){
		this.listener = listener;
	}
	
	public void onClick(View v) {
		cancel();
	}
	
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		if(listener != null){
			listener.onIconPick(icons.get(position));
		}
		dismiss();
    }
	
	public interface OnIconPickListener{
		public void onIconPick(int iconRes);
	}
	
	public class IconAdapter extends BaseAdapter{
		
		private Context context;
		private ArrayList<Integer> icons;
		
		public IconAdapter(Context context, ArrayList<Integer> icons){
			this.context = context;
			this.icons = icons;
		}
		
	    public int getCount() {
	        return icons.size();
	    }

	    public Object getItem(int position) {
	        return icons.get(position);
	    }

	    public long getItemId(int position) {
	        return icons.get(position);
	    }
		
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {
	            imageView = new ImageView(context);
	            imageView.setLayoutParams(new GridView.LayoutParams(48, 48));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	            convertView = imageView;
	        } else {
	            imageView = (ImageView) convertView;
	        }

	        int iconRes = (Integer)getItem(position);
	        imageView.setImageResource(iconRes);
	        return convertView;
	    }
		
	}
}
