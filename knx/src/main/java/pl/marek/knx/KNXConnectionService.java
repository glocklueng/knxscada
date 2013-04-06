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
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
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
	private static Handler messageHandler;
	private PowerManager.WakeLock wakeLock;
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		dbManager = new DatabaseManagerImpl(this);
		new ConnectTask().execute();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void setWakeLock(){
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getString(R.string.application_name));
        wakeLock.acquire();
	}
	
	private void clearWakeLock(){
		wakeLock.release();
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
		
		messageHandler = new MessageHandler(this);
		setWakeLock();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopForeground(true);
		if(dbManager != null){
			dbManager.close();
		}
		unregisterReceiver(stateRequestReceiver);
		setState(ConnectionState.DISCONNECTING);
		clearWakeLock();
		setDisconnectedStateInPreferences();
	}
	
	private void setDisconnectedStateInPreferences(){
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = p.edit();
		editor.putString(getString(R.string.connection_state_key), ConnectionState.DISCONNECTED.name());
		editor.commit();
	}
	
	private void setState(ConnectionState state){		
		this.state = state;
		state.showToast(this);
		sendState();
		doActionByState();
	}
	
	private void doActionByState(){
		switch(state){
			case CONNECTED:
				Notification notification = notificationManager.createConnectionServiceNotification();
				startForeground(SERVICE_ID, notification);
				registerReceiver(dataReceiver, dataReceiverFilter);
				addConnectionListeners();
				break;
			case DISCONNECTING:
				if(connection != null && connection.isConnected()){
					connection.disconnect();
					unregisterReceiver(dataReceiver);
					connection = null;
				}
				setState(ConnectionState.DISCONNECTED);
				break;
			case FAILED:
				stopSelf();
				break;
		default:
			break;
		}
	}
	
	private void addConnectionListeners(){
		linkListener = new KNXLinkListener(this, dbManager);
		connection.addLinkListener(linkListener);
		connection.addProcessListener(new KNXProcessListener());
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
		long rowId = dbManager.addTelegram(telegram);
		telegram.setId(rowId);
	}

	@Override
	public void readData(String groupAddress, String dptId) {
		Datapoint dp = createDatapoint(groupAddress, dptId, "READ");
		Thread read = new ReadThread(dp);
		read.start();
	}

	@Override
	public void writeData(String groupAddress, String dptId, final String value) {
		Datapoint dp = createDatapoint(groupAddress, dptId, "WRITE");
		Thread write = new WriteThread(dp, value);
		write.start();
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
	
	private static class MessageHandler extends Handler{
		
		private Context context;
		
		public MessageHandler(Context context){
			this.context = context;
		}
		
		@Override
		public void handleMessage(Message msg) {
			CustomToast.showShortToast(context, msg.getData().getString("message"));
		}
	}
	
	private class ConnectTask extends AsyncTask<Void, ConnectionState, Boolean>{
		
		private KNXConnectionSettings settings;
		private boolean isConnected;
		
		@Override
		protected void onPreExecute() {
			settings = new KNXConnectionSettings(getApplicationContext());
			isConnected = false;
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			
			publishProgress(ConnectionState.CONNECTING);
			connection = new KNXIPConnection(settings);
			try {
				isConnected = connection.connect();
				publishProgress(ConnectionState.CONNECTED);
			} catch (Exception e) {
				Log.w(LogTags.KNX_CONNECTION, e.getMessage());
				connection = null;
				publishProgress(ConnectionState.FAILED);
			}
			return isConnected;
		}
		
		@Override
		protected void onProgressUpdate(ConnectionState... values) {
			setState(values[0]);
		}
	}
	
	private class ReadThread extends Thread{
		
		private Datapoint dp;

		public ReadThread(Datapoint dp){
			this.dp = dp;
		}
		
		@Override
		public void run() {
			if (dp != null){
	            linkListener.setTmpDatapoint(dp);
				linkListener.setWaitForReadResponseFlag();
				
				ProcessCommunicator communicator = connection.getProcessCommunicator();
				try {
					communicator.read(dp);
				} catch (KNXException e) {
					linkListener.clearWaitForReadResponseFlag();
					Log.e(LogTags.KNX_CONNECTION, e.getMessage());
					
					Bundle bundle = new Bundle();
					bundle.putString("message", String.format(getApplicationContext().getString(R.string.telegram_response_timeout), dp.getMainAddress().toString()));
					Message msg = new Message();
					msg.setData(bundle);
					messageHandler.sendMessage(msg);
				}
			}
		}
	}
	
	private class WriteThread extends Thread{
		
		private Datapoint dp;
		private String value;
		
		public WriteThread(Datapoint dp, String value){
			this.dp = dp;
			this.value = value;
		}
		
		@Override
		public void run() {
			if (dp != null){
				linkListener.setTmpDatapoint(dp);
				linkListener.setWaitForWriteResponseFlag();
				ProcessCommunicator communicator = connection.getProcessCommunicator();
				try {
					communicator.write(dp, value);
				} catch (KNXException e) {
					linkListener.clearWaitForWriteResponseFlag();
					Log.e(LogTags.KNX_CONNECTION, e.getMessage());
				}
			}
		}
	}
}