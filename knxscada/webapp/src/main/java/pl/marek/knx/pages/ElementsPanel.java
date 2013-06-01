package pl.marek.knx.pages;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.components.PopupMenu;
import pl.marek.knx.components.PopupMenuItem;
import pl.marek.knx.components.controllers.Controller;
import pl.marek.knx.components.controllers.ControllerType;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.pages.DialogsPanel.DialogType;
@HtmlFile("elements.html")
public class ElementsPanel extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	private SubLayer subLayer;
	private RepeatingView elementsView;
	
	private ElementAjaxOperations elementAjaxOperations;

	public ElementsPanel(String id, DatabaseManager dbManager, SubLayer sublayer) {
		super(id, dbManager);
		this.subLayer = sublayer;
		loadComponents();
	}
	
	private void loadComponents(){
		removeAll();
		
		elementsView = new RepeatingView("elements");
		
		PopupMenuItem newElementItem = new PopupMenuItem("new-element-item", 
				new Model<String>(getString("add-element")), 
				new Model<String>("images/new_element.png"));
		newElementItem.add(new NewElementItemClickBehavior());
		add(newElementItem);
		
		elementAjaxOperations = new ElementAjaxOperations(this);
		add(elementAjaxOperations);
				
		if(subLayer != null){
			if(subLayer.getElements() != null){
				for(Element element: subLayer.getElements()){
					ControllerType type = ControllerType.valueOf(element.getType());
					Controller controller = type.getController(elementsView.newChildId(), element);
					controller.setElementAjaxOperations(elementAjaxOperations);
					
					PopupMenu menu = controller.getPopupMenu();
					PopupMenuItem editMenu = menu.createPopupMenuItem(getString("element.edit.menuitem"), "images/edit_icon.png", true);
					PopupMenuItem deleteMenu = menu.createPopupMenuItem(getString("element.remove.menuitem"), "images/trash_icon.png", true);

			    	editMenu.add(new ElementItemEditClickBehavior(element));
			    	deleteMenu.add(new ElementItemDeleteClickBehavior(element));
					
					elementsView.add(controller);
				}
			}
		}
		
		add(elementsView);
		
	}
	
	private class NewElementItemClickBehavior extends AjaxEventBehavior{

		private static final long serialVersionUID = 1L;

		public NewElementItemClickBehavior() {
			super("click");
		}
		
		@Override
		protected void onEvent(AjaxRequestTarget target) {
			DialogsPanel dialogs = getDialogsPanel();
			dialogs.setType(DialogType.NEW_ELEMENT);
			dialogs.setDialogPanel(new CreateEditElementFormPanel("dialog", getDBManager()));
			target.add(dialogs);
			target.appendJavaScript("initElementDialog('"+ getString("new-element") + "','"+getString("cancel")+"'); showDialog();");
		}
	}
	
	private class ElementItemEditClickBehavior extends AjaxEventBehavior{
		
		private static final long serialVersionUID = 1L;
		
		private Element element;
			
		public ElementItemEditClickBehavior(Element element) {
			super("click");
			this.element = element;
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) {
			DialogsPanel dialogs = getDialogsPanel();
			dialogs.setType(DialogType.EDIT_ELEMENT);
			dialogs.setDialogPanel(new CreateEditElementFormPanel("dialog", getDBManager(), element));
			target.add(dialogs);
			target.appendJavaScript("initElementDialog('"+ getString("edit-element") + "','"+getString("cancel")+"'); showDialog();");

		}
	}
	
	private class ElementItemDeleteClickBehavior extends AjaxEventBehavior{
		
		private static final long serialVersionUID = 1L;
		
		private Element element;
			
		public ElementItemDeleteClickBehavior(Element element) {
			super("click");
			this.element = element;
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) {

			DialogsPanel dialogs = getDialogsPanel();
			dialogs.setType(DialogType.DELETE_ELEMENT);
			dialogs.setDialogPanel(new DeleteFormPanel("dialog", getDBManager(), element));
			target.add(dialogs);
			target.appendJavaScript("initRemoveDialog('"+ getString("yes") + "', '"+getString("no")+"'); showDialog();");
			
		}
	}
	
	public void setSubLayer(SubLayer subLayer) {
		this.subLayer = subLayer;
		loadComponents();
	}
	
	public void refresh(){
		subLayer = getDBManager().getSubLayerByIdWithDependencies(getCurrentSubLayer().getId());
		loadComponents();
	}
	
	public String getElementItemIdByElement(Element element){
		String elementId = "";
		for(int i = 0; i < elementsView.size(); i++){
			Controller item = (Controller)elementsView.get(i);
			if(item.getElement().getId() == element.getId()){
				elementId = item.getMarkupId();
				break;
			}
		}
		return elementId;
	}
}
