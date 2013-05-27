package pl.marek.knx.database.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import pl.marek.knx.database.tables.TelegramTable;
import pl.marek.knx.database.tables.TelegramTable.TelegramColumns;
import pl.marek.knx.interfaces.Dao;
import pl.marek.knx.telegram.Telegram;
import pl.marek.knx.telegram.TelegramFlags;
import pl.marek.knx.utils.DataRepresentation;
import pl.marek.knx.utils.DateConversion;

public class TelegramDao implements Dao<Telegram>{
	
	private static final String INSERT_QUERY  = "INSERT INTO " + TelegramTable.TABLE_NAME + " ("
											   + TelegramColumns.PROJECT_ID + ", "
											   + TelegramColumns.PRIORITY + ", "
											   + TelegramColumns.SOURCE_ADDRESS + ", "
											   + TelegramColumns.DESTINATION_ADDRESS + ", "
											   + TelegramColumns.HOPCOUNT + ", "
											   + TelegramColumns.MESSAGE_CODE + ", "
											   + TelegramColumns.DPT_ID + ", "
											   + TelegramColumns.RAWDATA + ", "
											   + TelegramColumns.DATA + ", "
											   + TelegramColumns.RAWFRAME + ", "
											   + TelegramColumns.FRAME_LENGTH + ", "
											   + TelegramColumns.TYPE + ", "
											   + TelegramColumns.ACK + ", "
											   + TelegramColumns.CONFIRMATION + ", "
											   + TelegramColumns.REPEATED + ") "
											   + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			
	
	
	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	
	public TelegramDao(SQLiteDatabase db){
		this.db = db;
		insertStatement = db.compileStatement(TelegramDao.INSERT_QUERY);
	}

	@Override
	public long save(Telegram telegram) {
		insertStatement.clearBindings();
		insertStatement.bindString(1, String.valueOf(telegram.getProjectId()));
		insertStatement.bindString(2, telegram.getPriority());
		insertStatement.bindString(3, telegram.getSourceAddress());
		insertStatement.bindString(4, telegram.getDestinationAddress());
		insertStatement.bindString(5, Byte.toString(telegram.getHopcount()));
		insertStatement.bindString(6, telegram.getMsgCode());
		insertStatement.bindString(7, telegram.getDptId());
		insertStatement.bindString(8, DataRepresentation.byteArrayToHexString(telegram.getRawdata()));
		insertStatement.bindString(9, telegram.getData());
		insertStatement.bindString(10, DataRepresentation.byteArrayToHexString(telegram.getRawframe()));
		insertStatement.bindLong(11, telegram.getFrameLength());
		insertStatement.bindString(12, telegram.getType());
		insertStatement.bindLong(13, telegram.getFlags().isAck() ? 0 : 1);
		insertStatement.bindLong(14, telegram.getFlags().isConfirmation() ? 0 : 1);
		insertStatement.bindLong(15, telegram.getFlags().isRepeated() ? 0 : 1);
		return insertStatement.executeInsert();
	}

	@Override
	public void update(Telegram telegram) {
		final ContentValues values = new ContentValues();
		values.put(TelegramColumns.PROJECT_ID, telegram.getProjectId());
		values.put(TelegramColumns.PRIORITY, telegram.getPriority());
		values.put(TelegramColumns.SOURCE_ADDRESS, telegram.getSourceAddress());
		values.put(TelegramColumns.DESTINATION_ADDRESS, telegram.getDestinationAddress());
		values.put(TelegramColumns.HOPCOUNT, Byte.toString(telegram.getHopcount()));
		values.put(TelegramColumns.MESSAGE_CODE, telegram.getMsgCode());
		values.put(TelegramColumns.DPT_ID, telegram.getDptId());
		values.put(TelegramColumns.RAWDATA, DataRepresentation.byteArrayToHexString(telegram.getRawdata()));
		values.put(TelegramColumns.DATA, telegram.getData());
		values.put(TelegramColumns.RAWFRAME, DataRepresentation.byteArrayToHexString(telegram.getRawframe()));
		values.put(TelegramColumns.FRAME_LENGTH, telegram.getFrameLength());
		values.put(TelegramColumns.TYPE, telegram.getType());
		values.put(TelegramColumns.ACK, telegram.getFlags().isAck() ? 0 : 1);
		values.put(TelegramColumns.CONFIRMATION, telegram.getFlags().isConfirmation() ? 0 : 1);
		values.put(TelegramColumns.REPEATED, telegram.getFlags().isRepeated() ? 0 : 1);
		db.update(TelegramTable.TABLE_NAME, values, BaseColumns._ID + " = ?", new String[]{String.valueOf(telegram.getId())});
	}

