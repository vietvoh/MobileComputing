package rain.weather.mobile.manager;


import android.app.Notification;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

import rain.weather.mobile.R;
import rain.weather.mobile.RainWeatherApplication;

public class NotificationManager {
    private static final int NOTIFICATION_COLOR = 0x3498db;
    private static final int NOTIFICATION_TAKE_UMBRELLA_ID = 69;
    private final Context context;
    private final android.app.NotificationManager manager;
    //private final Uri notificationSound;
    private final RainWeatherApplication app;

    public NotificationManager(Context context) {
        this.app       = (RainWeatherApplication)context.getApplicationContext();
        this.context   = context;
        this.manager   = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //this.notificationSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://macbury.umbrella/raw/"+R.raw.notification);
    }

    public void showTakeUmbrella() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(context.getString(R.string.notification_title));
        mBuilder.setContentText(context.getString(R.string.notification_message_rain));
        mBuilder.setLights(NOTIFICATION_COLOR, 500, 100);
        //mBuilder.setSound(notificationSound);
        mBuilder.setAutoCancel(true);
        mBuilder.setSmallIcon(R.drawable.ic_stat_umbrella);
        mBuilder.setContentIntent(app.intents.showMainActivity(true));
        mBuilder.setDeleteIntent(app.intents.dismissNotificationReceiver());

        Notification notification = mBuilder.build();
        manager.notify(NOTIFICATION_TAKE_UMBRELLA_ID, notification);
    }

    public void syncWeatherNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("synced");
        Date date = new Date();
        mBuilder.setContentText(date.toString());
        mBuilder.setAutoCancel(true);

        Notification notification = mBuilder.build();
        manager.notify((int)date.getTime(), notification);
    }

    public void hideTakeUmbrella() {
        manager.cancel(NOTIFICATION_TAKE_UMBRELLA_ID);
    }
}
