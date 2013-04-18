package pl.marek.knx.pages;

import org.apache.wicket.markup.html.panel.Panel;

import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("footer.html")
public class FooterPanel extends Panel{
	
	private static final long serialVersionUID = 1L;

	public FooterPanel(String componentName) {
        super(componentName);
    }

}
