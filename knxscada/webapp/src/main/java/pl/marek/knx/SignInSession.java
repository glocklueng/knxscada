package pl.marek.knx;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import pl.marek.knx.utils.PasswordUtil;

public final class SignInSession extends AuthenticatedWebSession {

	private static final long serialVersionUID = 1L;

	private boolean logged;

	protected SignInSession(Request request) {
		super(request);
		logged = false;
	}

	@Override
	public final boolean authenticate(String password, String storedPassword) {
		
		if (!logged) {
			String pass = PasswordUtil.encryptPassword(password);
			if (pass.equals(storedPassword)) {
				logged = true;
			}
		}	
		
		//logged = true;
		
		return logged;
	}

	@Override
	public Roles getRoles() {
		if (isSignedIn()) {
			return new Roles(Roles.ADMIN);
		}
		return null;
	}
}