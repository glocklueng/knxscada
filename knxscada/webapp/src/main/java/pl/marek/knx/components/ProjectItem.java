package pl.marek.knx.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Project;
import pl.marek.knx.utils.ExternalImageResource;

@HtmlFile("components/projectitem.html")
public class ProjectItem extends Panel{
	
	private static final long serialVersionUID = 1L;
	
	private Label nameHint;
	private Label nameLabel;
	private Label descriptionHint;
	private Label descriptionLabel;
	private Image image;
	
	private PopupMenu menu;
	
	private IModel<Project> projectModel;
		
	public ProjectItem(String id, IModel<Project> projectModel) {
		super(id);
		this.projectModel = projectModel;
		setOutputMarkupId(true);
		loadComponents();
	}
	
	private void loadComponents(){
		
		menu = new PopupMenu("popup-menu", new PropertyModel<String>(projectModel, "name").getObject());
		menu.setOutputMarkupId(true);
		
		nameHint = new Label("projectItemNameLabel",new ResourceModel("project.name.label"));
		nameLabel = new Label("projectItemName", new PropertyModel<String>(projectModel, "name"));
		descriptionHint = new Label("projectItemDescriptionLabel",new ResourceModel("project.description.label"));
		descriptionLabel = new Label("projectItemDescription", new PropertyModel<String>(projectModel, "description"));
		image = new Image("projectItemImage", new ExternalImageResource(new PropertyModel<String>(projectModel, "image")));

		add(nameHint);
		add(nameLabel);
		add(descriptionHint);
		add(descriptionLabel);
		add(image);
		add(menu);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		
		int projectId = new PropertyModel<Integer>(projectModel,"id").getObject();
		tag.put("projectid", projectId);
		tag.put("class", "popup-menu-trigger");
		tag.put("popupMenuId", menu.getMarkupId());
		super.onComponentTag(tag);
	}
	
	public PopupMenu getPopupMenu(){
		return menu;
	}
	
	public Project getProject(){
		return projectModel.getObject();
	}

}
