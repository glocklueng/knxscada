package pl.marek.knx.pages;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;

public class BasePanel extends Panel{

	private static final long serialVersionUID = 1L;
	
	public BasePanel(String id) {
		super(id);
	}

	protected String getParameter(String key){
		String value = RequestCycle.get().getRequest().getRequestParameters().getParameterValue(key).toString();
		return value;
	}
	

}
