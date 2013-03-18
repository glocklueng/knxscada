package pl.marek.knx.controls;

import pl.marek.knx.R;
import android.content.Context;

public enum ControlType {
	
	ON_OFF_SWITCH {
		@Override
		public Controller createView(Context context) {
			OnOffSwitch onOffSwitch = new OnOffSwitch(context);
			return onOffSwitch;
		}
	},
	LIGHT_ON_OFF_SWITCH {
		@Override
		public Controller createView(Context context) {
			OnOffSwitch onOffSwitch = new OnOffSwitch(context);
			onOffSwitch.setOnBackgroundColor(context.getResources().getColor(R.color.control_light_on_background));
			onOffSwitch.setOffBackgroundColor(context.getResources().getColor(R.color.control_light_off_background));
			return onOffSwitch;
		}
	},
	SLIDER {
		@Override
		public Controller createView(Context context) {
			Slider slider = new Slider(context);
			return slider;
		}
	},
	LIGHT_SLIDER {
		@Override
		public Controller createView(Context context) {
			Slider slider = new Slider(context);
			slider.setStartColor(context.getResources().getColor(R.color.control_light_slider_low_color));
			slider.setStopColor(context.getResources().getColor(R.color.control_light_slider_high_color));
			return slider;
		}
	};
	
	public abstract Controller createView(Context context);

}
