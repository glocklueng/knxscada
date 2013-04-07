package pl.marek.knx.database.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import pl.marek.knx.database.DatapointEntity;
import pl.marek.knx.database.tables.DatapointsTable;
import pl.marek.knx.database.tables.DatapointsTable.DatapointsColumns;
import pl.marek.knx.interfaces.Dao;
import pl.marek.knx.utils.DateConversion;

public class DatapointsDao implements Dao<DatapointEntity>{
	
	private static final String INSERT_QUERY  = "INSERT INTO " + DatapointsTable.TABLE_NAME + " ("
			   + DatapointsColumns.GROUP_ADDRESS + ", "
			   + DatapointsColumns.PROJECT_ID + ", "
			   + DatapointsColumns.DPT_ID + ", "
			   + DatapointsColumns.STATE + ", "
			   + DatapointsColumns.MODIFY_DATE + ") "
			   + "values(?,?,?,?,?);";
	
	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	
	public DatapointsDao(SQLiteDatabase db){
		this.db = db;
		insertStatement = db.compileStatement(DatapointsDao.INSERT_QUERY);
	}
	
	@Override
	public long save(DatapointEntity datapoint) {
		insertStatement.clearBindings();
		insertStatement.bindString(1, datapoint.getGroupAddress());
		insertStatement.bindString(2, String.valueOf(datapoint.getProjectId()));
		insertStatement.bindString(3, datapoint.getDptId());
		insertStatement.bindString(4, datapoint.getState());
		return insertStatement.executeInsert();
	}

	@Override
	public void update(DatapointEntity datapoint) {
		final ContentValues values = new ContentValues();
		values.put(DatapointsColumns.DPT_ID, datapoint.getDptId());
		values.put(DatapointsColumns.STATE, datapoint.getState());
		values.put(DatapointsColumns.MODIFY_DATE, "datetime('now')");
		db.update(DatapointsTable.TABLE_NAME, values, DatapointsColumns.GROUP_ADDRESS + " = ?, and "+ DatapointsColumns.PROJECT_ID +" = ?", new String[]{datapoint.getGroupAddress(), String.valueOf(datapoint.getProjectId())});
	}

	@Override
	public void delete(DatapointEntity datapoint) {
		db.delete(DatapointsTable.TABLE_NAME, DatapointsColumns.GROUP_ADDRESS + " = ?, and "+ DatapointsColumns.PROJECT_ID +" = ?", new String[]{datapoint.getGroupAddress(), String.valueOf(datapoint.getProjectId())});
	}

	@Override
	public DatapointEntity get(Object id) {
		DatapointEntity datapoint = null;
		Cursor c = 
				db.query(DatapointsTable.TABLE_NAME, 
						new String[]{DatapointsColumns.GROUP_ADDRESS,
									 DatapointsColumns.PROJECT_ID,
									 DatapointsColumns.DPT_ID,
									 DatapointsColumns.STATE,
									 DatapointsColumns.MODIFY_DATE
						}, 
						DatapointsColumns.GROUP_ADDRESS + " = ?", new String[]{String.valueOf(String.valueOf(id))}, 
						null, null ,null, "1");
		if(c.moveToFirst()){
			datapoint = this.buildDatapoint(c);
		}
		if(!c.isClosed()){
			c.close();
		}
		return datapoint;
	}
	
	public List<DatapointEntity> getByProjectId(int id){
		return getList(DatapointsColumns.PROJECT_ID, String.valueOf(id), null, null, DatapointsColumns.MODIFY_DATE+" DESC", null);
	}

	@Override
	public List<DatapointEntity> getAll() {
		return getList(null, null, null, null, null, null);
	}
	
	private List<DatapointEntity> getList(String column, String arg, String groupBy, String having, String orderBy, String limit){
		
		String selection = null;
		if(column != null)
			selection = column + " = ?";
		
		String[] selectionArgs = null;
		if(arg != null)
			selectionArgs = new String[]{String.valueOf(arg)};
	
		List<DatapointEntity> datapoints = new ArrayList<DatapointEntity>();
		Cursor c = 
				db.query(DatapointsTable.TABLE_NAME, 
						new String[]{DatapointsColumns.GROUP_ADDRESS,
									 DatapointsColumns.PROJECT_ID,
									 DatapointsColumns.DPT_ID,
									 DatapointsColumns.STATE,
									 DatapointsColumns.MODIFY_DATE
						},  
						selection, selectionArgs, groupBy, having ,orderBy, limit);
		if(c.moveToFirst()){
			do{
				DatapointEntity datapoint = this.buildDatapoint(c);
				if(datapoint != null){
					datapoints.add(datapoint);
				}
			} while(c.moveToNext());
		}
		if(!c.isClosed()){
			c.close();
		}
		return datapoints;
	}
	
	private DatapointEntity buildDatapoint(Cursor c){
		DatapointEntity datapoint = null;
		if(c != null){
			datapoint = new DatapointEntity();
			datapoint.setGroupAddress(c.getString(0));
			datapoint.setProjectId(c.getInt(1));
			datapoint.setDptId(c.getString(2));
			datapoint.setState(c.getString(3));
			datapoint.setModifyDate(new DateConversion().getDateFromString(c.getString(3)));
		}
		return datapoint;
	}

}
