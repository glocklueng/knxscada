package pl.marek.knx.pages;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.json.simple.JSONObject;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.components.PopupMenuItem;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.pages.DialogsPanel.DialogType;
import pl.marek.knx.utils.JSONUtil;

@HtmlFile("elements.html")
public class ElementsPanel extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	private SubLayer subLayer;
	private RepeatingView elementsView;

	public ElementsPanel(String id, DBManager dbManager, SubLayer sublayer) {
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
		
		if(subLayer != null){
			if(subLayer.getElements() != null){
				for(Element element: subLayer.getElements()){
					
				}
			}
		}
		
		final ElementDragAndDropBehavior behavior = new ElementDragAndDropBehavior();
		add(behavior);
		
		final ElementRemoveBehavior removeBehavior = new ElementRemoveBehavior();
		add(removeBehavior);
		
		elementsView.add(new Label(elementsView.newChildId(),"element 1"){
			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("callback", behavior.getCallbackUrl());
				tag.put("removecallback", removeBehavior.getCallbackUrl());
				tag.put("elementid", "1");
				
				super.onComponentTag(tag);
			}
		});
		elementsView.add(new Label(elementsView.newChildId(),"element 2"){
			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("callback", behavior.getCallbackUrl());
				tag.put("removecallback", removeBehavior.getCallbackUrl());
				tag.put("elementid", "2");
				
				super.onComponentTag(tag);
			}
		});
		elementsView.add(new Label(elementsView.newChildId(),"element 3"){
			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("callback", behavior.getCallbackUrl());
				tag.put("removecallback", removeBehavior.getCallbackUrl());
				tag.put("elementid", "3");
				
				super.onComponentTag(tag);
			}
		});
		
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
	
	private class ElementDragAndDropBehavior extends AbstractDefaultAjaxBehavior{

		private static final long serialVersionUID = 1L;

		@Override
		protected void respond(AjaxRequestTarget target) {
			
			String s = getRequestMessage();
			
			System.out.println("JSON: " + s);
			JSONObject obj = JSONUtil.convertStringToObject(s);
			System.out.println("CONVERT: "+obj.toJSONString());
		}
	}
	
	private class ElementRemoveBehavior extends AbstractDefaultAjaxBehavior{

		private static final long serialVersionUID = 1L;

		@Override
		protected void respond(AjaxRequestTarget target) {
			
			String s = getRequestMessage();
			
			System.out.println("JSON: " + s);
			JSONObject obj = JSONUtil.convertStringToObject(s);
			System.out.println("CONVERT: "+obj.toJSONString());
		}
	}
	
	public void setSubLayer(SubLayer subLayer) {
		this.subLayer = subLayer;
		loadComponents();
	}
	
	public void refresh(){
		subLayer = getCurrentSubLayer();
		loadComponents();
	}
}
