package pl.marek.knx.database.tables;

import pl.marek.knx.database.tables.ProjectTable.ProjectColumns;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class LayerTable {
	
	public static class LayerColumns implements BaseColumns{
		public static final String PROJECT_ID = "project_id";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String ICON = "icon";
	}
	
	public static final String TABLE_NAME = "layers";
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE "+LayerTable.TABLE_NAME+" ("
												   + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
												   + LayerColumns.PROJECT_ID + " INTEGER, "
												   + LayerColumns.NAME + " TEXT, "
												   + LayerColumns.DESCRIPTION + " TEXT, "
												   + LayerColumns.ICON + " TEXT, " 
												   + "FOREIGN KEY("+LayerColumns.PROJECT_ID+") REFERENCES "
												   + ProjectTable.TABLE_NAME + "("+ProjectColumns._ID+"));";
	
	public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + LayerTable.TABLE_NAME;
	public static final String TRUNCATE_TABLE_QUERY = "TRUNCATE TABLE IF EXISTS " + LayerTable.TABLE_NAME;
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(LayerTable.CREATE_TABLE_QUERY);
		
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL(LayerTable.DROP_TABLE_QUERY);
		LayerTable.onCreate(db);
	}

}
