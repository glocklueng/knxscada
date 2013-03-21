package pl.marek.knx.database.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.database.ElementGroupAddress.ElementGroupAddressType;
import pl.marek.knx.database.tables.ElementGroupAddressTable;
import pl.marek.knx.database.tables.ElementGroupAddressTable.ElementGroupAddressColumns;
import pl.marek.knx.interfaces.Dao;

public class ElementGroupAddressDao implements Dao<ElementGroupAddress>{
	
	private static final String INSERT_QUERY = "INSERT INTO "
			+ ElementGroupAddressTable.TABLE_NAME + " (" 
			+ ElementGroupAddressColumns.ELEMENT_ID + ", "
			+ ElementGroupAddressColumns.ADDRESS + ", "
			+ ElementGroupAddressColumns.TYPE + ") "
			+ "values(?,?,?);";
	
	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	
	public ElementGroupAddressDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(ElementGroupAddressDao.INSERT_QUERY);
	}
	
	@Override
	public long save(ElementGroupAddress address) {
		insertStatement.clearBindings();
		insertStatement.bindLong(1, address.getElementId());
		insertStatement.bindString(2, address.getAddress());
		insertStatement.bindString(3, address.getType().name());
		return insertStatement.executeInsert();
	}

	@Override
	public void update(ElementGroupAddress address) {
		final ContentValues values = new ContentValues();
		values.put(ElementGroupAddressColumns.ADDRESS, address.getAddress());
		values.put(ElementGroupAddressColumns.TYPE, address.getType().name());
		db.update(ElementGroupAddressTable.TABLE_NAME, values, ElementGroupAddressColumns.ELEMENT_ID + " = ? and "+ElementGroupAddressColumns.ADDRESS+" = ?", new String[]{String.valueOf(address.getElementId()), address.getAddress()});
	}

	@Override
	public void delete(ElementGroupAddress address) {
		db.delete(ElementGroupAddressTable.TABLE_NAME, ElementGroupAddressColumns.ELEMENT_ID + " = ? and "+ElementGroupAddressColumns.ADDRESS+" = ?", new String[]{String.valueOf(address.getElementId()), address.getAddress()});
	}
	
	public void deleteByElementId(int id) {
		db.delete(ElementGroupAddressTable.TABLE_NAME, ElementGroupAddressColumns.ELEMENT_ID+" = ?", new String[]{String.valueOf(id)});
	}

	@Override
	public ElementGroupAddress get(Object id) {
		return getById(id);
	}
	
	
	public ElementGroupAddress getById(Object id){
		return get(id, ElementGroupAddressColumns._ID);
	}
	
	public ElementGroupAddress get(Object value, String column){
		ElementGroupAddress address = null;
		Cursor c = 
				db.query(ElementGroupAddressTable.TABLE_NAME, 
						new String[]{
									ElementGroupAddressColumns.ELEMENT_ID,
									ElementGroupAddressColumns.ADDRESS,
									ElementGroupAddressColumns.TYPE
						},
						column + " = ?", new String[]{String.valueOf(value)}, 
						null, null ,null, "1");
		if(c.moveToFirst()){
			address = this.build(c);
		}
		if(!c.isClosed()){
			c.close();
		}
		return address;
	}
	
	public List<ElementGroupAddress> getByElementId(int id){
		return getList(ElementGroupAddressColumns.ELEMENT_ID, String.valueOf(id), null, null, null, null);
	}

	@Override
	public List<ElementGroupAddress> getAll() {
		return getList(null, null, null, null, null, null);
	}
	
	private List<ElementGroupAddress> getList(String column, String arg, String groupBy, String having, String orderBy, String limit){
		
		String selection = null;
		if(column != null)
			selection = column + " = ?";
		
		String[] selectionArgs = null;
		if(arg != null)
			selectionArgs = new String[]{String.valueOf(arg)};
		List<ElementGroupAddress> addresses = new ArrayList<ElementGroupAddress>();
		Cursor c = 
				db.query(ElementGroupAddressTable.TABLE_NAME, 
						new String[]{
									ElementGroupAddressColumns.ELEMENT_ID,
									ElementGroupAddressColumns.ADDRESS,
									ElementGroupAddressColumns.TYPE
						}, 
						selection, selectionArgs, groupBy, having ,orderBy, limit);
		if(c.moveToFirst()){
			do{
				ElementGroupAddress address = this.build(c);
				if(address != null){
					addresses.add(address);
				}
			} while(c.moveToNext());
		}
		if(!c.isClosed()){
			c.close();
		}
		return addresses;
	}
	
	private ElementGroupAddress build(Cursor cursor){
		ElementGroupAddress address = null;
		if(cursor != null){
			address = new ElementGroupAddress();
			address.setElementId(cursor.getInt(0));
			address.setAddress(cursor.getString(1));
			address.setType(ElementGroupAddressType.valueOf(cursor.getString(2)));
		}
		return address;
	}

}
