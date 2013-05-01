package pl.marek.knx.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Project;
import pl.marek.knx.utils.StaticImage;

@HtmlFile("header.html")
public class HeaderPanel extends BasePanel {
	 
	private static final long serialVersionUID = 1L;

	private DBManager dbManager;
	
	private Label projectName;
	
	public HeaderPanel(String componentName, DBManager dbManager) {
        super(componentName);
        this.dbManager = dbManager;
        loadComponents();
    }
	
	private void loadComponents(){
        String pId = getParameter("project");
        if(pId != null && !"0".equals(pId)){
	        int projectId = Integer.parseInt(pId);
	        Project project = dbManager.getProjectById(projectId);
	        projectName = new Label("projectName", new PropertyModel<Project>(project, "name"));
        }else{
        	projectName = new Label("projectName", "");
        }
        
        add(getApplicationLogo());
        add(projectName);
        add(createLogoutForm());
	}
	
	private StaticImage getApplicationLogo(){
		StaticImage image =  new StaticImage("appLogo", new Model<String>("images/logo.png"));
		return image;
	}
	
	private Form<Void> createLogoutForm(){
		Form<Void> form = new Form<Void>("logoutForm"){

			private static final long serialVersionUID = 1L;

			protected void onSubmit() {
        		redirectToInterceptPage(new SignOut());
        	};
        };
        return form;
	}
	 
}