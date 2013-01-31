package pl.marek.knx.receivers;

import pl.marek.knx.KNXConnectionService;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KNXDataBroadcastReceiver extends BroadcastReceiver{
	
	KNXDataTransceiver transceiver;
	
	public KNXDataBroadcastReceiver(KNXDataTransceiver transceiver){
		this.transceiver = transceiver;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(KNXConnectionService.READ_DATA)){
			String groupAddress = intent.getStringExtra(KNXDataTransceiver.GROUP_ADDRESS);
			String dptId = intent.getStringExtra(KNXDataTransceiver.DPT_IP);
			transceiver.readData(groupAddress,dptId);
		}else if(intent.getAction().equals(KNXConnectionService.WRITE_DATA)){
			String groupAddress = intent.getStringExtra(KNXDataTransceiver.GROUP_ADDRESS);
			String dptId = intent.getStringExtra(KNXDataTransceiver.DPT_IP);
			String value = intent.getStringExtra(KNXDataTransceiver.VALUE);
			transceiver.writeData(groupAddress,dptId,value);
		}
	}
}
