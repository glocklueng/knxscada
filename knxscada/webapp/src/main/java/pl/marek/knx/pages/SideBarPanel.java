package pl.marek.knx.pages;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.components.LayerItem;
import pl.marek.knx.components.PopupMenu;
import pl.marek.knx.components.PopupMenuItem;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;

@HtmlFile("sidebar.html")
public class SideBarPanel extends BasePanel {
	 
	private static final long serialVersionUID = 1L;

	private DBManager dbManager;
	
	private static boolean execute;
	
	public SideBarPanel(String componentName, DBManager dbManager) {
        super(componentName);
        execute = true;
        this.dbManager = dbManager;
        
        loadComponents();
    }
	
	private void loadComponents(){
		
		RepeatingView layers = new RepeatingView("layer");
		String pId = getParameter("project");
		if(pId != null){
	        int projectId = Integer.parseInt(pId);   
	        Project project = dbManager.getProjectById(projectId);
	        for(Layer layer: project.getLayers()){
	        	LayerItem item = new LayerItem(layers.newChildId(), new Model<Layer>(layer));
	        	item.add(new LayerItemClickBehavior(layer));
	        	
	        	PopupMenu menu = item.getPopupMenu();
	        	PopupMenuItem editMenu = menu.createPopupMenuItem("Edytuj", "images/logo.png", true);
	        	PopupMenuItem deleteMenu = menu.createPopupMenuItem("Usuń", "images/logo.png", true);
	        	
	        	editMenu.add(new LayerItemEditClickBehavior(layer));
	        	deleteMenu.add(new LayerItemDeleteClickBehavior(layer));
	        	
	        	layers.add(item);
	        }
		}
		add(layers);
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
				Index index = (Index)getPage();
				MainPanel main = index.getMainPanel();
				main.setLayer(layer);
							
				target.appendJavaScript("resize(); load();");
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
			System.out.println("edit");
			execute = false;
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
			System.out.println("delete");
			execute = false;
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

}