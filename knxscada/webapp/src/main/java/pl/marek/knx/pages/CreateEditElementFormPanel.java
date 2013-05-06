package pl.marek.knx.pages;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Element;

@HtmlFile("create_edit_element_form_panel.html")
public class CreateEditElementFormPanel  extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	private Element element;
	
	public CreateEditElementFormPanel (String componentName, DBManager dbManager) {
		super(componentName, dbManager);
		loadComponents();	
	}

	
	public CreateEditElementFormPanel (String componentName, DBManager dbManager, Element element) {
		super(componentName, dbManager);
		this.element = element;
		loadComponents();	
	}
	
	private void loadComponents(){
		
		
		
	}
	
}
