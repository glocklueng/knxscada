package pl.marek.knx.database;

import android.os.Parcel;
import android.os.Parcelable;

public class Layer implements Parcelable{
	
	public static final String PROJECT = "PROJECT";
	
	private int id;
	private int projectId;
	private String name;
	private String description;
	private String icon;
	
	public Layer(){}
	
	public Layer(int projectId, String name, String description, String icon){
		this.projectId = projectId;
		this.name = name;
		this.description = description;
		this.icon = icon;
	}
	
	public Layer(int id, int projectId, String name, String description, String icon){
		this(projectId, name, description, icon);
		this.id = id;
	}
	
	public Layer(Parcel parcel){
		readFromParcel(parcel);
	}
	
	public static final Parcelable.Creator<Layer> CREATOR = new Parcelable.Creator<Layer>() {
		public Layer createFromParcel(Parcel parcel) {
			return new Layer(parcel);
		}

		public Layer[] newArray(int size) {
			return new Layer[size];
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(id);
		parcel.writeInt(projectId);
		parcel.writeString(name);
		parcel.writeString(description);
		parcel.writeString(icon);
	}
	
	public void readFromParcel(Parcel parcel){
		id = parcel.readInt();
		projectId = parcel.readInt();
		name = parcel.readString();
		description = parcel.readString();
		icon = parcel.readString();
	}
}
