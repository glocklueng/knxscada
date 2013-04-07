package pl.marek.knx.telegram;

import android.os.Parcel;
import android.os.Parcelable;
import tuwien.auto.calimero.dptxlator.DPT;

@Deprecated
public class DPTParcel extends DPT implements Parcelable{

	public DPTParcel(Parcel p){
		super(p.readString(), p.readString(), p.readString(), p.readString(), p.readString());
	}
	
	public DPTParcel(DPT dpt){
		super(dpt.getID(),dpt.getDescription(),dpt.getLowerValue(),dpt.getUpperValue(),dpt.getUnit());
	}
	
	public DPTParcel(String typeID, String description, String lower, String upper) {
		super(typeID, description, lower, upper);
	}
	
	public DPTParcel(String typeID, String description, String lower, String upper, String unit){
		super(typeID, description, lower, upper, unit);
	}
	
	public static final Parcelable.Creator<DPTParcel> CREATOR = new Parcelable.Creator<DPTParcel>() {
		public DPTParcel createFromParcel(Parcel parcel) {
			return new DPTParcel(parcel);
		}

		public DPTParcel[] newArray(int size) {
			return new DPTParcel[size];
		}
	};

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(getID());
		parcel.writeString(getDescription());
		parcel.writeString(getLowerValue());
		parcel.writeString(getUpperValue());
		parcel.writeString(getUnit());
	}

}
