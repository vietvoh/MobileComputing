package rain.weather.mobile.manager;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;

import rain.weather.mobile.RainWeatherApplication;
import rain.weather.mobile.activity.MainActivity;
import rain.weather.mobile.receiver.DismissedNotificationReceiver;
import rain.weather.mobile.receiver.WeatherReceiver;
import rain.weather.mobile.service.WeatherService;

public class IntentManager {
    private final RainWeatherApplication application;
    public final static String EXTRA_FORCE_REFRESH         = "rain.weather.mobile.ForceRefresh";
    public final static String EXTRA_SYNC_STATUS         = "rain.weather.mobile.SyncStatus";
    public static final String BROADCAST_ACTION_FINISHED_SYNCING = "rain.weather.mobile.BROADCAST_ACTION_SYNC";
    public final static String EXTRA_DISMISS = "rain.weather.mobile.Dismiss";

    public IntentManager(RainWeatherApplication application) {
        this.application = application;
    }

    public Intent checkWeatherService(boolean forceRefresh) {
        Intent intent = new Intent(application, WeatherService.class);
        intent.putExtra(EXTRA_FORCE_REFRESH, forceRefresh);
        return intent;
    }

    public Intent syncBroadcast(WeatherService.SyncStatus syncStatus) {
        Intent intent = new Intent(BROADCAST_ACTION_FINISHED_SYNCING);
        intent.putExtra(EXTRA_SYNC_STATUS, syncStatus);
        return intent;
    }

    public IntentFilter syncBroadcastFilter() {
        return new IntentFilter(BROADCAST_ACTION_FINISHED_SYNCING);
    }

    public PendingIntent checkWeatherReceiver() {
        Intent intent = new Intent(application, WeatherReceiver.class);
        return PendingIntent.getBroadcast(application, 0, intent, 0);
    }

    public PendingIntent showMainActivity(boolean dismissTakeUmbrella) {
        Intent intent = new Intent(application, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra(EXTRA_DISMISS, dismissTakeUmbrella);
        return PendingIntent.getActivity(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public PendingIntent dismissNotificationReceiver() {
        Intent intent = new Intent(application, DismissedNotificationReceiver.class);
        return PendingIntent.getBroadcast(application, 0, intent, 0);
    }
}
