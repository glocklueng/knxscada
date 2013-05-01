package pl.marek.knx.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Layer;

import pl.marek.knx.utils.StaticImage;

@HtmlFile("components/layeritem.html")
public class LayerItem extends Panel{
	
	private static final long serialVersionUID = 1L;
	
	private Label nameLabel;
	private Label descriptionLabel;
	private StaticImage icon;
	private PopupMenu menu;
	
	private IModel<Layer> layerModel;
	
	public LayerItem(String id, IModel<Layer> layerModel) {
		super(id);
		this.layerModel = layerModel;
		setOutputMarkupId(true);
		loadComponents();
	}

	private void loadComponents(){
		
		menu = new PopupMenu("popup-menu", new PropertyModel<String>(layerModel, "name").getObject());
		menu.setOutputMarkupId(true);
		
		descriptionLabel = new Label("layerItemDescription", new PropertyModel<String>(layerModel, "description"));
		descriptionLabel.setOutputMarkupId(true);
		
		nameLabel = new Label("layerItemName", new PropertyModel<String>(layerModel, "name")){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("descriptionBlockId", descriptionLabel.getMarkupId());
				tag.put("popupMenuId", menu.getMarkupId());
			}
		};
		nameLabel.setOutputMarkupId(true);
		icon = new StaticImage("layerItemIcon", new PropertyModel<String>(layerModel, "icon")){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("popupMenuId", menu.getMarkupId());
			}
		};
		
		
		add(icon);
		add(nameLabel);
		add(descriptionLabel);
		add(menu);
	}
	
	public PopupMenu getPopupMenu(){
		return menu;
	}
}
