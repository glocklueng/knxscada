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
			types.add(ElementGroupAddressType.OTHER);
			
			return types;
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
		
	};
	
	public abstract Controller getController(String id, Element element);
	public abstract ArrayList<ElementGroupAddressType> getAddressTypes();

}
