package pl.marek.knx.web;

import pl.marek.knx.R;
import pl.marek.knx.utils.ApplicationSettings;
import android.content.Context;
import android.preference.PreferenceManager;

public class WebServerSettings extends ApplicationSettings{
		
	public WebServerSettings(Context context){
		this.context = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public int getPort(){
		return preferences.getInt(getStringByResId(R.string.webserver_port), getIntByResId(R.string.webserver_port_default_value));
	}
	
	public void setPort(int port){
		setInt(getStringByResId(R.string.webserver_port), port);
	}
	
	public int getSSLPort(){
		return preferences.getInt(getStringByResId(R.string.webserver_ssl_port), getIntByResId(R.string.webserver_ssl_port_default_value));
	}
	
	public void setSSLPort(int port){
		setInt(getStringByResId(R.string.webserver_ssl_port), port);
	}
	
	public boolean isUseSSL(){
		return preferences.getBoolean(getStringByResId(R.string.use_ssl), getBoolByResId(R.string.use_ssl_default_value));
	}
	
	public void setUseSSL(boolean useSSL){
		setBoolean(getStringByResId(R.string.use_ssl), useSSL);
	}
	
}
