package rain.weather.mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.LocationAjaxCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import rain.weather.mobile.R;
import rain.weather.mobile.model.Forecast;
import rain.weather.mobile.provider.ForecastProviderError;
import rain.weather.mobile.provider.ForecastProviderListener;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "ForecastProvider";
    private Context context;
    private AQuery query;
    private ForecastProviderListener listener;
    private boolean running;
    private LocationAjaxCallback locationCallback;

    private static final long LOCATION_TIMEOUT    = 40 * 1000;
    private static final float LOCATION_ACCURACY  = 100;
    private static final float LOCATION_TOLERANCE = 10;
    private Location currentLocation;
    private static final String EXTRA_URL = "rain.weather.mobile.ExtraUrl";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        this.context = getApplicationContext();
        this.query   = new AQuery(context);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl("http://google.com");
        webView.setWebViewClient(new WebViewClient(){

//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
//                view.loadUrl(request.getUrl().toString());
//                return true;
//            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
    }
    @Override
    protected void onResume () {
        super.onResume();
        fetch();
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.weather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == R.id.current_weather) {
            Intent intent =  new Intent(this, WebViewActivity.class);
            intent.putExtra(EXTRA_URL, "current");
            startActivity(intent);
            return true;
        }

        if (id == R.id.hourly_forecast) {
            Intent intent =  new Intent(this, WebViewActivity.class);
            intent.putExtra(EXTRA_URL, "hourly");
            startActivity(intent);
            return true;
        }

        if (id == R.id.extended_forecast) {
            Intent intent =  new Intent(this, WebViewActivity.class);
            intent.putExtra(EXTRA_URL, "extended");
            startActivity(intent);
            return true;
        }
        if (id == R.id.uv_index_forecast) {
            Intent intent =  new Intent(this, WebViewActivity.class);
            intent.putExtra(EXTRA_URL, "uvindex");
            startActivity(intent);
            return true;
        }
        if (id == R.id.day_night) {
            Intent intent =  new Intent(this, WebViewActivity.class);
            intent.putExtra(EXTRA_URL, "daily");
            startActivity(intent);
            return true;
        }
        if (id == R.id.three_day_forecast) {
            Intent intent =  new Intent(this, WebViewActivity.class);
            intent.putExtra(EXTRA_URL, "3day");
            startActivity(intent);
            return true;
        }
        if (id == R.id.weekend_forecast) {
            Intent intent =  new Intent(this, WebViewActivity.class);
            intent.putExtra(EXTRA_URL, "weekend");
            startActivity(intent);
            return true;
        }
        if (id == R.id.month_outlook) {
            Intent intent =  new Intent(this, WebViewActivity.class);
            intent.putExtra(EXTRA_URL, "month12");
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void fetch() {
        if (this.running) {
            return;
        }
        this.running = true;

        this.locationCallback = new LocationAjaxCallback();
        locationCallback.weakHandler(this, "locationFoundCallback").timeout(LOCATION_TIMEOUT).accuracy(LOCATION_ACCURACY).iteration(2).tolerance(LOCATION_TOLERANCE).async(context);
    }

    public void locationFoundCallback(String url, Location loc, AjaxStatus status) {
        locationCallback.stop();
        if (loc == null) {
            Log.d(TAG, "Location not found!");
        } else {
            Log.i(TAG, "Located user " + loc.toString());
            currentLocation = loc;
            fetchWeatherForecast();
        }
    }

    private void fetchWeatherForecast() {
        String url;
        url = "http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=RnzWGmSa74tkxQGVO7fQXXWjv3bATqMK&q="+currentLocation.getLatitude()+","+currentLocation.getLongitude();
        Log.d(TAG, "GET: "+url);
        query.ajax(url, JSONObject.class, this, "forecastCallback");
    }

    public void forecastCallback(String url, JSONObject forecast, AjaxStatus status) {
        if (forecast == null) {
            Log.e(TAG, "forecast not found!");
        } else {
            Log.v(TAG, "Retrived forecast data: " + forecast.toString());
            try {
                String key = forecast.getString("Key");
                WebView webView = (WebView) findViewById(R.id.webView);
                String urlToView = "http://m.accuweather.com/location/select/";
                urlToView+= key;
                Intent intent = getIntent();
                String extraUrl = intent.getStringExtra(EXTRA_URL);
                if (extraUrl == null) {
                    extraUrl = "hourly";
                }
                urlToView += "?rn="+extraUrl;
                webView.loadUrl(urlToView);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
