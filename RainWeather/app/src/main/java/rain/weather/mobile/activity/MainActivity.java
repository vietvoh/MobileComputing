package rain.weather.mobile.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import rain.weather.mobile.R;
import rain.weather.mobile.RainWeatherApplication;
import rain.weather.mobile.layout.ForecastFragment;
import rain.weather.mobile.layout.LoadingFragment;
import rain.weather.mobile.model.Forecast;
import rain.weather.mobile.receiver.SyncStatusBroadcastReceiver;
import rain.weather.mobile.service.WeatherService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private RainWeatherApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (RainWeatherApplication) getApplication();
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume () {
        super.onResume();
        registerReceiver(syncReceiver, application.intents.syncBroadcastFilter());

        Forecast currentForecast = application.store.getForecast();

        if (currentForecast == null || currentForecast.isNotFresh()) {
            checkWeather(false);
        } else {
            showForecast();
            application.store.setUmbrellaNotificationDismissed(true);
            application.notifications.hideTakeUmbrella();
        }
    }

    private void checkWeather(boolean forceRefresh) {
        application.services.checkWeather(forceRefresh);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(syncReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            checkWeather(true);
            return true;
        }

        if (id == R.id.action_detail) {
            startActivity(new Intent(this, WebViewActivity.class));
            return true;
        }


        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startLoading() {
        Fragment loadingFragment = getFragmentManager().findFragmentById(R.id.frame_loading);
        if (loadingFragment == null) {
            getFragmentManager().beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.activity_main, new LoadingFragment())
                    .commit();
        }

    }

    private void showForecast() {
        ForecastFragment forecastFragment = (ForecastFragment) getFragmentManager().findFragmentById(R.id.forecast_fragment);
        if (forecastFragment == null) {
            forecastFragment = new ForecastFragment();
        }

        getFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.activity_main, forecastFragment)
                .commit();
    }

    SyncStatusBroadcastReceiver syncReceiver = new SyncStatusBroadcastReceiver() {
        @Override
        public void onSyncStatus(WeatherService.SyncStatus status) {
            if (status == WeatherService.SyncStatus.Started) {
                startLoading();
            } else if (status == WeatherService.SyncStatus.Error) {
                Toast.makeText(MainActivity.this, "Could not fetch data...", Toast.LENGTH_LONG);
            } else {
                showForecast();
            }
        }
    };
}
