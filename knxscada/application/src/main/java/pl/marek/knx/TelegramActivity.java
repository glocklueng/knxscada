package pl.marek.knx;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import pl.marek.knx.ReadWriteDialog.ReadWriteListener;
import pl.marek.knx.TelegramFilters.TelegramFiltersListener;
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

public class TelegramActivity extends ListActivity implements KNXTelegramListener, ReadWriteListener, TelegramFiltersListener{
	
	public static int NUMBER_OF_SHOW_TELEGRAMS = 200;
	
	private TelegramBroadcastReceiver telegramReceiver;
	private TelegramAdapter telegramAdapter;
	private DatabaseManager dbManager;
	private LinkedList<Telegram> telegrams;
	
	private ReadWriteDialog rwDialog;
	private TelegramFilters filters;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.telegram_list);
		filters = (TelegramFilters)findViewById(R.id.telegram_filters_view);
		filters.setListener(this);
		telegramReceiver = new TelegramBroadcastReceiver(this);
		registerReceiver(telegramReceiver, new IntentFilter(KNXTelegramListener.TELEGRAM_RECEIVED));
		
		dbManager = new DatabaseManagerImpl(this);
		
		telegrams = new LinkedList<Telegram>(dbManager.getRecentTelegrams(NUMBER_OF_SHOW_TELEGRAMS));
		
		telegramAdapter = new TelegramAdapter(this,telegrams);
		setListAdapter(telegramAdapter);
		setFiltersDates();
	}
	
	private void setFiltersDates(){
		Telegram telegram = telegrams.getLast();
		if(telegram != null){
			filters.setFromDate(telegram.getTime());
		}else{
			filters.setFromDate(new Date(0));
		}
		filters.setToDate(Calendar.getInstance().getTime());
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
		if(dbManager != null && dbManager.isOpen()){
			dbManager.close();
		}
		if(rwDialog != null && rwDialog.isShowing()){
			rwDialog.dismiss();
		}
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
	    	  
	    	  rwDialog = new ReadWriteDialog(this);
	    	  rwDialog.setListener(this);
	    	  rwDialog.show();
	    	  	    	  
	    	  break;
	      case R.id.telegram_menu_filter_item:
	    	  
	    	  filters.toggle();
	    	  
	    	  break;
	      default:            
	         return super.onOptionsItemSelected(item);    
	   }
	   return true;
	}
	
	public void onTelegramFiltersApply(String source, String destination,
			Date from, Date to, Map<String, Boolean> priorities,
			Map<String, Boolean> types) {
		
		telegrams.clear();
		
		String priority = "";
		for(Entry<String, Boolean> e: priorities.entrySet()){
			String key = e.getKey();
			boolean value = e.getValue();
			
			if(value){
				if(!priority.isEmpty()){
					priority = priority + ",";
				}
				priority = priority + key;
			}
		}
		if(priority.isEmpty()){
			priority = "empty";
		}
		
		String type = "";
		for(Entry<String, Boolean> e: types.entrySet()){
			String key = e.getKey();
			boolean value = e.getValue();
			if(value){
				if(!type.isEmpty()){
					type = type+",";
				}
				type = type + key;
			}
		}
		if(type.isEmpty()){
			type = "empty";
		}
		
		LinkedList<Telegram> t = new LinkedList<Telegram>(dbManager.getTelegrams(source, destination, priority, type, from, to, null));
		for(Telegram telegram:t){
			telegrams.add(telegram);
		}
		updateTelegramList();
	}
	
	private void updateTelegramList(){
		telegramAdapter.notifyDataSetChanged();
	}
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Telegram telegram = telegramAdapter.getItem(position);
		Intent intent = new Intent(this, TelegramDetailActivity.class);
		intent.putExtra(Telegram.TELEGRAM, telegram);
		startActivity(intent);
	}

	public void telegramReceived(Telegram telegram) {
		if(telegrams.size() == NUMBER_OF_SHOW_TELEGRAMS){
			telegrams.removeLast();
		}
		telegrams.addFirst(telegram);
		updateTelegramList();
	}

	public void read(String address, DPT dpt) {
		transferData(KNXDataTransceiver.READ_DATA, address, dpt, null);
	}

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
