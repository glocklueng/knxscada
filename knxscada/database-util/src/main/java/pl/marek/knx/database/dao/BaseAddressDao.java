package pl.marek.knx.database.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import pl.marek.knx.database.BaseAddress;
import pl.marek.knx.database.tables.BaseAddressColumns;
import pl.marek.knx.interfaces.Dao;

public abstract class BaseAddressDao<T extends BaseAddress> implements Dao<T>{
		
	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	private String tableName;

	public BaseAddressDao(SQLiteDatabase db, String tableName){
		this.db = db;
		this.tableName = tableName;
		insertStatement = db.compileStatement(createInsertQuery(tableName));
	}
	
	private String createInsertQuery(String tableName){
		return "INSERT INTO " + tableName + " ("
				   + BaseAddressColumns.ADDRESS + ", "
				   + BaseAddressColumns.PROJECT_ID + ", "
				   + BaseAddressColumns.NAME + ", "
				   + BaseAddressColumns.DESCRIPTION + ") "
				   + "values(?,?,?,?);";
	}
	
	@Override
	public long save(T block){
		insertStatement.clearBindings();
		insertStatement.bindString(1, block.getAddress());
		insertStatement.bindString(2, String.valueOf(block.getProjectId()));
		insertStatement.bindString(3, block.getName());
		insertStatement.bindString(4, block.getDescription());
		long rowId = insertStatement.executeInsert();
		return rowId;
	}

	@Override
	public void update(T block) {
		final ContentValues values = new ContentValues();
		values.put(BaseAddressColumns.NAME, block.getName());
		values.put(BaseAddressColumns.DESCRIPTION, block.getDescription());
		db.update(tableName, values, BaseAddressColumns.ADDRESS + " = ? and "+ BaseAddressColumns.PROJECT_ID + " = ?", new String[]{block.getAddress(), String.valueOf(block.getProjectId())});
	}

	@Override
	public void delete(T block) {
		db.delete(tableName, BaseAddressColumns.ADDRESS + " = ? and "+ BaseAddressColumns.PROJECT_ID + " = ?", new String[]{block.getAddress(), String.valueOf(block.getProjectId())});
	}

	@Override
	public T get(Object id) {
		T block = null;
		Cursor c = 
				db.query(tableName, 
						new String[]{BaseAddressColumns.ADDRESS,
									 BaseAddressColumns.PROJECT_ID,
									 BaseAddressColumns.NAME,
									 BaseAddressColumns.DESCRIPTION
						}, 
						BaseAddressColumns.ADDRESS + " = ?", new String[]{String.valueOf(String.valueOf(id))}, 
						null, null ,null, "1");
		if(c.moveToFirst()){
			block = this.build(c);
		}
		if(!c.isClosed()){
			c.close();
		}
		return block;
	}
	
	public List<T> getByProjectId(int id){
		return getList(BaseAddressColumns.PROJECT_ID, String.valueOf(id), null, null, null, null);
	}

	@Override
	public List<T> getAll() {
		return getList(null, null, null, null, null, null);
	}
	
	private List<T> getList(String column, String arg, String groupBy, String having, String orderBy, String limit){
		
		String selection = null;
		if(column != null)
			selection = column + " = ?";
		
		String[] selectionArgs = null;
		if(arg != null)
			selectionArgs = new String[]{String.valueOf(arg)};
		List<T> blocks = new ArrayList<T>();
		Cursor c = 
				db.query(tableName, 
						new String[]{BaseAddressColumns.ADDRESS,
									 BaseAddressColumns.PROJECT_ID,
									 BaseAddressColumns.NAME,
									 BaseAddressColumns.DESCRIPTION
						}, 
						selection, selectionArgs, groupBy, having ,orderBy, limit);
		if(c.moveToFirst()){
			do{
				T block = this.build(c);
				if(block != null){
					blocks.add(block);
				}
			} while(c.moveToNext());
		}
		if(!c.isClosed()){
			c.close();
		}
		return blocks;
	}
	
	public abstract T build(Cursor cursor);

}
