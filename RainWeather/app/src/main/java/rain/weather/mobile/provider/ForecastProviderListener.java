package rain.weather.mobile.provider;


import rain.weather.mobile.model.Forecast;

public interface ForecastProviderListener {

    public void onForecastComplete(ForecastProvider forecastProvider);
    public void onForecastError(ForecastProvider forecastProvider, ForecastProviderError error);


    public void onForecastSuccess(ForecastProvider forecastProvider, Forecast currentForecast);
}
