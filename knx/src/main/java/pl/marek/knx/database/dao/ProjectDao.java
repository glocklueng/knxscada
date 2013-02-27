package pl.marek.knx.database.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import pl.marek.knx.database.Device;
import pl.marek.knx.database.Group;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.database.DatapointEntity;
import pl.marek.knx.database.tables.ProjectTable;
import pl.marek.knx.database.tables.ProjectTable.ProjectColumns;
import pl.marek.knx.interfaces.Dao;
import pl.marek.knx.telegram.Telegram;
import pl.marek.knx.utils.DateConversion;

public class ProjectDao implements Dao<Project> {

	private static final String INSERT_QUERY = "INSERT INTO "
			+ ProjectTable.TABLE_NAME + " (" + ProjectColumns.NAME + ", "
			+ ProjectColumns.DESCRIPTION + ", "
			+ ProjectColumns.AUTHOR + ") "
			+ "values(?,?,?);";

	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	
	private TelegramDao telegramDao;
	private GroupsDao groupsDao;
	private DevicesDao devicesDao;
	private DatapointsDao datapointsDao;
	private LayerDao layerDao;

	public ProjectDao(SQLiteDatabase db) {
		this.db = db;
		insertStatement = db.compileStatement(ProjectDao.INSERT_QUERY);
		
		telegramDao = new TelegramDao(db);
		groupsDao = new GroupsDao(db);
		devicesDao = new DevicesDao(db);
		datapointsDao = new DatapointsDao(db);
		layerDao = new LayerDao(db);
	}

	@Override
	public long save(Project project) {
		insertStatement.clearBindings();
		insertStatement.bindString(1, project.getName());
		insertStatement.bindString(2, project.getDescription());
		insertStatement.bindString(3, project.getAuthor());
		return insertStatement.executeInsert();
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void update(Project project) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		final ContentValues values = new ContentValues();
		values.put(ProjectColumns.NAME, project.getName());
		values.put(ProjectColumns.DESCRIPTION, project.getDescription());
		values.put(ProjectColumns.AUTHOR, project.getAuthor());
		values.put(ProjectColumns.EDIT_DATE, dateFormat.format(Calendar.getInstance().getTime()));
		db.update(ProjectTable.TABLE_NAME, values, BaseColumns._ID + " = ?", new String[]{String.valueOf(project.getId())});
	}

	@Override
	public void delete(Project project) {
		if(project.getId() > 0){
			db.delete(ProjectTable.TABLE_NAME,  BaseColumns._ID + " = ?", new String[]{String.valueOf(project.getId())});
		}
	}

	@Override
	public Project get(Object id) {
		return getById(id);
	}
	
	public Project getById(Object id){
		return get(id, BaseColumns._ID);
	}
	
	public Project getByName(String name){
		return get(name, ProjectColumns.NAME);
	}
	
	public Project getByIdWithDependencies(int id){
		Project project = getById(id);
		project.setTelegrams((ArrayList<Telegram>)telegramDao.getByProjectId(id));
		project.setDevices((ArrayList<Device>)devicesDao.getByProjectId(id));
		project.setGroups((ArrayList<Group>)groupsDao.getByProjectId(id));
		project.setDatapoints((ArrayList<DatapointEntity>)datapointsDao.getByProjectId(id));
		project.setLayers((ArrayList<Layer>)layerDao.getByProjectId(id));
		return project;
	}
	
	public Project get(Object value, String column){
		Project project = null;
		Cursor c = 
				db.query(ProjectTable.TABLE_NAME, 
						new String[]{BaseColumns._ID, 
									ProjectColumns.NAME,
									ProjectColumns.DESCRIPTION,
									ProjectColumns.AUTHOR,
									ProjectColumns.CREATE_DATE,
									ProjectColumns.EDIT_DATE
						}, 
						column + " = ?", new String[]{String.valueOf(value)}, 
						null, null ,null, "1");
		if(c.moveToFirst()){
			project = this.buildProject(c);
		}
		if(!c.isClosed()){
			c.close();
		}
		return project;
	}

	@Override
	public List<Project> getAll() {		
		List<Project> projects = new ArrayList<Project>();
		Cursor c = 
				db.query(ProjectTable.TABLE_NAME, 
						new String[]{BaseColumns._ID, 
									ProjectColumns.NAME,
									ProjectColumns.DESCRIPTION,
									ProjectColumns.AUTHOR,
									ProjectColumns.CREATE_DATE,
									ProjectColumns.EDIT_DATE
						}, 
						null, null, null, null, ProjectColumns.NAME+" ASC", null);
		if(c.moveToFirst()){
			do{
				Project project = this.buildProject(c);
				if(project != null){
					projects.add(project);
				}
			} while(c.moveToNext());
		}
		if(!c.isClosed()){
			c.close();
		}
		return projects;
	}
	
	private Project buildProject(Cursor cursor){
		Project project = null;
		if(cursor != null){
			project = new Project();
			project.setId(cursor.getInt(0));
			project.setName(cursor.getString(1));
			project.setDescription(cursor.getString(2));
			project.setAuthor(cursor.getString(3));
			project.setCreateDate(new DateConversion().getDateFromString(cursor.getString(4)));
			project.setEditDate(new DateConversion().getDateFromString(cursor.getString(5)));
		}
		return project;
	}

}
