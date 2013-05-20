package pl.marek.knx.controls;

import java.util.ArrayList;

import pl.marek.knx.R;
import pl.marek.knx.database.Element;
import android.content.Context;

public enum ControllerType {
	
	ON_OFF_SWITCH {
		@Override
		public Controller createView(Context context, Element element) {
			OnOff onOff = new OnOff(context, this, element);
			onOff.setChangeBackgroundColorByState(true);
			return onOff;
		}

		@Override
		public String getTitle(Context context) {
			return context.getString(R.string.element_switch);
		}

		@Override
		public int getIcon() {
			return R.drawable.element_switch;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			types.add(ElementGroupAddressType.STATUS);
			return types;
		}

		@Override
		public int getLayout() {
			return R.layout.control_onoffswitch;
		}
	},
	ON_OFF_TOGGLE {
		@Override
		public Controller createView(Context context, Element element) {
			OnOff onOff = new OnOff(context, this, element);
			onOff.setChangeBackgroundColorByState(true);
			return onOff;
		}

		@Override
		public String getTitle(Context context) {
			return context.getString(R.string.element_toggle);
		}

		@Override
		public int getIcon() {
			return R.drawable.element_toggle;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			types.add(ElementGroupAddressType.STATUS);
			return types;
		}

		@Override
		public int getLayout() {
			return R.layout.control_onofftoggle;
		}
	},
	LIGHT_ON_OFF_SWITCH {
		@Override
		public Controller createView(Context context, Element element) {
			OnOff onOff = new OnOff(context, this, element);
			onOff.setChangeBackgroundColorByState(true);
			onOff.setOnBackgroundColor(context.getResources().getColor(R.color.control_light_on_background));
			onOff.setOffBackgroundColor(context.getResources().getColor(R.color.control_light_off_background));
			
			return onOff;
		}

		@Override
		public String getTitle(Context context) {
			return context.getString(R.string.element_light_switch);
		}

		@Override
		public int getIcon() {
			return R.drawable.element_bulb_on_10;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			types.add(ElementGroupAddressType.STATUS);
			return types;
		}

		@Override
		public int getLayout() {
			return R.layout.control_light_onoff;
		}
	},
	SLIDER {
		@Override
		public Controller createView(Context context, Element element) {
			Slider slider = new Slider(context, this, element);
			return slider;
		}

		@Override
		public String getTitle(Context context) {
			return context.getString(R.string.element_slider);
		}

		@Override
		public int getIcon() {
			return R.drawable.element_slider_cut;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			types.add(ElementGroupAddressType.STATUS);
			return types;
		}

		@Override
		public int getLayout() {
			return R.layout.control_slider;
		}
	},
	LIGHT_SLIDER {
		@Override
		public Controller createView(Context context, Element element) {
			Slider slider = new Slider(context, this, element);
			slider.setStartColor(context.getResources().getColor(R.color.control_light_slider_low_color));
			slider.setStopColor(context.getResources().getColor(R.color.control_light_slider_high_color));
			return slider;
		}

		@Override
		public String getTitle(Context context) {
			return context.getString(R.string.element_light_slider);
		}

		@Override
		public int getIcon() {
			return R.drawable.element_light_slider;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			types.add(ElementGroupAddressType.STATUS);
			return types;
		}

		@Override
		public int getLayout() {
			return R.layout.control_light_slider;
		}
	},
	DIGITAL_VALUE{

		@Override
		public Controller createView(Context context, Element element) {
			Controller digitalValue = new DigitalValue(context, this, element);
			return digitalValue;
		}

		@Override
		public String getTitle(Context context) {
			return context.getString(R.string.element_digital_value);
		}

		@Override
		public int getIcon() {
			return R.drawable.element_digital_value;
		}

		@Override
		public int getLayout() {
			return R.layout.control_digital_value;
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
		public Controller createView(Context context, Element element) {
			Controller digitalValue = new AnalogValue(context, this, element);
			return digitalValue;
		}

		@Override
		public String getTitle(Context context) {
			return context.getString(R.string.element_analog_value);
		}

		@Override
		public int getIcon() {
			return R.drawable.element_analog_value;
		}

		@Override
		public int getLayout() {
			return R.layout.control_digital_value;
		}

		@Override
		public ArrayList<ElementGroupAddressType> getAddressTypes() {
			ArrayList<ElementGroupAddressType> types = new ArrayList<ElementGroupAddressType>();
			types.add(ElementGroupAddressType.MAIN);
			return types;
		}
		
	};
	
	public abstract Controller createView(Context context, Element element);
	public abstract String getTitle(Context context);
	public abstract int getIcon();
	public abstract int getLayout();
	public abstract ArrayList<ElementGroupAddressType> getAddressTypes();

}
