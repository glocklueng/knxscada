package pl.marek.knx.components.controllers;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.panel.Panel;
import pl.marek.knx.components.PopupMenu;
import pl.marek.knx.database.Element;
import pl.marek.knx.pages.ElementsPanel.ElementDragAndDropBehavior;
import pl.marek.knx.pages.ElementsPanel.ElementRemoveBehavior;

public abstract class Controller extends Panel {

	private static final long serialVersionUID = 1L;
	
	private PopupMenu menu;
	
	private ElementDragAndDropBehavior dragAndDropBehavior;
	private ElementRemoveBehavior removeBehavior;
	
	private Element element;
	protected ControllerType type;

	public Controller(String id, Element element, ControllerType type) {
		super(id);
		this.element = element;
		this.type = type;
		
		setOutputMarkupId(true);
		loadComponents();
	}

	private void loadComponents(){
		menu = new PopupMenu("popup-menu");
		menu.setOutputMarkupId(true);
		
		add(menu);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		String htmlClass = tag.getAttribute("class");
		if(htmlClass.contains("popup-menu-trigger")){
			tag.put("popupMenuId", menu.getMarkupId());
		}
		if(dragAndDropBehavior != null){
			tag.put("callback", dragAndDropBehavior.getCallbackUrl());
		}
		if(removeBehavior != null){
			tag.put("removecallback", removeBehavior.getCallbackUrl());
		}
		if(element != null){
			tag.put("elementid", element.getId());
		}
		
		super.onComponentTag(tag);
	}

	public PopupMenu getPopupMenu(){
		return menu;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public ControllerType getType() {
		return type;
	}

	public void setType(ControllerType type) {
		this.type = type;
	}

	public void setDragAndDropBehavior(
			ElementDragAndDropBehavior dragAndDropBehavior) {
		this.dragAndDropBehavior = dragAndDropBehavior;
	}

	public void setRemoveBehavior(ElementRemoveBehavior removeBehavior) {
		this.removeBehavior = removeBehavior;
	}
	
	public abstract void setName(String name);
	public abstract void setDescription(String description);
	

}
