package pl.marek.knx.database.tables;

import pl.marek.knx.database.tables.LayerTable.LayerColumns;
import pl.marek.knx.database.tables.ProjectTable.ProjectColumns;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class SubLayerTable {
	
	public static class SubLayerColumns extends LayerColumns{
		public static final String LAYER_ID = "layer_id";
		public static final String BACKGROUND_IMAGE = "background_image";
	}
	
	public static final String TABLE_NAME = "sublayers";
	public static final String CREATE_TABLE_QUERY = "CREATE TABLE "+SubLayerTable.TABLE_NAME+" ("
												   + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
												   + SubLayerColumns.PROJECT_ID + " INTEGER, "
												   + SubLayerColumns.LAYER_ID + " INTEGER, "
												   + SubLayerColumns.NAME + " TEXT, "
												   + SubLayerColumns.DESCRIPTION + " TEXT, "
												   + SubLayerColumns.ICON + " TEXT, " 
												   + SubLayerColumns.BACKGROUND_IMAGE + " TEXT, " 
												   + "FOREIGN KEY("+SubLayerColumns.PROJECT_ID+") REFERENCES "
												   + ProjectTable.TABLE_NAME + "("+ProjectColumns._ID+"), " 
												   + "FOREIGN KEY("+SubLayerColumns.LAYER_ID+") REFERENCES " 
												   + LayerTable.TABLE_NAME + "("+LayerColumns._ID+"));";
	
	public static final String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + SubLayerTable.TABLE_NAME;
	public static final String TRUNCATE_TABLE_QUERY = "TRUNCATE TABLE IF EXISTS " + SubLayerTable.TABLE_NAME;
	
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(SubLayerTable.CREATE_TABLE_QUERY);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL(SubLayerTable.DROP_TABLE_QUERY);
		SubLayerTable.onCreate(db);
	}
}
