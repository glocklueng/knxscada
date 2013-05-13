package pl.marek.knx.pages;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.http.WebRequest;

public abstract class AjaxBehavior extends AbstractDefaultAjaxBehavior{
	
	private static final long serialVersionUID = 1L;

	protected String getRequestMessage(){
		String message = null;
		
		RequestCycle requestCycle = RequestCycle.get();
		WebRequest wr=(WebRequest)requestCycle.getRequest();

		HttpServletRequest hsr = (HttpServletRequest) wr.getContainerRequest();
		try {
			BufferedReader br = hsr.getReader();
			String  msg = br.readLine();
			if((msg != null) && !msg.isEmpty()){
				message = msg;
			}
			br.close();
		} catch (IOException ex) {
			requestCycle.scheduleRequestHandlerAfterCurrent(new TextRequestHandler("read-message-failed"));
		}
		
		return message;
	}
}
