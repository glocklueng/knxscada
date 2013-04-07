import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import android.content.Context;



public class HelloServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
    private static final String CONTENT_RESOLVER_ATTRIBUTE = "pl.marek.knx.contentResolver";
    private static final String ANDROID_CONTEXT_ATTRIBUTE = "pl.marek.knx.context"; 
	
	private Context context;
	
    public void init(ServletConfig config) throws ServletException
    {
    	super.init(config);
    	//to demonstrate it is possible
        Object o = config.getServletContext().getAttribute(CONTENT_RESOLVER_ATTRIBUTE);
        android.content.ContentResolver resolver = (android.content.ContentResolver)o;
        context = (android.content.Context)config.getServletContext().getAttribute(ANDROID_CONTEXT_ATTRIBUTE);
        if(context != null){
        	System.out.println(context.getPackageName());
        }
        
    }
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		System.out.println("hello");
		PrintWriter writer = resp.getWriter();
		writer.print("Hello from HelloServlet");
		writer.flush();
		writer.close();
		
	}

}
