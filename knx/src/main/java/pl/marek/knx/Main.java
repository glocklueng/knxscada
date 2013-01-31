package pl.marek.knx;

import pl.marek.knx.connection.ConnectionState;
import pl.marek.knx.interfaces.KNXConnectionStateListener;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.interfaces.KNXTelegramListener;
import pl.marek.knx.interfaces.StateSender;
import pl.marek.knx.interfaces.WebServerStateListener;
import pl.marek.knx.log.LogTags;
import pl.marek.knx.preferences.SettingsActivity;
import pl.marek.knx.receivers.KNXConnectionStateBroadcastReceiver;
import pl.marek.knx.receivers.TelegramBroadcastReceiver;
import pl.marek.knx.receivers.WebServerStateBroadcastReceiver;
import pl.marek.knx.telegram.Telegram;
import pl.marek.knx.web.WebServerState;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class Main extends Activity implements KNXTelegramListener, KNXConnectionStateListener, WebServerStateListener{
	
	
	public static final String CONNECTION_RECEIVER_REGISTERED = "pl.marek.knx.connection_receiver_registered";
	
	
	private TelegramBroadcastReceiver telegramReceiver;
	private KNXConnectionStateBroadcastReceiver connectionStateReceiver;
	private WebServerStateBroadcastReceiver webServerStateReceiver;
		
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application);
		
		telegramReceiver = new TelegramBroadcastReceiver(this);
		connectionStateReceiver = new KNXConnectionStateBroadcastReceiver(this);
		webServerStateReceiver = new WebServerStateBroadcastReceiver(this);
		
		registerReceiver(telegramReceiver, new IntentFilter(KNXTelegramListener.TELEGRAM_RECEIVED));
		registerReceiver(connectionStateReceiver, new IntentFilter(StateSender.CONNECTION_STATE_CHANGED));
		registerReceiver(webServerStateReceiver, new IntentFilter(StateSender.WEBSERVER_STATE_CHANGED));
		
		getActionBar().setHomeButtonEnabled(true);
//		Intent intent = getIntent();
//		if(intent != null){
//			if(intent.getBooleanExtra(Main.CONNECTION_RECEIVER_REGISTERED, false)){
//				//registerReceiver(connectionReceiver, connectionReceiverFilter);
//			}
//		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{    
	   switch (item.getItemId()) 
	   {        
	      case android.R.id.home:            
	         Intent intent = new Intent(this, Main.class);            
	         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	         startActivity(intent);
	         break;
	      case R.id.menu:
	    	  
	    	  startActivity(new Intent(this, SettingsActivity.class));
	    	  
	    	  break;
	      default:            
	         return super.onOptionsItemSelected(item);    
	   }
	   return true;
	}
	
	Dialog d;
	
	public void showDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title")
        	   .setMessage("123")
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	  d.cancel();
                   }
               });
        d = builder.create();
        d.show();
        
      
	}

	public void connect(View view){
        //registerReceiver(connectionReceiver, connectionReceiverFilter);
        startService(new Intent(this, KNXConnectionService.class));
	}
	
	public void disconnect(View view){
		//unregisterReceiver(connectionReceiver);
		stopService(new Intent(this, KNXConnectionService.class));
	}
	
	public void startWWW(View view){
		startService(new Intent(this, WebServerService.class));		
	}
	
	public void stopWWW(View view){
		stopService(new Intent(this, WebServerService.class));	
	}
	
	public void test(View view){
		Intent intent = new Intent(KNXConnectionService.WRITE_DATA);
		intent.putExtra(KNXDataTransceiver.GROUP_ADDRESS, "0/0/1");
		intent.putExtra(KNXDataTransceiver.DPT_IP, "1.001");
		intent.putExtra(KNXDataTransceiver.VALUE, "on");
		sendBroadcast(intent);
//		
//		Intent intent = new Intent(KNXConnectionService.GET_CONNECTION_STATE);
//		sendBroadcast(intent);
//		
//		Intent intent = new Intent(WebServerService.GET_WEBSERVER_STATE);
//		sendBroadcast(intent);
		
//		sendBroadcast(new Intent(WebServerController.STOP_WEBSERVER));
		
	}
	
	@Override
	public void onStop(){
		super.onStop();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		unregisterReceiver(telegramReceiver);
		unregisterReceiver(webServerStateReceiver);
		unregisterReceiver(connectionStateReceiver);
	}
	
	@Override
	public void telegramReceived(Telegram telegram) {
		Log.d(LogTags.TELEGRAM, telegram.toString());
	}

	@Override
	public void connectionStateChanged(ConnectionState state) {
		Log.i("CONNECTION STATE", state.name());
	}

	@Override
	public void webServerStateChanged(WebServerState state) {
		Log.i("WEBSERVER STATE", state.name());
		
	}
}