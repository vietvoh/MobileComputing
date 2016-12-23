package rain.weather.mobile.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import rain.weather.mobile.RainWeatherApplication;

public class WeatherReceiver extends BroadcastReceiver {
    private static final String TAG = "CheckWeatherReceiver";

    public WeatherReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Recived trigger from: " + intent.getAction());
        RainWeatherApplication application = (RainWeatherApplication)context.getApplicationContext();
        application.services.checkWeather(true);
    }
}
