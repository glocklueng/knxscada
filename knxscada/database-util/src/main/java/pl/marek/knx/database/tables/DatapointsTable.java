package pl.marek.knx.database.tables;

import pl.marek.knx.database.tables.ElementTable.ElementColumns;
import pl.marek.knx.database.tables.ProjectTable.ProjectColumns;
import android.database.sqlite.SQLiteDatabase;

public class DatapointsTable {
	
	public static class DatapointsColumns{
		public static final String GROUP_ADDRESS = "group_address";
		public static final String DPT_ID = "dpt_id";
		public static final String STATE = "state";
		public static final String MODIFY_DATE = "modify_date";
		public static final String PROJECT_ID = "project_id";
		public static final String ELEMENT_ID = "element_id";
	}
	
	public static final String TABLE_NAME = "datapoints";
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE "+DatapointsTable.TABLE_NAME+" ("
												   + DatapointsColumns.GROUP_ADDRESS + " TEXT, "
												   + DatapointsColumns.PROJECT_ID + " INTEGER, "
												   + DatapointsColumns.ELEMENT_ID + " INTEGER, "
												   + DatapointsColumns.DPT_ID + " TEXT, "
												   + DatapointsColumns.STATE + " TEXT, "
												   + DatapointsColumns.MODIFY_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " 
												   + "PRIMARY KEY("+DatapointsColumns.GROUP_ADDRESS+","+DatapointsColumns.PROJECT_ID+","+DatapointsColumns.ELEMENT_ID+"), " 
												   + "FOREIGN KEY("+DatapointsColumns.PROJECT_ID+") REFERENCES "
												   + ProjectTable.TABLE_NAME + "("+ProjectColumns._ID+"), "
												   + "FOREIGN KEY("+DatapointsColumns.ELEMENT_ID+") REFERENCES "
												   + ElementTable.TABLE_NAME + "("+ElementColumns._ID+"));";
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
