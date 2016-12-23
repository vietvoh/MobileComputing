package rain.weather.mobile.provider;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import rain.weather.mobile.model.Forecast;

public class ForecastProvider {
    private static final String TAG = "ForecastProvider";
    private final Context context;
    private final AQuery query;
    private ForecastProviderListener listener;
    private boolean running;
    private LocationAjaxCallback locationCallback;

    private Forecast currentForecast;

    private static final long LOCATION_TIMEOUT    = 40 * 1000;
    private static final float LOCATION_ACCURACY  = 100;
    private static final float LOCATION_TOLERANCE = 10;
    private Location currentLocation;
    private String currentCity;

    public ForecastProvider(Context context) {
        this.context = context;
        this.query   = new AQuery(context);
        Log.d(TAG, "Initialized...");
    }

    public void fetch() {
//        if (this.running) {
//            return;
//        }
        this.running = true;

        this.locationCallback = new LocationAjaxCallback();
        locationCallback.weakHandler(this, "locationFoundCallback").timeout(LOCATION_TIMEOUT).accuracy(LOCATION_ACCURACY).iteration(2).tolerance(LOCATION_TOLERANCE).async(context);
    }

    public void locationFoundCallback(String url, Location loc, AjaxStatus status) {
        locationCallback.stop();
        if (loc == null) {
            Log.d(TAG, "Location not found!");
            error(new ForecastProviderError("Location not found"));
        } else {
            Log.i(TAG, "Located user " + loc.toString());
            currentLocation = loc;
            fetchWeatherForecast();
        }
    }

    private void fetchWeatherForecast() {
        Log.i(TAG, "Fetching current city...");
        currentCity  = null;
        String url   = null;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
            if (addresses.size() > 0) {
                currentCity = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (currentCity == null) {
            Log.i(TAG, "Fetching forecast for current location");
            url = "http://api.openweathermap.org/data/2.5/forecast?cnt=1&type=accurace&lat="+currentLocation.getLatitude()+"&lon="+currentLocation.getLongitude();
        } else {
            Log.i(TAG, "Fetching forecast for current city: " + currentCity);
            url = "http://api.openweathermap.org/data/2.5/forecast?cnt=1&q="+ Uri.encode(currentCity);
        }
        url += "&APPID=625da67d1dd43239781566f5573b144b";
        Log.d(TAG, "GET: "+url);
        query.ajax(url, JSONObject.class, this, "forecastCallback");
    }

    public void forecastCallback(String url, JSONObject forecast, AjaxStatus status) {
        if (forecast == null) {
            Log.e(TAG, "forecast not found!");
            error(new ForecastProviderError("Forecast not found"));
        } else {
            Log.v(TAG, "Retrived forecast data: " + forecast.toString());
            currentForecast = new Forecast();
            currentForecast.parse(forecast);
            currentForecast.setCityIfEmpty(currentCity);
            success();
        }
    }

    private void success() {
        Log.i(TAG, "Success!");
        listener.onForecastSuccess(this, currentForecast);
        complete();
    }

    private void error(ForecastProviderError error) {
        running = false;
        Log.e(TAG, error.getMessage());
        listener.onForecastError(this, error);
        complete();
    }

    private void complete() {
        listener.onForecastComplete(this);
    }

    public Forecast getCurrentForecast() {
        return currentForecast;
    }

    public ForecastProviderListener getListener() {
        return listener;
    }

    public void setListener(ForecastProviderListener listener) {
        this.listener = listener;
    }

    public boolean isRunning() {
        return running;
    }
}
