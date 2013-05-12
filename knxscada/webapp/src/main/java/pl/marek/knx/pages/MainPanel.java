package pl.marek.knx.pages;

import java.util.ArrayList;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.interfaces.DatabaseManager;

@HtmlFile("main.html")
public class MainPanel extends BasePanel {
	 
	private static final long serialVersionUID = 1L;
		
	private SubLayersPanel subLayersPanel;
	private ContentPanel contentPanel;
	
	private Layer layer;
	
	public MainPanel(String componentName, DatabaseManager dbManager) {
        super(componentName, dbManager);
        setOutputMarkupId(true);
	}
		
	@Override
	protected void onRender() {
		super.onRender();
        
	}
		
    protected void onBeforeRender() {
    	
        if(get("sublayers") == null){
            this.setSubLayersWrapper("sublayers");
        }
        if(get("content")==null){
            this.setContentWrapper("content");
        }

        
        super.onBeforeRender();
    }
 
    protected void setSubLayersWrapper(String componentName){
    	subLayersPanel = new SubLayersPanel(componentName, getDBManager());
        add(subLayersPanel);
    }
    
    protected void setContentWrapper(String componentName){
    	contentPanel = new ContentPanel(componentName, getDBManager());
        add(contentPanel);
    }

	public SubLayersPanel getSubLayersPanel() {
		return subLayersPanel;
	}

	public ContentPanel getContentPanel() {
		return contentPanel;
	}
	
	public void setLayer(Layer layer){
		this.layer = layer;
		subLayersPanel.setLayer(layer);
		
		ArrayList<SubLayer> subLayers = layer.getSubLayers();
		if(subLayers != null && subLayers.size() > 0){
			contentPanel.setSubLayer(subLayers.get(0));
		}else{
			contentPanel.setSubLayer(null);
		}
	}

	public Layer getLayer() {
		return layer;
	}

}
