package pl.marek.knx.interfaces;

import java.util.List;

import pl.marek.knx.database.DPTEntity;
import pl.marek.knx.database.DatapointEntity;
import pl.marek.knx.database.Device;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.database.Group;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.telegram.Telegram;

public interface DatabaseManager {
	
	public void open();
	public void close();
	public boolean isOpen();
	
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
	
	//Layer Operations
	public void addLayer(Layer layer);
	public Layer getLayerById(int id);
	public Layer getLayerByIdWithDependencies(int id);
	public Layer getLayerByName(String name);
	public List<Layer> getAllLayers();
	public void removeLayer(Layer layer);
	public void updateLayer(Layer layer);
	
	//SubLayer Operations
	public void addSubLayer(SubLayer subLayer);
	public SubLayer getSubLayerById(int id);
	public SubLayer getSubLayerByIdWithDependencies(int id);
	public SubLayer getSubLayerByName(String name);
	public List<SubLayer> getAllSubLayers();
	public void removeSubLayer(SubLayer subLayer);
	public void updateSubLayer(SubLayer subLayer);
	
	//Element Operations
	public void addElement(Element element);
	public Element getElementById(int id);
	public Element getElementByName(String name);
	public List<Element> getAllElementss();
	public void removeElement(Element element);
	public void updateElement(Element element);
	
	//ElementGroupAddress Operations
	public void addElementGroupAddress(ElementGroupAddress address);
	public List<ElementGroupAddress> getElementGroupAddressByElementId(int id);
	public void removeElementGroupAddress(ElementGroupAddress address);
	public void updateElementGroupAddress(ElementGroupAddress address);	
	
}
