package pl.marek.knx.database;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import pl.marek.knx.telegram.Telegram;

public class Project implements Parcelable{
	
	public static final String PROJECT = "PROJECT";
	
	private int id;
	private String name;
	private String description;
	private String author;
	private Date createDate;
	private Date editDate;
	private ArrayList<Telegram> telegrams;
	private ArrayList<Device> devices;
	private ArrayList<Group> groups;
	private ArrayList<DatapointEntity> datapoints;
	
	public Project(){}
	
	public Project(int id, String name, String description){
		this(id, name, description, "");
	}
	
	public Project(int id, String name, String description, String author){
		this(id, name, description, author, Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
	}
	
	public Project(int id, String name, String description, String author, Date createDate, Date editDate){
		this.id = id;
		this.name = name;
		this.description = description;
		this.author = author;
		this.createDate = createDate;
		this.editDate = editDate;
	}
	
	public Project(Parcel parcel){
		readFromParcel(parcel);
	}
	
	public static final Parcelable.Creator<Project> CREATOR = new Parcelable.Creator<Project>() {
		public Project createFromParcel(Parcel parcel) {
			return new Project(parcel);
		}

		public Project[] newArray(int size) {
			return new Project[size];
		}
	};
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getEditDate() {
		return editDate;
	}
	
	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public ArrayList<Telegram> getTelegrams() {
		return telegrams;
	}

	public void setTelegrams(ArrayList<Telegram> telegrams) {
		this.telegrams = telegrams;
	}

	public ArrayList<Device> getDevices() {
		return devices;
	}

	public void setDevices(ArrayList<Device> devices) {
		this.devices = devices;
	}

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<Group> groups) {
		this.groups = groups;
	}

	public ArrayList<DatapointEntity> getDatapoints() {
		return datapoints;
	}

	public void setDatapoints(ArrayList<DatapointEntity> datapoints) {
		this.datapoints = datapoints;
	}
	
	

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(id);
		parcel.writeString(name);
		parcel.writeString(description);
		parcel.writeString(author);
		parcel.writeSerializable(createDate);
		parcel.writeSerializable(editDate);
	}
	
	public void readFromParcel(Parcel parcel){
		id = parcel.readInt();
		name = parcel.readString();
		description = parcel.readString();
		author = parcel.readString();
		createDate = (Date)parcel.readSerializable();
		editDate = (Date)parcel.readSerializable();
	}
}
