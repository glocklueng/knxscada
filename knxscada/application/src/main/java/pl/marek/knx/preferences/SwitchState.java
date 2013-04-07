package pl.marek.knx.preferences;

import android.widget.Switch;

public enum SwitchState {
	OFF {
		@Override
		public void setSwitchMode(Switch controlSwitch) {
			controlSwitch.setEnabled(true);
			controlSwitch.setChecked(false);
		}
	},
	OFF_ON_CHANGE {
		@Override
		public void setSwitchMode(Switch controlSwitch) {
			controlSwitch.setEnabled(false);
			controlSwitch.setChecked(true);
		}
	},
	ON {
		@Override
		public void setSwitchMode(Switch controlSwitch) {
			controlSwitch.setEnabled(true);
			controlSwitch.setChecked(true);
		}
	},
	ON_OFF_CHANGE {
		@Override
		public void setSwitchMode(Switch controlSwitch) {
			controlSwitch.setEnabled(false);
			controlSwitch.setChecked(false);
		}
	};
	
	public abstract void setSwitchMode(Switch controlSwitch);
}
