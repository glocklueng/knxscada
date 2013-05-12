package pl.marek.knx.pages;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Project;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.utils.ExternalImageResource;
import pl.marek.knx.utils.FileUploadUtil;

@HtmlFile("create_edit_project_form_panel.html")
public class CreateEditProjectFormPanel extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	private Project project;
	private ProjectForm form;
	private FeedbackPanel feedbackPanel;
	
	private Image image;
		
	public CreateEditProjectFormPanel(String componentName, DatabaseManager dbManager) {
		this(componentName, dbManager, null);
	}
	
	public CreateEditProjectFormPanel(String componentName, DatabaseManager dbManager, Project project) {
		super(componentName, dbManager);
		this.project = project;
		loadComponents();
	}
	
	private void loadComponents(){
		removeAll();
		if(project == null){
			project = new Project();
		}
		form = new ProjectForm("project-form", project);
		form.setOutputMarkupId(true);
		add(form);
		
		feedbackPanel = new FeedbackPanel("project-feedback");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		
		add(new ImageForm("project-image-form"));
		
		String imageFile = project.getImage();
		
		image = new Image("project-image", new ExternalImageResource(imageFile));
		image.setOutputMarkupId(true);
		if(imageFile == null || "".equals(imageFile)){
			image.setVisible(false);
			image.setOutputMarkupPlaceholderTag(true);
		}
		add(image);
		
	}
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
		loadComponents();
	}

	public ProjectForm getForm() {
		return form;
	}



	private class ProjectForm extends Form<Project>{

		private static final long serialVersionUID = 1L;
		
		private CompoundPropertyModel<Project> model;
		
		public ProjectForm(String id, Project project) {
			super(id);
			model = new CompoundPropertyModel<Project>(project);
			setModel(model);
			loadFields();
		}
		
		private void loadFields(){
			
			RequiredTextField<String> nameField = new RequiredTextField<String>("name");
			nameField.setLabel(new ResourceModel("project-form.name.label"));
			add(nameField);
			add(new SimpleFormComponentLabel("project-name-label", nameField));

			TextArea<String> descriptionField = new TextArea<String>("description");
			descriptionField.setLabel(new ResourceModel("project-form.description.label"));
			add(descriptionField);
			add(new SimpleFormComponentLabel("project-description-label", descriptionField));
			
			AjaxFormValidatingBehavior.addToAllFormComponents(this, "onkeyup", Duration.ONE_SECOND);

			AjaxButton submitButton = new AjaxButton("project-form-submit-button", this) {

				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					Project p = (Project)form.getModel().getObject();
					
					if(p.getId() == 0){
						getDBManager().addProject(p);
					}else{
						getDBManager().updateProject(p);
						
						Project currProject = getCurrentProject();
						if(currProject != null){
							if(currProject.getId() == p.getId()){
								getHeaderPanel().refresh();
								target.add(getHeaderPanel());
							}
						}
					}
					
					ProjectChooserPanel panel = getProjectChooserPanel();
					panel.setProjects(getDBManager().getAllProjects());
					
					target.add(panel);
					target.add(feedbackPanel);
					target.appendJavaScript("loadAfterUpdateProject();");
					
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedbackPanel);
				}
				
			};
			
			add(submitButton);	
			
		}
	}
	
	private class ImageForm extends Form<Void>{

		private static final long serialVersionUID = 1L;
		
		private FileUploadField file;
		
		public ImageForm(String id) {
			super(id);
			add(file = new FileUploadField("project-image-file"));
			AjaxButton submitButton = new AjaxButton("project-image-submit-button", this) {

				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
		            FileUpload upload = file.getFileUpload();
		            FileUploadUtil uploadUtil = new FileUploadUtil(upload);
		            if(uploadUtil.uploadFile()){
		            	project.setImage(uploadUtil.getUploadedFilePath());
		            	ExternalImageResource res = new ExternalImageResource(project.getImage());
		            	if(res.exists()){
			            	image.setImageResource(res);
							image.setVisible(true);
							target.add(image);
							info(getString("project-image-form.image.uploaded"));
		            	}
		            }else{
		            	warn(getString("project-image-form.noimage.message"));
		            }
		            
		            target.appendJavaScript("hideProjectImageLoading();");
					target.add(feedbackPanel);
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedbackPanel);
				}
				
			};
			
			add(submitButton);
			
			Button removeButton = new Button("project-image-remove-button");
			removeButton.add(new AjaxEventBehavior("click") {
				
				private static final long serialVersionUID = 1L;

				@Override
				protected void onEvent(AjaxRequestTarget target) {
					project.setImage("");
					image.setVisible(false);
					image.setOutputMarkupPlaceholderTag(true);
					target.add(image);
					info(getString("project-image-form.image.removed"));
					target.add(feedbackPanel);
				}
			});
			add(removeButton);
		}
	}
}