package pl.marek.knx;

import java.util.ArrayList;
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
import pl.marek.knx.interfaces.DatabaseManager;
import pl.marek.knx.telegram.Telegram;

public class DBManager implements DatabaseManager{
	
	private ArrayList<Project> projects = new ArrayList<Project>();
	private ArrayList<Layer> layers = new ArrayList<Layer>();
	private ArrayList<SubLayer> subLayers = new ArrayList<SubLayer>();
	
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
	      
	        project.setLayers(projectLayers);
	        
	        projects.add(project);
	        
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Telegram getTelegramById(int id) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateElement(Element element) {
		// TODO Auto-generated method stub
		
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
