package pl.marek.knx.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GroupsDao extends BasicBlockDao<Group> {
	
	public GroupsDao(SQLiteDatabase db){
		super(db, GroupsTable.TABLE_NAME);
	}

	@Override
	public Group build(Cursor cursor) {
		Group group= null;
		if(cursor != null){
			group = new Group();
			group.setAddress(cursor.getString(0));
			group.setName(cursor.getString(1));
			group.setDescription(cursor.getString(2));
		}
		return group;
	}
}
