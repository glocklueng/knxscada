package pl.marek.knx.utils;

import pl.marek.knx.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;

public class MessageDialog {
	
	private Context context;
	
	public MessageDialog(Context context){
		this.context = context;
	}
	
	public void showDialog(String title, String msg, Drawable icon){
		AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialogConfirmTheme);
		builder.setTitle(title);
		if(icon != null)
			builder.setIcon(icon);
		builder.setMessage(msg);
		builder.setPositiveButton(context.getString(android.R.string.ok), new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}
	
	
	

}
