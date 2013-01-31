package pl.marek.knx.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ApplicationSettings {
	
	protected SharedPreferences preferences;
	protected Context context;
	
	protected void setString(String name, String value){
		Editor editor = preferences.edit();
		editor.putString(name, value);
		editor.commit();
	}
	
	protected void setInt(String name, int value){
		Editor editor = preferences.edit();
		editor.putInt(name, value);
		editor.commit();
	}
	
	protected void setBoolean(String name, boolean value){
		Editor editor = preferences.edit();
		editor.putBoolean(name, value);
		editor.commit();
	}
	
	protected String getStringByResId(int id){
		return context.getString(id);
	}
	
	protected int getIntByResId(int id){
		return Integer.valueOf(context.getString(id));
	}
	
	protected boolean getBoolByResId(int id){
		return Boolean.valueOf(context.getString(id));
	}
}
