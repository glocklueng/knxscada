package pl.marek.knx.database;

import java.util.ArrayList;

import pl.marek.knx.controls.ControlType;
import android.os.Parcel;
import android.os.Parcelable;

public class Element implements Parcelable{
	
	public static final String ELEMENT = "ELEMENT";
	
	private int id;
	private int projectId;
	private int layerId;
	private int subLayerId;
	private int x;
	private int y;
	private String name;
	private String description;
	private ArrayList<ElementGroupAddress> groupAddresses;
	private String deviceAddress;
	private ControlType type;
	
	public Element(){}
	
	public Element(int id, int projectId, int layerId, int subLayerId, int x, int y, String name, String description, ArrayList<ElementGroupAddress> groupAddresses, String deviceAddress, ControlType type){
		this.id = id;
		this.projectId = projectId;
		this.layerId = layerId;
		this.subLayerId = subLayerId;
		this.x = x;
		this.y = y;
		this.name = name;
		this.description = description;
		this.groupAddresses = groupAddresses;
		this.deviceAddress = deviceAddress;
		this.type = type;
	}
	
	public Element(Parcel parcel){
		readFromParcel(parcel);
	}
	
	public static final Parcelable.Creator<Element> CREATOR = new Parcelable.Creator<Element>() {
		public Element createFromParcel(Parcel parcel) {
			return new Element(parcel);
		}

		public Element[] newArray(int size) {
			return new Element[size];
		}
	};

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public int getLayerId() {
		return layerId;
	}
	public void setLayerId(int layerId) {
		this.layerId = layerId;
	}
	public int getSubLayerId() {
		return subLayerId;
	}
	public void setSubLayerId(int subLayerId) {
		this.subLayerId = subLayerId;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
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

	public ArrayList<ElementGroupAddress> getGroupAddresses() {
		return groupAddresses;
	}

	public void setGroupAddresses(ArrayList<ElementGroupAddress> groupAddresses) {
		this.groupAddresses = groupAddresses;
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public ControlType getType() {
		return type;
	}

	public void setType(ControlType type) {
		this.type = type;
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(id);
		parcel.writeInt(projectId);
		parcel.writeInt(layerId);
		parcel.writeInt(subLayerId);
		parcel.writeInt(x);
		parcel.writeInt(y);
		parcel.writeString(name);
		parcel.writeString(description);
		parcel.writeString(deviceAddress);
		parcel.writeString(type.name());
	}
	
	public void readFromParcel(Parcel parcel){
		id = parcel.readInt();
		projectId = parcel.readInt();
		layerId = parcel.readInt();
		subLayerId = parcel.readInt();
		x = parcel.readInt();
		y = parcel.readInt();
		name = parcel.readString();
		description = parcel.readString();
		deviceAddress = parcel.readString();
		type = ControlType.valueOf(parcel.readString());
	}
}