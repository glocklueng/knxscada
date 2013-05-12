package pl.marek.knx.preferences;

import pl.marek.knx.utils.PasswordUtil;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

public class EncryptedEditTextPreference extends EditTextPreference {
	  public EncryptedEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	  }

	  public EncryptedEditTextPreference(Context context, AttributeSet attrs) {
	    super(context, attrs);
	  }

	  public EncryptedEditTextPreference(Context context) {
	    super(context);
	  }

	  @Override
	  public String getText() {
	    return "";
	  }

	  @Override
	  protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
	    super.setText(restoreValue ? getPersistedString(null) : (String) defaultValue);
	  }

	  @Override
	  public void setText(String text) {
		  if(text.isEmpty()){
			  super.setText("");
		  }else{
			  String password = PasswordUtil.encryptPassword(text);
			  super.setText(password);
		  }
	  }
}