package pl.marek.knx.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("components/popupmenu.html")
public class PopupMenu extends Panel{

	private static final long serialVersionUID = 1L;
	
	private Label titleLabel;
	private RepeatingView items;
	
	private Model<String> titleModel;
	
	public PopupMenu(String id) {
		super(id);
		titleModel = new Model<String>("");
		loadComponents();
	}
	
	public PopupMenu(String id, String title) {
		super(id);
		titleModel = new Model<String>(title);
		loadComponents();
	}
	
	private void loadComponents(){
		setOutputMarkupId(true);
		titleLabel = new Label("popup-menu-title", titleModel){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				if(titleModel == null || titleModel.getObject().isEmpty()){
					tag.put("style", "display:none;");
				}
				super.onComponentTag(tag);
			}
		};
		items = new RepeatingView("popup-menu-item");

		
		add(titleLabel);
		add(items);
		
	}
	
	public String getNewChildId(){
		return items.newChildId();
	}
	
	public void addPopupMenuItem(PopupMenuItem item){
		items.add(item);
	}

	public void addPopupMenuItem(String name, String icon){
		PopupMenuItem item= createPopupMenuItem(name, icon);
		addPopupMenuItem(item);
	}
	
	public PopupMenuItem createPopupMenuItem(String name, String icon){
		PopupMenuItem item= new PopupMenuItem(getNewChildId(), new Model<String>(name), new Model<String>(icon));
		return item;
	}
	
	public PopupMenuItem createPopupMenuItem(String name, String icon, boolean autoAdd){
		PopupMenuItem item= new PopupMenuItem(getNewChildId(), new Model<String>(name), new Model<String>(icon));
		if(autoAdd){
			addPopupMenuItem(item);
		}
		return item;
	}
}
