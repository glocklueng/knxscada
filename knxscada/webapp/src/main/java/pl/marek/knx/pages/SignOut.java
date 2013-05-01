package pl.marek.knx.pages;

import org.apache.wicket.markup.html.WebPage;

import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("signout.html")
public class SignOut extends WebPage{
	
	private static final long serialVersionUID = 1L;

	public SignOut(){
		getSession().invalidateNow();
		redirectToInterceptPage(new SignIn());
	}

}
