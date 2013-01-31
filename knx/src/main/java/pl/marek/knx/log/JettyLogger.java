package pl.marek.knx.log;

import org.eclipse.jetty.util.log.Logger;
import android.util.Log;

public class JettyLogger implements Logger {
	public static final String JETTY_TAG = "Jetty";
	public static boolean isIgnoredEnabled = false;
	public String name;

	public JettyLogger() {
		this("pl.marek.knx.utils.AndroidLogger");
	}

	public JettyLogger(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void debug(Throwable th) {
		if (Log.isLoggable(JETTY_TAG, Log.DEBUG)) {
			Log.d(JETTY_TAG, "", th);
		}
	}

	public void debug(String msg, Throwable th) {
		if (Log.isLoggable(JETTY_TAG, Log.DEBUG)) {
			Log.d(JETTY_TAG, msg, th);
		}
	}

	public void debug(String msg, Object... args) {
		if (Log.isLoggable(JETTY_TAG, Log.DEBUG)) {
			Log.d(JETTY_TAG, msg);
		}
	}

	public Logger getLogger(String name) {
		return new JettyLogger(name);
	}

	public void info(String msg, Object... args) {
		Log.i(JETTY_TAG, msg);
	}

	public void info(Throwable th) {
		Log.i(JETTY_TAG, "", th);
	}

	public void info(String msg, Throwable th) {
		Log.i(JETTY_TAG, msg, th);
	}

	public boolean isDebugEnabled() {
		return Log.isLoggable(JETTY_TAG, Log.DEBUG);
	}

	public void warn(Throwable th) {
		if (Log.isLoggable(JETTY_TAG, Log.WARN))
			Log.e(JETTY_TAG, "", th);
	}

	public void warn(String msg, Object... args) {
		if (Log.isLoggable(JETTY_TAG, Log.WARN))
			Log.w(JETTY_TAG, msg);
	}

	public void warn(String msg, Throwable th) {
		if (Log.isLoggable(JETTY_TAG, Log.ERROR))
			Log.e(JETTY_TAG, msg, th);
	}

	public boolean isIgnoredEnabled() {
		return isIgnoredEnabled;
	}

	public void ignore(Throwable ignored) {
		if (isIgnoredEnabled)
			Log.w(JETTY_TAG, "IGNORED", ignored);
	}

	public void setIgnoredEnabled(boolean enabled) {
		isIgnoredEnabled = enabled;
	}

	public void setDebugEnabled(boolean enabled) {}
}
