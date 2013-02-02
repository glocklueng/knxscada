package pl.marek.knx;

import pl.marek.knx.connection.ConnectionState;
import pl.marek.knx.connection.KNXConnectionSettings;
import pl.marek.knx.connection.KNXIPConnection;
import pl.marek.knx.connection.KNXLinkListener;
import pl.marek.knx.connection.KNXProcessListener;
import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.interfaces.KNXTelegramListener;
import pl.marek.knx.interfaces.StateSender;
import pl.marek.knx.log.LogTags;
import pl.marek.knx.receivers.KNXDataBroadcastReceiver;
import pl.marek.knx.receivers.StateRequestReceiver;
import pl.marek.knx.telegram.Telegram;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.datapoint.StateDP;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.exception.KNXFormatException;
import tuwien.auto.calimero.process.ProcessCommunicator;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class KNXConnectionService extends Service implements KNXTelegramListener, KNXDataTransceiver, StateSender{

	public static final int SERVICE_ID = 10000;
	
	private KNXIPConnection connection = null;
	private ConnectionState state;
	private NotificationManager notificationManager;
	private DatabaseManager dbManager;
	private StateRequestReceiver stateRequestReceiver;
	private KNXDataBroadcastReceiver dataReceiver;
	private IntentFilter dataReceiverFilter;
	private KNXLinkListener linkListener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		dbManager = new DatabaseManagerImpl(this);
		if (connectToKNX()) {
			setState(ConnectionState.CONNECTED);
			Notification notification = notificationManager.createConnectionServiceNotification();
			startForeground(SERVICE_ID, notification);
			registerReceiver(dataReceiver, dataReceiverFilter);
			addConnectionListeners();
		} else{
			setState(ConnectionState.FAILED);
			stopSelf();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private boolean connectToKNX() {
		setState(ConnectionState.CONNECTING);
		boolean isConnected = false;
		KNXConnectionSettings settings = new KNXConnectionSettings(this);
		connection = new KNXIPConnection(settings);
		try {
			isConnected = connection.connect();
		} catch (Exception e) {
			notificationManager.showExceptionNotification(e);
			connection = null;
		}
		return isConnected;
	}
	
	private void addConnectionListeners(){
		linkListener = new KNXLinkListener(this, dbManager);
		connection.addLinkListener(linkListener);
		connection.addProcessListener(new KNXProcessListener());
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		notificationManager = new NotificationManager(this);
		dataReceiver = new KNXDataBroadcastReceiver(this);
		dataReceiverFilter = new IntentFilter();
		dataReceiverFilter.addAction(READ_DATA);
		dataReceiverFilter.addAction(WRITE_DATA);
		
		stateRequestReceiver = new StateRequestReceiver(this);
		registerReceiver(stateRequestReceiver, new IntentFilter(GET_CONNECTION_STATE));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopForeground(true);
		if(connection != null){
			connection.disconnect();
			unregisterReceiver(dataReceiver);
		}
		if(dbManager != null){
			dbManager.close();
		}
		setState(ConnectionState.DISCONNECTED);
		unregisterReceiver(stateRequestReceiver);
	}
	
	private void setState(ConnectionState state){		
		this.state = state;
		state.showToast(this);
		sendState();
	}
	
	@Override
	public void sendState(){
		Intent intent = new Intent(StateSender.CONNECTION_STATE_CHANGED);
		intent.putExtra(CONNECTION_STATE, state);
		sendBroadcast(intent);	
	}

	@Override
	public void telegramReceived(Telegram telegram) {
		addToDatabase(telegram);
		Intent intent = new Intent(KNXTelegramListener.TELEGRAM_RECEIVED);
		intent.putExtra(Telegram.TELEGRAM, telegram);
		sendBroadcast(intent);
	}
	
	private void addToDatabase(Telegram telegram){
		dbManager.addTelegram(telegram);
	}

	@Override
	public void readData(String groupAddress, String dptId) {
		Datapoint dp = createDatapoint(groupAddress, dptId, "READ");
		
		if (dp != null){
            linkListener.setTmpDatapoint(dp);
			linkListener.setWaitForReadResponseFlag();
			
			ProcessCommunicator communicator = connection.getProcessCommunicator();
			try {
				communicator.read(dp);
			} catch (KNXException e) {
				linkListener.clearWaitForReadResponseFlag();
				Log.e(LogTags.KNX_CONNECTION, e.getMessage());
			}
		}
	}

	@Override
	public void writeData(String groupAddress, String dptId, String value) {
		Datapoint dp = createDatapoint(groupAddress, dptId, "WRITE");
		
		if (dp != null){
			linkListener.setTmpDatapoint(dp);
			linkListener.setWaitForWriteResponseFlag();
			ProcessCommunicator communicator = connection.getProcessCommunicator();
			try {
				communicator.read(dp);
			} catch (KNXException e) {
				linkListener.clearWaitForWriteResponseFlag();
				Log.e(LogTags.KNX_CONNECTION, e.getMessage());
			}
		}
	}
	
	private Datapoint createDatapoint(String groupAddress, String dptId, String name){
        Datapoint dp = null;
        try {
        	dp = new StateDP(new GroupAddress(groupAddress),name,0,dptId);
        }
        catch (KNXFormatException ex) {
        	Log.e(LogTags.KNX_CONNECTION, ex.getMessage());
        }
        return dp;
	}
}