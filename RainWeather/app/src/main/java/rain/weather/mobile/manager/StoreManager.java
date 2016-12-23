package rain.weather.mobile.manager;


import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import rain.weather.mobile.R;
import rain.weather.mobile.RainWeatherApplication;
import rain.weather.mobile.model.Forecast;

public class StoreManager {
    private static final String TAG                                     = "StoreManager";
//    private final RainWeatherApplication application;
private static final String FORECAST_STORE                          = "FORECAST_STORE";
    public  static final String KEY_SYNC_EVERY                          = "sync_every";
    private static final String KEY_FORECAST                            = "KEY_FORECAST";
    private static final String KEY_VERSION                             = "KEY_VERSION";
    private static final String KEY_DISMISSED_NOTIFICATION_UMBRELLA     = "KEY_DISMISSED_NOTIFICATION_UMBRELLA";


    private final Context context;
    private final SharedPreferences preferences;
    private final SharedPreferences settings;

    public StoreManager(Context context) {
        this.context = context;
        preferences  = context.getSharedPreferences(FORECAST_STORE, Context.MODE_PRIVATE);
        settings     = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void put(Forecast forecast) {
        preferences.edit().putString(KEY_FORECAST, forecast.toJSON()).commit();
    }

    public void setUmbrellaNotificationDismissed(boolean dismissed) {
        Log.v(TAG, "Umbrella Notification Dismissed="+dismissed);
        preferences.edit().putBoolean(KEY_DISMISSED_NOTIFICATION_UMBRELLA, dismissed).commit();
    }

    public boolean isUmbrellaNotificationDismissed() {
        return false;
        //return preferences.getBoolean(KEY_DISMISSED_NOTIFICATION_UMBRELLA, false);
    }

    public Forecast getForecast() {
        Forecast forecast = null;
        if (preferences.contains(KEY_FORECAST)) {
            String forecastJSON = preferences.getString(KEY_FORECAST, null);
            if (forecastJSON != null) {
                Gson g   = new Gson();
                forecast = g.fromJson(forecastJSON, Forecast.class);
            }
        }
        return forecast;
    }

    public long getSyncEveryInMiliseconds() {
        String defaultVal = context.getResources().getString(R.string.refresh_rate_hour);
        String rawMili    = settings.getString(KEY_SYNC_EVERY, defaultVal);
        return rawMili.equals(defaultVal) ? AlarmManager.INTERVAL_HOUR : Integer.parseInt(rawMili);
    }

    public String getHumanReadableSynceEvery() {
        String labels[]     = context.getResources().getStringArray(R.array.refresh_rate_labels);
        String values[]     = context.getResources().getStringArray(R.array.refresh_rate_values);
        long currentVal     = getSyncEveryInMiliseconds();
        for(int i = 0; i < labels.length; i++) {
            if (Long.parseLong(values[i]) == currentVal) {
                return labels[i];
            }
        }

        return "Error";
    }
}
