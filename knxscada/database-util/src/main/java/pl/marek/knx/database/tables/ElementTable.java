package pl.marek.knx.database.tables;

import pl.marek.knx.database.tables.LayerTable.LayerColumns;
import pl.marek.knx.database.tables.ProjectTable.ProjectColumns;
import pl.marek.knx.database.tables.SubLayerTable.SubLayerColumns;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class ElementTable {
	
	public static class ElementColumns implements BaseColumns{
		public static final String PROJECT_ID = "project_id";
		public static final String LAYER_ID = "layer_id";
		public static final String SUBLAYER_ID = "sublayer_id";
		public static final String X = "x";
		public static final String Y = "y";
		public static final String NAME = "name";
		public static final String DESCRIPTION = "description";
		public static final String DEVICE_ADDRESS = "device_address";
		public static final String TYPE = "type";
	}
	
	public static final String TABLE_NAME = "elements";
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE "+ElementTable.TABLE_NAME+" ("
												   + ElementColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
												   + ElementColumns.PROJECT_ID + " INTEGER, "
												   + ElementColumns.LAYER_ID + " INTEGER, "
												   + ElementColumns.SUBLAYER_ID + " INTEGER, "
												   + ElementColumns.X + " INTEGER, "
												   + ElementColumns.Y + " INTEGER, "
												   + ElementColumns.NAME + " TEXT, "
												   + ElementColumns.DESCRIPTION + " TEXT, "
												   + ElementColumns.DEVICE_ADDRESS + " TEXT, " 
												   + ElementColumns.TYPE + " TEXT, " 
												   + "FOREIGN KEY("+ElementColumns.PROJECT_ID+") REFERENCES "
												   + ProjectTable.TABLE_NAME + "("+ProjectColumns._ID+"), "
												   + "FOREIGN KEY("+ElementColumns.LAYER_ID+") REFERENCES " 
												   + LayerTable.TABLE_NAME + "("+LayerColumns._ID+"), "
												   + "FOREIGN KEY("+ElementColumns.SUBLAYER_ID+") REFERENCES "
												   + SubLayerTable.TABLE_NAME + "("+SubLayerColumns._ID+"));";
	
	public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + ElementTable.TABLE_NAME;
	public static final String TRUNCATE_TABLE_QUERY = "TRUNCATE TABLE IF EXISTS " + ElementTable.TABLE_NAME;
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(ElementTable.CREATE_TABLE_QUERY);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL(ElementTable.DROP_TABLE_QUERY);
		ElementTable.onCreate(db);
	}

}
