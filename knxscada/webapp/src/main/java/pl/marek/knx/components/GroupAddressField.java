package pl.marek.knx.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.ElementGroupAddress;

@HtmlFile("components/groupaddress.html")
public class GroupAddressField extends FormComponentPanel<String>{

	private static final long serialVersionUID = 1L;
	
	private TextField<String> mainGroup;
	private TextField<String> subGroup;
	private TextField<String> group;
	
	private Label mainGroupSeparator;
	private Label groupSeparator;
	
	private String separator;
	
	private boolean removable;
	private GroupAddressLevel level;
	private ElementGroupAddress address;
	
	public GroupAddressField(String id) {
		this(id, GroupAddressLevel.THREE);
	}
	
	public GroupAddressField(String id, GroupAddressLevel level) {
		this(id, level, null);
	}
	
	public GroupAddressField(String id, GroupAddressLevel level, ElementGroupAddress address) {
		super(id);
		separator = "/";
		removable = true;
		this.level = level;
		this.address = address;
		loadComponents();
		
		if(address != null){
			setGroupAddress(address.getAddress());
		}
	}
	
	private void loadComponents(){
		removeAll();
		setModel(new Model<String>());
		mainGroup = new TextField<String>("main-group", new Model<String>());
		subGroup = new TextField<String>("sub-group", new Model<String>());
		group = new TextField<String>("group", new Model<String>());
		
		mainGroupSeparator = new Label("main-group-separator", new Model<String>(separator));
		groupSeparator = new Label("group-separator", new Model<String>(separator));
		
		add(mainGroup);
		add(mainGroupSeparator);
		add(subGroup);
		add(groupSeparator);
		add(group);
		
		switch(level){
			case TWO:
				mainGroup.setVisible(false);
				mainGroupSeparator.setVisible(false);
			break;
			default:
				mainGroup.setVisible(true);
				mainGroupSeparator.setVisible(true);
			break;
		}
		
		addFieldUpdateBehavior();
	}
	
	private void addFieldUpdateBehavior(){
		mainGroup.add(new FieldUpdateBehavior());
		subGroup.add(new FieldUpdateBehavior());
		group.add(new FieldUpdateBehavior());
		add(new FieldUpdateBehavior());
	}
	
    @Override
    protected void convertInput() {
	   	address.setAddress(getGroupAddress());
        setConvertedInput(getGroupAddress()); 
    }
	
	public void refresh(){
		loadComponents();
	}
	
	public void setSeparator(String separator){
		this.separator = separator;
	}
	
	public boolean isRemovable(){
		return removable;
	}
	
	public void setRemovable(boolean removable){
		this.removable = removable;
	}
	
	public void setLevel(GroupAddressLevel level){
		this.level = level;
	}

	public ElementGroupAddress getAddress() {
		address.setAddress(getGroupAddress());
		return address;
	}

	public void setAddress(ElementGroupAddress address) {
		this.address = address;
	}

	public void setGroupAddress(String groupAddress){
		if(groupAddress != null && !groupAddress.isEmpty()){
			String[] addr = groupAddress.split("/");
			if(addr.length >= 2){
				if(addr.length == 2){
					level = GroupAddressLevel.TWO;
				}else{
					level = GroupAddressLevel.THREE;
				}
		
				switch(level){
					case TWO:
						subGroup.setDefaultModelObject(addr[0]);
						group.setDefaultModelObject(addr[1]);
					break;
					default:
						mainGroup.setDefaultModelObject(addr[0]);
						subGroup.setDefaultModelObject(addr[1]);
						group.setDefaultModelObject(addr[2]);
					break;
				}
			}
		}
	}
	
	public String getGroupAddress(){
		
		String main = mainGroup.getDefaultModelObjectAsString();
		String sub = subGroup.getDefaultModelObjectAsString();
		String grp = group.getDefaultModelObjectAsString();
				
		String address = "";
		
		switch(level){
			case TWO:
				address = String.format("%s/%s", sub, grp);
			break;
			default:
				address = String.format("%s/%s/%s", main, sub, grp);
			break;
		}	
		return address;
	}
	
	public enum GroupAddressLevel{
		TWO, THREE
	}
	
	private class FieldUpdateBehavior extends AjaxFormComponentUpdatingBehavior{
		
		private static final long serialVersionUID = 1L;
		
		public FieldUpdateBehavior() {
			super("onkeyup");
		}
		@Override
		protected void onUpdate(AjaxRequestTarget target) {}
	}
}