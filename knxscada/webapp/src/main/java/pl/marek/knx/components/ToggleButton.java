package pl.marek.knx.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("components/toggle.html")
public class ToggleButton extends CompoundButton{

	private static final long serialVersionUID = 1L;
	
	private WebMarkupContainer toggleContainer;
	private Label toggleLabel;
	
	public ToggleButton(String id) {
		super(id);
		setOutputMarkupId(true);
		
		load();
	}
	
	private void load(){
		removeAll();
		
		toggleContainer = new WebMarkupContainer("toggle"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				updateComponentTag(tag);
				super.onComponentTag(tag);
			}
		};
		
		toggleLabel = new Label("toggle-label", getStateText());
		toggleContainer.add(toggleLabel);
		add(toggleContainer);
		
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		tag.put("style", "position: absolute; top: 10px;");
		super.onComponentTag(tag);
	}

	@Override
	public void refresh() {
		load();
	}
}
