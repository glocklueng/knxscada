package pl.marek.knx.database.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import pl.marek.knx.controls.ControlType;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.database.tables.ElementTable;
import pl.marek.knx.database.tables.ElementTable.ElementColumns;
import pl.marek.knx.interfaces.Dao;

public class ElementDao implements Dao<Element>{
	
	private static final String INSERT_QUERY = "INSERT INTO "
			+ ElementTable.TABLE_NAME + " (" 
			+ ElementColumns.PROJECT_ID + ", "
			+ ElementColumns.LAYER_ID + ","
			+ ElementColumns.SUBLAYER_ID + ","
			+ ElementColumns.X + ","
			+ ElementColumns.Y + ", "
			+ ElementColumns.NAME + ", "
			+ ElementColumns.DESCRIPTION + ", "
			+ ElementColumns.DEVICE_ADDRESS + ", "
			+ ElementColumns.TYPE + ") "
			+ "values(?,?,?,?,?,?,?,?,?);";
	

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	private ElementGroupAddressDao elementGroupAddressDao;
	
	public ElementDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(ElementDao.INSERT_QUERY);
		elementGroupAddressDao = new ElementGroupAddressDao(db);
	}
	

	@Override
	public long save(Element element) {
		insertStatement.clearBindings();
		insertStatement.bindLong(1, element.getProjectId());
		insertStatement.bindLong(2, element.getLayerId());
		insertStatement.bindLong(3, element.getSubLayerId());
		insertStatement.bindLong(4, element.getX());
		insertStatement.bindLong(5, element.getY());
		insertStatement.bindString(6, element.getName());
		insertStatement.bindString(7, element.getDescription());
		insertStatement.bindString(8, element.getDeviceAddress());
		insertStatement.bindString(9, element.getType().name());
		long id = insertStatement.executeInsert();
		for(ElementGroupAddress address: element.getGroupAddresses()){
			address.setElementId((int)id);
			elementGroupAddressDao.save(address);
		}
		return id;
	}

	@Override
	public void update(Element element) {
		final ContentValues values = new ContentValues();
		values.put(ElementColumns.X, element.getX());
		values.put(ElementColumns.Y, element.getY());
		values.put(ElementColumns.NAME, element.getName());
		values.put(ElementColumns.DESCRIPTION, element.getDescription());
		values.put(ElementColumns.DEVICE_ADDRESS, element.getDeviceAddress());
		values.put(ElementColumns.TYPE, element.getType().name());
		
		elementGroupAddressDao.deleteByElementId(element.getId());
		for(ElementGroupAddress address: element.getGroupAddresses()){
			address.setElementId(element.getId());
			elementGroupAddressDao.save(address);
		}
		db.update(ElementTable.TABLE_NAME, values, ElementColumns._ID + " = ? and "+ElementColumns.PROJECT_ID+" = ? and "+ElementColumns.LAYER_ID+" = ? and "+ElementColumns.SUBLAYER_ID+" = ?", new String[]{String.valueOf(element.getId()), String.valueOf(element.getProjectId()), String.valueOf(element.getLayerId()), String.valueOf(element.getSubLayerId())});
	}

	@Override
	public void delete(Element element) {
		elementGroupAddressDao.deleteByElementId(element.getId());
		db.delete(ElementTable.TABLE_NAME, ElementColumns._ID + " = ? and "+ElementColumns.PROJECT_ID+" = ? and "+ElementColumns.LAYER_ID+" = ? and "+ElementColumns.SUBLAYER_ID+" = ?", new String[]{String.valueOf(element.getId()), String.valueOf(element.getProjectId()), String.valueOf(element.getLayerId()), String.valueOf(element.getSubLayerId())});
	}

	@Override
	public Element get(Object id) {
		return getById(id);
	}
	
	public Element getById(Object id){
		return get(id, ElementColumns._ID);
	}
	
	public Element getByName(String name){
		return get(name, ElementColumns.NAME);
	}
	
	public Element get(Object value, String column){
		Element element = null;
		Cursor c = 
				db.query(ElementTable.TABLE_NAME, 
						new String[]{
									ElementColumns._ID,
									ElementColumns.PROJECT_ID,
									ElementColumns.LAYER_ID,
									ElementColumns.SUBLAYER_ID,
									ElementColumns.X,
									ElementColumns.Y,
									ElementColumns.NAME,
									ElementColumns.DESCRIPTION,
									ElementColumns.DEVICE_ADDRESS,
									ElementColumns.TYPE
						},
						column + " = ?", new String[]{String.valueOf(value)}, 
						null, null ,null, "1");
		if(c.moveToFirst()){
			element = this.buildElement(c);
		}
		if(!c.isClosed()){
			c.close();
		}
		return element;
	}

	public List<Element> getByProjectId(int id){
		return getList(ElementColumns.PROJECT_ID, String.valueOf(id), null, null, null, null);
	}
	
	public List<Element> getByLayerId(int id){
		return getList(ElementColumns.LAYER_ID, String.valueOf(id), null, null, null, null);
	}
	
	public List<Element> getBySubLayerId(int id){
		return getList(ElementColumns.SUBLAYER_ID, String.valueOf(id), null, null, null, null);
	}

	@Override
	public List<Element> getAll() {
		return getList(null, null, null, null, null, null);
	}
	
	private List<Element> getList(String column, String arg, String groupBy, String having, String orderBy, String limit){
		
		String selection = null;
		if(column != null)
			selection = column + " = ?";
		
		String[] selectionArgs = null;
		if(arg != null)
			selectionArgs = new String[]{String.valueOf(arg)};
		List<Element> elements = new ArrayList<Element>();
		Cursor c = 
				db.query(ElementTable.TABLE_NAME, 
						new String[]{
									ElementColumns._ID,
									ElementColumns.PROJECT_ID,
									ElementColumns.LAYER_ID,
									ElementColumns.SUBLAYER_ID,
									ElementColumns.X,
									ElementColumns.Y,
									ElementColumns.NAME,
									ElementColumns.DESCRIPTION,
									ElementColumns.DEVICE_ADDRESS,
									ElementColumns.TYPE
						}, 
						selection, selectionArgs, groupBy, having ,orderBy, limit);
		if(c.moveToFirst()){
			do{
				Element element = this.buildElement(c);
				if(element != null){
					elements.add(element);
				}
			} while(c.moveToNext());
		}
		if(!c.isClosed()){
			c.close();
		}
		return elements;
	}
	
	private Element buildElement(Cursor cursor){
		Element element = null;
		if(cursor != null){
			element = new Element();
			element.setId(cursor.getInt(0));
			element.setProjectId(cursor.getInt(1));
			element.setLayerId(cursor.getInt(2));
			element.setSubLayerId(cursor.getInt(3));
			element.setX(cursor.getInt(4));
			element.setY(cursor.getInt(5));
			element.setName(cursor.getString(6));
			element.setDescription(cursor.getString(7));
			element.setDeviceAddress(cursor.getString(8));
			element.setType(ControlType.valueOf(cursor.getString(9)));
			element.setGroupAddresses((ArrayList<ElementGroupAddress>)elementGroupAddressDao.getByElementId(element.getId()));
		}
		return element;
	}
}
