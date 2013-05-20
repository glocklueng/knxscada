package pl.marek.knx.controls;

import pl.marek.knx.R;
import android.content.Context;

public enum ElementGroupAddressType{
	MAIN {
		@Override
		public String getLabel(Context context) {
			return context.getString(R.string.group_address_default_label);
		}
	},
	STATUS {
		@Override
		public String getLabel(Context context) {
			return context.getString(R.string.group_address_status_label);
		}
	},
	OTHER {
		@Override
		public String getLabel(Context context) {
			return context.getString(R.string.group_address_other_label);
		}
	};
	
	public abstract String getLabel(Context context);
}
