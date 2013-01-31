package pl.marek.knx.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class TelegramTable {
	
	public static class TelegramColumns implements BaseColumns{
		public static final String TIME = "time";
		public static final String PRIORITY = "priority";
		public static final String SOURCE_ADDRESS = "source_address";
		public static final String DESTINATION_ADDRESS = "destination_address";
		public static final String HOPCOUNT = "hopcount";
		public static final String MESSAGE_CODE = "message_code";
		public static final String DPT_ID = "dpt_id";
		public static final String RAWDATA = "rawdata";
		public static final String DATA = "data";
		public static final String RAWFRAME = "rawframe";
		public static final String FRAME_LENGTH = "frame_length";
		public static final String TYPE = "type";
		public static final String ACK = "ack";
		public static final String CONFIRMATION = "confirmation";
		public static final String REPEATED = "repeated";		
	}
	
	public static final String TABLE_NAME = "telegrams";
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE "+TelegramTable.TABLE_NAME+" ("
												   + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
												   + TelegramColumns.TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
												   + TelegramColumns.PRIORITY + " TEXT, "
												   + TelegramColumns.SOURCE_ADDRESS + " TEXT, "
												   + TelegramColumns.DESTINATION_ADDRESS + " TEXT, "
												   + TelegramColumns.HOPCOUNT + " INTEGER, "
												   + TelegramColumns.MESSAGE_CODE + " TEXT, "
												   + TelegramColumns.DPT_ID + " TEXT, "
												   + TelegramColumns.RAWDATA + " TEXT, "
												   + TelegramColumns.DATA + " TEXT, "
												   + TelegramColumns.RAWFRAME + " TEXT, "
												   + TelegramColumns.FRAME_LENGTH + " INTEGER, "
												   + TelegramColumns.TYPE + " TEXT, "
												   + TelegramColumns.ACK + " INTEGER, "
												   + TelegramColumns.CONFIRMATION + " INTEGER, "
												   + TelegramColumns.REPEATED + " INTEGER);";
	public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "+ TelegramTable.TABLE_NAME;
	public static final String TRUNCATE_TABLE_QUERY = "TRUNCATE TABLE IF EXISTS "+ TelegramTable.TABLE_NAME;
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(TelegramTable.CREATE_TABLE_QUERY);
		
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL(TelegramTable.DROP_TABLE_QUERY);
		TelegramTable.onCreate(db);
	}
	
}
