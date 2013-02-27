package pl.marek.knx.database.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import pl.marek.knx.database.Layer;
import pl.marek.knx.database.tables.LayerTable;
import pl.marek.knx.database.tables.LayerTable.LayerColumns;
import pl.marek.knx.interfaces.Dao;

public class LayerDao implements Dao<Layer>{
	
	private static final String INSERT_QUERY = "INSERT INTO "
			+ LayerTable.TABLE_NAME + " (" + LayerColumns.PROJECT_ID + ", "
			+ LayerColumns.NAME + ", "
			+ LayerColumns.DESCRIPTION + ", "
			+ LayerColumns.ICON + ") "
			+ "values(?,?,?,?);";

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	
	public LayerDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(LayerDao.INSERT_QUERY);	
	}

	@Override
	public long save(Layer layer) {
		insertStatement.clearBindings();
		insertStatement.bindLong(1, layer.getProjectId());
		insertStatement.bindString(2, layer.getName());
		insertStatement.bindString(3, layer.getDescription());
		insertStatement.bindString(4, layer.getIcon());
		return insertStatement.executeInsert();
	}

	@Override
	public void update(Layer layer) {
		final ContentValues values = new ContentValues();
		values.put(LayerColumns.NAME, layer.getName());
		values.put(LayerColumns.DESCRIPTION, layer.getDescription());
		values.put(LayerColumns.ICON, layer.getIcon());
		db.update(LayerTable.TABLE_NAME, values, BaseColumns._ID + " = ? and "+LayerColumns.PROJECT_ID+" = ?", new String[]{String.valueOf(layer.getId()), String.valueOf(layer.getProjectId())});
	}

	@Override
	public void delete(Layer layer) {
		db.delete(LayerTable.TABLE_NAME, BaseColumns._ID + " = ? and "+LayerColumns.PROJECT_ID+" = ?", new String[]{String.valueOf(layer.getId()), String.valueOf(layer.getProjectId())});
	}

	@Override
	public Layer get(Object id) {
		return getById(id);
	}
	
	public Layer getById(Object id){
		return get(id, LayerColumns._ID);
	}
	
	public Layer getByName(String name){
		return get(name, LayerColumns.NAME);
	}
	
	public Layer get(Object value, String column){
		Layer layer = null;
		Cursor c = 
				db.query(LayerTable.TABLE_NAME, 
						new String[]{LayerColumns._ID,
									LayerColumns.PROJECT_ID,
									LayerColumns.NAME,
									LayerColumns.DESCRIPTION,
									LayerColumns.ICON
						},
						column + " = ?", new String[]{String.valueOf(value)}, 
						null, null ,null, "1");
		if(c.moveToFirst()){
			layer = this.buildLayer(c);
		}
		if(!c.isClosed()){
			c.close();
		}
		return layer;
	}

	public List<Layer> getByProjectId(int id){
		return getList(LayerColumns.PROJECT_ID, String.valueOf(id), null, null, null, null);
	}

	@Override
	public List<Layer> getAll() {
		return getList(null, null, null, null, null, null);
	}
	
	private List<Layer> getList(String column, String arg, String groupBy, String having, String orderBy, String limit){
		
		String selection = null;
		if(column != null)
			selection = column + " = ?";
		
		String[] selectionArgs = null;
		if(arg != null)
			selectionArgs = new String[]{String.valueOf(arg)};
		List<Layer> layers = new ArrayList<Layer>();
		Cursor c = 
				db.query(LayerTable.TABLE_NAME, 
						new String[]{LayerColumns._ID,
									 LayerColumns.PROJECT_ID,
									 LayerColumns.NAME,
									 LayerColumns.DESCRIPTION,
									 LayerColumns.ICON
						}, 
						selection, selectionArgs, groupBy, having ,orderBy, limit);
		if(c.moveToFirst()){
			do{
				Layer layer = this.buildLayer(c);
				if(layer != null){
					layers.add(layer);
				}
			} while(c.moveToNext());
		}
		if(!c.isClosed()){
			c.close();
		}
		return layers;
	}
	
	private Layer buildLayer(Cursor cursor){
		Layer layer = null;
		if(cursor != null){
			layer = new Layer();
			layer.setId(cursor.getInt(0));
			layer.setProjectId(cursor.getInt(1));
			layer.setName(cursor.getString(2));
			layer.setDescription(cursor.getString(3));
			layer.setIcon(cursor.getString(4));
		}
		return layer;
	}

}
