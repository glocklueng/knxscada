package pl.marek.knx.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PasswordUtil {

	public static String encryptPassword(String password) {
		String sha1 = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(password.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			Log.d(e.getClass().getName(), e.getMessage());
		} catch (UnsupportedEncodingException e) {
			Log.d(e.getClass().getName(), e.getMessage());
		}
		return sha1;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	
	public static String getStoredPassword(Context context, String preferenceFile){
		String password = "";
		SharedPreferences preferences = context.getSharedPreferences(preferenceFile, Context.MODE_MULTI_PROCESS);
	    password = preferences.getString("webapp_password", "");
		return password;
	}
}