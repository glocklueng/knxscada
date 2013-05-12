package pl.marek.knx.components.controllers;

import java.util.ArrayList;

import pl.marek.knx.database.Element;

public enum ControllerType {
	
	ON_OFF_SWITCH {
		
		@Override
		public Controller getController(String id, Element element) {
			OnOffSwitch onOffSwitch = new OnOffSwitch(id, element);
			return onOffSwitch;
		}

		@Override
		public String getTitle() {
			return "";
		}

		@Override
		public int getIcon() {
			return 0;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			types.add(ElementGroupAddressType.OTHER);
			
			return types;
		}
	};
	
	public abstract Controller getController(String id, Element element);
	public abstract String getTitle();
	public abstract int getIcon();
	public abstract ArrayList<ElementGroupAddressType> getAddressTypes();

}
