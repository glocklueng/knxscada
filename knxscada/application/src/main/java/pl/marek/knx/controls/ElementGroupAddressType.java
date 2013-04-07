package pl.marek.knx.controls;

import pl.marek.knx.R;
import android.content.Context;

public enum ElementGroupAddressType{
	MAIN {
		@Override
		public String getLabel(Context context) {
			return context.getString(R.string.dialog_controller_group_address_default_label);
		}
	},
	OTHER {
		@Override
		public String getLabel(Context context) {
			return context.getString(R.string.dialog_controller_group_address_other_label);
		}
	};
	
	public abstract String getLabel(Context context);
}
