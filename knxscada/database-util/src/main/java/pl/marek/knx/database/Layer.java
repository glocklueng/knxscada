package pl.marek.knx.database;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Layer implements Parcelable, Serializable{
	
	private static final long serialVersionUID = 1L;

	public static final String LAYER = "LAYER";
	public static final String MAIN_LAYER = "MAIN_LAYER";
	
	protected int id;
	protected int projectId;
	protected String name;
	protected String description;
	protected String icon;
	private ArrayList<SubLayer> subLayers;
	
	public Layer(){
		id = 0;
		projectId = 0;
		name = "";
		description = "";
		icon = "";
	}
	
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
		if(name == null){
			name = "";
		}
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		if(description == null){
			description = "";
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		if(icon == null){
			icon = "";
		}
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public ArrayList<SubLayer> getSubLayers() {
		return subLayers;
	}

	public void setSubLayers(ArrayList<SubLayer> subLayers) {
		this.subLayers = subLayers;
	}
	
	public boolean isMainLayer(){
		if(name.equals(MAIN_LAYER)){
			return true;
		}
		return false;
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
