package pl.marek.knx.utils;

import org.apache.wicket.model.Model;

public class IconUtil {
	
	public static String getSubLayerIconPath(String name){
		return getIconPath(name, "rooms/");
	}
	
	public static String getLayerIconPath(String name){
		return getIconPath(name, "floors/");
	}
	
	public static String getIconPath(String name, String parent){
		String path = "";
		String relativePath = "images/" + parent; 
		String ext = "png";
		if(name != null){
			path = relativePath + name + "." + ext;
		}
		return path;
	}
	
	public static StaticImage getApplicationLogo(){
		StaticImage image =  new StaticImage("appLogo", new Model<String>("images/logo.png"));
		return image;
	}

}
