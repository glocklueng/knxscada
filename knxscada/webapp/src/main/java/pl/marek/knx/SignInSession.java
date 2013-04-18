package pl.marek.knx;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

public final class SignInSession extends AuthenticatedWebSession {

	private static final long serialVersionUID = 1L;

	private String user;

	protected SignInSession(Request request) {
		super(request);
	}

	@Override
	public final boolean authenticate(final String username,
			final String password) {
		final String WICKET = "knx";

		if (user == null) {

			if (WICKET.equalsIgnoreCase(password)) {
				user = username;
			}
		}

		return user != null;
	}

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	@Override
	public Roles getRoles() {
		if (isSignedIn()) {
			return new Roles(Roles.ADMIN);
		}
		return null;
	}

}