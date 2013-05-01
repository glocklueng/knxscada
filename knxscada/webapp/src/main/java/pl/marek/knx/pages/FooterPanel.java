package pl.marek.knx.pages;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("footer.html")
public class FooterPanel extends BasePanel{
	
	private static final long serialVersionUID = 1L;
	
	private DBManager dbManager;
	

	
	public FooterPanel(String componentName, DBManager dbManager) {
        super(componentName);
        this.dbManager = dbManager;
        setOutputMarkupId(true);
        

        
    }
	

}
