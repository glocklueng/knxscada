package pl.marek.knx.database;

public class ElementGroupAddress {
	
	private int elementId;
	private String address;
	private String type;
	
	public ElementGroupAddress(){}
	
	public ElementGroupAddress(int elementId, String address, String type){
		this.elementId = elementId;
		this.address = address;
		this.type = type;
	}

	public int getElementId() {
		return elementId;
	}

	public void setElementId(int elementId) {
		this.elementId = elementId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
