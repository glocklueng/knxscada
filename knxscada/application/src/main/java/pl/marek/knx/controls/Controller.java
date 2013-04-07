package pl.marek.knx.controls;

import pl.marek.knx.database.Element;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.interfaces.KNXTelegramListener;
import pl.marek.knx.receivers.TelegramBroadcastReceiver;
import tuwien.auto.calimero.dptxlator.DPT;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public abstract class Controller extends LinearLayout implements KNXTelegramListener{
	
	private LinearLayout mView;
	private int layoutId;
	
	protected Element element;
	protected ControllerType type;
	
	protected TelegramBroadcastReceiver telegramReceiver;
	protected boolean receiverRegistered = false;
	
	private Controller(Context context){
		super(context);
	}
	
	public Controller(Context context, Element element, ControllerType type, int layoutId) {
		this(context);
		this.layoutId = layoutId;
		this.element = element;
		this.type = type;
		initialize();
		onResume();
	}
	
	public void initialize(){
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(layoutId, this, true);
		mView = (LinearLayout)getChildAt(0);
		
		//Stupid workaround for not working onItemLongClick in ListView
		setOnLongClickListener(new OnLongClickListener() {
			
			public boolean onLongClick(View v) {
				return false;
			}
		});
		
		
	}
	
	public void setBackgroundColor(int color){
		LayerDrawable layerDrawable = (LayerDrawable)mView.getBackground();
		GradientDrawable backgroundDrawable = (GradientDrawable)layerDrawable.getDrawable(0);
		backgroundDrawable.setColor(color);
	}
	
	public LinearLayout getMainView(){
		return mView;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
		if(element != null){
			setName(element.getName());
			setDescription(element.getDescription());
		}
	}
	
	public ControllerType getType(){
		return type;
	}
	
	public void onResume(){
		if(!receiverRegistered){
			registerReceiver();
		}
	}
	
	public void onPause(){
		if(receiverRegistered){
			unregisterReceiver();
		}
	}
	
	private void registerReceiver(){
		telegramReceiver = new TelegramBroadcastReceiver(this);
		getContext().registerReceiver(telegramReceiver, new IntentFilter(KNXTelegramListener.TELEGRAM_RECEIVED));
		receiverRegistered = true;
	}
	
	private void unregisterReceiver(){
		getContext().unregisterReceiver(telegramReceiver);
		receiverRegistered = false;
	}
	
	protected void transferTelegram(String type,String address, DPT dpt, String value){
  		Intent dataIntent = new Intent(type);
  		dataIntent.putExtra(KNXDataTransceiver.GROUP_ADDRESS, address);
  		dataIntent.putExtra(KNXDataTransceiver.DPT_ID, dpt.getID());
  		if(value != null){
  			dataIntent.putExtra(KNXDataTransceiver.VALUE, value);
  		}
  		getContext().sendBroadcast(dataIntent);
	}
	
	public abstract void setName(String name);
	public abstract String getName();
	public abstract void setDescription(String description);
	public abstract String getDescription();

}
