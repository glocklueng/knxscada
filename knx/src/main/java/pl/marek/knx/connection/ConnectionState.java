package pl.marek.knx.connection;

import pl.marek.knx.CustomToast;
import pl.marek.knx.R;
import android.content.Context;

public enum ConnectionState {
	CONNECTING {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.connection_service_connecting));
		}
	},
	CONNECTED {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.connection_service_connected));
		}
	},
	DISCONNECTING {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.connection_service_disconnecting));
		}
	},	
	DISCONNECTED {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.connection_service_disconnected));
		}
	},
	FAILED {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.connection_service_failed));
		}
	},
	UNKNOWN {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.connection_service_unknown));
		}
	};
	
	public abstract void showToast(Context context);
}