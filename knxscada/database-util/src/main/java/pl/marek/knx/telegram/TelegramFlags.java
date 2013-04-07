package pl.marek.knx.telegram;

import android.os.Parcel;
import android.os.Parcelable;

public class TelegramFlags implements Parcelable{
	
    private boolean ack;
    private boolean confirmation;
    private boolean repeated;
    
    public TelegramFlags(){
    	ack = false;
    	confirmation = false;
    	repeated = false;
    }
    
    public TelegramFlags(Parcel parcel){
    	boolean[] values = new boolean[3];
    	parcel.readBooleanArray(values);
    	ack = values[0];
    	confirmation = values[1];
    	repeated = values[2];
    }
    
    public TelegramFlags(boolean ack, boolean confirmation, boolean repeated){
    	this.ack = ack;
    	this.confirmation = confirmation;
    	this.repeated = repeated;
    }
    
	public static final Parcelable.Creator<TelegramFlags> CREATOR = new Parcelable.Creator<TelegramFlags>() {
		public TelegramFlags createFromParcel(Parcel parcel) {
			return new TelegramFlags(parcel);
		}

		public TelegramFlags[] newArray(int size) {
			return new TelegramFlags[size];
		}
	};

	public boolean isAck() {
		return ack;
	}

	public void setAck(boolean ack) {
		this.ack = ack;
	}

	public boolean isConfirmation() {
		return confirmation;
	}

	public void setConfirmation(boolean confirmation) {
		this.confirmation = confirmation;
	}

	public boolean isRepeated() {
		return repeated;
	}

	public void setRepeated(boolean repeated) {
		this.repeated = repeated;
	}
	
	@Override
	public String toString(){
		return String.format(
                "ACK: %s\n" +
                "Confirm: %s\n" +
                "Repeated: %s",
				ack, 
                confirmation, 
                repeated);
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeBooleanArray(new boolean[]{ack, confirmation, repeated});
	}
}
