package pl.marek.knx.components.controllers;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import pl.marek.knx.annotations.HtmlFile;
import pl.marek.knx.database.Element;
import pl.marek.knx.pages.AjaxBehavior;

@HtmlFile("components/controllers/slider.html")
public class Slider extends Controller{

	private static final long serialVersionUID = 1L;
	
	private Label nameView;
	private Label descriptionView;
	private Label sliderView; 
	
	private OnSliderChangeBehavior onSliderChangeBehavior;

	public Slider(String id, Element element, ControllerType type) {
		super(id, element, type);
		loadComponents();
	}
	
	private void loadComponents(){
		nameView = new Label("controller-name", Model.of(""));
		descriptionView = new Label("controller-description", Model.of(""));
		sliderView = new Label("controller-slider", Model.of("")){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("value", "0");
				tag.put("callback", onSliderChangeBehavior.getCallbackUrl().toString());
				super.onComponentTag(tag);
			}
		};
		
		if(getElement() != null){
			nameView.setDefaultModel(new Model<String>(getElement().getName()));
			descriptionView.setDefaultModel(new Model<String>(getElement().getDescription()));
		}
		
		onSliderChangeBehavior = new OnSliderChangeBehavior();
				
		add(nameView);
		add(descriptionView);
		add(sliderView);
		add(onSliderChangeBehavior);
	}
	
	@Override
	public void setName(String name) {
		nameView.setDefaultModelObject(name);
	}

	@Override
	public void setDescription(String description) {
		descriptionView.setDefaultModelObject(description);
	}
	
	protected class OnSliderChangeBehavior extends AjaxBehavior{

		private static final long serialVersionUID = 1L;

		@Override
		protected void respond(AjaxRequestTarget target) {
			System.out.println(getRequestMessage());
		}
		
	}

}
