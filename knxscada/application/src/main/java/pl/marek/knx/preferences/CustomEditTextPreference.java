package pl.marek.knx.preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CustomEditTextPreference extends EditTextPreference{
    
	public CustomEditTextPreference(Context context) {
        super(context); 
    }

    public CustomEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    protected View onCreateView(ViewGroup parent) {
    	setSummary(getPersistedString(null));
    	return super.onCreateView(parent);
    }
    
    @Override
    protected void onBindDialogView(View view) {
    	super.onBindDialogView(view);
    	String value = getPersistedString(null);
    	getEditText().setText(value);
    	getEditText().setSelection(value.length());
    }
    
    @Override
    public void onClick(DialogInterface dialog, int which) {
    	super.onClick(dialog, which);
    	if(DialogInterface.BUTTON_POSITIVE == which){
    		 setSummary(getEditText().getText().toString());
    	}    	
    }
}
