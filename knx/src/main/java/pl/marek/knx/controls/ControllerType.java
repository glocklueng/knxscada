package pl.marek.knx.controls;

import java.util.ArrayList;

import pl.marek.knx.R;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress.ElementGroupAddressType;
import android.content.Context;

public enum ControllerType {
	
	ON_OFF_SWITCH {
		@Override
		public Controller createView(Context context, Element element) {
			OnOffSwitch onOffSwitch = new OnOffSwitch(context, element);
			onOffSwitch.setChangeBackgroundColorByState(true);
			return onOffSwitch;
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
			types.add(ElementGroupAddressType.OTHER);
			
			return types;
		}
	};
//	LIGHT_ON_OFF_SWITCH {
//		@Override
//		public Controller createView(Context context, Element element) {
//			OnOffSwitch onOffSwitch = new OnOffSwitch(context, element);
//			onOffSwitch.setOnBackgroundColor(context.getResources().getColor(R.color.control_light_on_background));
//			onOffSwitch.setOffBackgroundColor(context.getResources().getColor(R.color.control_light_off_background));
//			return onOffSwitch;
//		}
//
//		@Override
//		public String getTitle(Context context) {
//			// TODO Auto-generated method stub
//			return "Włącznik światła";
//		}
//
//		@Override
//		public int getIcon() {
//			// TODO Auto-generated method stub
//			return R.drawable.room_bathroom_icon;
//		}
//
//		@Override
//		public ArrayList<ElementGroupAddressType> getAddressTypes() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//	},
//	SLIDER {
//		@Override
//		public Controller createView(Context context, Element element) {
//			Slider slider = new Slider(context, element);
//			return slider;
//		}
//
//		@Override
//		public String getTitle(Context context) {
//			// TODO Auto-generated method stub
//			return "Regulator";
//		}
//
//		@Override
//		public int getIcon() {
//			// TODO Auto-generated method stub
//			return R.drawable.room_bedroom_icon;
//		}
//
//		@Override
//		public ArrayList<ElementGroupAddressType> getAddressTypes() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//	},
//	LIGHT_SLIDER {
//		@Override
//		public Controller createView(Context context, Element element) {
//			Slider slider = new Slider(context, element);
//			slider.setStartColor(context.getResources().getColor(R.color.control_light_slider_low_color));
//			slider.setStopColor(context.getResources().getColor(R.color.control_light_slider_high_color));
//			return slider;
//		}
//
//		@Override
//		public String getTitle(Context context) {
//			// TODO Auto-generated method stub
//			return "Ściamniacz";
//		}
//
//		@Override
//		public int getIcon() {
//			// TODO Auto-generated method stub
//			return R.drawable.room_toilet_icon;
//		}
//
//		@Override
//		public ArrayList<ElementGroupAddressType> getAddressTypes() {
//			// TODO Auto-generated method stub
//			return null;
//		}
//	};
	
	public abstract Controller createView(Context context, Element element);
	public abstract String getTitle(Context context);
	public abstract int getIcon();
	public abstract ArrayList<ElementGroupAddressType> getAddressTypes();

}
