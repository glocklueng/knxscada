package pl.marek.knx.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.interfaces.AuthenticatedWebPage;

@HtmlFile("index.html")
public class Index extends WebPage implements AuthenticatedWebPage{

	private static final long serialVersionUID = 1L;
	
	private DBManager dbManager;
	
	private HeaderPanel headerPanel;
	private SideBarPanel sideBarPanel;
	private MainPanel mainPanel;
	private FooterPanel footerPanel;
	private ProjectChooserPanel projectChooserPanel;
	private FormsPanel formsPanel;
	
	public Index(){
		super();
		init();
	}
	
	public Index(PageParameters parameters){
		super(parameters);
		init();
	}
	
	private void init(){
		dbManager = new DBManager();
		
//		PageParameters parameters = getPageParameters();
		
//		if(parameters.get("project").isNull()){
//			//redirectToInterceptPage(new ProjectChooserPanel());	
//		}
		
	}
	
	public Index(IModel<?> model) {
        super(model);
    }
 
    protected void onBeforeRender() {
    	
        if(get("header") == null){
            this.setHeaderWrapper("header");
        }
        if(get("sidebar")==null){
            this.setLeftWrapper("sidebar");
        }
        if(get("main")==null){
            this.setCenterWrapper("main");
        }
        if(get("footer")==null){
            this.setFooterWrapper("footer");
        }
        if(get("projectchooser")==null){
            this.setProjectChooserWrapper("projectchooser");
        }
        if(get("forms")==null){
            this.setFormsWrapper("forms");
        }
        
        super.onBeforeRender();
    }
 
    protected void setHeaderWrapper(String componentName){
    	headerPanel = new HeaderPanel(componentName, dbManager);
        add(headerPanel);
    }
    
    protected void setLeftWrapper(String componentName){
    	sideBarPanel = new SideBarPanel(componentName, dbManager);
        add(sideBarPanel);
    }
    
    protected void setCenterWrapper(String componentName){
    	mainPanel = new MainPanel(componentName, dbManager);
        add(mainPanel);
    }
    
    protected void setFooterWrapper(String componentName){
    	footerPanel = new FooterPanel(componentName, dbManager);
        add(footerPanel);
    }
    
    protected void setProjectChooserWrapper(String componentName){
    	projectChooserPanel = new ProjectChooserPanel(componentName, dbManager);
        add(projectChooserPanel);
    }
    
    protected void setFormsWrapper(String componentName){
    	formsPanel = new FormsPanel(componentName, dbManager);
        add(formsPanel);
    }

	public HeaderPanel getHeaderPanel() {
		return headerPanel;
	}

	public SideBarPanel getSideBarPanel() {
		return sideBarPanel;
	}

	public MainPanel getMainPanel() {
		return mainPanel;
	}

	public FooterPanel getFooterPanel() {
		return footerPanel;
	}

	public ProjectChooserPanel getProjectChooserPanel() {
		return projectChooserPanel;
	}

	public FormsPanel getFormsPanel() {
		return formsPanel;
	}
	
}