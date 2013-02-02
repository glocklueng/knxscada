package pl.marek.knx.preferences;

import pl.marek.knx.R;
import pl.marek.knx.log.LogTags;
import android.content.Context;
import android.util.Log;

public class KNXConnectionSwitchExecutor extends SwitchExecutor{
	
	public KNXConnectionSwitchExecutor(Context context) {
		super(context);
	}

	@Override
	public void start() {
		Log.d(LogTags.KNX_CONNECTION, "Starting KNX");
	}

	@Override
	public void stop() {
		Log.d(LogTags.KNX_CONNECTION, "Stopping KNX");
	}

	@Override
	public void updateSwitchState(boolean state) {
		application.setKnxConnected(state);
		setSwitchState(context.getString(R.string.connection_switcher_key), !state);
	}

	@Override
	public boolean getSwitchState() {
		return application.isKnxConnected();
	}

}
