package pl.marek.knx;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupAddressView extends LinearLayout{
	
	private TextView labelView;
	private EditText mainGroupView;
	private EditText subGroupView;
	private EditText groupView;
	
	private TextView firstSeparatorView;
	private TextView secondSeparatorView;
	
	private GroupAddressLevel level;
		
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
		View view = layoutInflater.inflate(R.layout.group_address_view, this, true);
		
		labelView = (TextView)findViewById(R.id.group_address_label);
		mainGroupView = (EditText)view.findViewById(R.id.group_address_main_group);
		subGroupView = (EditText)view.findViewById(R.id.group_address_sub_group);
		groupView = (EditText)view.findViewById(R.id.group_address_group);
		firstSeparatorView = (TextView) view.findViewById(R.id.group_address_separator_1);
		secondSeparatorView = (TextView) view.findViewById(R.id.group_address_separator_2);
		level = GroupAddressLevel.THREE;

		
	}
	
	public void setLabel(String label){
		if(label != null){
			labelView.setText(label);
			labelView.setVisibility(View.VISIBLE);
		} else{
			labelView.setVisibility(View.GONE);
		}
	}
	
	public void setGroupAddressLevel(GroupAddressLevel level){
		this.level = level;
		setViewByLevel();
	}
	
	private void setViewByLevel(){
		switch(level){
			case TWO:
				mainGroupView.setVisibility(View.GONE);
				firstSeparatorView.setVisibility(View.GONE);
			break;
			default:
				mainGroupView.setVisibility(View.VISIBLE);
				firstSeparatorView.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	public GroupAddressLevel getGroupAddressLevel(){
		return level;
	}
	
	public void setSeparator(String separator){
		firstSeparatorView.setText(separator);
		secondSeparatorView.setText(separator);
	}
	
	public void setGroupAddress(String groupAddress){
		String[] addr = groupAddress.split("/");
		
		if(addr.length == 2){
			level = GroupAddressLevel.TWO;
		}else{
			level = GroupAddressLevel.THREE;
		}

		switch(level){
			case TWO:
				subGroupView.setText(addr[0]);
				groupView.setText(addr[1]);
			break;
			default:
				mainGroupView.setText(addr[0]);
				subGroupView.setText(addr[1]);
				groupView.setText(addr[2]);
			break;
		}
	}
	
	public String getGroupAddress(){
		
		String main = mainGroupView.getText().toString();
		String sub = subGroupView.getText().toString();
		String group = groupView.getText().toString();
		
		String address = "";
		
		switch(level){
			case TWO:
				address = String.format("%s/%s", sub, group);
			break;
			default:
				address = String.format("%s/%s/%s", main, sub, group);
			break;
		}	
		return address;
	}
	
	public enum GroupAddressLevel{
		TWO, THREE
	}
}
