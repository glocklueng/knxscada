package pl.marek.knx.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.SubLayer;

import pl.marek.knx.utils.IconUtil;
import pl.marek.knx.utils.StaticImage;

@HtmlFile("components/sublayeritem.html")
public class SubLayerItem extends Panel{
	
	private static final long serialVersionUID = 1L;
	
	private Label nameLabel;
	private Label descriptionLabel;
	private StaticImage icon;
	private boolean isMainSubLayer;
	
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
		
		nameLabel = new Label("subLayerItemName", new PropertyModel<String>(layerModel, "name"));
		nameLabel.setOutputMarkupId(true);
		String iconName = new PropertyModel<String>(layerModel, "icon").getObject();
		icon = new StaticImage("subLayerItemIcon", new Model<String>(IconUtil.getSubLayerIconPath(iconName)));
		if(iconName.isEmpty()){
			icon.setVisible(false);
			icon.setOutputMarkupPlaceholderTag(true);
		}else{
			icon.setVisible(true);
		}

		add(icon);
		add(nameLabel);
		add(descriptionLabel);
		add(menu);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		SubLayer subLayer = layerModel.getObject();
		tag.put("sublayerid", String.valueOf(subLayer.getId()));
		
		if(isMainSubLayer){
			tag.put("class", "sublayer");
		}else{
			tag.put("class", "sublayer popup-menu-trigger");	
		}
		tag.put("popupMenuId", menu.getMarkupId());
		tag.put("descriptionBlockId", descriptionLabel.getMarkupId());
	}
		
	public PopupMenu getPopupMenu(){
		return menu;
	}
	
	public SubLayer getSubLayer(){
		return layerModel.getObject();
	}

	public void setMainSubLayer(boolean isMainSubLayer) {
		this.isMainSubLayer = isMainSubLayer;
	}
	
	
}
