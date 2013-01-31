package pl.marek.knx.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.telegram.Telegram;

public class DatabaseManagerImpl implements DatabaseManager{
	
	private Context context;
	private SQLiteDatabase db;
	private SQLiteOpenHelper openHelper;
	
	private DatapointsDao datapointsDao;
	private DevicesDao devicesDao;
	private GroupsDao groupsDao;
	private DPTDao dptDao;
	private TelegramDao telegramDao;
	
	public DatabaseManagerImpl(Context context){
		this.context = context;
		
		open();
		
		datapointsDao = new DatapointsDao(db);
		devicesDao = new DevicesDao(db);
		groupsDao = new GroupsDao(db);
		dptDao = new DPTDao(db);
		telegramDao = new TelegramDao(db);
	}
	
	@Override
	public void open() {
		openHelper = new DatabaseOpenHelper(context);
		db = openHelper.getWritableDatabase();
	}

	@Override
	public void close() {
		openHelper.close();
	}
	
	@Override
	public void addTelegram(Telegram telegram) {
		db.beginTransaction();
		try{
			telegramDao.save(telegram);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}


	@Override
	public void addGroup(Group group){
		db.beginTransaction();
		try{
			groupsDao.save(group);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}


	@Override
	public void addDevice(Device device){
		db.beginTransaction();
		try {
			devicesDao.save(device);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}


	@Override
	public void addDPT(DPTEntity dpt) {
		db.beginTransaction();
		try {
			dptDao.save(dpt);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}


	@Override
	public void addDatapoint(DatapointEntity datapoint) {
		db.beginTransaction();
		try {
			datapointsDao.save(datapoint);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}
	
	@Override
	public DatapointEntity getDatapointEntityByAddress(String groupAddress) {
		return datapointsDao.get(groupAddress);
	}

	@Override
	public Group getGroupByAddress(String address) {
		return groupsDao.get(address);
	}

	@Override
	public Device getDeviceByAddress(String address) {
		return devicesDao.get(address);
	}
}
