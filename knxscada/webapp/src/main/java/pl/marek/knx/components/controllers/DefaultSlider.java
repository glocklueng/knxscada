package pl.marek.knx.components.controllers;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Element;

@HtmlFile("components/controllers/slider.html")
public class DefaultSlider extends Slider{

	private static final long serialVersionUID = 1L;
	
	private Label valueView;
	
	public DefaultSlider(String id, Element element, ControllerType type) {
		super(id, element, type);
		loadComponents();
	}
	
	private void loadComponents(){
		valueView = new Label("controller-value", new Model<String>("0 %"));
		
		add(valueView);
	}
	
	public void setValue(String value){
		valueView.setDefaultModelObject(value);
	}
}
