package pl.marek.knx.components.controllers;

import org.apache.wicket.markup.html.WebMarkupContainer;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Element;

@HtmlFile("components/controllers/lightslider.html")
public class LightSlider extends Slider{

	private static final long serialVersionUID = 1L;
	
	private WebMarkupContainer valueImages;

	public LightSlider(String id, Element element, ControllerType type) {
		super(id, element, type);
		loadComponents();
	}
	
	private void loadComponents(){
		valueImages = new WebMarkupContainer("controller-value");
		
		add(valueImages);
	}

	
}
