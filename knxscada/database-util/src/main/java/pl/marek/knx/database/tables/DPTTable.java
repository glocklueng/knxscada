package pl.marek.knx.database.tables;

import android.database.sqlite.SQLiteDatabase;

public class DPTTable {
	public static class DPTColumns {
		public static final String DPT_ID = "dpt_id";
		public static final String DATA_GROUP = "data_group";
		public static final String NAME = "name";
		public static final String MIN_VALUE = "min_value";
		public static final String MAX_VALUE = "max_value";
		public static final String UNIT = "unit";
	}

	public static final String TABLE_NAME = "dpt";
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE "
			+ DPTTable.TABLE_NAME + " ("
			+ DPTColumns.DPT_ID + " TEXT PRIMARY KEY, "
			+ DPTColumns.DATA_GROUP + " TEXT, " 
			+ DPTColumns.NAME + " TEXT, " 
			+ DPTColumns.MIN_VALUE + " TEXT, " 
			+ DPTColumns.MAX_VALUE + " TEXT, " 
			+ DPTColumns.UNIT + " TEXT);";
	public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + DPTTable.TABLE_NAME;
	public static final String TRUNCATE_TABLE_QUERY = "TRUNCATE TABLE IF EXISTS " + DPTTable.TABLE_NAME;

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(DPTTable.CREATE_TABLE_QUERY);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		db.execSQL(DPTTable.DROP_TABLE_QUERY);
		DPTTable.onCreate(db);
	}
}
