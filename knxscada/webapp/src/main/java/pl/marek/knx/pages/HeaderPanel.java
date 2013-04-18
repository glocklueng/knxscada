package pl.marek.knx.pages;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("header.html")
public class HeaderPanel extends Panel {
	 
	private static final long serialVersionUID = 1L;

	public HeaderPanel(String componentName) {
        super(componentName);
        
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
 
}