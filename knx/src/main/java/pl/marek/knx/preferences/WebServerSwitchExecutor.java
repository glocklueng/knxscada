package pl.marek.knx.preferences;

import pl.marek.knx.R;
import pl.marek.knx.log.LogTags;
import android.content.Context;
import android.util.Log;

public class WebServerSwitchExecutor extends SwitchExecutor{

	public WebServerSwitchExecutor(Context context) {
		super(context);
	}

	@Override
	public void start() {
		Log.d(LogTags.WEB_SERVER, "Starting WWW");
	}

	@Override
	public void stop() {
		Log.d(LogTags.WEB_SERVER, "Stopping WWW");
			
	}

	@Override
	public void updateSwitchState(boolean state) {
		application.setWebServerStarted(state);
		setSwitchState(context.getString(R.string.webserver_switcher_key), !state);
	}

	@Override
	public boolean getSwitchState() {
		return application.isWebServerStarted();
	}

}
