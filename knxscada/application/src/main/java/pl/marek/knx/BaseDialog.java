package pl.marek.knx;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;

public class BaseDialog extends Dialog{

	public BaseDialog(Context context) {
		super(context);
	}
	
	public BaseDialog(Context context, int theme){
		super(context, theme);
	}
	
	protected void setDialogSize(){
		Window window = getWindow();
		WindowManager windowManager = window.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		
		int rotation = display.getRotation();
		int width = metrics.widthPixels;
		
		if(getScreenType() == Configuration.SCREENLAYOUT_SIZE_XLARGE){
			if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
				width = getScaledWidth(width, 0.3f);
			}else{
				width = getScaledWidth(width, 0.5f);
			}
		}else{
			if(rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
				width = getScaledWidth(width, 0.6f);
			}else{
				width = getScaledWidth(width, 0.95f);
			}
		}

		window.setLayout(width, LayoutParams.WRAP_CONTENT);
	}
	
	private int getScaledWidth(int width, float factor){
		return (int)(width * factor);
	}
	
	private int getScreenType(){
		int screenType = getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		return screenType;
	}
	
	

}
