package pl.marek.knx;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {

	public static void showLongToast(Context context, String message) {
		showToast(context, message, Toast.LENGTH_LONG, -1);
	}

	public static void showLongToast(Context context, String message,
			int imageId) {
		showToast(context, message, Toast.LENGTH_LONG, imageId);
	}

	public static void showShortToast(Context context, String message) {
		showToast(context, message, Toast.LENGTH_SHORT, -1);
	}

	public static void showShortToast(Context context, String message,
			int imageId) {
		showToast(context, message, Toast.LENGTH_SHORT, imageId);
	}

	private static void showToast(Context context, String message,
			int duration, int imageId) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.custom_toast, null);
		TextView text = (TextView) layout.findViewById(R.id.toast_message);
		text.setText(message);

		createImage(layout, imageId);

		Toast toast = new Toast(context);
		toast.setGravity(Gravity.BOTTOM, 0, 20);
		toast.setDuration(duration);
		toast.setView(layout);
		toast.show();
	}

	private static void createImage(View layout, int imageId) {
		if (imageId != -1) {
			ImageView imageView = (ImageView) layout.findViewById(R.id.toast_image);
			if (imageView != null) {
				imageView.setImageResource(imageId);
			}
		}
	}
}