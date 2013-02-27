package pl.marek.knx.database;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.telegram.Telegram;
import pl.marek.knx.database.dao.*;

public class DatabaseManagerImpl implements DatabaseManager{
	
	private Context context;
	private SQLiteDatabase db;
	private SQLiteOpenHelper openHelper;
	
	private DatapointsDao datapointsDao;
	private DevicesDao devicesDao;
	private GroupsDao groupsDao;
	private DPTDao dptDao;
	private TelegramDao telegramDao;
	private ProjectDao projectDao;
	private LayerDao layerDao;
	
	public DatabaseManagerImpl(Context context){
		this.context = context;
		
		open();
		
		datapointsDao = new DatapointsDao(db);
		devicesDao = new DevicesDao(db);
		groupsDao = new GroupsDao(db);
		dptDao = new DPTDao(db);
		telegramDao = new TelegramDao(db);
		projectDao = new ProjectDao(db);
		layerDao = new LayerDao(db);
	}
	
	@Override
	public void open() {
		openHelper = new DatabaseOpenHelper(context);
		db = openHelper.getWritableDatabase();
	}

	@Override
	public void close() {
		openHelper.close();
	}
	
	@Override
	public boolean isOpen(){
		return db.isOpen();
	}

	@Override
	public void addGroup(Group group){
		db.beginTransaction();
		try{
			groupsDao.save(group);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}


	@Override
	public void addDevice(Device device){
		db.beginTransaction();
		try {
			devicesDao.save(device);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}


	@Override
	public void addDPT(DPTEntity dpt) {
		db.beginTransaction();
		try {
			dptDao.save(dpt);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}


	@Override
	public void addDatapoint(DatapointEntity datapoint) {
		db.beginTransaction();
		try {
			datapointsDao.save(datapoint);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}
	
	@Override
	public DatapointEntity getDatapointEntityByAddress(String groupAddress) {
		return datapointsDao.get(groupAddress);
	}

	@Override
	public Group getGroupByAddress(String address) {
		return groupsDao.get(address);
	}

	@Override
	public Device getDeviceByAddress(String address) {
		return devicesDao.get(address);
	}

	
	//Telegram Operations
	@Override
	public long addTelegram(Telegram telegram) {
		long rowId = -1;
		db.beginTransaction();
		try{
			rowId = telegramDao.save(telegram);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
		return rowId;
	}
	
	@Override
	public List<Telegram> getAllTelegrams() {
		return telegramDao.getAll();
	}

	@Override
	public Telegram getTelegramById(int id) {
		return telegramDao.get(id);
	}

	@Override
	public List<Telegram> getTelegramBySourceAddr(String sourceAddr) {
		return telegramDao.getBySrcAddr(sourceAddr);
	}

	@Override
	public List<Telegram> getTelegramByDestAddr(String destAddr) {
		return telegramDao.getByDstAddr(destAddr);
	}

	@Override
	public List<Telegram> getRecentTelegrams(int limit) {
		return telegramDao.getRecentTelegrams(limit);
	}
	
	
	//Project Operations
	@Override
	public void addProject(Project project) {
		db.beginTransaction();
		try{
			int id = (int)projectDao.save(project);
			project.setId(id);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}

	@Override
	public Project getProjectById(int id) {
		return projectDao.get(id);
	}
	
	@Override
	public Project getProjectByIdWithDependencies(int id) {
		return projectDao.getByIdWithDependencies(id);
	}
	
	@Override
	public Project getProjectByName(String name) {
		return projectDao.getByName(name);
	}

	@Override
	public List<Project> getAllProjects() {
		return projectDao.getAll();
	}

	@Override
	public void removeProject(Project project) {
		db.beginTransaction();
		try{
			projectDao.delete(project);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}

	@Override
	public void updateProject(Project project) {
		db.beginTransaction();
		try{
			projectDao.update(project);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}
	
	
	//Layer Operations
	
	@Override
	public void addLayer(Layer layer) {
		db.beginTransaction();
		try{
			int id = (int)layerDao.save(layer);
			layer.setId(id);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}

	@Override
	public Layer getLayerById(int id) {
		return layerDao.getById(id);
	}

	@Override
	public Layer getLayerByName(String name) {
		return layerDao.getByName(name);
	}

	@Override
	public List<Layer> getAllLayers() {
		return layerDao.getAll();
	}

	@Override
	public void removeLayer(Layer layer) {
		db.beginTransaction();
		try{
			layerDao.delete(layer);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}

	@Override
	public void updateLayer(Layer layer) {
		db.beginTransaction();
		try{
			layerDao.update(layer);
			db.setTransactionSuccessful();
		} finally{
			db.endTransaction();
		}
	}
}
