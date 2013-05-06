package pl.marek.knx.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import pl.marek.knx.DBManager;
import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("dialogs.html")
public class DialogsPanel extends BasePanel{

	private static final long serialVersionUID = 1L;
	
	private Component dialogPanel;
	private DialogType type;
		
	public DialogsPanel(String componentName, DBManager dbManager) {
		super(componentName, dbManager);
		setOutputMarkupId(true);
		type = DialogType.DUMMY;
		loadComponents();
	}
	
    private void loadComponents(){
    	removeAll();
    	if(type.equals(DialogType.DUMMY)){
    		dialogPanel = new Label("dialog");	
    	}
    	add(dialogPanel);
    	
    }
    
	public Component getDialogPanel() {
		return dialogPanel;
	}

	public void setDialogPanel(Component dialogPanel) {
		this.dialogPanel = dialogPanel;
		loadComponents();
	}

	public DialogType getType() {
		return type;
	}

	public void setType(DialogType type) {
		this.type = type;
		loadComponents();
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		String title = getString(type.getTitle(),Model.of(""),"");
		tag.put("title", title);
	}
	
	public enum DialogType{
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
			
		},
		DELETE_PROJECT{

			@Override
			public String getTitle() {
				return "delete-project-confirmation-title";
			}
			
		},
		NEW_LAYER {

			@Override
			public String getTitle() {
				return "new-layer";
			}
		},
		EDIT_LAYER{

			@Override
			public String getTitle() {
				return "edit-layer";
			}
			
		},
		DELETE_LAYER{

			@Override
			public String getTitle() {
				return "delete-layer-confirmation-title";
			}
			
		},
		NEW_SUBLAYER {

			@Override
			public String getTitle() {
				return "new-sublayer";
			}
		},
		EDIT_SUBLAYER{

			@Override
			public String getTitle() {
				return "edit-sublayer";
			}
			
		},
		DELETE_SUBLAYER{

			@Override
			public String getTitle() {
				return "delete-sublayer-confirmation-title";
			}
			
		},
		NEW_ELEMENT{

			@Override
			public String getTitle() {
				return "new-element";
			}
			
		},
		EDIT_ELEMENT{

			@Override
			public String getTitle() {
				return "edit-element";
			}
			
		},
		DELETE_ELEMENT{

			@Override
			public String getTitle() {
				return "delete-element-confirmation-title";
			}
			
		},
		TELEGRAMS{

			@Override
			public String getTitle() {
				return "telegrams";
			}
			
		};
		
		
		public abstract String getTitle();
	}
	
}
