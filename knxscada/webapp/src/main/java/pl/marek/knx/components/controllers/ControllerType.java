package pl.marek.knx.components.controllers;

import java.util.ArrayList;

import pl.marek.knx.database.Element;

public enum ControllerType {
	
	ON_OFF_SWITCH {
		
		@Override
		public Controller getController(String id, Element element) {
			OnOffSwitch onOffSwitch = new OnOffSwitch(id, this, element);
			return onOffSwitch;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			types.add(ElementGroupAddressType.STATUS);
			
			return types;
		}

		@Override
		public String getName() {
			return "element-switch";
		}

		@Override
		public String getIcon() {
			return "images/elements/element_switch.png";
		}
	},
	ON_OFF_TOGGLE {
		@Override
		public Controller getController(String id, Element element) {
			OnOffSwitch onOffSwitch = new OnOffSwitch(id, this, element);
			return onOffSwitch;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			types.add(ElementGroupAddressType.STATUS);
			return types;
		}

		@Override
		public String getName() {
			return "element-toggle";
		}

		@Override
		public String getIcon() {
			return "images/elements/element_toggle.png";
		}

	},
	LIGHT_ON_OFF_SWITCH {
		@Override
		public Controller getController(String id, Element element) {
			OnOffSwitch onOffSwitch = new OnOffSwitch(id, this, element);
			return onOffSwitch;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			types.add(ElementGroupAddressType.STATUS);
			return types;
		}

		@Override
		public String getName() {
			return "element-light-switch";
		}

		@Override
		public String getIcon() {
			return "images/elements/element_bulb_on_10.png";
		}

	},
	SLIDER {
		@Override
		public Controller getController(String id, Element element) {
			Controller slider = new DefaultSlider(id, element, this);
			return slider;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			types.add(ElementGroupAddressType.STATUS);
			return types;
		}

		@Override
		public String getName() {
			return "element-slider";
		}

		@Override
		public String getIcon() {
			return "images/elements/element_slider_cut.png";
		}

	},
	LIGHT_SLIDER {
		@Override
		public Controller getController(String id, Element element) {
			Controller slider = new LightSlider(id, element, this);
			return slider;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			types.add(ElementGroupAddressType.STATUS);
			return types;
		}

		@Override
		public String getName() {
			return "element-light-slider";
		}

		@Override
		public String getIcon() {
			return "images/elements/element_light_slider.png";
		}

	},
	
	DIGITAL_VALUE{

		@Override
		public Controller getController(String id, Element element) {
			Controller digitalValue = new DigitalValueViewer(id, element, this);
			return digitalValue;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			return types;
		}

		@Override
		public String getName() {
			return "element-digital-value";
		}

		@Override
		public String getIcon() {
			return "images/elements/element_digital_value.png";
		}
		
	},
	ANALOG_VALUE{

		@Override
		public Controller getController(String id, Element element) {
			Controller analogValue = new AnalogValueViewer(id, element, this);
			return analogValue;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			return types;
		}

		@Override
		public String getName() {
			return "element-analog-value";
		}

		@Override
		public String getIcon() {
			return "images/elements/element_analog_value.png";
		}
		
	};
	
	public abstract Controller getController(String id, Element element);
	public abstract ArrayList<ElementGroupAddressType> getAddressTypes();
	public abstract String getName();
	public abstract String getIcon();

}
