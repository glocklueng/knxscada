package pl.marek.knx;

import pl.marek.knx.preferences.SettingsActivity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class NotificationManager {

	private Context context;

	public NotificationManager(Context context) {
		this.context = context;
	}

	public Notification createConnectionServiceNotification() {

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.knx_logo)
				.setContentTitle(context.getString(R.string.connection_service_title))
				.setContentText(context.getString(R.string.connection_service_message));
		Intent resultIntent = new Intent(context, SettingsActivity.class);

		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mBuilder.setContentIntent(resultPendingIntent);
		return mBuilder.build();
	}

	public Notification createWebServerServiceNotification() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.www_logo)
				.setContentTitle(context.getString(R.string.webserver_service_title))
				.setContentText(context.getString(R.string.webserver_service_message));
		Intent resultIntent = new Intent(context, SettingsActivity.class);
		
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mBuilder.setContentIntent(resultPendingIntent);
		return mBuilder.build();
	}

	public void showExceptionNotification(Exception e) {
		Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
	}
}