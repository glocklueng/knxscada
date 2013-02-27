package pl.marek.knx;

import java.util.LinkedList;

import pl.marek.knx.ReadWriteDialog.ReadWriteListener;
import pl.marek.knx.database.DatabaseManagerImpl;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.interfaces.KNXDataTransceiver;
import pl.marek.knx.interfaces.KNXTelegramListener;
import pl.marek.knx.receivers.TelegramBroadcastReceiver;
import pl.marek.knx.telegram.Telegram;
import tuwien.auto.calimero.dptxlator.DPT;
import android.app.ListActivity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class TelegramActivity extends ListActivity implements KNXTelegramListener, ReadWriteListener{
	
	public static int NUMBER_OF_SHOW_TELEGRAMS = 1000;
	
	private TelegramBroadcastReceiver telegramReceiver;
	private TelegramAdapter telegramAdapter;
	private DatabaseManager dbManager;
	private LinkedList<Telegram> telegrams;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telegram_list);
		telegramReceiver = new TelegramBroadcastReceiver(this);
		registerReceiver(telegramReceiver, new IntentFilter(KNXTelegramListener.TELEGRAM_RECEIVED));
		
		dbManager = new DatabaseManagerImpl(this);
		telegrams = new LinkedList<Telegram>(dbManager.getRecentTelegrams(NUMBER_OF_SHOW_TELEGRAMS));
		
		telegramAdapter = new TelegramAdapter(this,telegrams);
		setListAdapter(telegramAdapter);
//		getListView().setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getActionBar().setTitle(getString(R.string.telegrams_activity_title));
		getActionBar().setHomeButtonEnabled(true);
		if(dbManager != null && !dbManager.isOpen())
			dbManager.open();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(dbManager != null && dbManager.isOpen())
			dbManager.close();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(telegramReceiver);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.telegram_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{    
	   switch (item.getItemId()) 
	   {        
	      case android.R.id.home:            
	         Intent intent = new Intent(this, TelegramActivity.class);            
	         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
	         startActivity(intent);
	         break;
	      case R.id.telegram_menu_read_write_item:
	    	  
	    	  ReadWriteDialog dialog = new ReadWriteDialog(this);
	    	  dialog.setListener(this);
	    	  dialog.show();
	    	  	    	  
	    	  break;
	      default:            
	         return super.onOptionsItemSelected(item);    
	   }
	   return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Telegram telegram = telegramAdapter.getItem(position);
		Intent intent = new Intent(this, TelegramDetailActivity.class);
		intent.putExtra(Telegram.TELEGRAM, telegram);
		startActivity(intent);
	}

	@Override
	public void telegramReceived(Telegram telegram) {
		if(telegrams.size() == NUMBER_OF_SHOW_TELEGRAMS){
			telegrams.removeLast();
		}
		telegrams.addFirst(telegram);
		telegramAdapter.notifyDataSetChanged();
		
	}

	@Override
	public void read(String address, DPT dpt) {
		transferData(KNXDataTransceiver.READ_DATA, address, dpt, null);
	}

	@Override
	public void write(String address, DPT dpt, String value) {
		transferData(KNXDataTransceiver.WRITE_DATA, address, dpt, value);
	}
	
	private void transferData(String type,String address, DPT dpt, String value){
  		Intent dataIntent = new Intent(type);
  		dataIntent.putExtra(KNXDataTransceiver.GROUP_ADDRESS, address);
  		dataIntent.putExtra(KNXDataTransceiver.DPT_ID, dpt.getID());
  		if(value != null)
  			dataIntent.putExtra(KNXDataTransceiver.VALUE, value);
  		sendBroadcast(dataIntent);
	}
}
