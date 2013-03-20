package pl.marek.knx;

import pl.marek.knx.GroupAddressView.GroupAddressLevel;
import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class GroupAddressLevelChooser extends LinearLayout implements CompoundButton.OnCheckedChangeListener{

	private GroupAddressLevel currentLevel;
	
	private TextView labelView;
	private RadioButton button2;
	private RadioButton button3;
	private OnGroupAddressLevelChange listener;

	public GroupAddressLevelChooser(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.GroupAddressLevelChooser, 0, 0);

		try {
			int level = a.getInteger( R.styleable.GroupAddressLevelChooser_level, 3);
			if (level == 2) {
				currentLevel = GroupAddressLevel.TWO;
			} else {
				currentLevel = GroupAddressLevel.THREE;
			}

		} finally {
			a.recycle();
		}
		init();
	}

	private void init(){
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.group_address_level_chooser, this, true);
		
		labelView = (TextView)view.findViewById(R.id.group_address_level_label);
		button2 = (RadioButton)view.findViewById(R.id.group_address_level_2_radio);
		button3 = (RadioButton)view.findViewById(R.id.group_address_level_3_radio);
		button2.setOnCheckedChangeListener(this);
		button3.setOnCheckedChangeListener(this);
		if(currentLevel == GroupAddressLevel.TWO){
			button2.setChecked(true);
		}else{
			button3.setChecked(true);
		}	
	}
	
	public void setOnGroupAddressLevelChangeListener(OnGroupAddressLevelChange listener){
		this.listener = listener;
		updateListenerLevel();
	}
	
	public void setLabel(String label){
		labelView.setText(label);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			if(buttonView.equals(button2)){
				currentLevel = GroupAddressLevel.TWO;
			}else if(buttonView.equals(button3)){
				currentLevel = GroupAddressLevel.THREE;
			}
			updateListenerLevel();
		}
	}
	
	private void updateListenerLevel(){
		if(listener != null){
			listener.onGroupAddressLevelChange(currentLevel);
		}
	}
	
	public interface OnGroupAddressLevelChange{
		public void onGroupAddressLevelChange(GroupAddressLevel level);
	}
}
