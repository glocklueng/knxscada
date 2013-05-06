package pl.marek.knx.pages;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Project;
import pl.marek.knx.pages.DialogsPanel.DialogType;
import pl.marek.knx.utils.IconUtil;
import pl.marek.knx.utils.StaticImage;

@HtmlFile("header.html")
public class HeaderPanel extends BasePanel {
	 
	private static final long serialVersionUID = 1L;
	
	private Label projectName;
	
	private StaticImage telegramsIcon;
	
	public HeaderPanel(String componentName, DBManager dbManager) {
        super(componentName, dbManager);
        loadComponents();
    }
	
	private void loadComponents(){
		
		Project project = getCurrentProject();
		
		if(project != null){
	        projectName = new Label("projectName", new PropertyModel<Project>(project, "name"));
        }else{
        	projectName = new Label("projectName", "");
        }
		
		telegramsIcon = new StaticImage("open-telegrams-dialog-button-icon", new Model<String>("images/telegram_icon.png"));
        telegramsIcon.add(new TelegramsIconClickBehavior());
		
        add(IconUtil.getApplicationLogo());
        add(projectName);
        add(telegramsIcon);
        add(createLogoutForm());
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
	
	private class TelegramsIconClickBehavior extends AjaxEventBehavior{

		private static final long serialVersionUID = 1L;

		public TelegramsIconClickBehavior() {
			super("click");
		}
		
		@Override
		protected void onEvent(AjaxRequestTarget target) {
			DialogsPanel dialogs = getDialogsPanel();
			dialogs.setType(DialogType.TELEGRAMS);
			dialogs.setDialogPanel(new TelegramsDialog("dialog", getDBManager()));
			target.add(dialogs);
			target.appendJavaScript("initTelegramsDialog('"+getString("close")+"'); showDialog();");
		
		}
		
	}
	 
}