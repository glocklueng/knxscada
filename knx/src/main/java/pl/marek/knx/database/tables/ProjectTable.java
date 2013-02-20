package pl.marek.knx.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class ProjectTable {
	
	public static class ProjectColumns implements BaseColumns{
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String AUTHOR = "author";
		public static final String CREATE_DATE = "create_date";
		public static final String EDIT_DATE = "edit_date";
		
	}
	public static final String TABLE_NAME = "projects";
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE "+ProjectTable.TABLE_NAME+" ("
												   + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
												   + ProjectColumns.NAME + " TEXT, "
												   + ProjectColumns.DESCRIPTION + " TEXT, "
												   + ProjectColumns.AUTHOR + " TEXT, "
												   + ProjectColumns.CREATE_DATE + " TIMESTAMP DEFAULT (datetime('now','localtime')), "
												   + ProjectColumns.EDIT_DATE + " TIMESTAMP DEFAULT (datetime('now','localtime')));";
												 
	
	public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS "+ ProjectTable.TABLE_NAME;
	public static final String TRUNCATE_TABLE_QUERY = "TRUNCATE TABLE IF EXISTS "+ ProjectTable.TABLE_NAME;
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(ProjectTable.CREATE_TABLE_QUERY);
		
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL(ProjectTable.DROP_TABLE_QUERY);
		ProjectTable.onCreate(db);
	}
}
