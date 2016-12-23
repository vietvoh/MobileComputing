package rain.weather.mobile.service;


import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import rain.weather.mobile.RainWeatherApplication;
import rain.weather.mobile.manager.IntentManager;
import rain.weather.mobile.model.Forecast;
import rain.weather.mobile.provider.ForecastProvider;
import rain.weather.mobile.provider.ForecastProviderError;
import rain.weather.mobile.provider.ForecastProviderListener;

public class WeatherService extends Service implements ForecastProviderListener {
    private static final String TAG           = "WeatherService";
    private RainWeatherApplication application;
    private ForecastProvider forecastProvider;
    private Forecast currentForecast;

    public enum SyncStatus {
        Started,
        Working,
        Success,
        Error
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.application          = (RainWeatherApplication)getApplication();
        forecastProvider  = new ForecastProvider(this);
        forecastProvider.setListener(this);
        setStatus(SyncStatus.Started);
    }

    private void setStatus(SyncStatus status) {
        sendBroadcast(application.intents.syncBroadcast(status));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RainWeatherApplication app = (RainWeatherApplication)getApplication();

        Forecast storedForecast = app.store.getForecast();

        boolean forceRefresh = intent != null && intent.getBooleanExtra(IntentManager.EXTRA_FORCE_REFRESH, false);

        if (storedForecast == null || storedForecast.isNotFresh()) {
            app.store.setUmbrellaNotificationDismissed(false);
        }

        //app.notifications.syncWeatherNotification(); //TODO: remove only for debug

        if (storedForecast == null || storedForecast.isNotFresh() || forceRefresh) {
            Log.i(TAG, "Starting command" );
            forecastProvider.fetch();
        } else if(!forecastProvider.isRunning()) {
            Log.i(TAG, "Fresh date skipping");
            setStatus(SyncStatus.Success);
            stopSelf();
        } else {
            Log.i(TAG, "Already running...");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onForecastComplete(ForecastProvider forecastProvider) {
        stopSelf();
    }

    @Override
    public void onForecastError(ForecastProvider forecastProvider, ForecastProviderError error) {
        // show notification error here?
        setStatus(SyncStatus.Error);
    }

    @Override
    public void onForecastSuccess(ForecastProvider forecastProvider, Forecast currentForecast) {
        this.currentForecast = currentForecast;
        setStatus(SyncStatus.Success);
        showNotificationIfNeeded();
    }

    private void showNotificationIfNeeded() {
        application.store.put(currentForecast);

        if (currentForecast.isHeavyRain() && !application.store.isUmbrellaNotificationDismissed()) {
            application.notifications.showTakeUmbrella();
        } else {
            application.notifications.hideTakeUmbrella();
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Stopping service...");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
