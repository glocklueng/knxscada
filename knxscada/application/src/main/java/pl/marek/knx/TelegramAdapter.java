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
		
		ImageView imageView = null;
		TextView srcAddrView = null;
		TextView dstAddrView = null;
		TextView valueView = null;
		
		ViewHolder holder = (ViewHolder)view.getTag();
		if(holder != null){
			imageView = holder.getImage();
			srcAddrView = holder.getSrcAddr();
			dstAddrView = holder.getDstAddr();
			valueView = holder.getValue();
		
		} else{
			imageView = (ImageView)view.findViewById(R.id.telegram_item_priority_image);
			srcAddrView = (TextView) view.findViewById(R.id.telegram_item_from);
			dstAddrView = (TextView) view.findViewById(R.id.telegram_item_to);
			valueView = (TextView) view.findViewById(R.id.telegram_item_value);
			holder = new ViewHolder(imageView, srcAddrView, dstAddrView, valueView);
			view.setTag(holder);
		}
		
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
	
	private class ViewHolder{
		private ImageView image;
		private TextView srcAddr;
		private TextView dstAddr;
		private TextView value;
		
		public ViewHolder(ImageView image, TextView srcAddr, TextView dstAddr, TextView value){
			this.image = image;
			this.srcAddr = srcAddr;
			this.dstAddr = dstAddr;
			this.value = value;
		}

		public ImageView getImage() {
			return image;
		}

		public TextView getSrcAddr() {
			return srcAddr;
		}

		public TextView getDstAddr() {
			return dstAddr;
		}

		public TextView getValue() {
			return value;
		}
	}

}
