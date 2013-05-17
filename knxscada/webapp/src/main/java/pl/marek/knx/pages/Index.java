package pl.marek.knx.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import android.content.Context;

import pl.marek.knx.DBManager;
import pl.marek.knx.KNXWebApplication;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.database.Project;
import pl.marek.knx.interfaces.AuthenticatedWebPage;
import pl.marek.knx.interfaces.DatabaseManager;

@HtmlFile("index.html")
@RequireHttps
public class Index extends WebPage implements AuthenticatedWebPage{

	private static final long serialVersionUID = 1L;
	
	private DatabaseManager dbManager;
	
	private HeaderPanel headerPanel;
	private SideBarPanel sideBarPanel;
	private MainPanel mainPanel;
	private FooterPanel footerPanel;
	private ProjectChooserPanel projectChooserPanel;
	private DialogsPanel dialogsPanel;
	
	private Label pageTitle;
	
	public Index(){
		super();
		init();
	}
	
	public Index(PageParameters parameters){
		super(parameters);
		init();
	}
	
	private void init(){
		KNXWebApplication app = (KNXWebApplication)getApplication();
		Context context = app.getAndroidContext();
		if(context != null){
			dbManager = new DatabaseManagerImpl(context);
		}else{
			dbManager = new DBManager();
		}
		
		pageTitle = new Label("page-title", new ResourceModel("page-title"));
		add(pageTitle);
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
        if(get("dialogs")==null){
            this.setDialogsWrapper("dialogs");
        }
        
        super.onBeforeRender();
    }
 
    protected void setHeaderWrapper(String componentName){
    	headerPanel = new HeaderPanel(componentName, dbManager);
    	
    	Project project = headerPanel.getCurrentProject();
    	if(project != null){
    		setTitle(project.getName());
    	}
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
    
    protected void setDialogsWrapper(String componentName){
    	dialogsPanel = new DialogsPanel(componentName, dbManager);
        add(dialogsPanel);
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

	public DialogsPanel getDialogsPanel() {
		return dialogsPanel;
	}
	
	public void setTitle(String title){
		pageTitle.setDefaultModelObject(title);
	}
	
}