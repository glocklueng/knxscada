package pl.marek.knx.pages;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;

import android.content.Context;

import pl.marek.knx.KNXWebApplication;
import pl.marek.knx.SignInSession;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.utils.PasswordUtil;

@HtmlFile("signin.html")
public class SignIn extends WebPage {

	private static final long serialVersionUID = 1L;
	
	private FeedbackPanel feedback;
	
	public SignIn() {
		feedback = new FeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);
		add(feedback);
		add(new SignInForm("signInForm"));
		

	}
	
	@Override
	protected void onBeforeRender() {
		if(getStoredPassword().isEmpty()){
			warn(getString("password.NotSet"));
		}
		super.onBeforeRender();
	}
	
	private String getStoredPassword(){
		KNXWebApplication app = (KNXWebApplication)getApplication();
		Context con = app.getAndroidContext();
		if(con == null){
			return "";
		}
		String pref = app.getDefaultPreferenceFile();
		return PasswordUtil.getStoredPassword(con, pref);
	}
	

	public final class SignInForm extends Form<Void> {
		private static final long serialVersionUID = 1L;

		private static final String PASSWORD = "password";

		private final ValueMap properties = new ValueMap();

		public SignInForm(final String id) {
			super(id);
			
			PasswordTextField passwordField = new PasswordTextField(PASSWORD, new PropertyModel<String>(properties, PASSWORD));
			add(passwordField);
			setOutputMarkupId(true);

			add(new AjaxButton("signInButton", this)
	        {
				private static final long serialVersionUID = 1L;

				@Override
	            protected void onSubmit(AjaxRequestTarget target, Form<?> form){	            	
	    			SignInSession session = getMySession();
	    			
	    			if (session.signIn(getPassword(), getStoredPassword())) {
	    				continueToOriginalDestination();
	    				setResponsePage(Index.class);
	    			} else {
	    				String errmsg = getString("password.Incorrect");
	    				error(errmsg);
	    			}
	                target.add(feedback);
	            }

	            @Override
	            protected void onError(AjaxRequestTarget target, Form<?> form){
	                target.add(feedback);
	            }
	        });
		}
		
		
		private String getPassword() {
			return properties.getString(PASSWORD);
		}

		private SignInSession getMySession() {
			return (SignInSession) getSession();
		}
		
		
	}
}
