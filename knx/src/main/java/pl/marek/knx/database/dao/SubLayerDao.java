package pl.marek.knx.database.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import pl.marek.knx.database.Element;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.database.tables.SubLayerTable;
import pl.marek.knx.database.tables.LayerTable.LayerColumns;
import pl.marek.knx.database.tables.SubLayerTable.SubLayerColumns;
import pl.marek.knx.interfaces.Dao;

public class SubLayerDao implements Dao<SubLayer>{
	
	private static final String INSERT_QUERY = "INSERT INTO "
			+ SubLayerTable.TABLE_NAME + " (" 
			+ SubLayerColumns.PROJECT_ID + ", "
			+ SubLayerColumns.LAYER_ID + ", "
			+ SubLayerColumns.NAME + ", "
			+ SubLayerColumns.DESCRIPTION + ", "
			+ SubLayerColumns.ICON + ") "
			+ "values(?,?,?,?,?);";

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	private ElementDao elementDao;
	
	public SubLayerDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(SubLayerDao.INSERT_QUERY);
		elementDao = new ElementDao(db);
	}
	
	@Override
	public long save(SubLayer subLayer) {
		insertStatement.clearBindings();
		insertStatement.bindLong(1, subLayer.getProjectId());
		insertStatement.bindLong(2, subLayer.getLayerId());
		insertStatement.bindString(3, subLayer.getName());
		insertStatement.bindString(4, subLayer.getDescription());
		insertStatement.bindString(5, subLayer.getIcon());
		return insertStatement.executeInsert();
	}

	@Override
	public void update(SubLayer subLayer) {
		final ContentValues values = new ContentValues();
		values.put(SubLayerColumns.NAME, subLayer.getName());
		values.put(SubLayerColumns.DESCRIPTION, subLayer.getDescription());
		values.put(SubLayerColumns.ICON, subLayer.getIcon());
		db.update(SubLayerTable.TABLE_NAME, values, BaseColumns._ID + " = ? and "+SubLayerColumns.PROJECT_ID+" = ? and "+SubLayerColumns.LAYER_ID+" = ?", new String[]{String.valueOf(subLayer.getId()), String.valueOf(subLayer.getProjectId()), String.valueOf(subLayer.getLayerId())});
	}

	@Override
	public void delete(SubLayer subLayer) {
		db.delete(SubLayerTable.TABLE_NAME, BaseColumns._ID + " = ? and "+SubLayerColumns.PROJECT_ID+" = ? and "+SubLayerColumns.LAYER_ID+" = ?", new String[]{String.valueOf(subLayer.getId()), String.valueOf(subLayer.getProjectId()), String.valueOf(subLayer.getLayerId())});
	}

	@Override
	public SubLayer get(Object id) {
		return getById(id);
	}
	
	public SubLayer getById(Object id){
		return get(id, LayerColumns._ID);
	}
	
	public SubLayer getByIdWithDependencies(int id){
		SubLayer subLayer = getById(id);
		subLayer.setElements((ArrayList<Element>)elementDao.getBySubLayerId(id));
		return subLayer;
	}
	
	public SubLayer getByName(String name){
		return get(name, LayerColumns.NAME);
	}
	
	public SubLayer get(Object value, String column){
		SubLayer subLayer = null;
		Cursor c = 
				db.query(SubLayerTable.TABLE_NAME, 
						new String[]{SubLayerColumns._ID,
									SubLayerColumns.PROJECT_ID,
									SubLayerColumns.LAYER_ID,
									SubLayerColumns.NAME,
									SubLayerColumns.DESCRIPTION,
									SubLayerColumns.ICON
						},
						column + " = ?", new String[]{String.valueOf(value)}, 
						null, null ,null, "1");
		if(c.moveToFirst()){
			subLayer = this.buildSubLayer(c);
		}
		if(!c.isClosed()){
			c.close();
		}
		return subLayer;
	}

	public List<SubLayer> getByProjectId(int id){
		return getList(SubLayerColumns.PROJECT_ID, String.valueOf(id), null, null, null, null);
	}
	
	public List<SubLayer> getByLayerId(int id){
		return getList(SubLayerColumns.LAYER_ID, String.valueOf(id), null, null, null, null);
	}
	
	public List<SubLayer> getByLayerIdWithDependencies(int id){
		List<SubLayer> subLayers =  getList(SubLayerColumns.LAYER_ID, String.valueOf(id), null, null, null, null);
		for(SubLayer subLayer: subLayers){
			subLayer.setElements((ArrayList<Element>)elementDao.getBySubLayerId(subLayer.getId()));
		}
		return subLayers;
	}

	@Override
	public List<SubLayer> getAll() {
		return getList(null, null, null, null, null, null);
	}
	
	private List<SubLayer> getList(String column, String arg, String groupBy, String having, String orderBy, String limit){
		
		String selection = null;
		if(column != null)
			selection = column + " = ?";
		
		String[] selectionArgs = null;
		if(arg != null)
			selectionArgs = new String[]{String.valueOf(arg)};
		List<SubLayer> subLayers = new ArrayList<SubLayer>();
		Cursor c = 
				db.query(SubLayerTable.TABLE_NAME, 
						new String[]{SubLayerColumns._ID,
									 SubLayerColumns.PROJECT_ID,
									 SubLayerColumns.LAYER_ID,
									 SubLayerColumns.NAME,
									 SubLayerColumns.DESCRIPTION,
									 SubLayerColumns.ICON
						}, 
						selection, selectionArgs, groupBy, having ,orderBy, limit);
		if(c.moveToFirst()){
			do{
				SubLayer subLayer = this.buildSubLayer(c);
				if(subLayer != null){
					subLayers.add(subLayer);
				}
			} while(c.moveToNext());
		}
		if(!c.isClosed()){
			c.close();
		}
		return subLayers;
	}
	
	private SubLayer buildSubLayer(Cursor cursor){
		SubLayer subLayer = null;
		if(cursor != null){
			subLayer = new SubLayer();
			subLayer.setId(cursor.getInt(0));
			subLayer.setProjectId(cursor.getInt(1));
			subLayer.setLayerId(cursor.getInt(2));
			subLayer.setName(cursor.getString(3));
			subLayer.setDescription(cursor.getString(4));
			subLayer.setIcon(cursor.getString(5));
		}
		return subLayer;
	}
}