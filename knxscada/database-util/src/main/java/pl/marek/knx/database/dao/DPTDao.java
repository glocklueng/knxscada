package pl.marek.knx.database.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import pl.marek.knx.database.DPTEntity;
import pl.marek.knx.database.tables.DPTTable;
import pl.marek.knx.database.tables.DPTTable.DPTColumns;
import pl.marek.knx.interfaces.Dao;

public class DPTDao implements Dao<DPTEntity>{
	
	private static final String INSERT_QUERY  = "INSERT INTO " + DPTTable.TABLE_NAME + " ("
			+ DPTColumns.DPT_ID + ", "
			+ DPTColumns.DATA_GROUP + ", " 
			+ DPTColumns.NAME + ", " 
			+ DPTColumns.MIN_VALUE + ", " 
			+ DPTColumns.MAX_VALUE + ", " 
			+ DPTColumns.UNIT + ")"
			+ "values(?,?,?,?,?,?);";
	
	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	
	public DPTDao(SQLiteDatabase db){
		this.db = db;
		insertStatement = db.compileStatement(DPTDao.INSERT_QUERY);
	}

	@Override
	public long save(DPTEntity dpt) {
		insertStatement.clearBindings();
		insertStatement.bindString(1, dpt.getID());
		insertStatement.bindString(2, dpt.getDataGroup());
		insertStatement.bindString(3, dpt.getDescription());
		insertStatement.bindString(4, dpt.getLowerValue());
		insertStatement.bindString(5, dpt.getUpperValue());
		insertStatement.bindString(6, dpt.getUnit());
		return insertStatement.executeInsert();
	}

	@Override
	public void update(DPTEntity dpt) {
		final ContentValues values = new ContentValues();
		values.put(DPTColumns.DATA_GROUP, dpt.getDataGroup());
		values.put(DPTColumns.NAME, dpt.getDescription());
		values.put(DPTColumns.MIN_VALUE, dpt.getLowerValue());
		values.put(DPTColumns.MAX_VALUE, dpt.getUpperValue());
		values.put(DPTColumns.UNIT, dpt.getUnit());
		db.update(DPTTable.TABLE_NAME, values, DPTColumns.DPT_ID + " = ?", new String[]{dpt.getID()});	
	}

	@Override
	public void delete(DPTEntity dpt) {
		db.delete(DPTTable.TABLE_NAME, DPTColumns.DPT_ID + " = ?", new String[]{dpt.getID()});
	}

	@Override
	public DPTEntity get(Object id) {
		DPTEntity dpt = null;
		Cursor c = 
				db.query(DPTTable.TABLE_NAME, 
						new String[]{DPTColumns.DPT_ID,
									 DPTColumns.DATA_GROUP,
									 DPTColumns.NAME,
									 DPTColumns.MIN_VALUE,
									 DPTColumns.MAX_VALUE,
									 DPTColumns.UNIT
						}, 
						DPTColumns.DPT_ID + " = ?", new String[]{String.valueOf(id)}, 
						null, null ,null, "1");
		if(c.moveToFirst()){
			dpt = this.build(c);
		}
		if(!c.isClosed()){
			c.close();
		}
		return dpt;
	}

	@Override
	public List<DPTEntity> getAll() {
		List<DPTEntity> dpts = new ArrayList<DPTEntity>();
		Cursor c = 
				db.query(DPTTable.TABLE_NAME, 
						new String[]{DPTColumns.DPT_ID,
									 DPTColumns.DATA_GROUP,
									 DPTColumns.NAME,
									 DPTColumns.MIN_VALUE,
									 DPTColumns.MAX_VALUE,
									 DPTColumns.UNIT
						}, 
						null, null, null, null, null);
		if(c.moveToFirst()){
			do{
				DPTEntity dpt = this.build(c);
				if(dpt != null){
					dpts.add(dpt);
				}
			} while(c.moveToNext());
		}
		if(!c.isClosed()){
			c.close();
		}
		return dpts;
	}
	
	private DPTEntity build(Cursor c){
		DPTEntity dpt = null;
		if(c != null){
			String id = c.getString(0);
			String dataGroup = c.getString(1);
			String name = c.getString(2);
			String min = c.getString(3);
			String max = c.getString(4);
			String unit = c.getString(5);
			dpt = new DPTEntity(id, dataGroup, name, min, max, unit);
		}
		return dpt;
	}
}
