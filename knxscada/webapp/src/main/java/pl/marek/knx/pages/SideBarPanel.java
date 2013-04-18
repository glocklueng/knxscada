package pl.marek.knx.pages;

import org.apache.wicket.markup.html.panel.Panel;

import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("sidebar.html")
public class SideBarPanel extends Panel {
	 
	private static final long serialVersionUID = 1L;

	public SideBarPanel(String componentName) {
        super(componentName);
    }
 
}