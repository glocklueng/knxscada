package pl.marek.knx.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("rawtypes")
public class JSONUtil {

	@SuppressWarnings("unchecked")
	public static JSONObject convertStringToObject(String jsonString) {

		JSONObject object = new JSONObject();
		JSONParser parser = new JSONParser();

		try {
			Map json = (Map) parser.parse(jsonString, getContainerFactory());
			Iterator iter = json.entrySet().iterator();

			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				object.put(entry.getKey(), entry.getValue());
			}
		} catch (ParseException pe) {
			System.out.println(pe);
		}
		return object;
	}
	
	public static int convertJSONValueToInt(JSONObject object, String key){
		String value = object.get(key).toString();
		try{
			return Integer.parseInt(value);
		}catch(NumberFormatException ex){
			return Integer.MAX_VALUE;
		}
	}

	private static ContainerFactory getContainerFactory() {
		ContainerFactory containerFactory = new ContainerFactory() {
			public List creatArrayContainer() {
				return new LinkedList();
			}

			public Map createObjectContainer() {
				return new LinkedHashMap();
			}

		};
		return containerFactory;
	}

}
