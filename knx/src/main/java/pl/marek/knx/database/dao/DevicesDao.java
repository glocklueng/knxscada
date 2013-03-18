package pl.marek.knx.database.dao;

import pl.marek.knx.database.Device;
import pl.marek.knx.database.tables.DevicesTable;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DevicesDao extends BaseAddressDao<Device>{

	public DevicesDao(SQLiteDatabase db) {
		super(db, DevicesTable.TABLE_NAME);
	}

	@Override
	public Device build(Cursor cursor) {
		Device device = null;
		if(cursor != null){
			device = new Device();
			device.setAddress(cursor.getString(0));
			device.setProjectId(cursor.getInt(1));
			device.setName(cursor.getString(2));
			device.setDescription(cursor.getString(3));
		}
		return device;
	}
}