package pl.marek.knx.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import pl.marek.knx.database.TelegramTable.TelegramColumns;
import pl.marek.knx.interfaces.Dao;
import pl.marek.knx.telegram.Telegram;
import pl.marek.knx.telegram.TelegramFlags;
import pl.marek.knx.utils.DataRepresentation;
import pl.marek.knx.utils.DateConversion;

public class TelegramDao implements Dao<Telegram>{
	
	private static final String INSERT_QUERY  = "INSERT INTO " + TelegramTable.TABLE_NAME + " ("
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
											   + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
			
	
	
	private SQLiteDatabase db;
	private SQLiteStatement insertStatement;
	
	public TelegramDao(SQLiteDatabase db){
		this.db = db;
		insertStatement = db.compileStatement(TelegramDao.INSERT_QUERY);
	}

	@Override
	public long save(Telegram telegram) {
		insertStatement.clearBindings();
		insertStatement.bindString(1, telegram.getPriority());
		insertStatement.bindString(2, telegram.getSourceAddress());
		insertStatement.bindString(3, telegram.getDestinationAddress());
		insertStatement.bindString(4, Byte.toString(telegram.getHopcount()));
		insertStatement.bindString(5, telegram.getMsgCode());
		insertStatement.bindString(6, telegram.getDptId());
		insertStatement.bindString(7, DataRepresentation.byteArrayToHexString(telegram.getRawdata()));
		insertStatement.bindString(8, telegram.getData());
		insertStatement.bindString(9, DataRepresentation.byteArrayToHexString(telegram.getRawframe()));
		insertStatement.bindLong(10, telegram.getFrameLength());
		insertStatement.bindString(11, telegram.getType());
		insertStatement.bindLong(12, telegram.getFlags().isAck() ? 0 : 1);
		insertStatement.bindLong(13, telegram.getFlags().isConfirmation() ? 0 : 1);
		insertStatement.bindLong(14, telegram.getFlags().isRepeated() ? 0 : 1);
		return insertStatement.executeInsert();
	}

	@Override
	public void update(Telegram telegram) {
		final ContentValues values = new ContentValues();
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
		Telegram telegram = null;
		Cursor c = 
				db.query(TelegramTable.TABLE_NAME, 
						new String[]{BaseColumns._ID, 
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
						BaseColumns._ID + " = ?", new String[]{String.valueOf(String.valueOf(id))}, 
						null, null ,null, "1");
		if(c.moveToFirst()){
			telegram = this.buildTelegram(c);
		}
		if(!c.isClosed()){
			c.close();
		}
		return telegram;
	}

	@Override
	public List<Telegram> getAll() {
		List<Telegram> telegrams = new ArrayList<Telegram>();
		Cursor c = 
				db.query(TelegramTable.TABLE_NAME, 
						new String[]{BaseColumns._ID, 
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
						null,null, null ,null, null);
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
			telegram.setTime(new DateConversion().getDateFromString(cursor.getString(1)));
			telegram.setPriority(cursor.getString(2));
			telegram.setSourceAddress(cursor.getString(3));
			telegram.setDestinationAddress(cursor.getString(4));
			telegram.setHopcount(Byte.decode(cursor.getString(5)));
			telegram.setMsgCode(cursor.getString(6));
			telegram.setDptId(cursor.getString(7));
			telegram.setRawdata(DataRepresentation.hexStringToByteArray(cursor.getString(8)));
			telegram.setData(cursor.getString(9));
			telegram.setRawframe(DataRepresentation.hexStringToByteArray(cursor.getString(10)));
			telegram.setFrameLength(cursor.getInt(11));
			telegram.setType(cursor.getString(12));
			
			TelegramFlags flags = new TelegramFlags();
			flags.setAck(cursor.getInt(13) != 0 ? true : false);
			flags.setConfirmation(cursor.getInt(14) != 0 ? true : false);
			flags.setRepeated(cursor.getInt(15) != 0 ? true : false);

			telegram.setFlags(flags);
		}
		return telegram;
	}

}