package pl.marek.knx.pages;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("footer.html")
public class FooterPanel extends BasePanel{
	
	private static final long serialVersionUID = 1L;

	public FooterPanel(String componentName, DBManager dbManager) {
        super(componentName, dbManager);
        setOutputMarkupId(true);        
    }
}
