package pl.marek.knx.components;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("components/switch.html")
public class Switch extends CompoundButton{

	private static final long serialVersionUID = 1L;
	
	private WebMarkupContainer switchContainer;
	private Label switchThumb;

	public Switch(String id) {
		super(id);
		setOutputMarkupId(true);
		
		load();
	}
	
	private void load(){
		removeAll();
		
		switchContainer = new WebMarkupContainer("switch"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				updateComponentTag(tag);
				super.onComponentTag(tag);
			}
		};
		
		switchThumb = new Label("switch-thumb", getStateText());
		switchContainer.add(switchThumb);
		add(switchContainer);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		tag.put("style", "position: absolute; top: 25px;");
		super.onComponentTag(tag);
	}

	@Override
	public void refresh() {
		load();
	}

}
