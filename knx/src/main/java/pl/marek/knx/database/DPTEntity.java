package pl.marek.knx.database;

import tuwien.auto.calimero.dptxlator.DPT;

public class DPTEntity  extends DPT{
	
	private String dataGroup;
	
	public DPTEntity(DPT dpt, String dataGroup){
		super(dpt.getID(),dpt.getDescription(),dpt.getLowerValue(),dpt.getUpperValue(),dpt.getUnit());
		this.dataGroup = dataGroup;
	}
	
	public DPTEntity(String typeID, String dataGroup, String description, String lower, String upper) {
		super(typeID, description, lower, upper);
		this.dataGroup = dataGroup;
	}
	
	public DPTEntity(String typeID, String dataGroup, String description, String lower, String upper, String unit){
		super(typeID, description, lower, upper, unit);
		this.dataGroup = dataGroup;
	}

	public String getDataGroup() {
		return dataGroup;
	}

	public void setDataGroup(String dataGroup) {
		this.dataGroup = dataGroup;
	}
}
