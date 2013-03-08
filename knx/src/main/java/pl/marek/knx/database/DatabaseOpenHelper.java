package pl.marek.knx.database;

import pl.marek.knx.database.tables.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper{
	
	public static final String DATABASE_NAME = "knx";
	public static final int DATABASE_VERSION = 1;
	
	@SuppressWarnings("unused")
	private Context context;
	
	public DatabaseOpenHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		TelegramTable.onCreate(db);
		DatapointsTable.onCreate(db);
		DevicesTable.onCreate(db);
		GroupsTable.onCreate(db);
		DPTTable.onCreate(db);
		ProjectTable.onCreate(db);
		LayerTable.onCreate(db);
		SubLayerTable.onCreate(db);
		ElementTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		TelegramTable.onUpgrade(db, oldVersion, newVersion);
		DatapointsTable.onUpgrade(db, oldVersion, newVersion);
		DevicesTable.onUpgrade(db, oldVersion, newVersion);
		GroupsTable.onUpgrade(db, oldVersion, newVersion);
		DPTTable.onUpgrade(db, oldVersion, newVersion);
		ProjectTable.onUpgrade(db, oldVersion, newVersion);
		LayerTable.onUpgrade(db, oldVersion, newVersion);
		SubLayerTable.onUpgrade(db, oldVersion, newVersion);
		ElementTable.onUpgrade(db, oldVersion, newVersion);
	}
}
