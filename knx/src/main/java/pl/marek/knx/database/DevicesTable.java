package pl.marek.knx.database;

import android.database.sqlite.SQLiteDatabase;

public class DevicesTable {
	
	public static class DevicesColumns extends BasicBlockColumns{}
	
	public static final String TABLE_NAME = "devices";
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE "+DevicesTable.TABLE_NAME+" ("
												   + DevicesColumns.ADDRESS + " TEXT PRIMARY KEY, "
												   + DevicesColumns.NAME + " TEXT, "
												   + DevicesColumns.DESCRIPTION + " TEXT);";
	public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "+ DevicesTable.TABLE_NAME;
	public static final String TRUNCATE_TABLE_QUERY = "TRUNCATE TABLE IF EXISTS "+ DevicesTable.TABLE_NAME;
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(DevicesTable.CREATE_TABLE_QUERY);
		
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL(DevicesTable.DROP_TABLE_QUERY);
		DevicesTable.onCreate(db);
	}
}
