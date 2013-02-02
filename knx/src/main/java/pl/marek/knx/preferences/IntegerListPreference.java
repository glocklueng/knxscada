package pl.marek.knx.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class IntegerListPreference extends ListPreference{

	public IntegerListPreference(Context context) {
		super(context);
	}
	
	public IntegerListPreference(Context context, AttributeSet attributeSet){
		super(context, attributeSet);
	}
	@Override
	protected View onCreateView(ViewGroup parent) {
		setSummary(getEntry());
		return super.onCreateView(parent);
	}
		
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		setSummary(getEntry());
	}
	
	
    @Override
    protected void onBindDialogView(View view) {
    	super.onBindDialogView(view);
    	
    	String value = getPersistedString(null);
    	setValueIndex(Integer.parseInt(value));
    }
	
    @Override
    protected boolean persistString(String value) {
    	int val = -1;
    	try{
    		val = Integer.valueOf(value);
    	    return persistInt(val);
    	} catch(NumberFormatException ex){
    		//TODO Wyświetlić jakiś dialog z błędem czy coś
    	}
        return false;
    }
    
    @Override
    protected String getPersistedString(String defaultReturnValue) {
    	SharedPreferences pref = getSharedPreferences();
    	int defaultValue = -1;
    	try{
    		defaultValue = Integer.parseInt(defaultReturnValue);
    	} catch(NumberFormatException ex){}
    	
        int mode = pref.getInt(this.getKey(), defaultValue);
    	return String.valueOf(mode);
    }
}