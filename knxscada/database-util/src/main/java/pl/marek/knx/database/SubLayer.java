package pl.marek.knx.database;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class SubLayer extends Layer implements Parcelable{
	
	private static final long serialVersionUID = 1L;

	public static final String SUBLAYER = "SUBLAYER";
	
	protected int layerId;
	protected String backgroundImage;
	private ArrayList<Element> elements;
	
	public SubLayer(){}
	
	public SubLayer(int projectId, int layerId, String name, String description, String icon, String backgroundImage){
		super(projectId, name, description, icon);
		this.layerId = layerId;
		this.backgroundImage = backgroundImage;
	}
	
	public SubLayer(int id, int projectId, int layerId, String name, String description, String icon, String backgroundImage){
		this(projectId, layerId, name, description, icon, backgroundImage);
		this.id = id;
	}
	
	public SubLayer(Parcel parcel){
		readFromParcel(parcel);
	}
	
	public static final Parcelable.Creator<SubLayer> CREATOR = new Parcelable.Creator<SubLayer>() {
		public SubLayer createFromParcel(Parcel parcel) {
			return new SubLayer(parcel);
		}

		public SubLayer[] newArray(int size) {
			return new SubLayer[size];
		}
	};

	public int getLayerId() {
		return layerId;
	}

	public void setLayerId(int layerId) {
		this.layerId = layerId;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public ArrayList<Element> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Element> elements) {
		this.elements = elements;
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
		parcel.writeString(name);
		parcel.writeString(description);
		parcel.writeString(icon);
		parcel.writeString(backgroundImage);
	}
	
	public void readFromParcel(Parcel parcel){
		id = parcel.readInt();
		projectId = parcel.readInt();
		layerId = parcel.readInt();
		name = parcel.readString();
		description = parcel.readString();
		icon = parcel.readString();
		backgroundImage = parcel.readString();
	}
}