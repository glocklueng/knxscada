package pl.marek.knx.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Layer;

import pl.marek.knx.utils.IconUtil;
import pl.marek.knx.utils.StaticImage;

@HtmlFile("components/layeritem.html")
public class LayerItem extends Panel{
	
	private static final long serialVersionUID = 1L;
	
	private Label nameLabel;
	private Label descriptionLabel;
	private StaticImage icon;
	private PopupMenu menu;
	private boolean isMainLayer;
	
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
		
		nameLabel = new Label("layerItemName", new PropertyModel<String>(layerModel, "name"));
		nameLabel.setOutputMarkupId(true);
		String iconName = new PropertyModel<String>(layerModel, "icon").getObject();
		icon = new StaticImage("layerItemIcon", new Model<String>(IconUtil.getLayerIconPath(iconName)));
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
		Layer layer = layerModel.getObject();
		tag.put("layerid", String.valueOf(layer.getId()));
		if(isMainLayer){
			tag.put("class", "layer");
		}else{
			tag.put("class", "layer popup-menu-trigger");	
		}
		tag.put("popupMenuId", menu.getMarkupId());
		tag.put("descriptionBlockId", descriptionLabel.getMarkupId());
		super.onComponentTag(tag);
	}
		
	public PopupMenu getPopupMenu(){
		return menu;
	}
	
	public Layer getLayer(){
		return layerModel.getObject();
	}

	public void setMainLayer(boolean isMainLayer) {
		this.isMainLayer = isMainLayer;
	}
	
}
