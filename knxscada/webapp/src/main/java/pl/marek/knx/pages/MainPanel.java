package pl.marek.knx.pages;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.components.ProjectItem;
import pl.marek.knx.database.Project;

@HtmlFile("main.html")
public class MainPanel extends Panel {
	 
	private static final long serialVersionUID = 1L;
	
	public MainPanel(String componentName) {
        super(componentName);
        
        Project project = new Project();
        project.setName("Project");
        project.setDescription("Description");
        project.setImage("/home/marek/Magisterka/Grafika/WWW/logo.png");
        
        List<ProjectItem> components = new ArrayList<ProjectItem>();
		components.add( new ProjectItem("component",project) );
		components.add( new ProjectItem("component", project) );
		add( new ListView( "components", components )
		{			
			protected void populateItem(ListItem item)
			{
				item.add( (ProjectItem)item.getModelObject() );
			}
		});
	}
	 
	
}
