package pl.marek.knx.interfaces;

public interface KNXDataTransceiver {
	public static final String READ_DATA = "pl.marek.knx.read_data";
	public static final String WRITE_DATA = "pl.marek.knx.write_data";
	public static final String GROUP_ADDRESS = "pl.marek.knx.group_address";
	public static final String DPT_IP = "pl.marek.knx.dpt_id";
	public static final String VALUE = "pl.marek.knx.value";
	
	public void readData(String groupAddress, String dptId);
	public void writeData(String groupAddress, String dptId, String value);
}
