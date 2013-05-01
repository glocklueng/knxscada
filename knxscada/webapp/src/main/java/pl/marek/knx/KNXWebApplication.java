package pl.marek.knx;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;

import pl.marek.knx.interfaces.AuthenticatedWebPage;
import pl.marek.knx.pages.*;

public class KNXWebApplication extends WebApplication {

	@Override
	public Class<? extends Page> getHomePage() {
		return Index.class;
	}

	@Override
	protected void init() {
		super.init();
		setWebPagesFinder();
		setAuthorizationStrategy();
		mountPages();
		mountResources();
		
	}

	private void setWebPagesFinder() {
		WebPagesFinder finder = new WebPagesFinder(getServletContext());
		getResourceSettings().getResourceFinders().add(finder);
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new SignInSession(request);
	}

	public void setAuthorizationStrategy() {
		getSecuritySettings().setAuthorizationStrategy(new AuthorizationStrategy());
	}

	private void mountPages() {
		mountPage("signin", SignIn.class);
		mountPage("signout", SignOut.class);

	}
	
	private void mountResources(){
//		 getSharedResources().add("image", new ExternalImageResource("/home/marek/Magisterka/Grafika/WWW/logo.png"));
//	     mountResource("image", new SharedResourceReference("downloads"));
	   
	}
	
	private class AuthorizationStrategy implements IAuthorizationStrategy {

		public boolean isActionAuthorized(Component component, Action action) {
			return true;
		}

		public <T extends IRequestableComponent> boolean isInstantiationAuthorized(Class<T> componentClass) {

			if (AuthenticatedWebPage.class.isAssignableFrom(componentClass)) {
				if (((SignInSession) Session.get()).isSignedIn()) {
					return true;
				}
				throw new RestartResponseAtInterceptPageException(SignIn.class);
			}
			return true;
		}
	}
}
