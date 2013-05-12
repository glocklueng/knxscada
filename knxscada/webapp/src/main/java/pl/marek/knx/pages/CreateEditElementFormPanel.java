package pl.marek.knx.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.components.GroupAddressField;
import pl.marek.knx.components.GroupAddressField.GroupAddressLevel;
import pl.marek.knx.components.PopupMenuItem;
import pl.marek.knx.components.controllers.Controller;
import pl.marek.knx.components.controllers.ControllerType;
import pl.marek.knx.components.controllers.ElementGroupAddressType;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.interfaces.DatabaseManager;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.exception.KNXFormatException;

@HtmlFile("create_edit_element_form_panel.html")
public class CreateEditElementFormPanel  extends BasePanel{

	private static final long serialVersionUID = 1L;

	private FeedbackPanel feedbackPanel;
	private ElementForm elementForm;
	private RepeatingView elementChooser;
	private WebMarkupContainer elementChooserPreview;
	private WebMarkupContainer elementChooserView;
	
	private Element element;
	private Controller currentController;
	
	public CreateEditElementFormPanel (String componentName, DatabaseManager dbManager) {
		this(componentName, dbManager, null);	
	}

	public CreateEditElementFormPanel (String componentName, DatabaseManager dbManager, Element element) {
		super(componentName, dbManager);
		this.element = element;
		loadComponents();	
	}
	
