package pl.marek.knx.pages;

import java.util.ArrayList;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.components.PopupMenu;
import pl.marek.knx.components.PopupMenuItem;
import pl.marek.knx.components.SubLayerItem;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.pages.DialogsPanel.DialogType;
import pl.marek.knx.utils.StaticImage;

@HtmlFile("sublayers.html")
public class SubLayersPanel extends BasePanel{
	
	private static final long serialVersionUID = 1L;
	
	private RepeatingView subLayers;
	private Layer layer;
	private SubLayer currentSubLayer;

	private static boolean execute;
	
	public SubLayersPanel(String componentName, DatabaseManager dbManager) {
        super(componentName, dbManager);
        setOutputMarkupId(true);
        execute = true;
        currentSubLayer = null;
        
        Project project = getCurrentProject();
        if(project != null){
        	ArrayList<Layer> projectLayers = project.getLayers();
        	if(projectLayers != null){
        		layer = projectLayers.get(0);
        	}
        }
        
        loadComponents();
	}
	
	private void loadComponents(){
		
		removeAll();
        
        subLayers = new RepeatingView("sublayer");
        
        StaticImage newItemIcon = new StaticImage("new-sublayer-item", new Model<String>("images/new_room.png"));
        newItemIcon.add(new NewSubLayerItemClickBehavior());
        
        if(layer != null){
	        for(SubLayer l: layer.getSubLayers()){
	        	boolean isMainSubLayer = false;
	        	if(l.isMainSubLayer()){
	        		if(layer.isMainLayer()){
	        			l.setName(getCurrentProject().getName());
	        		}else{
	        			l.setName(layer.getName());
	        		}
	        		l.setIcon("logo");
	        		isMainSubLayer = true;
	        	}
	        	
	        	SubLayerItem item = new SubLayerItem(subLayers.newChildId(), new Model<SubLayer>(l));
	        	item.setMainSubLayer(isMainSubLayer);
	        	
	        	PopupMenu menu = item.getPopupMenu();
				PopupMenuItem editMenu = menu.createPopupMenuItem(getString("sublayer.edit.menuitem"), "images/edit_icon.png", true);
				PopupMenuItem deleteMenu = menu.createPopupMenuItem(getString("sublayer.remove.menuitem"), "images/trash_icon.png", true);

	        	editMenu.add(new SubLayerItemEditClickBehavior(l));
	        	deleteMenu.add(new SubLayerItemDeleteClickBehavior(l));
	        	
	        	subLayers.add(item);
	        	item.add(new SubLayerItemClickBehavior(l));
	        	
	        }
        }else{
        	newItemIcon.setVisible(false);
        }
        add(newItemIcon);
        add(subLayers);
	}
	
	public void setLayer(Layer layer){
		this.layer = layer;
		loadComponents();
	}
	
	public void refresh(){
		if(getCurrentLayer() != null){
			layer = getDBManager().getLayerByIdWithDependencies(getCurrentLayer().getId());
		}
		loadComponents();
	}
	
	private class NewSubLayerItemClickBehavior extends AjaxEventBehavior{

		private static final long serialVersionUID = 1L;

		public NewSubLayerItemClickBehavior() {
			super("click");
		}
		
		@Override
		protected void onEvent(AjaxRequestTarget target) {

			DialogsPanel dialogs = getDialogsPanel();
			dialogs.setType(DialogType.NEW_SUBLAYER);
			dialogs.setDialogPanel(new CreateEditLayerFormPanel("dialog", getDBManager(), SubLayer.SUBLAYER));
			target.add(dialogs);
			target.appendJavaScript("initLayerDialog('"+ getString("new-sublayer") + "','"+getString("cancel")+"'); showDialog();");
		
		}
		
	}
	
	private class SubLayerItemClickBehavior extends AjaxEventBehavior{
		
		private static final long serialVersionUID = 1L;
		
		private SubLayer subLayer;
			
		public SubLayerItemClickBehavior(SubLayer subLayer) {
			super("click");
			this.subLayer = subLayer;
		}
				
		@Override
		protected void onEvent(AjaxRequestTarget target) {
			if(execute){
				currentSubLayer = getDBManager().getSubLayerByIdWithDependencies(subLayer.getId());
				ContentPanel content = getContentPanel();
				content.setSubLayer(currentSubLayer);
				target.appendJavaScript("loadSubLayer();");
				target.add(content);
			}
			execute = true;
		}
	}
	
	private class SubLayerItemEditClickBehavior extends AjaxEventBehavior{
		
		private static final long serialVersionUID = 1L;
		
		private SubLayer subLayer;
			
		public SubLayerItemEditClickBehavior(SubLayer subLayer) {
			super("click");
			this.subLayer = subLayer;
		}
				
		@Override
		protected void onEvent(AjaxRequestTarget target) {
			execute = false;
			DialogsPanel dialogs = getDialogsPanel();
			dialogs.setType(DialogType.EDIT_SUBLAYER);
			dialogs.setDialogPanel(new CreateEditLayerFormPanel("dialog", getDBManager(), SubLayer.SUBLAYER, subLayer));
			target.add(dialogs);
			target.appendJavaScript("initLayerDialog('"+ getString("edit-sublayer") + "','"+getString("cancel")+"'); showDialog();");
		}
	}
	
	private class SubLayerItemDeleteClickBehavior extends AjaxEventBehavior{
		
		private static final long serialVersionUID = 1L;
		
		private SubLayer subLayer;
			
		public SubLayerItemDeleteClickBehavior(SubLayer subLayer) {
			super("click");
			this.subLayer = subLayer;
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) {
			execute = false;
			DialogsPanel dialogs = getDialogsPanel();
			dialogs.setType(DialogType.DELETE_SUBLAYER);
			dialogs.setDialogPanel(new DeleteFormPanel("dialog", getDBManager(), subLayer));
			target.add(dialogs);
			target.appendJavaScript("initRemoveDialog('"+ getString("yes") + "', '"+getString("no")+"'); showDialog();");
		}
	}
	
	public String getSubLayerItemIdBySubLayer(SubLayer subLayer){
		String subLayerId = "";
		for(int i=0;i<subLayers.size();i++){
			SubLayerItem item = (SubLayerItem)subLayers.get(i);
			if(item.getSubLayer().getId() == subLayer.getId()){
				subLayerId = item.getMarkupId();
				break;
			}
		}
		return subLayerId;
	}
	
	public String getOtherSubLayerItemIdByRemovedSubLayer(SubLayer subLayer){
		String subLayerId = "";
		if(getCurrentSubLayer().getId() == subLayer.getId()){
			boolean checked = false;
			for(int i=0;i<subLayers.size();i++){
				SubLayerItem item = (SubLayerItem)subLayers.get(i);
				if(item.getSubLayer().getId() != subLayer.getId()){
					subLayerId = item.getMarkupId();
				}else{
					checked = true;
				}
				if(!subLayerId.isEmpty() && checked){
					break;
				}
			}
		}
		return subLayerId;
	}
	
	@Override
	public SubLayer getCurrentSubLayer(){
		if(currentSubLayer == null){
			if(subLayers.size() > 0){
				SubLayerItem item = (SubLayerItem)subLayers.get(0);
				currentSubLayer = item.getSubLayer();
			}
		}
		return currentSubLayer;
	}
}
