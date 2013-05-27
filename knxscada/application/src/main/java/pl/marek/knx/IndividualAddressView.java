package pl.marek.knx;

import tuwien.auto.calimero.IndividualAddress;
import tuwien.auto.calimero.exception.KNXFormatException;
import android.content.Context;
import android.util.AttributeSet;

public class IndividualAddressView extends GroupAddressView{
	
	public IndividualAddressView(Context context) {
		super(context);
		setSeparator(".");
	}
	
	public IndividualAddressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setSeparator(".");
	}
	
	public IndividualAddressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setSeparator(".");
	}
	
	public String getIndividualAddress(){
		return getGroupAddress();
	}
	
	public void setIndividualAddress(String address){
		setGroupAddress(address);
	}
	
	public boolean isCorrectIndividualAddress(){
		try{
			new IndividualAddress(getIndividualAddress());
			return true;
		}catch(KNXFormatException ex){
			return false;
		}	
	}
}
