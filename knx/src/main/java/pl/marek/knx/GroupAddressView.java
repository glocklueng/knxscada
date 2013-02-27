package pl.marek.knx;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class GroupAddressView extends LinearLayout{
	
	private EditText mainGroupView;
	private EditText subGroupView;
	private EditText groupView;
		
	public GroupAddressView(Context context) {
		super(context);
		init();
	}
	
	public GroupAddressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public GroupAddressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.group_address_view,this);
		
		mainGroupView = (EditText)view.findViewById(R.id.group_address_main_group); 
		subGroupView = (EditText)view.findViewById(R.id.group_address_sub_group); 
		groupView = (EditText)view.findViewById(R.id.group_address_group); 
		
	}
	
	public void setGroupAddress(String groupAddress){
		String[] addr = groupAddress.split("/");
		mainGroupView.setText(addr[0]);
		subGroupView.setText(addr[1]);
		groupView.setText(addr[2]);
	}
	
	public String getGroupAddress(){
		
		String main = mainGroupView.getText().toString();
		String sub = subGroupView.getText().toString();
		String group = groupView.getText().toString();
		
		return String.format("%s/%s/%s", main, sub, group);
	}
	
	

}
