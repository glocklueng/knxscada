package pl.marek.knx.database;

import java.util.Date;

public class DatapointEntity {
	
	private String groupAddress;
	private String dptId;
	private String state;
	private Date modifyDate;
	
	public DatapointEntity(){}
	
	public DatapointEntity(String groupAddress, String dptId, String state, Date modifyDate){
		this.groupAddress = groupAddress;
		this.dptId = dptId;
		this.state = state;
		this.modifyDate = modifyDate;
	}

	public String getGroupAddress() {
		return groupAddress;
	}

	public void setGroupAddress(String groupAddress) {
		this.groupAddress = groupAddress;
	}

	public String getDptId() {
		return dptId;
	}

	public void setDptId(String dptId) {
		this.dptId = dptId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
}
