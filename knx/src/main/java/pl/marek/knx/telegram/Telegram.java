package pl.marek.knx.telegram;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import pl.marek.knx.utils.DataRepresentation;

public class Telegram implements Parcelable{
	
	public static final String TELEGRAM = "TELEGRAM";
	
	private long id;
    private Date time;
    private String priority;
    private String sourceAddress;
    private String destinationAddress;
    private byte hopcount;
    private String type;
    private String msgCode;
    private byte[] rawdata;
    private String data;    
    private byte[] rawframe;
    private int frameLength;
    private String dptId;
    private TelegramFlags flags;
    
	public static final Parcelable.Creator<Telegram> CREATOR = new Parcelable.Creator<Telegram>() {
		public Telegram createFromParcel(Parcel parcel) {
			return new Telegram(parcel);
		}

		public Telegram[] newArray(int size) {
			return new Telegram[size];
		}
	};
	
	public Telegram(){
		
	}
	
	public Telegram(Parcel parcel){
		readFromParcel(parcel);
	}
    
    public Telegram(Date time, String priority, String sourceAddress, String destinationAddress,
            byte hopcount, String type, String msgCode,  byte[] rawdata, String data, String dptId, 
            TelegramFlags flags, byte[] rawframe, int frameLength){
        this.time = time;
        this.priority = priority; 
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.hopcount = hopcount; 
        this.type = type;
        this.msgCode = msgCode;
        this.rawdata = rawdata;
        this.data = data;
        this.rawframe = rawframe;
        this.frameLength = frameLength;
        this.dptId = dptId;
        this.flags = flags;
    }
    
    public Telegram(long id, Date time, String priority,  String sourceAddress, String destinationAddress,
            byte hopcount, String type, String msgCode,  byte[] rawdata, String data, String dptId, 
            TelegramFlags flags, byte[] rawframe, int frameLength){
        this(time, priority, sourceAddress, destinationAddress, hopcount,type,msgCode,rawdata,data,dptId,flags,rawframe,frameLength);
        this.id = id;
    }
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public byte getHopcount() {
		return hopcount;
	}

	public void setHopcount(byte hopcount) {
		this.hopcount = hopcount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public byte[] getRawdata() {
		return rawdata;
	}

	public void setRawdata(byte[] rawdata) {
		this.rawdata = rawdata;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public byte[] getRawframe() {
		return rawframe;
	}

	public void setRawframe(byte[] rawframe) {
		this.rawframe = rawframe;
	}

	public int getFrameLength() {
		return frameLength;
	}

	public void setFrameLength(int frameLength) {
		this.frameLength = frameLength;
	}

	public String getDptId() {
		return dptId;
	}

	public void setDptId(String dptId) {
		this.dptId = dptId;
	}

	public TelegramFlags getFlags() {
		return flags;
	}

	public void setFlags(TelegramFlags flags) {
		this.flags = flags;
	}

	@Override
    public String toString(){
        return String.format(
                  "Time: %s\n"
                + "Priority: %s\n"
                + "Source Address: %s\n"
                + "Destination Address: %s\n"
                + "HOP Count: %s\n"
                + "Type: %s\n"
                + "Message Code: %s\n"
                + "Raw Data: %s\n"
                + "Data: %s\n"
                + "DPT ID: %s\n"
                + "%s\n"
                + "Raw Frame: %s\n"
                + "FrameLength: %s\n",
                time.toString(), 
                priority, 
                sourceAddress,
                destinationAddress,
                hopcount, 
                type, 
                msgCode, 
                DataRepresentation.byteArrayToHexString(rawdata), 
                data,
                dptId,
                flags, 
                DataRepresentation.byteArrayToHexString(rawframe), 
                frameLength);  
    }

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel parcel, int parcelFlags) {
	    parcel.writeLong(id);
		parcel.writeSerializable(time);
	    parcel.writeString(priority);
	    parcel.writeString(sourceAddress);
	    parcel.writeString(destinationAddress);
	    parcel.writeByte(hopcount);
	    parcel.writeString(type);
	    parcel.writeString(msgCode);
	    parcel.writeInt(rawdata.length);
	    parcel.writeByteArray(rawdata);
	    parcel.writeString(data);
	    parcel.writeInt(frameLength);
	    parcel.writeByteArray(rawframe);
	    parcel.writeString(dptId);
	    parcel.writeParcelable(flags, parcelFlags);
	}
	
	private void readFromParcel(Parcel parcel){
		id = parcel.readLong();
		time = (Date)parcel.readSerializable();
		priority = parcel.readString();
		sourceAddress = parcel.readString();
		destinationAddress = parcel.readString();
		hopcount = parcel.readByte();
		type = parcel.readString();
		msgCode = parcel.readString();
		int dataLength = parcel.readInt();
		rawdata = new byte[dataLength];
		parcel.readByteArray(rawdata);
		data = parcel.readString();
		frameLength = parcel.readInt();
		rawframe = new byte[frameLength];
		parcel.readByteArray(rawframe);
		dptId = parcel.readString();
		flags = parcel.readParcelable(TelegramFlags.class.getClassLoader());
	}
}