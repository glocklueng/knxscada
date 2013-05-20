package pl.marek.knx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import pl.marek.knx.GroupAddressLevelChooser.OnGroupAddressLevelChange;
import pl.marek.knx.GroupAddressView.GroupAddressLevel;
import pl.marek.knx.controls.ControllerType;
import pl.marek.knx.controls.ElementGroupAddressType;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.utils.MessageDialog;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.exception.KNXException;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ControllerDialog extends BaseDialog implements View.OnClickListener, OnGroupAddressLevelChange{

	
	private TextView titleView;
	private ImageView titleIconView;
	private GroupAddressLevelChooser groupAddressLevelChooser;
	private LinearLayout groupAddressesView;
	private LinkedHashMap<GroupAddressView, ElementGroupAddressType> groupAddresses;
	private EditText nameView;
	private EditText descriptionView;
	private Button approveButton;
	private Button cancelButton;
	private Button addAddressButton;
	private Button removeAddressButton;
	private LinearLayout extraFieldsView;
	
	private EditText maxValueView;
	
	private boolean editMode;
	private Element element;
	private ControllerType type;
	private ArrayList<ElementGroupAddressType> addressTypes;
	private GroupAddressLevel currentGroupAddressLevel = GroupAddressLevel.THREE;
	
	private OnControllerDialogApproveListener listener;
	
	public ControllerDialog(Context context, ArrayList<ElementGroupAddressType> addressTypes) {
		super(context, R.style.dialogTheme);
		this.editMode = false;
		this.addressTypes = addressTypes;
	}
	
	public ControllerDialog(Context context, Element element) {
		super(context, R.style.dialogTheme);
		if(element != null){
			this.editMode = true;
		}
		this.element = element;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.controller_dialog);
		setDialogSize();
		init();
		initValues();
	}
	
	private void init(){
		groupAddresses = new LinkedHashMap<GroupAddressView, ElementGroupAddressType>();
		
		titleView = (TextView)findViewById(R.id.dialog_title_text);
		titleIconView = (ImageView)findViewById(R.id.dialog_title_icon);
		groupAddressLevelChooser = (GroupAddressLevelChooser)findViewById(R.id.group_address_level_chooser);
		groupAddressLevelChooser.setOnGroupAddressLevelChangeListener(this);
		
		groupAddressesView = (LinearLayout)findViewById(R.id.dialog_controller_group_addresses);
		nameView = (EditText)findViewById(R.id.dialog_controller_name_edittext);
		descriptionView = (EditText)findViewById(R.id.dialog_controller_description_edittext);
		approveButton = (Button)findViewById(R.id.dialog_controller_create_edit_button);
		cancelButton = (Button)findViewById(R.id.dialog_controller_cancel_button);
		addAddressButton = (Button)findViewById(R.id.dialog_controller_add_address_button);
		removeAddressButton = (Button)findViewById(R.id.dialog_controller_remove_address_button);
		approveButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		addAddressButton.setOnClickListener(this);
		removeAddressButton.setOnClickListener(this);
		
		extraFieldsView = (LinearLayout)findViewById(R.id.dialog_controller_extra_data_fields);
		maxValueView = (EditText)findViewById(R.id.dialog_controller_max_value);
		
	}
	
	private void initValues(){
		if(editMode){
			approveButton.setText(getContext().getString(R.string.dialog_controller_edit_button));
			nameView.setText(element.getName());
			descriptionView.setText(element.getDescription());
			type = ControllerType.valueOf(element.getType());
			ArrayList<ElementGroupAddress> addresses = element.getGroupAddresses();
			for(ElementGroupAddress a: addresses){
				GroupAddressView v = new GroupAddressView(getContext());
				v.setGroupAddress(a.getAddress());
				ElementGroupAddressType aType = ElementGroupAddressType.valueOf(a.getType());
				v.setLabel(getNextLabel(aType));
				addGroupAddressView(v, aType);
				currentGroupAddressLevel = v.getGroupAddressLevel();
			}
			maxValueView.setText(String.valueOf(element.getMaxValue()));
			groupAddressLevelChooser.setLevel(currentGroupAddressLevel);
		} else{
			if(addressTypes == null || addressTypes.isEmpty()){
				addMainGroupAddress();
			}else{
				for(ElementGroupAddressType type : addressTypes){
					GroupAddressView v = new GroupAddressView(getContext());
					v.setLabel(getNextLabel(type));
					addGroupAddressView(v, type);
				}
			}
		}
	}
	
	public void setOnControllerDialogApproveListener(OnControllerDialogApproveListener listener){
		this.listener = listener;
	}
	
	public void addMainGroupAddress(){
		GroupAddressView mainGroupAddress = new GroupAddressView(getContext());
		mainGroupAddress.setLabel(getNextLabel(ElementGroupAddressType.MAIN));
		mainGroupAddress.setGroupAddressLevel(currentGroupAddressLevel);
		addGroupAddressView(mainGroupAddress, ElementGroupAddressType.MAIN);
	}
	
	public void removeMainGroupAddress(){
		
		GroupAddressView mainGroupAddress = null;
		int countAddresses = 0;
		Iterator<Entry<GroupAddressView,ElementGroupAddressType>>  iterator = groupAddresses.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<GroupAddressView,ElementGroupAddressType> e = iterator.next();
			if(e.getValue().equals(ElementGroupAddressType.MAIN)){
				mainGroupAddress = e.getKey();
				countAddresses++;
			}
		}
		if(countAddresses > 1){
			removeGroupAddressView(mainGroupAddress);
		}
		
	}
	
	public void addGroupAddressView(GroupAddressView view, ElementGroupAddressType type){
		groupAddresses.put(view, type);
		groupAddressesView.addView(view);
	}
	
	public void removeGroupAddressView(GroupAddressView view){
		groupAddresses.remove(view);
		groupAddressesView.removeView(view);
	}
	
	public String getNextLabel(ElementGroupAddressType type){;
		Iterator<ElementGroupAddressType> iterator = groupAddresses.values().iterator();
		int counter = 1;
		while(iterator.hasNext()){
			ElementGroupAddressType v = iterator.next();
			if(v.equals(type)){
				counter++;
			}
		}
		String label = type.getLabel(getContext());
		if(counter > 1){
			label = String.format("%s %d", label, counter);
		}
		return label;
	}
	
	public void setType(ControllerType type){
		this.type = type;
		if(type.equals(ControllerType.SLIDER) || type.equals(ControllerType.LIGHT_SLIDER)){
			extraFieldsView.setVisibility(View.VISIBLE);
		}else{
			extraFieldsView.setVisibility(View.GONE);
		}
	}
	
	public void setTitle(String title){
		titleView.setText(title);
	}
	
	public void setTitleIcon(int iconRes){
		titleIconView.setImageResource(iconRes);
	}
	
	public void setName(String name){
		nameView.setText(name);
	}
	
	public String getName(){
		return nameView.getText().toString();
	}
	
	private boolean validateName(){
		String name = getName();
		if("".equals(name.trim())){
			return false;
		}
		return true;
	}
	
	private void showEmptyNameDialog(){
		new MessageDialog(getContext()).showDialog(
				getContext().getString(R.string.dialog_controller_name_empty_title), 
				getContext().getString(R.string.dialog_controller_name_empty), 
				getContext().getResources().getDrawable(android.R.drawable.ic_dialog_alert));
	}
	
	public void setDescription(String description){
		descriptionView.setText(description);
	}
	
	public String getDescription(){
		return descriptionView.getText().toString();
	}
	
	public double getMaxValue(){
		String max = "0";
		if(type.equals(ControllerType.SLIDER) || type.equals(ControllerType.LIGHT_SLIDER)){
			max = maxValueView.getText().toString();	
		}
		return Double.parseDouble(max);
	}
	
	public ArrayList<ElementGroupAddress> getGroupAddresses(){
		ArrayList<ElementGroupAddress> groups = new ArrayList<ElementGroupAddress>();
		
		Iterator<Entry<GroupAddressView, ElementGroupAddressType>> iterator = groupAddresses.entrySet().iterator();
		
		while(iterator.hasNext()){
			Entry<GroupAddressView, ElementGroupAddressType> entry = iterator.next();
			
			GroupAddressView groupAddressView = entry.getKey();
			ElementGroupAddressType addressType = entry.getValue();
			
			ElementGroupAddress group = new ElementGroupAddress();
			group.setAddress(groupAddressView.getGroupAddress());
			group.setType(addressType.name());
			
			groups.add(group);
		}
		
		return groups;
	}
	
	private boolean validateGroupAddresses(){
		ArrayList<ElementGroupAddress> addresses = getGroupAddresses();
		for(ElementGroupAddress address: addresses){
			try{
				new GroupAddress(address.getAddress());
			} catch(KNXException ex){
				return false;
			}
		}
		return true;
	}
	
	private void showWrongAddressDialog(){
		new MessageDialog(getContext()).showDialog(
				getContext().getString(R.string.dialog_controller_wrong_address_title), 
				getContext().getString(R.string.dialog_controller_wrong_address), 
				getContext().getResources().getDrawable(android.R.drawable.ic_dialog_alert));
	}
	
	private boolean validate(){
		if(!validateName()){
			showEmptyNameDialog();
			return false;
		}
		if(!validateGroupAddresses()){
			showWrongAddressDialog();
			return false;
		}
		return true;
	}
	 
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.dialog_controller_create_edit_button:
				if(listener != null && validate()){
					if(editMode){
						if(element != null){
							element.setName(getName());
							element.setDescription(getDescription());
							element.setGroupAddresses(getGroupAddresses());
							element.setDeviceAddress("");
							element.setMaxValue(getMaxValue());
							listener.onControllerDialogEditAction(element);
						}
					}else{
						Element element = new Element();
						element.setName(getName());
						element.setDescription(getDescription());
						element.setGroupAddresses(getGroupAddresses());
						element.setDeviceAddress("");
						element.setMaxValue(getMaxValue());
						element.setType(type.name());
						listener.onControllerDialogAddAction(element);
					}
					dismiss();
				}
				break;
			case R.id.dialog_controller_cancel_button:
				cancel();
				break;
			case R.id.dialog_controller_add_address_button:
				addMainGroupAddress();
				break;
			case R.id.dialog_controller_remove_address_button:
				removeMainGroupAddress();
				break;
		}
	}

	public void onGroupAddressLevelChange(GroupAddressLevel level) {
		Set<GroupAddressView> groupsView = groupAddresses.keySet();
		Iterator<GroupAddressView> iterator = groupsView.iterator();
		while(iterator.hasNext()){
			GroupAddressView g = iterator.next();
			g.setGroupAddressLevel(level);
		}
		currentGroupAddressLevel = level;
	}
	
	public interface OnControllerDialogApproveListener{
		public void onControllerDialogAddAction(Element element);
		public void onControllerDialogEditAction(Element element);
	}
}
