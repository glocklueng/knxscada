package pl.marek.knx.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import android.util.Log;

public class NetworkInformator {
	
	public static ArrayList<NetworkInterface> getActiveNetworkInterfaces(){
		ArrayList<NetworkInterface> interfaces = new ArrayList<NetworkInterface>();
	    try {
	    	Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
	    	
	        while(en.hasMoreElements()) {
	            NetworkInterface intf = en.nextElement();
	            if(!intf.isLoopback() && intf.isUp()){
	            	interfaces.add(intf);
		        }
	        }
	    } catch (SocketException ex) {
	        Log.d(ex.getClass().getName(), ex.getMessage());
	    }
	    return interfaces;
	}
	
	public static NetworkInterface getNetworkInterfaceByIP(String ip){
		for(NetworkInterface intf: getActiveNetworkInterfaces()){
			Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
			while(enumIpAddr.hasMoreElements()) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (inetAddress.getHostAddress().equals(ip)) {
                	return intf;
                }
            }
		}
		return null;
	}
	
	public static String getIPAddress(){
		String ip = "0.0.0.0";
		for(NetworkInterface intf: getActiveNetworkInterfaces()){
			Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
			while(enumIpAddr.hasMoreElements()) {
                InetAddress inetAddress = enumIpAddr.nextElement();
                if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                	ip = inetAddress.getHostAddress();
                	break;
                }
            }
		}
		return ip;
	}
}
