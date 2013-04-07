package pl.marek.knx.utils;

import android.graphics.Color;

public class ColorUtils {
	
	public static int interpolate(int startColor, int stopColor, float p) {
	    int a = calculate(Color.alpha(startColor), Color.alpha(stopColor), p);
	    int r = calculate(Color.red(startColor), Color.red(stopColor), p);
	    int g = calculate(Color.green(startColor), Color.green(stopColor), p);
	    int b = calculate(Color.blue(startColor), Color.blue(stopColor), p);
	    return Color.argb(a, r, g, b);
	}
	
	private static int calculate(int src, int dst, float p) {
	    return src + Math.round(p * (dst - src));
	}

}
