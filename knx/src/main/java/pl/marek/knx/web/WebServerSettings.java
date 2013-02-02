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
		return preferences.getInt(getStringByResId(R.string.webserver_port_key), getIntByResId(R.integer.webserver_port_default_value));
	}
	
	public void setPort(int port){
		setInt(getStringByResId(R.string.webserver_port_key), port);
	}
	
	public int getSSLPort(){
		return preferences.getInt(getStringByResId(R.string.webserver_ssl_port_key), getIntByResId(R.integer.webserver_ssl_port_default_value));
	}
	
	public void setSSLPort(int port){
		setInt(getStringByResId(R.string.webserver_ssl_port_key), port);
	}
	
	public boolean isUseSSL(){
		return preferences.getBoolean(getStringByResId(R.string.use_ssl_key), getBoolByResId(R.bool.use_ssl_default_value));
	}
	
	public void setUseSSL(boolean useSSL){
		setBoolean(getStringByResId(R.string.use_ssl_key), useSSL);
	}
	
}
