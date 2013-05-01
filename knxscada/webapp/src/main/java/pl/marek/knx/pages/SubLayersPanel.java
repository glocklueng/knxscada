package pl.marek.knx.pages;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.components.PopupMenu;
import pl.marek.knx.components.PopupMenuItem;
import pl.marek.knx.components.SubLayerItem;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.database.SubLayer;

@HtmlFile("sublayers.html")
public class SubLayersPanel extends BasePanel{
	
	private static final long serialVersionUID = 1L;
	
	//private DBManager dbManager;
	
	private Layer layer;

	private static boolean execute;
	
	public SubLayersPanel(String componentName, DBManager dbManager) {
        super(componentName);
       // this.dbManager = dbManager;
        execute = true;
        String pId = getParameter("project");
        if(pId != null){
	        int projectId = Integer.parseInt(pId);
	        Project project = dbManager.getProjectById(projectId);
	        layer = project.getLayers().get(0);
        }
        loadComponents();

	}
	
	private void loadComponents(){
		
		removeAll();
		
        RepeatingView subLayers = new RepeatingView("sublayer");
        if(layer != null){
	        for(SubLayer l: layer.getSubLayers()){
	        	SubLayerItem item = new SubLayerItem(subLayers.newChildId(), new Model<SubLayer>(l));
	        	
	        	
	        	PopupMenu menu = item.getPopupMenu();
	        	PopupMenuItem editMenu = menu.createPopupMenuItem("Edytuj", "images/logo.png", true);
	        	PopupMenuItem deleteMenu = menu.createPopupMenuItem("Usuń", "images/logo.png", true);
	        	
	        	editMenu.add(new SubLayerItemEditClickBehavior(l));
	        	deleteMenu.add(new SubLayerItemDeleteClickBehavior(l));
	        	
	        	subLayers.add(item);
	        	item.add(new SubLayerItemClickBehavior(l));
	        	
	        }
        }
        add(subLayers);
	}
	
	public void setLayer(Layer layer){
		this.layer = layer;
		loadComponents();
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
				Index index = (Index)getPage();
				MainPanel main = index.getMainPanel();
				ContentPanel content = main.getContentPanel();
				System.out.println("tutaj");
				content.setSubLayer(subLayer);
			
				target.appendJavaScript("load(); resize();");
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
			System.out.println("edit");
			execute = false;
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
			System.out.println("delete");
			execute = false;
		}
	}
	
}
