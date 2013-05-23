package pl.marek.knx.components.controllers;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Element;

@HtmlFile("components/controllers/valueviewer.html")
public abstract class ValueViewer extends Controller{

	private static final long serialVersionUID = 1L;
	
	private Label nameView;
	private Label descriptionView;
	private Label valueView;
	
	public ValueViewer(String id, Element element, ControllerType type) {
		super(id, element, type);
		loadComponents();
	}
	
	private void loadComponents(){
		nameView = new Label("controller-name", Model.of(""));
		descriptionView = new Label("controller-description", Model.of(""));
		valueView = new Label("controller-value", Model.of(""));
		
		if(getElement() != null){
			nameView.setDefaultModel(new Model<String>(getElement().getName()));
			descriptionView.setDefaultModel(new Model<String>(getElement().getDescription()));
		}
		
		add(nameView);
		add(descriptionView);
		add(valueView);
		add(new OnValueViewerClickListener());
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
	public void setMinValue(double minValue) {	}
	
	@Override
	public void setMaxValue(double maxValue) {}
	
	public void setValue(String value){
		valueView.setDefaultModelObject(value);
	}
	
	protected class OnValueViewerClickListener extends AjaxEventBehavior{

		private static final long serialVersionUID = 1L;
		
		public OnValueViewerClickListener() {
			super("click");
		}
		
		@Override
		protected void onEvent(AjaxRequestTarget target) {
			readTelegram();
		}
	}
	
	protected abstract void readTelegram();
	
}