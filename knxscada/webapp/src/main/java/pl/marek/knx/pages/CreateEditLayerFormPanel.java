package pl.marek.knx.pages;

import java.io.File;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.utils.IconUtil;
import pl.marek.knx.utils.StaticImage;

@HtmlFile("create_edit_layer_form_panel.html")
public class CreateEditLayerFormPanel extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	private Layer layer;
	private FeedbackPanel feedbackPanel;
	private LayerForm form;
	private String itemType;
	private StaticImage iconView;
	private RepeatingView chooserIcons;
	
	public CreateEditLayerFormPanel(String componentName, DBManager dbManager, String itemType) {
		super(componentName, dbManager);
		this.itemType = itemType;
		loadComponents();	
	}

	
	public CreateEditLayerFormPanel(String componentName, DBManager dbManager, String itemType, Layer layer) {
		super(componentName, dbManager);
		this.layer = layer;
		this.itemType = itemType;
		loadComponents();	
	}
	
	private void loadComponents(){
		
		if(layer == null){
			if(Layer.LAYER.equals(itemType)){
				layer = new Layer();
			}else if(SubLayer.SUBLAYER.equals(itemType)){
				layer = new SubLayer();
			}
		}
		
		form = new LayerForm("layer-form", layer);
		form.setOutputMarkupId(true);
		add(form);
		
		feedbackPanel = new FeedbackPanel("layer-feedback");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		
		String icon = getIconPath();
		iconView = new StaticImage("layer-icon", new Model<String>(icon));
		iconView.setOutputMarkupId(true);
		if(icon == null || icon.isEmpty()){
			iconView.setVisible(false);
			iconView.setOutputMarkupPlaceholderTag(true);
		}
		
		add(iconView);
		
		chooserIcons = new RepeatingView("layer-icon-chooser-item");
		loadChooserIcons();
		add(chooserIcons);
		
	}
	
	private String getIconPath(){
		String icon = "";
		if(Layer.LAYER.equals(itemType)){
			icon = IconUtil.getLayerIconPath(layer.getIcon());
		}else if(SubLayer.SUBLAYER.equals(itemType)){
			icon = IconUtil.getSubLayerIconPath(layer.getIcon());
		}
		return icon;
	}
	
	private void loadChooserIcons(){
		
		File imagesPath = new File(getServletContext().getRealPath("images"));
		String relativePath = "";
		File imgDirectory = null;
		if(Layer.LAYER.equals(itemType)){
			relativePath = "images/floors/";
			imgDirectory = new File(imagesPath.getAbsolutePath(),"floors");
		}else if(SubLayer.SUBLAYER.equals(itemType)){
			relativePath = "images/rooms/";
			imgDirectory = new File(imagesPath.getAbsolutePath(),"rooms");
		}
		
		if(imgDirectory != null){		
			for(File file: imgDirectory.listFiles()){
				String iconPath = relativePath + file.getName();
				StaticImage img = new StaticImage(chooserIcons.newChildId(), new Model<String>(iconPath));
				img.add(new LayerIconChooserClickBehavior(iconPath));
				chooserIcons.add(img);
			}
		}

		
	}

	public Layer getLayer() {
		return layer;
	}


	public void setLayer(Layer layer) {
		this.layer = layer;
		loadComponents();
	}
	
	private class LayerIconChooserClickBehavior extends AjaxEventBehavior{

		private static final long serialVersionUID = 1L;
		
		private String iconPath;
		
		public LayerIconChooserClickBehavior(String iconPath) {
			super("click");
			this.iconPath = iconPath;
		}
		
		@Override
		protected void onEvent(AjaxRequestTarget target) {
			layer.setIcon(getIconName());
			iconView.setDefaultModelObject(iconPath);
			iconView.setVisible(true);
			target.add(iconView);
		}
		
		private String getIconName(){
			int lastSlash = iconPath.lastIndexOf("/");
			int lastDot = iconPath.lastIndexOf(".");
			String name = iconPath.substring(lastSlash + 1, lastDot);
			
			return name;
		}
		
	}


	private class LayerForm extends Form<Layer>{

		private static final long serialVersionUID = 1L;
		
		private CompoundPropertyModel<Layer> model;
		
		public LayerForm(String id, Layer layer) {
			super(id);
			model = new CompoundPropertyModel<Layer>(layer);
			setModel(model);
			loadFields();
		}
		
		private void loadFields(){
			
			RequiredTextField<String> nameField = new RequiredTextField<String>("name");
			TextArea<String> descriptionField = new TextArea<String>("description");
			
			if(Layer.LAYER.equals(itemType)){
				nameField.setLabel(new ResourceModel("sublayer-form.name.label"));
				descriptionField.setLabel(new ResourceModel("layer-form.description.label"));
			}else if(SubLayer.SUBLAYER.equals(itemType)){
				nameField.setLabel(new ResourceModel("sublayer-form.name.label"));
				descriptionField.setLabel(new ResourceModel("sublayer-form.description.label"));
			}
			
			add(nameField);
			add(new SimpleFormComponentLabel("layer-name-label", nameField));

			add(descriptionField);
			add(new SimpleFormComponentLabel("layer-description-label", descriptionField));
			
			
			AjaxFormValidatingBehavior.addToAllFormComponents(this, "onkeyup", Duration.ONE_SECOND);

			AjaxButton submitButton = new AjaxButton("layer-form-submit-button", this) {

				private static final long serialVersionUID = 1L;
				
				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					
					if(Layer.LAYER.equals(itemType)){
						Layer l = (Layer)form.getModel().getObject();
						
						String projectId = getParameter("project");
						l.setProjectId(Integer.valueOf(projectId));
						
						//TODO
						if(l.getId() == 0){
							System.out.println("ADDING LAYER");
							System.out.println(l.getName()+" "+l.getDescription());
							System.out.println(l.getIcon());
							
							
							getDBManager().addLayer(l);
						}else{
							System.out.println("EDITING LAYER");
							System.out.println(l.getName()+" "+l.getDescription());
							System.out.println(l.getIcon());
							
							getDBManager().updateLayer(l);
						}
						
						Index index = (Index)getPage();
						SideBarPanel panel = index.getSideBarPanel();
						panel.refresh();
						
						target.add(panel);
					}else if(SubLayer.SUBLAYER.equals(itemType)){
						
						SubLayer l = (SubLayer)form.getModel().getObject();
						Index index = (Index)getPage();
						SideBarPanel panel = index.getSideBarPanel();
						
						String projectId = getParameter("project");
						l.setProjectId(Integer.valueOf(projectId));
						l.setLayerId(panel.getCurrentLayer().getId());
						
						//TODO
						if(l.getId() == 0){
							System.out.println("ADDING SUBLAYER");
							System.out.println(l.getName()+" "+l.getDescription());
							System.out.println(l.getIcon());
							
							
							getDBManager().addSubLayer(l);
						}else{
							System.out.println("EDITING SUBLAYER");
							System.out.println(l.getName()+" "+l.getDescription());
							System.out.println(l.getIcon());
							
							getDBManager().updateSubLayer(l);
						}
						
						SubLayersPanel spanel = index.getMainPanel().getSubLayersPanel();
						spanel.refresh();
						
						target.add(spanel);
					}
					
					
					target.add(feedbackPanel);
					target.appendJavaScript("load(); resize(); hideDialog();");
				}
				
				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedbackPanel);
				}
				
			};
			add(submitButton);	
			
		}
	}

}
