package pl.marek.knx.database;

public class ElementGroupAddress {
	
	private int id;
	private int elementId;
	private String address;
	private ElementGroupAddressType type;
	
	public ElementGroupAddress(){}
	
	public ElementGroupAddress(int id, int elementId, String address, ElementGroupAddressType type){
		this.id = id;
		this.elementId = elementId;
		this.address = address;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public ElementGroupAddressType getType() {
		return type;
	}

	public void setType(ElementGroupAddressType type) {
		this.type = type;
	}



	public enum ElementGroupAddressType{
		MAIN,OTHER
	}

}
