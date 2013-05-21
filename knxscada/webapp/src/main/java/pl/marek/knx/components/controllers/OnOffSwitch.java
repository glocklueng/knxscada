package pl.marek.knx.components.controllers;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.components.CompoundButton;
import pl.marek.knx.components.CompoundButton.OnChangeListener;
import pl.marek.knx.components.LightImageButton;
import pl.marek.knx.components.Switch;
import pl.marek.knx.components.ToggleButton;
import pl.marek.knx.database.Element;

@HtmlFile("components/controllers/onoffswitch.html")
public class OnOffSwitch extends Controller implements OnChangeListener{

	private static final long serialVersionUID = 1L;
	
	private Label nameView;
	private Label descriptionView;
	private CompoundButton buttonView;
	
	public OnOffSwitch(String id, ControllerType type, Element element) {
		super(id, element, type);
		loadComponents();
	}
	
	private void loadComponents(){
		nameView = new Label("controller-name", Model.of(""));
		descriptionView = new Label("controller-description", Model.of(""));
		
		if(getElement() != null){
			nameView.setDefaultModel(new Model<String>(getElement().getName()));
			descriptionView.setDefaultModel(new Model<String>(getElement().getDescription()));
		}
		
		switch(getType()){
			case ON_OFF_SWITCH:
				buttonView = new Switch("controller-button");
				break;
			case ON_OFF_TOGGLE:
				buttonView = new ToggleButton("controller-button");
				break;
			case LIGHT_ON_OFF_SWITCH:
				buttonView = new LightImageButton("controller-button");
				break;
		}
		buttonView.setOnChangeListener(this);
		
		add(nameView);
		add(descriptionView);
		add(buttonView);
	}

	@Override
	public void setName(String name) {
		nameView.setDefaultModelObject(name);
	}

	@Override
	public void setDescription(String description) {
		descriptionView.setDefaultModelObject(description);
	}

	@Override
	public void onChange(boolean state) {
		System.out.println(state);
	}
	
}