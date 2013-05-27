package pl.marek.knx;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.marek.knx.components.controllers.ControllerType;
import pl.marek.knx.components.controllers.ElementGroupAddressType;
import pl.marek.knx.database.DPTEntity;
import pl.marek.knx.database.DatapointEntity;
import pl.marek.knx.database.Device;
import pl.marek.knx.database.Element;
import pl.marek.knx.database.ElementGroupAddress;
import pl.marek.knx.database.Group;
import pl.marek.knx.database.Layer;
import pl.marek.knx.database.Project;
import pl.marek.knx.database.SubLayer;
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.telegram.Telegram;
import pl.marek.knx.telegram.TelegramFlags;

public class DBManager implements DatabaseManager{
	
	private ArrayList<Project> projects = new ArrayList<Project>();
	private ArrayList<Layer> layers = new ArrayList<Layer>();
	private ArrayList<SubLayer> subLayers = new ArrayList<SubLayer>();
	private ArrayList<Element> elements = new ArrayList<Element>();
	private ArrayList<Telegram> telegrams = new ArrayList<Telegram>();
	
	public DBManager(){
		
		for(int i = 1;i<5;i++){
			
			ArrayList<Layer> projectLayers = new ArrayList<Layer>();
			
			for(int j = 1;j<5;j++){
				Layer layer = new Layer();
				
				ArrayList<SubLayer> projectSubLayers = new ArrayList<SubLayer>();
				
				layer.setId(j);
				layer.setProjectId(i);
				layer.setName(String.format("Layer %d.%d", i,j));
				layer.setDescription(String.format("Layer Description %d", j));
				layer.setIcon("logo");
				
				for(int k=1;k<5;k++){
					SubLayer subLayer = new SubLayer();
					
					subLayer.setId(k);
					subLayer.setProjectId(i);
					subLayer.setLayerId(j);
					
					subLayer.setName(String.format("SubLayer %d.%d.%d", i,j,k));
					subLayer.setDescription(String.format("SubLayer Description %d.%d.%d", i,j,k));
					subLayer.setIcon("logo");
					
					
					ArrayList<Element> elems = new ArrayList<Element>();
					for(int l=1;l<7;l++){
						Element element = new Element();
						element.setId(l);
						element.setProjectId(i);
						element.setLayerId(j);
						element.setSubLayerId(k);
						element.setName("Nazwa");
						element.setDescription("Opis");
						
						if(l == 1){
							element.setType(ControllerType.ON_OFF_SWITCH.name());
							element.setX(10);
							element.setY(10);
						}
						if(l == 2){
							element.setType(ControllerType.ON_OFF_TOGGLE.name());
							element.setX(200);
							element.setY(10);
						}
						if(l == 3){
							element.setType(ControllerType.LIGHT_ON_OFF_SWITCH.name());
							element.setX(400);
							element.setY(10);
						}
						if(l == 4){
							element.setType(ControllerType.DIGITAL_VALUE.name());
							element.setX(10);
							element.setY(100);
						}
						if(l == 5){
							element.setType(ControllerType.SLIDER.name());
							element.setX(200);
							element.setY(100);
						}
						if(l == 6){
							element.setType(ControllerType.LIGHT_SLIDER.name());
							element.setX(400);
							element.setY(100);
						}
						
						ElementGroupAddress addr = new ElementGroupAddress();
						addr.setElementId(l);
						addr.setAddress("0/0/1");
						addr.setType(ElementGroupAddressType.MAIN.name());
						
						ElementGroupAddress addr1 = new ElementGroupAddress();
						addr1.setElementId(l);
						addr1.setAddress("0/0/2");
						addr1.setType(ElementGroupAddressType.OTHER.name());
						
						ArrayList<ElementGroupAddress> addrs = new ArrayList<ElementGroupAddress>();
						addrs.add(addr);
						addrs.add(addr1);
						element.setGroupAddresses(addrs);
						
						if(j > 1)
						elems.add(element);
						elements.add(element);
					}
					subLayer.setElements(elems);
					
					subLayers.add(subLayer);
					projectSubLayers.add(subLayer);
				}
				layer.setSubLayers(projectSubLayers);
				layers.add(layer);
				projectLayers.add(layer);
			}
			
	        Project project = new Project();
	        project.setId(i);
	        project.setName(String.format("Project %d", i));
	        project.setDescription(String.format("Description %d", i));
	        project.setImage("/home/marek/Magisterka/Grafika/WWW/logo.png");
	        
	        if(i == 2){
	        	project.setImage("");
	        }
	        project.setLayers(projectLayers);
	        
	        projects.add(project);
	        
		}
		
		for(int i=0; i<100;i++){
			Telegram telegram = new Telegram();
			telegram.setId(i);
			if(i%2 == 0){
				telegram.setSourceAddress("1.1.2");
				telegram.setDestinationAddress("0/0/1");
				telegram.setPriority("low");
			}else if(i%3 == 0){
				telegram.setSourceAddress("1.1.3");
				telegram.setDestinationAddress("0/0/2");
				telegram.setPriority("normal");
			}else if(i%5 == 0){
				telegram.setSourceAddress("1.1.5");
				telegram.setDestinationAddress("0/0/3");
				telegram.setPriority("urgent");
			}else if(i%7 == 0){
				telegram.setSourceAddress("1.1.7");
				telegram.setDestinationAddress("0/0/4");
				telegram.setPriority("system");
			}else{
				telegram.setSourceAddress("1.1.1");
				telegram.setDestinationAddress("0/0/5");
				telegram.setPriority("normal");
			}
			telegram.setData(String.valueOf(i));
			
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis() - (i*(1800000)));
			
			telegram.setTime(cal.getTime());
			telegram.setDptId("1.001");
			telegram.setFrameLength(11);
			telegram.setHopcount((byte)5);
			telegram.setMsgCode("L_data.ind");
			telegram.setFlags(new TelegramFlags());
			
			if(i%5 == 0){
				telegram.setType("Read");
			}else if((i-1)%5 == 0){
				telegram.setType("Response");
			}else{
				telegram.setType("Write");
			}
			telegrams.add(telegram);
		}
		
		
	}
	
	@Override
	public void open() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isOpen() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addGroup(Group group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDevice(Device device) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDPT(DPTEntity dpt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDatapoint(DatapointEntity datapoint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DatapointEntity getDatapointEntityByAddress(String groupAddress) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group getGroupByAddress(String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Device getDeviceByAddress(String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long addTelegram(Telegram telegram) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Telegram> getAllTelegrams() {
		return telegrams;
	}

	@Override
	public Telegram getTelegramById(int id) {
		return telegrams.get(id);
	}

	@Override
	public List<Telegram> getTelegramBySourceAddr(String sourceAddr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Telegram> getTelegramByDestAddr(String destAddr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Telegram> getRecentTelegrams(int number) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Telegram> getTelegrams(String source, String destination,
			String priority, String type, Date from, Date to, String limit) {
		if(limit != null){
			ArrayList<Telegram> tel = new ArrayList<Telegram>();
			String l = limit.replace(" ", "").trim();
			String[] ll = l.split(",");
			
			int iFrom = Integer.parseInt(ll[0]);
			int count = Integer.parseInt(ll[1]);
			
			for(int i=iFrom; i<(iFrom+count) && i<telegrams.size();i++){
				tel.add(telegrams.get(i));
			}
			return tel;
		}else{
			return telegrams;
		}
	}

	@Override
	public void addProject(Project project) {
		System.out.println("Adding project:" + project.toString());
		project.setId(projects.size()+1);
		projects.add(project);
	}

	@Override
	public Project getProjectById(int id) {
		
		for(Project project : projects){
			if(project.getId() == id){
				return project;
			}
		}
		return null;
	}

	@Override
	public Project getProjectByIdWithDependencies(int id) {
		return getProjectById(id);
	}

	@Override
	public Project getProjectByName(String name) {
		for(Project project : projects){
			if(project.getName().equals(name)){
				return project;
			}
		}
		return null;
	}

	@Override
	public List<Project> getAllProjects() {
		return projects;
	}

	@Override
	public void removeProject(Project project) {
		System.out.println("REMOVING PROJECT: "+project.toString());
		projects.remove(project);
		
	}

	@Override
	public void updateProject(Project project) {
		System.out.println("UPDATING PROJECT: "+project.toString());
		for(int i=0;i<projects.size();i++){
			Project p = projects.get(i);
			if(project.getId() == p.getId()){
				projects.set(i, project);
			}
		}
	}

	@Override
	public void addLayer(Layer layer) {
		System.out.println("ADDING LAYER: "+layer.toString());
		layers.add(layer);
		Project p = getProjectById(layer.getProjectId());
		p.getLayers().add(layer);
	}

	@Override
	public Layer getLayerById(int id) {
		
		for(Layer layer: layers){
			if(layer.getId() == id){
				return layer;
			}
		}
		
		return null;
	}

	@Override
	public Layer getLayerByIdWithDependencies(int id) {
		return getLayerById(id);
	}

	@Override
	public Layer getLayerByName(String name) {
		
		for(Layer layer: layers){
			if(layer.getName().equals(name)){
				return layer;
			}
		}
		
		return null;
	}

	@Override
	public List<Layer> getAllLayers() {
		return layers;
	}

	@Override
	public void removeLayer(Layer layer) {
		System.out.println("REMOVING LAYER: "+layer.toString());
		layers.remove(layer);
		Project p = getProjectById(layer.getProjectId());
		p.getLayers().remove(layer);
	}

	@Override
	public void updateLayer(Layer layer) {
		System.out.println("UPDATING LAYER: "+layer.toString());
		for(int i=0; i<layers.size();i++){
			Layer l = layers.get(i);
			if(l.getId() == layer.getId()){
				layers.set(i, layer);
			}
		}
		Project p = getProjectById(layer.getProjectId());
		for(int i=0; i<p.getLayers().size();i++){
			Layer l = p.getLayers().get(i);
			if(l.getId() == layer.getId()){
				p.getLayers().set(i, layer);
			}
		}
	}

	@Override
	public void addSubLayer(SubLayer subLayer) {
		System.out.println("ADDING SUBLAYER: "+subLayer.toString());
		subLayers.add(subLayer);
		Project p = getProjectById(subLayer.getProjectId());
		//Layer l = getLayerById(subLayer.getLayerId());
		for(int i=0;i<p.getLayers().size();i++){
			Layer ll = p.getLayers().get(i);
			if(subLayer.getLayerId() == ll.getId()){
				ll.getSubLayers().add(subLayer);
			}
		}
	}

	@Override
	public SubLayer getSubLayerById(int id) {
		
		for(SubLayer subLayer: subLayers){
			if(subLayer.getId() == id){
				return subLayer;
			}
		}
		
		return null;
	}

	@Override
	public SubLayer getSubLayerByIdWithDependencies(int id) {
		return getSubLayerById(id);
	}

	@Override
	public SubLayer getSubLayerByName(String name) {
		for(SubLayer subLayer: subLayers){
			if(subLayer.getName().equals(name)){
				return subLayer;
			}
		}
		return null;
	}

	@Override
	public List<SubLayer> getAllSubLayers() {
		return subLayers;
	}

	@Override
	public void removeSubLayer(SubLayer subLayer) {
		System.out.println("REMOVING SUBLAYER: "+subLayer.toString());
		subLayers.remove(subLayer);
		Project p = getProjectById(subLayer.getProjectId());
		for(int i=0;i<p.getLayers().size();i++){
			Layer ll = p.getLayers().get(i);
			if(subLayer.getLayerId() == ll.getId()){
				ll.getSubLayers().remove(subLayer);
			}
		}
	}

	@Override
	public void updateSubLayer(SubLayer subLayer) {
		System.out.println("UPDATING SUBLAYER: "+subLayer.toString());
		for(int i=0; i<subLayers.size();i++){
			SubLayer l = subLayers.get(i);
			if(l.getId() == subLayer.getId()){
				subLayers.set(i, subLayer);
			}
		}
		Layer l = getLayerById(subLayer.getLayerId());
		for(int i=0; i<l.getSubLayers().size();i++){
			SubLayer s = l.getSubLayers().get(i);
			if(s.getId() == subLayer.getId()){
				l.getSubLayers().set(i, subLayer);
			}
		}
	}

	@Override
	public void addElement(Element element) {
		System.out.println("ADDING ELEMENT:"+ element.getName());
		System.out.println("id: "+element.getId());
		System.out.println("Layer id: "+element.getLayerId());
		System.out.println("SubLayer id: "+element.getSubLayerId());
		System.out.println("Project id: "+element.getProjectId());
		System.out.println("Desc: "+element.getDescription());
		for(ElementGroupAddress addr: element.getGroupAddresses()){
			System.out.println("Address: " + addr.getAddress() + " TYPE: "+addr.getType());
		}
		
	}

	@Override
	public Element getElementById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getElementByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Element> getAllElementss() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeElement(Element element) {
		System.out.println("REMOVE ELEMENT:"+ element.getName());
	}

	@Override
	public void updateElement(Element element) {
		System.out.println("UPDATE ELEMENT:"+ element.getName());
	}

	@Override
	public void addElementGroupAddress(ElementGroupAddress address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ElementGroupAddress> getElementGroupAddressByElementId(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeElementGroupAddress(ElementGroupAddress address) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateElementGroupAddress(ElementGroupAddress address) {
		// TODO Auto-generated method stub
		
	}

}
