package pl.marek.knx.pages;

import java.io.File;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.utils.ExternalImageResource;

@HtmlFile("sublayer_settings.html")
public class SubLayerSettingsPanel extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	private SubLayer subLayer;
	
	private Image backgroundImage;
	
	public SubLayerSettingsPanel(String id, DBManager dbManager, SubLayer subLayer) {
		super(id, dbManager);
		this.subLayer = subLayer;
		loadComponents();
	}
	
	private void loadComponents(){
		removeAll();
		
		add(new ImageForm("background-image-form"));
		
		Button backgroundImageRemoveButton = new Button("background-image-remove-button");
		backgroundImageRemoveButton.add(new BackgroundImageRemoveBehavior());
		
		add(backgroundImageRemoveButton);
				
	}
	
	public void setSubLayer(SubLayer subLayer) {
		this.subLayer = subLayer;
		loadComponents();
	}
	
	public void setBackgroundImage(Image image){
		this.backgroundImage = image;
	}
	
	public void refresh(){
		subLayer = getCurrentSubLayer();
		loadComponents();
	}
	
	private class BackgroundImageRemoveBehavior extends AjaxEventBehavior{

		private static final long serialVersionUID = 1L;
		
		public BackgroundImageRemoveBehavior() {
			super("click");
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) {
			if(backgroundImage != null){
				subLayer.setBackgroundImage("");
				getDBManager().updateSubLayer(subLayer);
				backgroundImage.setVisible(false);
				target.add(backgroundImage);
			}
		}
		
	}
	
	private class ImageForm extends Form<Void>{

		private static final long serialVersionUID = 1L;
		
		private FileUploadField file;
		
		public ImageForm(String id) {
			super(id);
			add(file = new FileUploadField("background-image-file"));
			AjaxButton submitButton = new AjaxButton("background-image-submit-button", this) {

				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
		            FileUpload upload = file.getFileUpload();
		            if(uploadFile(upload)){
		            	ExternalImageResource res = new ExternalImageResource(subLayer.getBackgroundImage());
		            	if(res.exists()){
			            	backgroundImage.setImageResource(res);
			            	backgroundImage.setVisible(true);
							target.add(backgroundImage);
							target.appendJavaScript("hideLoadingPanel();");
//							info(getString("project-image-form.image.uploaded"));
		            	}
		            }else{
//		            	warn(getString("project-image-form.noimage.message"));
		            }
		            
//		            target.appendJavaScript("hideLoading();");
//					target.add(feedbackPanel);
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
//					target.add(feedbackPanel);
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
                	subLayer.setBackgroundImage(file.getAbsolutePath());
                	getDBManager().updateSubLayer(subLayer);
                	
					upload.writeTo(file);
				} catch (Exception e) {
					return false;
				}
            }
			return true;
		}
	}

}
