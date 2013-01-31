package pl.marek.knx.connection;

import android.util.Log;
import pl.marek.knx.log.LogTags;
import pl.marek.knx.utils.DataRepresentation;
import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListener;

public class KNXProcessListener implements ProcessListener {

	@Override
	public void groupWrite(ProcessEvent e) {
		String output = String.format("Source: %s\nDestination: %s\nData: %s\n", e.getSourceAddr(),e.getDestination(),DataRepresentation.byteArrayToHexString(e.getASDU()));
		Log.d(LogTags.KNX_CONNECTION,output);
	}

	@Override
	public void detached(DetachEvent e) {
		Log.d(LogTags.KNX_CONNECTION, e.toString());
	}
}
