package pl.marek.knx.web;

import pl.marek.knx.CustomToast;
import pl.marek.knx.R;
import android.content.Context;

public enum WebServerState {
	STARTING {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.webserver_service_starting));
		}
	},
	STARTED {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.webserver_service_started));
		}
	},
	STOPPING {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.webserver_service_stopping));
		}
	},	
	STOPPED {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.webserver_service_stopped));
		}
	},
	FAILED {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.webserver_service_failed));
		}
	},
	UNKNOWN {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.webserver_service_unknown));
		}
	},
	DEPLOYING {
		@Override
		public void showToast(Context context) {
			CustomToast.showShortToast(context, context.getString(R.string.webserver_service_deploy));
		}
		
	};
	
	public abstract void showToast(Context context);
}
