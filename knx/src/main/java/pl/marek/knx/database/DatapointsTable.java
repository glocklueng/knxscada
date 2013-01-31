package pl.marek.knx.database;

import android.database.sqlite.SQLiteDatabase;

public class DatapointsTable {
	
	public static class DatapointsColumns{
		public static final String GROUP_ADDRESS = "group_address";
		public static final String DPT_ID = "dpt_id";
		public static final String STATE = "state";
		public static final String MODIFY_DATE = "modify_date";
	}
	
	public static final String TABLE_NAME = "datapoints";
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE "+DatapointsTable.TABLE_NAME+" ("
												   + DatapointsColumns.GROUP_ADDRESS + " TEXT PRIMARY KEY, "
												   + DatapointsColumns.DPT_ID + " TEXT, "
												   + DatapointsColumns.STATE + " TEXT, "
												   + DatapointsColumns.MODIFY_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
	public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "+ DatapointsTable.TABLE_NAME;
	public static final String TRUNCATE_TABLE_QUERY = "TRUNCATE TABLE IF EXISTS "+ DatapointsTable.TABLE_NAME;
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(DatapointsTable.CREATE_TABLE_QUERY);
		
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL(DatapointsTable.DROP_TABLE_QUERY);
		DatapointsTable.onCreate(db);
	}
}
