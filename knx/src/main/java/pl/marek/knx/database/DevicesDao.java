package pl.marek.knx.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DevicesDao extends BasicBlockDao<Device>{

	public DevicesDao(SQLiteDatabase db) {
		super(db, DevicesTable.TABLE_NAME);
	}

	@Override
	public Device build(Cursor cursor) {
		Device device = null;
		if(cursor != null){
			device = new Device();
			device.setAddress(cursor.getString(0));
			device.setName(cursor.getString(1));
			device.setDescription(cursor.getString(2));
		}
		return device;
	}
}