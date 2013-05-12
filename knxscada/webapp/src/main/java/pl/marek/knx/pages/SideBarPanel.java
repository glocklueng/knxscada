package pl.marek.knx.pages;

import java.util.ArrayList;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.components.LayerItem;
import pl.marek.knx.components.PopupMenu;
import pl.marek.knx.components.PopupMenuItem;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.pages.DialogsPanel.DialogType;

@HtmlFile("sidebar.html")
public class SideBarPanel extends BasePanel {
	 
	private static final long serialVersionUID = 1L;

	private RepeatingView layers;
	
	private static Layer currentLayer;
	
	private static boolean execute;
	
	public SideBarPanel(String componentName, DatabaseManager dbManager) {
        super(componentName, dbManager);
        execute = true;
        currentLayer = null;
        setOutputMarkupId(true);
        loadComponents();
    }
	
	private void loadComponents(){
		removeAll();
		PopupMenuItem newLayerItem = new PopupMenuItem("new-layer-item", 
														new Model<String>(getString("add-layer")), 
														new Model<String>("images/new_item.png"));
		newLayerItem.add(new NewLayerItemClickBehavior());
		add(newLayerItem);
		
		layers = new RepeatingView("layer");
		  
	    Project project = getCurrentProject();
	    
	    if(project != null){
	    	ArrayList<Layer> projectLayers = project.getLayers();
	    	if(projectLayers != null){
		        for(Layer layer: projectLayers){
		        	boolean isMainLayer = false;
		        	if(layer.isMainLayer()){
		        		layer.setName(project.getName());
		        		layer.setIcon("logo");
		        		isMainLayer = true;
		        	}
		        	
		        	LayerItem item = new LayerItem(layers.newChildId(), new Model<Layer>(layer));
		        	item.setMainLayer(isMainLayer);
		        	item.add(new LayerItemClickBehavior(layer));
		        	
		        	PopupMenu menu = item.getPopupMenu();
					PopupMenuItem editMenu = menu.createPopupMenuItem(getString("layer.edit.menuitem"), "images/edit_icon.png", true);
					PopupMenuItem deleteMenu = menu.createPopupMenuItem(getString("layer.remove.menuitem"), "images/trash_icon.png", true);
	
		        	editMenu.add(new LayerItemEditClickBehavior(layer));
		        	deleteMenu.add(new LayerItemDeleteClickBehavior(layer));
		        	
		        	layers.add(item);
		        }
	    	}
		}
		add(layers);
	}
	
	public void refresh(){
		loadComponents();
	}
	
	private class NewLayerItemClickBehavior extends AjaxEventBehavior{

		private static final long serialVersionUID = 1L;

		public NewLayerItemClickBehavior() {
			super("click");
		}
		
		@Override
		protected void onEvent(AjaxRequestTarget target) {
			DialogsPanel dialogs = getDialogsPanel();
			dialogs.setType(DialogType.NEW_LAYER);
			dialogs.setDialogPanel(new CreateEditLayerFormPanel("dialog", getDBManager(), Layer.LAYER));
			target.add(dialogs);
			target.appendJavaScript("initLayerDialog('"+ getString("new-layer") + "','"+getString("cancel")+"'); showDialog();");
		}
	}
	
	private class LayerItemClickBehavior extends AjaxEventBehavior{
		
		private static final long serialVersionUID = 1L;
		
		private Layer layer;
			
		public LayerItemClickBehavior(Layer layer) {
			super("click");
			this.layer = layer;
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) {
			if(execute){
				currentLayer = getDBManager().getLayerByIdWithDependencies(layer.getId());
				MainPanel main = getMainPanel();
				main.setLayer(currentLayer);
				
				target.appendJavaScript("loadLayer();");
				target.add(main);
			}
			execute = true;
		}
	}
	
	private class LayerItemEditClickBehavior extends AjaxEventBehavior{
		
		private static final long serialVersionUID = 1L;
		
		private Layer layer;
			
		public LayerItemEditClickBehavior(Layer layer) {
			super("click");
			this.layer = layer;
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) {
			execute = false;
			DialogsPanel dialogs = getDialogsPanel();
			dialogs.setType(DialogType.EDIT_LAYER);
			dialogs.setDialogPanel(new CreateEditLayerFormPanel("dialog", getDBManager(), Layer.LAYER, layer));
			target.add(dialogs);
			target.appendJavaScript("initLayerDialog('"+ getString("edit-layer") + "','"+getString("cancel")+"'); showDialog();");
		
		}
	}
	
	private class LayerItemDeleteClickBehavior extends AjaxEventBehavior{
		
		private static final long serialVersionUID = 1L;
		
		private Layer layer;
			
		public LayerItemDeleteClickBehavior(Layer layer) {
			super("click");
			this.layer = layer;
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) {
			execute = false;
			DialogsPanel dialogs = getDialogsPanel();
			dialogs.setType(DialogType.DELETE_LAYER);
			dialogs.setDialogPanel(new DeleteFormPanel("dialog", getDBManager(), layer));
			target.add(dialogs);
			target.appendJavaScript("initRemoveDialog('"+ getString("yes") + "', '"+getString("no")+"'); showDialog();");
			
		}
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		String pId = getParameter("project");
		if(pId == null){
			tag.put("style", "display:none;");
		}
	}
	
	public String getLayerItemIdByLayer(Layer layer){
		String layerId = "";
		for(int i = 0; i < layers.size(); i++){
			LayerItem item = (LayerItem)layers.get(i);
			if(item.getLayer().getId() == layer.getId()){
				layerId = item.getMarkupId();
				break;
			}
		}
		return layerId;
	}
	
	public String getOtherLayerItemIdByRemovedLayer(Layer layer){
		String layerId = "";
		if(getCurrentLayer().getId() == layer.getId()){
			boolean checked = false;
			for(int i = 0; i < layers.size(); i++){
				LayerItem item = (LayerItem)layers.get(i);
				if(item.getLayer().getId() != layer.getId()){
					layerId = item.getMarkupId();
				}else{
					checked = true;
				}
				if(!layerId.isEmpty() && checked){
					break;
				}
			}
		}
		return layerId;
	}
	
	@Override
	public Layer getCurrentLayer(){
		if(currentLayer == null){
			if(layers.size() > 0){
				LayerItem item = (LayerItem)layers.get(0);
				currentLayer = item.getLayer();
			}
		}
		return currentLayer;
	}

}