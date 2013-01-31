package pl.marek.knx.telegram;

import android.os.Parcel;
import android.os.Parcelable;

@Deprecated
public class KNXAddresses implements Parcelable{
	
    private String sourceAddr;
    private String sourceName;
    private String destinationAddr;
    private String destinationName;
    
    public KNXAddresses(Parcel parcel){
    	sourceAddr = parcel.readString();
    	sourceName = parcel.readString();
    	destinationAddr = parcel.readString();
    	destinationAddr = parcel.readString();
    }
    
    public KNXAddresses(String sourceAddr, String destinationAddr){
    	this.sourceAddr = sourceAddr;
    	this.sourceName = sourceAddr;
    	this.destinationAddr = destinationAddr;
    	this.destinationName = destinationAddr;
    }
    
    public KNXAddresses(String sourceAddr, String sourceName, String destinationAddr, String destinationName){
    	this.sourceAddr = sourceAddr;
    	this.sourceName = sourceName;
    	this.destinationAddr = destinationAddr;
    	this.destinationName = destinationName;
    }
    
	public static final Parcelable.Creator<KNXAddresses> CREATOR = new Parcelable.Creator<KNXAddresses>() {
		public KNXAddresses createFromParcel(Parcel parcel) {
			return new KNXAddresses(parcel);
		}

		public KNXAddresses[] newArray(int size) {
			return new KNXAddresses[size];
		}
	};

	public String getSourceAddr() {
		return sourceAddr;
	}

	public void setSourceAddr(String sourceAddr) {
		this.sourceAddr = sourceAddr;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getDestinationAddr() {
		return destinationAddr;
	}

	public void setDestinationAddr(String destinationAddr) {
		this.destinationAddr = destinationAddr;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	
	@Override
	public String toString(){
		return String.format(
                  "Source Address: %s\n"
                + "Source Name: %s\n"
                + "Destination Address: %s\n"
                + "Destination Name: %s",
                sourceAddr, 
                sourceName, 
                destinationAddr, 
                destinationName);
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(sourceAddr);
		parcel.writeString(sourceName);
		parcel.writeString(destinationAddr);
		parcel.writeString(destinationName);
	}
}
