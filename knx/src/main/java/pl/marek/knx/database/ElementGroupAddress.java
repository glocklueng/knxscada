package pl.marek.knx.database;

import pl.marek.knx.R;
import android.content.Context;

public class ElementGroupAddress {
	
	private int elementId;
	private String address;
	private ElementGroupAddressType type;
	
	public ElementGroupAddress(){}
	
	public ElementGroupAddress(int elementId, String address, ElementGroupAddressType type){
		this.elementId = elementId;
		this.address = address;
		this.type = type;
	}

	public int getElementId() {
		return elementId;
	}

	public void setElementId(int elementId) {
		this.elementId = elementId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ElementGroupAddressType getType() {
		return type;
	}

	public void setType(ElementGroupAddressType type) {
		this.type = type;
	}



	public enum ElementGroupAddressType{
		MAIN {
			@Override
			public String getLabel(Context context) {
				return context.getString(R.string.dialog_controller_group_address_default_label);
			}
		},
		OTHER {
			@Override
			public String getLabel(Context context) {
				return context.getString(R.string.dialog_controller_group_address_other_label);
			}
		};
		
		public abstract String getLabel(Context context);
	}

}