	@Override
	public void delete(Telegram telegram) {
		if(telegram.getId() > 0){
			db.delete(TelegramTable.TABLE_NAME,  BaseColumns._ID + " = ?", new String[]{String.valueOf(telegram.getId())});
		}
	}

	@Override
	public Telegram get(Object id) {
		return getById(id);
	}
		
	public Telegram getById(Object id) {
		return get(id, BaseColumns._ID);
	}
	
	public Telegram get(Object value, String column){
		Telegram telegram = null;
		Cursor c = 
				db.query(TelegramTable.TABLE_NAME, 
						new String[]{BaseColumns._ID,
									TelegramColumns.PROJECT_ID,
									TelegramColumns.TIME,
									TelegramColumns.PRIORITY,
								    TelegramColumns.SOURCE_ADDRESS,
								    TelegramColumns.DESTINATION_ADDRESS,
								    TelegramColumns.HOPCOUNT,
								    TelegramColumns.MESSAGE_CODE,
								    TelegramColumns.DPT_ID,
								    TelegramColumns.RAWDATA,
								    TelegramColumns.DATA,
								    TelegramColumns.RAWFRAME,
								    TelegramColumns.FRAME_LENGTH,
								    TelegramColumns.TYPE,
								    TelegramColumns.ACK,
								    TelegramColumns.CONFIRMATION,
								    TelegramColumns.REPEATED
						
						}, 
						column + " = ?", new String[]{String.valueOf(value)}, 
						null, null ,null, "1");
		if(c.moveToFirst()){
			telegram = this.buildTelegram(c);
		}
		if(!c.isClosed()){
			c.close();
		}
		return telegram;
	}
	
	public List<Telegram> getBySrcAddr(String addr) {
		return getList(TelegramColumns.SOURCE_ADDRESS+"=?", new String[]{addr}, null, null, null, null);
	}
	
	public List<Telegram> getByDstAddr(String addr) {
		return getList(TelegramColumns.DESTINATION_ADDRESS+"=?", new String[]{addr}, null, null, null,null);
	}
	
	public List<Telegram> getRecentTelegrams(int limit){
		return getList(null, null, null, null, TelegramColumns.TIME+" DESC", String.valueOf(limit));
	}
	
	public List<Telegram> getByProjectId(int id){
		return getList(TelegramColumns.PROJECT_ID+"=?", new String[]{String.valueOf(id)}, null, null, TelegramColumns.TIME+" DESC", null);
	}
	
	public List<Telegram> getTelegrams(String source, String destination, String priority, String type, Date from, Date to, String limit){
		
		String selection = "";
		ArrayList<String> args = new ArrayList<String>();
		boolean isFirst = true;
		if(source != null && !source.isEmpty()){
			selection = TelegramColumns.SOURCE_ADDRESS + "=?";
			args.add(source);
			isFirst = false;
		}
		if(destination != null && !destination.isEmpty()){
			if(!isFirst){
				selection = selection + " AND ";
			}
			selection = selection + TelegramColumns.DESTINATION_ADDRESS + "=?";
			args.add(destination);
			isFirst = false;
		}
		if(priority != null && !priority.isEmpty()){
			if(!isFirst){
				selection = selection + " AND ";
			}
			selection = selection + " (";
			String[] pr = priority.split(",");
			boolean isFirstPriority = true;
			for(String p: pr){
				if(!p.isEmpty()){
					if(!isFirstPriority){
						selection = selection + " OR ";
					}
					selection = selection + TelegramColumns.PRIORITY + "=?";
					args.add(p);
					isFirstPriority = false;
				}
			}
			selection = selection + " )";
			isFirst = false;
		}
		if(type != null && !type.isEmpty()){
			if(!isFirst){
				selection = selection + " AND ";
			}
			selection = selection + " (";
			boolean isFirstType = true;
			String[] tt = type.split(",");
			for(String t: tt){
				if(!isFirstType){
					selection = selection + " OR ";
				}
				selection = selection + TelegramColumns.TYPE + "=?";
				args.add(t);
				isFirstType = false;
			}
			selection = selection + ") ";
			isFirst = false;
		}
		if(from != null && to == null){
			if(!isFirst){
				selection = selection + " AND ";
			}
			selection = selection + TelegramColumns.TIME + ">=?";
			args.add(DateConversion.getDateTimeString(from));
			
			isFirst = false;
		}
		else if(to != null && from == null){
			if(!isFirst){
				selection = selection + " AND ";
			}
			selection = selection + TelegramColumns.TIME + "<=?";
			args.add(DateConversion.getDateTimeString(to));
			isFirst = false;
		}
		else if(from != null && to != null){
			if(!isFirst){
				selection = selection + " AND ";
			}
			selection = selection + TelegramColumns.TIME + ">=? AND "+TelegramColumns.TIME+"<=?";
			args.add(DateConversion.getDateTimeString(from));
			args.add(DateConversion.getDateTimeString(to));
		}
				
		String[] arguments = new String[args.size()];
		for(int i=0; i<args.size();i++){
			arguments[i] = args.get(i);
		}
		
		return getList(selection, arguments, null, null, TelegramColumns.TIME+" DESC", limit);
	}
	
