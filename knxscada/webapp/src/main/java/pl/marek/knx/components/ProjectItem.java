package pl.marek.knx.components;


import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Project;
import pl.marek.knx.utils.ExternalImageResource;

@HtmlFile("components/projectitem.html")
public class ProjectItem extends Panel{
	
	private static final long serialVersionUID = 1L;
	
	private Label nameHint;
	private Label nameLabel;
	private Label descriptionHint;
	private Label descritionLabel;
	private Image image;
	
	private Project project;
	
	public ProjectItem(String id, Project project) {
		super(id);
		this.project = project;
		
		nameHint = new Label("projectItemNameLabel","Nazwa");
		nameLabel = new Label("projectItemName");
		descriptionHint = new Label("projectItemDescriptionLabel","Opis");
		descritionLabel = new Label("projectItemDescription");
		
		if(project != null){
			nameLabel.setDefaultModel(new Model<String>(project.getName()));
			descritionLabel.setDefaultModel(new Model<String>(project.getDescription()));
			image = new Image("projectItemImage", new ExternalImageResource(project.getImage()));
		}
		
		add(nameHint);
		add(nameLabel);
		add(descriptionHint);
		add(descritionLabel);
		add(image);

	}    

	

}
