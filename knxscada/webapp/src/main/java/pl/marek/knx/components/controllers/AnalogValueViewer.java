package pl.marek.knx.components.controllers;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Element;

@HtmlFile("components/controllers/valueviewer.html")
public class AnalogValueViewer extends ValueViewer{

	private static final long serialVersionUID = 1L;

	public AnalogValueViewer(String id, Element element, ControllerType type) {
		super(id, element, type);
	}
}