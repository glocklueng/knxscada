package pl.marek.knx;

import java.util.List;
import pl.marek.knx.telegram.Telegram;
import tuwien.auto.calimero.Priority;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TelegramAdapter extends ArrayAdapter<Telegram>{
	
	private LayoutInflater inflater;
	private Context context;
	
	public TelegramAdapter(Context context, List<Telegram> telegrams) {
		super(context, android.R.layout.simple_list_item_single_choice, telegrams);
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Telegram telegram = getItem(position);
		View view = inflater.inflate(R.layout.telegram_item, parent, false);
		
		ImageView imageView = (ImageView)view.findViewById(R.id.telegram_item_priority_image);
		TextView srcAddrView = (TextView) view.findViewById(R.id.telegram_item_from);
		TextView dstAddrView = (TextView) view.findViewById(R.id.telegram_item_to);
		TextView valueView = (TextView) view.findViewById(R.id.telegram_item_value);
		
		srcAddrView.setText(telegram.getSourceAddress());
		dstAddrView.setText(telegram.getDestinationAddress());
		valueView.setText(telegram.getData());
		
		Priority priority = Priority.get(telegram.getPriority());
		imageView.setImageDrawable(getIconByPriority(priority));
		
		return view;
	}
	
	private Drawable getIconByPriority(Priority priority){
		Drawable icon = null;
		if(priority.equals(Priority.LOW)){
			icon = context.getResources().getDrawable(R.drawable.telegram_priority_low);
		} else if(priority.equals(Priority.NORMAL)){
			icon = context.getResources().getDrawable(R.drawable.telegram_priority_normal);
		} else if(priority.equals(Priority.SYSTEM)){
			icon = context.getResources().getDrawable(R.drawable.telegram_priority_system);
		} else if(priority.equals(Priority.URGENT)){
			icon = context.getResources().getDrawable(R.drawable.telegram_priority_urgent);
		}
		return icon;
	}

}
