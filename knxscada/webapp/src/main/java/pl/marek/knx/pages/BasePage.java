package pl.marek.knx.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.interfaces.AuthenticatedWebPage;

@HtmlFile("index.html")
public class BasePage extends WebPage implements AuthenticatedWebPage{

	private static final long serialVersionUID = 1L;
	
	public BasePage(){

	}
	
	public BasePage(IModel<?> model) {
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
        super.onBeforeRender();
    }
 
    protected void setHeaderWrapper(String componentName){
        add(new HeaderPanel(componentName));
    }
    
    protected void setLeftWrapper(String componentName){
        add(new SideBarPanel(componentName));
    }
    
    protected void setCenterWrapper(String componentName){
        add(new MainPanel(componentName));
    }
    
    protected void setFooterWrapper(String componentName){
        add(new FooterPanel(componentName));
    }
}