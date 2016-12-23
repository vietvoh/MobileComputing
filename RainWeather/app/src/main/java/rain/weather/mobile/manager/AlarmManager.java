package rain.weather.mobile.manager;


import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import java.util.Calendar;

import rain.weather.mobile.RainWeatherApplication;

public class AlarmManager {
    private static final String TAG = "AlarmsManager";
    private final RainWeatherApplication application;
    private android.app.AlarmManager manager;

    public AlarmManager(RainWeatherApplication application) {
        this.application = application;
        this.manager     = (android.app.AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
    }

    public void setup() {
        Log.i(TAG, "Setup alarms");
        Log.d(TAG, "Refresh Every: " + application.store.getHumanReadableSynceEvery());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7); // Read this from config etc...

        PendingIntent refreshIntent = application.intents.checkWeatherReceiver();

        manager.cancel(refreshIntent);
        manager.setInexactRepeating(android.app.AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), application.store.getSyncEveryInMiliseconds(), refreshIntent);
    }
}
