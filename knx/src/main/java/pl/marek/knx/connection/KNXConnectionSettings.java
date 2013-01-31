package pl.marek.knx.connection;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.preference.PreferenceManager;

import pl.marek.knx.R;
import pl.marek.knx.exceptions.WrongParameterException;
import pl.marek.knx.utils.ApplicationSettings;
import pl.marek.knx.utils.NetworkInformator;

import tuwien.auto.calimero.IndividualAddress;
import tuwien.auto.calimero.exception.KNXFormatException;
import tuwien.auto.calimero.link.KNXNetworkLinkIP;
import tuwien.auto.calimero.link.medium.KNXMediumSettings;
import tuwien.auto.calimero.link.medium.TPSettings;

public class KNXConnectionSettings extends ApplicationSettings{
		
    public KNXConnectionSettings(Context context){
    	this.context = context;
    	preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
	public int getServiceMode() {
		return preferences.getInt(getStringByResId(R.string.service_mode),
								  getIntByResId(R.string.service_mode_default_value));
	}

	public void setServiceMode(int serviceMode) throws WrongParameterException{
		if(serviceMode == KNXNetworkLinkIP.TUNNEL || 
		   serviceMode == KNXNetworkLinkIP.ROUTER){
			setInt(getStringByResId(R.string.service_mode), serviceMode);
		} else{
			throw new WrongParameterException();
		}
	}

	public InetSocketAddress getLocalEndPoint() throws UnknownHostException {
		String ip = preferences.getString(getStringByResId(R.string.local_ip), getStringByResId(R.string.local_ip_default_value));
		int port = preferences.getInt(getStringByResId(R.string.local_port), getIntByResId(R.string.local_port_default_value));
		if(ip.equals(getStringByResId(R.string.local_ip_default_value))){
			ip = NetworkInformator.getIPAddress();
		}
		InetSocketAddress address = new InetSocketAddress(InetAddress.getByName(ip), port);
		return address;
	}
	
	public void setLocalEndpoint(String ip, int port) throws UnknownHostException{
		InetSocketAddress address = new InetSocketAddress(InetAddress.getByName(ip), port);
		setString(getStringByResId(R.string.local_ip), address.getAddress().getHostAddress());
		setInt(getStringByResId(R.string.local_port), address.getPort());
	}

	public InetSocketAddress getRemoteEndPoint() throws UnknownHostException {
		String ip = preferences.getString(getStringByResId(R.string.remote_ip), getStringByResId(R.string.remote_ip_default_value));
		int port = preferences.getInt(getStringByResId(R.string.remote_port), getIntByResId(R.string.remote_port_default_value));
		InetSocketAddress address = new InetSocketAddress(InetAddress.getByName(ip), port);
		return address;
	}

	public void setRemoteEndPoint(String ip, int port) throws UnknownHostException {
		InetSocketAddress address = new InetSocketAddress(InetAddress.getByName(ip), port);
		setString(getStringByResId(R.string.remote_ip), address.getAddress().getHostAddress());
		setInt(getStringByResId(R.string.remote_port), address.getPort());
	}

	public boolean isUseNAT() {
		return preferences.getBoolean(getStringByResId(R.string.use_nat), getBoolByResId(R.string.use_nat_default_value));
	}

	public void setUseNAT(boolean useNAT) {
		setBoolean(getStringByResId(R.string.use_nat), useNAT);
	}

	public IndividualAddress getIndividualAddress() throws KNXFormatException {
		return new IndividualAddress(preferences.getString(getStringByResId(R.string.individual_address), getStringByResId(R.string.individual_address_default_value)));
	}

	public void setIndividualAddress(IndividualAddress individualAddress) {
		setString(getStringByResId(R.string.individual_address), individualAddress.toString());
	}

	public boolean isUseTP1() {
		return preferences.getBoolean(getStringByResId(R.string.use_tp1), getBoolByResId(R.string.use_tp1_default_value));
	}

	public void setUseTP1(boolean useTP1) {
		setBoolean(getStringByResId(R.string.use_tp1), useTP1);
	}

	public int getTimeout() {
		return preferences.getInt(getStringByResId(R.string.timeout), getIntByResId(R.string.timeout_default_value));
	}

	public void setTimeout(int timeout) {
		setInt(getStringByResId(R.string.timeout), timeout);
	}

	public KNXMediumSettings getMediumSettings() throws KNXFormatException {
		return new TPSettings(getIndividualAddress(), isUseTP1());
	}
	
	public boolean isDiscoverWhileConnecting(){
		return preferences.getBoolean(getStringByResId(R.string.discover_while_connecting), getBoolByResId(R.string.discover_while_connecting_default_value));
	}
	
	public void setDiscoverWhileConnecting(boolean discover){
		setBoolean(getStringByResId(R.string.discover_while_connecting), discover);
	}
	
	public int getDiscoverTimeout(){
		return preferences.getInt(getStringByResId(R.string.discover_timeout), getIntByResId(R.string.discover_timeout_default_value));
	}
	
	public void setDiscoverTimeout(int timeout){
		setInt(getStringByResId(R.string.discover_timeout), timeout);
	}
}
