package rain.weather.mobile.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import rain.weather.mobile.RainWeatherApplication;

public class DismissedNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "DisNotificationReceiver";

    public DismissedNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Dismissed take umbrella notification");
        RainWeatherApplication app = (RainWeatherApplication)context.getApplicationContext();
        app.store.setUmbrellaNotificationDismissed(true);
    }
}
