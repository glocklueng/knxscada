package pl.marek.knx.database.tables;

import pl.marek.knx.database.tables.ElementTable.ElementColumns;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class ElementGroupAddressTable {
	
	public static class ElementGroupAddressColumns implements BaseColumns{
		public static final String ELEMENT_ID = "element_id";
		public static final String ADDRESS = "address";
		public static final String TYPE = "type";
	}
	
	public static final String TABLE_NAME = "element_group_addresses";
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE "
													+ ElementGroupAddressTable.TABLE_NAME + " (" 
													+ ElementGroupAddressColumns.ELEMENT_ID + " INTEGER, "
													+ ElementGroupAddressColumns.ADDRESS + " TEXT, "
													+ ElementGroupAddressColumns.TYPE + " TEXT, "  
												    + "FOREIGN KEY("+ElementGroupAddressColumns.ELEMENT_ID+") REFERENCES "
												    + ElementTable.TABLE_NAME + "("+ElementColumns._ID+"));";
	public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + ElementGroupAddressTable.TABLE_NAME;
	public static final String TRUNCATE_TABLE_QUERY = "TRUNCATE TABLE IF EXISTS " + ElementGroupAddressTable.TABLE_NAME;
	
	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(ElementGroupAddressTable.CREATE_TABLE_QUERY);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(ElementGroupAddressTable.DROP_TABLE_QUERY);
		ElementGroupAddressTable.onCreate(db);
	}
}
