package pl.marek.knx.interfaces;

import java.util.List;

import pl.marek.knx.database.DPTEntity;
import pl.marek.knx.database.DatapointEntity;
import pl.marek.knx.database.Device;
import pl.marek.knx.database.Group;
import pl.marek.knx.database.Project;
import pl.marek.knx.telegram.Telegram;

public interface DatabaseManager {
	
	public void open();
	public void close();
	
	public void addGroup(Group group);
	public void addDevice(Device device);
	public void addDPT(DPTEntity dpt);
	public void addDatapoint(DatapointEntity datapoint);
	public DatapointEntity getDatapointEntityByAddress(String groupAddress);
	public Group getGroupByAddress(String address);
	public Device getDeviceByAddress(String address);
	
	//Telegram Operations
	public long addTelegram(Telegram telegram);
	public List<Telegram> getAllTelegrams();
	public Telegram getTelegramById(int id);
	public List<Telegram> getTelegramBySourceAddr(String sourceAddr);
	public List<Telegram> getTelegramByDestAddr(String destAddr);
	public List<Telegram> getRecentTelegrams(int number);
	
	//Project Operations
	public void addProject(Project project);
	public Project getProjectById(int id);
	public Project getProjectByIdWithDependencies(int id);
	public Project getProjectByName(String name);
	public List<Project> getAllProjects();
	public void removeProject(Project project);
	public void updateProject(Project project);
	
}
