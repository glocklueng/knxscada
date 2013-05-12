package pl.marek.knx.components.controllers;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Element;

@HtmlFile("components/controllers/onoffswitch.html")
public class OnOffSwitch extends Controller{

	private static final long serialVersionUID = 1L;
	
	private Label nameView;
	private Label descriptionView;
	
	public OnOffSwitch(String id, Element element) {
		super(id, element, ControllerType.ON_OFF_SWITCH);
		loadComponents();
	}
	
	private void loadComponents(){
		nameView = new Label("controller-name", Model.of(""));
		descriptionView = new Label("controller-description", Model.of(""));
		
		if(getElement() != null){
			nameView.setDefaultModel(new Model<String>(getElement().getName()));
			descriptionView.setDefaultModel(new Model<String>(getElement().getDescription()));
		}
		
		add(nameView);
		add(descriptionView);
	}

	@Override
	public void setName(String name) {
		nameView.setDefaultModelObject(name);
	}

	@Override
	public void setDescription(String description) {
		descriptionView.setDefaultModelObject(description);
	}
	
}
