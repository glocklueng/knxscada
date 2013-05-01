package pl.marek.knx.pages;

import org.apache.wicket.markup.html.basic.Label;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.database.SubLayer;

@HtmlFile("content.html")
public class ContentPanel extends BasePanel{
	
	private static final long serialVersionUID = 1L;

	private SubLayer subLayer;
	
	public ContentPanel(String componentName, DBManager dbManager) {
        super(componentName);
        setOutputMarkupId(true);
        
        String pId = getParameter("project");
        if(pId != null){
	        int projectId = Integer.parseInt(pId);
	        Project project = dbManager.getProjectById(projectId);
	        Layer layer = project.getLayers().get(0);
	        subLayer = layer.getSubLayers().get(0);
        }
        loadComponents();
    }

	private void loadComponents(){
		removeAll();
		
		Label l = new Label("msg","");
		if(subLayer != null){
			l.setDefaultModelObject(subLayer.getDescription());
		}
		add(l);
				
	}

	public void setSubLayer(SubLayer subLayer) {
		this.subLayer = subLayer;
		loadComponents();
	}
		
}



