package pl.marek.knx.pages;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.utils.ExternalImageResource;

@HtmlFile("content.html")
public class ContentPanel extends BasePanel{
	
	private static final long serialVersionUID = 1L;

	private SubLayer subLayer;
	
	private ElementsPanel elementsPanel;
	private SubLayerSettingsPanel settingsPanel;
	
	private Image backgroundImage;
	
	public ContentPanel(String componentName, DatabaseManager dbManager) {
        super(componentName, dbManager);
        setOutputMarkupId(true);
        
        Project project = getCurrentProject();
        if(project != null){
        	if(project.getLayers().size()>0){
        		Layer layer = project.getLayers().get(0);
        		if(layer.getSubLayers().size()>0){
        			subLayer = layer.getSubLayers().get(0);
        		}
        	}
        }
        
        loadComponents();
    }
	
	protected void onBeforeRender() {
    	
        if(get("elements-panel") == null){
            this.setElementsWrapper("elements-panel");
        }
        if(get("settings-panel") == null){
            this.setSettingsWrapper("settings-panel");
        }
        super.onBeforeRender();
    }
 
    protected void setElementsWrapper(String componentName){
    	elementsPanel = new ElementsPanel(componentName, getDBManager(), subLayer);
        add(elementsPanel);
    }
    
    protected void setSettingsWrapper(String componentName){
    	settingsPanel = new SubLayerSettingsPanel(componentName, getDBManager(), subLayer);
    	settingsPanel.setBackgroundImage(backgroundImage);
        add(settingsPanel);
    }
    
    @Override
    protected void onComponentTag(ComponentTag tag) {
    	Project project = getCurrentProject();
    	if(project == null){
    		tag.put("style", "display:none;");
    	}
    	super.onComponentTag(tag);
    }
    

	private void loadComponents(){
		removeAll();
		
		ExternalImageResource imageResource = new ExternalImageResource("");
		if(subLayer != null){
			imageResource = new ExternalImageResource(subLayer.getBackgroundImage());
		}
		backgroundImage = new Image("background-image", imageResource);
		
		backgroundImage.setOutputMarkupId(true);
		if(imageResource.exists()){
			backgroundImage.setImageResource(imageResource);
			backgroundImage.setVisible(true);
		}else{
			backgroundImage.setVisible(false);
			backgroundImage.setOutputMarkupPlaceholderTag(true);
		}
		
		add(backgroundImage);
		
		
		Label l = new Label("msg","");
		if(subLayer != null){
			l.setDefaultModelObject(subLayer.getDescription());
		}
		add(l);
				
	}

	public void setSubLayer(SubLayer subLayer) {
		this.subLayer = subLayer;
		elementsPanel.setSubLayer(subLayer);
		settingsPanel.setSubLayer(subLayer);
		loadComponents();
	}
	
	public void refresh(){
        subLayer = getDBManager().getSubLayerByIdWithDependencies(getCurrentSubLayer().getId());
        elementsPanel.setSubLayer(subLayer);
        settingsPanel.setSubLayer(subLayer);
        loadComponents();
	}
	
	public ElementsPanel getElementsPanel(){
		return elementsPanel;
	}
	
	public SubLayerSettingsPanel getSubLayerSettingsPanel(){
		return settingsPanel;
	}
	
	public Image getCurrentBackgroundImage(){
		return backgroundImage;
	}
	
}



