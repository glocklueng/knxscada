package pl.marek.knx.pages;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.interfaces.DatabaseManager;

@HtmlFile("footer.html")
public class FooterPanel extends BasePanel{
	
	private static final long serialVersionUID = 1L;

	public FooterPanel(String componentName, DatabaseManager dbManager) {
        super(componentName, dbManager);
        setOutputMarkupId(true);        
    }
}
