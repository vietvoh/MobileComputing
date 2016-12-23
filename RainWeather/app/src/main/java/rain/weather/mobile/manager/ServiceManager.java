package rain.weather.mobile.manager;


import android.location.Location;

import rain.weather.mobile.RainWeatherApplication;

public class ServiceManager {
    private final RainWeatherApplication application;

    public ServiceManager(RainWeatherApplication application) {
        this.application = application;
    }

    public void checkWeather(boolean forceRefresh) {
        application.startService(application.intents.checkWeatherService(forceRefresh));
    }
}
