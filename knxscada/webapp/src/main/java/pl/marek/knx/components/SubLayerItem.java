package pl.marek.knx.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.SubLayer;

import pl.marek.knx.utils.StaticImage;

@HtmlFile("components/sublayeritem.html")
public class SubLayerItem extends Panel{
	
	private static final long serialVersionUID = 1L;
	
	private Label nameLabel;
	private Label descriptionLabel;
	private StaticImage icon;
	
	private PopupMenu menu;
	
	private IModel<SubLayer> layerModel;
	
	public SubLayerItem(String id, IModel<SubLayer> layerModel) {
		super(id);
		this.layerModel = layerModel;
		setOutputMarkupId(true);
		loadComponents();
	}

	private void loadComponents(){
		
		menu = new PopupMenu("popup-menu", new PropertyModel<String>(layerModel, "name").getObject());
		menu.setOutputMarkupId(true);
		
		descriptionLabel = new Label("subLayerItemDescription", new PropertyModel<String>(layerModel, "description"));
		descriptionLabel.setOutputMarkupId(true);
		
		nameLabel = new Label("subLayerItemName", new PropertyModel<String>(layerModel, "name")){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("descriptionBlockId", descriptionLabel.getMarkupId());
				
			}
		};
		nameLabel.setOutputMarkupId(true);
		icon = new StaticImage("subLayerItemIcon", new PropertyModel<String>(layerModel, "icon")){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				
			}
		};

		add(icon);
		add(nameLabel);
		add(descriptionLabel);
		add(menu);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.put("class", "sublayer popup-menu-trigger");
		tag.put("popupMenuId", menu.getMarkupId());
	}
	
	public PopupMenu getPopupMenu(){
		return menu;
	}
}
