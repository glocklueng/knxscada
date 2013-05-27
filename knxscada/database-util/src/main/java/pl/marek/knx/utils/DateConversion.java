package pl.marek.knx.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class DateConversion {
	
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT = "yyyy-MM-dd";
		
	public static Date getDateFromString(String time){
		Date date = null;
		
		DateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			Log.d("", e.getMessage());
		}		
		return date;
	}
	
	public static String getDateTimeString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_FORMAT);
		String d = sdf.format(date);
		return d;
	}
	
	public static String getDateString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String d = sdf.format(date);
		return d;
	}
}