	@Override
	public List<Telegram> getAll() {
		return getList(null,null,null,null,null,null);
	}
	
	private List<Telegram> getList(String selection, String[] args, String groupBy, String having, String orderBy, String limit){
						
		List<Telegram> telegrams = new ArrayList<Telegram>();
		Cursor c = 
				db.query(TelegramTable.TABLE_NAME, 
						new String[]{BaseColumns._ID,
									TelegramColumns.PROJECT_ID,
									TelegramColumns.TIME,
									TelegramColumns.PRIORITY,
								    TelegramColumns.SOURCE_ADDRESS,
								    TelegramColumns.DESTINATION_ADDRESS,
								    TelegramColumns.HOPCOUNT,
								    TelegramColumns.MESSAGE_CODE,
								    TelegramColumns.DPT_ID,
								    TelegramColumns.RAWDATA,
								    TelegramColumns.DATA,
								    TelegramColumns.RAWFRAME,
								    TelegramColumns.FRAME_LENGTH,
								    TelegramColumns.TYPE,
								    TelegramColumns.ACK,
								    TelegramColumns.CONFIRMATION,
								    TelegramColumns.REPEATED
						}, 
						selection, args, groupBy, having ,orderBy, limit);
		if(c.moveToFirst()){
			do{
				Telegram telegram = this.buildTelegram(c);
				if(telegram != null){
					telegrams.add(telegram);
				}
			} while(c.moveToNext());
		}
		if(!c.isClosed()){
			c.close();
		}
		return telegrams;
	}
	
	private Telegram buildTelegram(Cursor cursor){
		Telegram telegram = null;
		if(cursor != null){
			telegram = new Telegram();
			telegram.setId(cursor.getLong(0));
			telegram.setProjectId(cursor.getInt(1));
			new DateConversion();
			telegram.setTime(DateConversion.getDateFromString(cursor.getString(2)));
			telegram.setPriority(cursor.getString(3));
			telegram.setSourceAddress(cursor.getString(4));
			telegram.setDestinationAddress(cursor.getString(5));
			telegram.setHopcount(Byte.decode(cursor.getString(6)));
			telegram.setMsgCode(cursor.getString(7));
			telegram.setDptId(cursor.getString(8));
			telegram.setRawdata(DataRepresentation.hexStringToByteArray(cursor.getString(9)));
			telegram.setData(cursor.getString(10));
			telegram.setRawframe(DataRepresentation.hexStringToByteArray(cursor.getString(11)));
			telegram.setFrameLength(cursor.getInt(12));
			telegram.setType(cursor.getString(13));
			
			TelegramFlags flags = new TelegramFlags();
			flags.setAck(cursor.getInt(14) != 0 ? true : false);
			flags.setConfirmation(cursor.getInt(15) != 0 ? true : false);
			flags.setRepeated(cursor.getInt(16) != 0 ? true : false);

			telegram.setFlags(flags);
		}
		return telegram;
	}

}
