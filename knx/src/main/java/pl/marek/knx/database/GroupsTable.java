package pl.marek.knx.database;

import android.database.sqlite.SQLiteDatabase;

public class GroupsTable {
	
	public static class GroupsColumns extends BasicBlockColumns{}

	public static final String TABLE_NAME = "groups";
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE "
			+ GroupsTable.TABLE_NAME + " (" + GroupsColumns.ADDRESS
			+ " TEXT PRIMARY KEY, " + GroupsColumns.NAME + " TEXT, "
			+ GroupsColumns.DESCRIPTION + " TEXT);";
	public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "
			+ GroupsTable.TABLE_NAME;
	public static final String TRUNCATE_TABLE_QUERY = "TRUNCATE TABLE IF EXISTS "
			+ GroupsTable.TABLE_NAME;

	public static void onCreate(SQLiteDatabase db) {
		db.execSQL(GroupsTable.CREATE_TABLE_QUERY);
	}

	public static void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		db.execSQL(GroupsTable.DROP_TABLE_QUERY);
		GroupsTable.onCreate(db);
	}
}