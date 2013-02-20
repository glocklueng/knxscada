package pl.marek.knx.database;

public class BasicBlock {
	
	protected String address;
	protected int projectId;
	protected String name;
	protected String description;
	
	public BasicBlock(){}
	
	public BasicBlock(String address, int projectId, String name, String description){
		this.address = address;
		this.projectId = projectId;
		this.name = name;
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
