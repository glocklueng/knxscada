package pl.marek.knx.pages;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.utils.ExternalImageResource;
import pl.marek.knx.utils.FileUploadUtil;

@HtmlFile("sublayer_settings.html")
public class SubLayerSettingsPanel extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	private SubLayer subLayer;
	
	private Image backgroundImage;
	
	public SubLayerSettingsPanel(String id, DatabaseManager dbManager, SubLayer subLayer) {
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
		            FileUploadUtil uploadUtil = new FileUploadUtil(upload);
		            if(uploadUtil.uploadFile()){
		            	subLayer.setBackgroundImage(uploadUtil.getUploadedFilePath());
	                	getDBManager().updateSubLayer(subLayer);
		            	ExternalImageResource res = new ExternalImageResource(subLayer.getBackgroundImage());
		            	if(res.exists()){
			            	backgroundImage.setImageResource(res);
			            	backgroundImage.setVisible(true);
							target.add(backgroundImage);
							target.appendJavaScript("hideLoadingPanel();");
		            	}
		            }

				}
			};
			
			add(submitButton);	
		}
	}

}
