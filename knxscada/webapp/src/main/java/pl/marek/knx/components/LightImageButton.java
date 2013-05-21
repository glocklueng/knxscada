package pl.marek.knx.components;

import pl.marek.knx.annotations.HtmlFile;

@HtmlFile("components/imagebutton.html")
public class LightImageButton extends ImageButton{

	private static final long serialVersionUID = 1L;

	public LightImageButton(String id) {
		super(id);
		setOnImage("images/elements/element_bulb_on_10.png");
		setOffImage("images/elements/element_bulb_off.png");	
	}
}
