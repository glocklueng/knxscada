package pl.marek.knx.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.marek.knx.log.LogTags;

import android.content.Context;
import android.util.Log;

public class DateConversion {
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@SuppressWarnings("unused")
	private Context context;
	
	public DateConversion(){}
	
	public DateConversion(Context context){
		this.context = context;
	}
	
	public Date getDateFromString(String time){
		Date date = null;
		
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			Log.d(LogTags.APPLICATION, e.getMessage());
		}		
		return date;
	}
}
