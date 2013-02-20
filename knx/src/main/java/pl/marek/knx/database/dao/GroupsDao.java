package pl.marek.knx.database.dao;

import pl.marek.knx.database.Group;
import pl.marek.knx.database.tables.GroupsTable;
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
			group.setProjectId(cursor.getInt(1));
			group.setName(cursor.getString(2));
			group.setDescription(cursor.getString(3));
		}
		return group;
	}
}
