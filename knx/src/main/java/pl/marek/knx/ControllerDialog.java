package pl.marek.knx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import pl.marek.knx.GroupAddressLevelChooser.OnGroupAddressLevelChange;
import pl.marek.knx.GroupAddressView.GroupAddressLevel;
import pl.marek.knx.controls.ControllerType;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.database.ElementGroupAddress.ElementGroupAddressType;
import pl.marek.knx.utils.MessageDialog;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.exception.KNXException;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ControllerDialog extends Dialog implements View.OnClickListener, OnGroupAddressLevelChange{

	
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
	
	private boolean editMode;
	private Element element;
	private ControllerType type;
	
	private OnControllerDialogApproveListener listener;
	
	public ControllerDialog(Context context, Element element) {
		this(context, false, element);
	}
	
	public ControllerDialog(Context context, boolean editMode, Element element) {
		super(context, R.style.dialogTheme);
		this.editMode = editMode;
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
	}
	
	private void initValues(){
		if(editMode){
			approveButton.setText(getContext().getString(R.string.dialog_controller_edit_button));
		} else{
			addMainGroupAddress();
		}
		if(element != null){
			nameView.setText(element.getName());
			descriptionView.setText(element.getDescription());
			type = element.getType();
			ArrayList<ElementGroupAddress> addresses = element.getGroupAddresses();
			for(ElementGroupAddress a: addresses){
				GroupAddressView v = new GroupAddressView(getContext());
				v.setGroupAddress(a.getAddress());				
				v.setLabel(getNextLabel(a.getType()));
				addGroupAddressView(v, a.getType());
			}
		}
		
		
	}
	
	public void setOnControllerDialogApproveListener(OnControllerDialogApproveListener listener){
		this.listener = listener;
	}
	
	private void setDialogSize(){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = (int)(metrics.widthPixels * 0.95f);
		getWindow().setLayout(width, LayoutParams.WRAP_CONTENT);
	}
	
	public void addMainGroupAddress(){
		GroupAddressView mainGroupAddress = new GroupAddressView(getContext());
		mainGroupAddress.setLabel(getNextLabel(ElementGroupAddressType.MAIN));
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
	
	public ArrayList<ElementGroupAddress> getGroupAddresses(){
		ArrayList<ElementGroupAddress> groups = new ArrayList<ElementGroupAddress>();
		
		Iterator<Entry<GroupAddressView, ElementGroupAddressType>> iterator = groupAddresses.entrySet().iterator();
		
		while(iterator.hasNext()){
			Entry<GroupAddressView, ElementGroupAddressType> entry = iterator.next();
			
			GroupAddressView groupAddressView = entry.getKey();
			ElementGroupAddressType addressType = entry.getValue();
			
			ElementGroupAddress group = new ElementGroupAddress();
			group.setAddress(groupAddressView.getGroupAddress());
			group.setType(addressType);
			
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
	 
	@Override
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
							listener.onControllerDialogEditAction(element);
						}
					}else{
						Element element = new Element();
						element.setName(getName());
						element.setDescription(getDescription());
						element.setGroupAddresses(getGroupAddresses());
						element.setDeviceAddress("");
						element.setType(type);
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

	@Override
	public void onGroupAddressLevelChange(GroupAddressLevel level) {
		Set<GroupAddressView> groupsView = groupAddresses.keySet();
		Iterator<GroupAddressView> iterator = groupsView.iterator();
		while(iterator.hasNext()){
			GroupAddressView g = iterator.next();
			g.setGroupAddressLevel(level);
		}
	}
	
	public interface OnControllerDialogApproveListener{
		public void onControllerDialogAddAction(Element element);
		public void onControllerDialogEditAction(Element element);
	}
}