	private void loadComponents(){
				
		if(element == null){
			element = new Element();
		}
		
		feedbackPanel = new FeedbackPanel("element-form-feedback");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		
		elementForm = new ElementForm("element-form", element);
		elementForm.setOutputMarkupId(true);
		add(elementForm);
		
		elementChooserPreview = new WebMarkupContainer("element-chooser-preview");
		elementChooserPreview.setOutputMarkupId(true);
		
		if(element.getId() > 0){
			ControllerType t = ControllerType.valueOf(element.getType());
			currentController = t.getController("element-chooser-preview-item", element);
			elementChooserPreview.add(currentController);
		}
		else{
			elementChooserPreview.add(new Label("element-chooser-preview-item"));
		}
		add(elementChooserPreview);
		
		elementChooserView = new WebMarkupContainer("element-chooser"){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				if(element.getId() > 0){
					tag.put("style", "display:none;");
				}
				super.onComponentTag(tag);
			}
		};
		elementChooser = new RepeatingView("element-chooser-item");
		for(ControllerType type: ControllerType.values()){
			Controller controller = type.getController(elementChooser.newChildId(), null);
			controller.add(new OnElementChooserItemClick(controller));
			elementChooser.add(controller);
		}
		elementChooserView.add(elementChooser);
		add(elementChooserView);
		
	}
	
	private void setElementChooserPreview(ControllerType type){
		elementChooserPreview.removeAll();
		Controller c = type.getController("element-chooser-preview-item", null);
		currentController = c;
		elementChooserPreview.add(c);
	}
	
	private class OnElementChooserItemClick extends AjaxEventBehavior{

		private static final long serialVersionUID = 1L;
		
		private Controller controller;
		
		public OnElementChooserItemClick(Controller controller) {
			super("click");
			this.controller = controller;
		}
		
		@Override
		protected void onEvent(AjaxRequestTarget target) {
			elementForm.setGroupAddressFieldsByControllerType(controller.getType());
			setElementChooserPreview(controller.getType());
			target.add(elementChooserPreview);
			target.add(elementForm);
			target.appendJavaScript("initElementChooserPreview(); hideElementChooser();");
		}
		
	}
	
	private class ElementForm extends Form<Void>{

		private static final long serialVersionUID = 1L;
		private List<String> groupAddressLevels;

		private RadioChoice<String> groupAddressType;
		private RepeatingView groupAddressFields;
		
		private Element element;
		
		public ElementForm(String id, Element element) {
			super(id);
			this.element = element;
			groupAddressLevels = Arrays.asList(getString("group-address-level-chooser-two-levels"),
											   getString("group-address-level-chooser-three-levels"));
			loadFields();
		}
		
		private void loadFields(){
			
			RequiredTextField<String> nameField = new RequiredTextField<String>("name", new PropertyModel<String>(element,"name"));
			nameField.setLabel(new ResourceModel("element-form.name.label"));
			add(nameField);
			add(new SimpleFormComponentLabel("element-name-label", nameField));

			TextArea<String> descriptionField = new TextArea<String>("description", new PropertyModel<String>(element,"description"));
			descriptionField.setLabel(new ResourceModel("element-form.description.label"));
			add(descriptionField);
			add(new SimpleFormComponentLabel("element-description-label", descriptionField));
			
			nameField.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {
				
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					currentController.setName(element.getName());
					target.add(elementChooserPreview);
					target.appendJavaScript("initElementChooserPreview();");
				}
			});
			
			descriptionField.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {
				
				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					currentController.setDescription(element.getDescription());
					target.add(elementChooserPreview);
					target.appendJavaScript("initElementChooserPreview();");
				}
			});
			
			int ordinal = getCurrentElementGroupAddressLevel().ordinal();
			groupAddressType = new RadioChoice<String>("group-address-level-chooser-options", 
													  new Model<String>(groupAddressLevels.get(ordinal)),
													  groupAddressLevels);
			
			groupAddressType.add(new ChooseAddressLevelBehavior(this));
			add(groupAddressType);
			
			groupAddressFields = new RepeatingView("group-address-row");
			groupAddressFields.setOutputMarkupId(true);
			add(groupAddressFields);
			loadGroupAddressFields();
			
			AjaxButton submitButton = new AjaxButton("element-form-submit-button", this) {

				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					boolean addressesCorrect = true;
					
					element.setProjectId(getCurrentProject().getId());
					element.setLayerId(getCurrentLayer().getId());
					element.setSubLayerId(getCurrentSubLayer().getId());
					element.setType(currentController.getType().name());
					
					ArrayList<ElementGroupAddress> addresses = new ArrayList<ElementGroupAddress>();
					for(int i=0;i<groupAddressFields.size();i++){
						AbstractItem a = (AbstractItem)groupAddressFields.get(i);
						GroupAddressField f = (GroupAddressField)a.get(1);
						f.setLevel(getCurrentGroupAddressLevel());
						if(validateGroupAddress(f)){
							addresses.add(f.getAddress());
						}else{
							addressesCorrect = false;
						}
					}
					element.setGroupAddresses(addresses);
					
					if(addressesCorrect){
						if(element.getId() == 0){
							getDBManager().addElement(element);
						} else{
							getDBManager().updateElement(element);
						}
						
						ElementsPanel panel = getElementsPanel();
						panel.refresh();
						
						target.add(panel);
						target.add(feedbackPanel);
						target.appendJavaScript("loadElements();");
					}
					
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedbackPanel);
				}
			};
			add(submitButton);	
			
			PopupMenuItem addButton = new PopupMenuItem("group-address-add-button", 
														new Model<String>(getString("group-address-add-button-label")),
														new Model<String>("images/new_item.png"));
			PopupMenuItem deleteButton = new PopupMenuItem("group-address-delete-button", 
														new Model<String>(getString("group-address-delete-button-label")),
														new Model<String>("images/trash_icon.png"));
			addButton.add(new AddAddressBehavior(this));
			deleteButton.add(new DeleteAddressBehavior(this));
			
			add(addButton);
			add(deleteButton);
			
			AjaxFormValidatingBehavior.addToAllFormComponents(this, "onkeyup", Duration.ONE_SECOND);
		}

		public void setGroupAddressFieldsByControllerType(ControllerType type){
			clearGroupAddressFields();
			ArrayList<ElementGroupAddressType> types = type.getAddressTypes();
			for(ElementGroupAddressType t: types){
				addGroupAddressField(t, null);
			}
		}
		
		public GroupAddressLevel getCurrentElementGroupAddressLevel(){
			GroupAddressLevel level = GroupAddressLevel.THREE;
			ArrayList<ElementGroupAddress> addrs = element.getGroupAddresses();
			for(ElementGroupAddress a: addrs){
				String[] addr = a.getAddress().split("/");
				if(addr.length >= 2){
					if(addr.length == 2){
						level = GroupAddressLevel.TWO;
					}
				}
			}
			return level;
		}
		
		private void clearGroupAddressFields(){
			groupAddressFields.removeAll();
		}

		public boolean validateGroupAddress(GroupAddressField field) {
			String groupAddress = field.getGroupAddress();
			try{
				new GroupAddress(groupAddress);				
			}
			catch(KNXFormatException ex){
				String label = field.getLabel().getObject();
				error(String.format(getString("wrong-group-address"),label));
				return false;
			}
			return true;
		}
		
		private void loadGroupAddressFields(){
			if(element != null){
				ArrayList<ElementGroupAddress>  addresses = element.getGroupAddresses();
				if(addresses != null){
					for(ElementGroupAddress addr : addresses){
						ElementGroupAddressType type = ElementGroupAddressType.valueOf(addr.getType());
						addGroupAddressField(type, addr);
					}
				}
			}
		}
		
		private void setGroupAddressLevel(GroupAddressLevel level){
			for(int i=0;i<groupAddressFields.size();i++){
				AbstractItem item = (AbstractItem)groupAddressFields.get(i);
				GroupAddressField field = (GroupAddressField)item.get(1);
				field.setLevel(level);
				field.refresh();
			}
		}
		
		private GroupAddressLevel getCurrentGroupAddressLevel(){
			GroupAddressLevel currLevel =GroupAddressLevel.THREE;
			String selected = groupAddressType.getDefaultModelObjectAsString();
			if(selected.equals(getString("group-address-level-chooser-two-levels"))){
				currLevel = GroupAddressLevel.TWO;
			}
			return currLevel;
		}
		
		private void addGroupAddressField(ElementGroupAddressType type, ElementGroupAddress address){
			
			AbstractItem item = new AbstractItem(groupAddressFields.newChildId());
			if(address == null){
				address = new ElementGroupAddress();
				address.setType(type.name());
			}
			GroupAddressField groupAddressField = new GroupAddressField("group-address-field", getCurrentGroupAddressLevel(), address);
			
			String label = getString(type.getLabel());
			String newLabel = getNextGroupAddressLabel(label);
			
			if(label.equals(newLabel)){
				groupAddressField.setRemovable(false);
			}
			
			groupAddressField.setOutputMarkupId(true);
			groupAddressField.setLabel(new Model<String>(newLabel));
			
			item.add(new SimpleFormComponentLabel("group-address-label", groupAddressField));
			item.add(groupAddressField);
			
			groupAddressFields.add(item);
		}
		
		private String getNextGroupAddressLabel(String base){
			String label = base;
			int counter = 1;
			for(int i=0;i<groupAddressFields.size();i++){
				AbstractItem item = (AbstractItem)groupAddressFields.get(i);
				SimpleFormComponentLabel l = (SimpleFormComponentLabel)item.get(0);
				String currentLabel = l.getDefaultModelObjectAsString();
				if(currentLabel.contains(base)){
					counter++;
				}
			}
			if(counter > 1){
				label = String.format("%s %d", base, counter);
			}
			
			return label;
		}
		
		private class ChooseAddressLevelBehavior extends AjaxFormChoiceComponentUpdatingBehavior{

			private static final long serialVersionUID = 1L;
			
			private Form<?> form;
			
			public ChooseAddressLevelBehavior(Form<?> form){
				this.form = form;
			}

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				setGroupAddressLevel(getCurrentGroupAddressLevel());
				target.add(form);
			}
			
		}
		
		private class AddAddressBehavior extends AjaxEventBehavior{

			private static final long serialVersionUID = 1L;
			
			private Form<?> form;
			
			public AddAddressBehavior(Form<?> form){
				super("click");
				this.form = form;
			}
			
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				
				ElementGroupAddressType type = ElementGroupAddressType.MAIN;
				addGroupAddressField(type, null);
				
				target.add(form);
				target.add(feedbackPanel);
			}
			
		}
		
		private class DeleteAddressBehavior extends AjaxEventBehavior{

			private static final long serialVersionUID = 1L;
			
			private Form<?> form;
			
			public DeleteAddressBehavior(Form<?> form){
				super("click");
				this.form = form;
			}
			@Override
			protected void onEvent(AjaxRequestTarget target) {
				int size = groupAddressFields.size();
				int index = size -1;
				if(index >= 0){
					AbstractItem component = (AbstractItem)groupAddressFields.get(index);
					GroupAddressField groupField = (GroupAddressField)component.get(1);
					if(groupField.isRemovable()){
						groupAddressFields.remove(component);
						target.add(form);
					}else{
						info(getString("group-address-not-removable-addresses"));
						target.add(feedbackPanel);
					}
				}
			}
			
		}
	}
	
	
	
}
