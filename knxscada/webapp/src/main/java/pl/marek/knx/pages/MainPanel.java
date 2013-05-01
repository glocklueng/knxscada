package pl.marek.knx.pages;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Layer;

@HtmlFile("main.html")
public class MainPanel extends BasePanel {
	 
	private static final long serialVersionUID = 1L;
	
	private DBManager dbManager;
	
	private SubLayersPanel subLayersPanel;
	private ContentPanel contentPanel;
	
	private Layer layer;
	
	public MainPanel(String componentName, DBManager dbManager) {
        super(componentName);
        this.dbManager = dbManager;
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
    	subLayersPanel = new SubLayersPanel(componentName, dbManager);
        add(subLayersPanel);
    }
    
    protected void setContentWrapper(String componentName){
    	contentPanel = new ContentPanel(componentName, dbManager);
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
		contentPanel.setSubLayer(layer.getSubLayers().get(0));
	}

	public Layer getLayer() {
		return layer;
	}

}
