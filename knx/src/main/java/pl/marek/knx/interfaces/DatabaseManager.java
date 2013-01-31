package pl.marek.knx.interfaces;

import java.sql.SQLException;

import pl.marek.knx.database.DPTEntity;
import pl.marek.knx.database.DatapointEntity;
import pl.marek.knx.database.Device;
import pl.marek.knx.database.Group;
import pl.marek.knx.telegram.Telegram;

public interface DatabaseManager {
	
	public void open();
	public void close();
	public void addTelegram(Telegram telegram);
	public void addGroup(Group group);
	public void addDevice(Device device);
	public void addDPT(DPTEntity dpt);
	public void addDatapoint(DatapointEntity datapoint);
	public DatapointEntity getDatapointEntityByAddress(String groupAddress);
	public Group getGroupByAddress(String address);
	public Device getDeviceByAddress(String address);
}
