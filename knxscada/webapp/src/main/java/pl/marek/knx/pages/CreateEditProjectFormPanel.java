package pl.marek.knx.pages;

import java.io.File;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
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

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Project;
import pl.marek.knx.utils.ExternalImageResource;

@HtmlFile("create_edit_project_form_panel.html")
public class CreateEditProjectFormPanel extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	private DBManager dbManager;
	
	private Project project;
	private ProjectForm form;
	private FeedbackPanel feedbackPanel;
	
	private Image image;
		
	public CreateEditProjectFormPanel(String componentName, DBManager dbManager) {
		super(componentName);
		this.dbManager = dbManager;
		loadComponents();
	}
	
	public CreateEditProjectFormPanel(String componentName, DBManager dbManager, Project project) {
		super(componentName);
		this.dbManager = dbManager;
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
					//TODO Zrobić dodawanie projektu
					System.out.println("DODAJE PROJEKT:");
					System.out.println(p.getName()+" "+p.getDescription());
					System.out.println(p.getImage());
					
					dbManager.addProject(p);
					Index index = (Index)getPage();
					ProjectChooserPanel panel = index.getProjectChooserPanel();
					panel.setProjects(dbManager.getAllProjects());
					
					target.add(panel);
					target.add(feedbackPanel);
					
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
		            if(uploadFile(upload)){
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
		            
		            target.appendJavaScript("hideLoading();");
					target.add(feedbackPanel);
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedbackPanel);
				}
				
			};
			
			add(submitButton);	
		}
		
		private boolean uploadFile(FileUpload upload){
			
			if (upload == null){
                return false;
            }
            else{
            	String fileName = upload.getClientFileName();

                try {
                	File parent = new File("/home/marek/tmp/");
                	if(!parent.exists()){
                		parent.mkdirs();
                	}
                	File file = new File(parent, fileName);
                	
                	project.setImage(file.getAbsolutePath());
                	
					upload.writeTo(file);
				} catch (Exception e) {
					return false;
				}
            }
			return true;
		}
	}
}