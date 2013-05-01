package pl.marek.knx.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("forms.html")
public class FormsPanel extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	private Component formPanel;
	
	private DBManager dbManager;
	
	private FormType type;
		
	public FormsPanel(String componentName, DBManager dbManager) {
		super(componentName);
		
		setOutputMarkupId(true);

		this.dbManager = dbManager;
		type = FormType.DUMMY;
		loadComponents();
		
	}
	
    private void loadComponents(){
    	removeAll();
    	if(type.equals(FormType.DUMMY)){
    		formPanel = new Label("form");	
    	}
    	add(formPanel);
    	
    }
    
	public Component getFormPanel() {
		return formPanel;
	}

	public void setFormPanel(Component formPanel) {
		this.formPanel = formPanel;
		loadComponents();
	}

	public FormType getType() {
		return type;
	}

	public void setType(FormType type) {
		this.type = type;
		loadComponents();
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		String title = getString(type.getTitle(),Model.of(""),"");
		tag.put("title", title);
	}
	
	public enum FormType{
		DUMMY {

			@Override
			public String getTitle() {
				return "";
			}
		}, 
		NEW_PROJECT {

			@Override
			public String getTitle() {
				return "new-project";
			}
		},
		EDIT_PROJECT{

			@Override
			public String getTitle() {
				return "edit-project";
			}
			
		};
		
		public abstract String getTitle();
	}
	
}
