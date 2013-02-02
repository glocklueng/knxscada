package pl.marek.knx.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class IntegerEditTextPreference extends EditTextPreference {

    public IntegerEditTextPreference(Context context) {
        super(context); 
    }

    public IntegerEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntegerEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected View onCreateView(ViewGroup parent) {
    	setPreferenceSummary(getPersistedString(null));
    	return super.onCreateView(parent);
    }
    
    @Override
    public void onClick(DialogInterface dialog, int which) {
    	super.onClick(dialog, which);
    	if(DialogInterface.BUTTON_POSITIVE == which){
    		 setPreferenceSummary(getEditText().getText().toString());
    	}    	
    }
    
    @Override
    protected void onBindDialogView(View view) {
    	super.onBindDialogView(view);
    	String value = getPersistedString(null);
    	getEditText().setText(value);
    	getEditText().setSelection(value.length());
    }
    
    @Override
    protected String getPersistedString(String defaultReturnValue) {
    	SharedPreferences pref = getSharedPreferences();
    	int defaultValue = -1;
    	try{
    		defaultValue = Integer.parseInt(defaultReturnValue);
    	} catch(NumberFormatException ex){}
    	
        int port = pref.getInt(this.getKey(), defaultValue);
    	return String.valueOf(port);
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
    
    private void setPreferenceSummary(String value){
    	if(getKey().contains("timeout")){
    		value = value + "s.";
    	}
    	setSummary(value);
    }
}