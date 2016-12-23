package rain.weather.mobile.provider;


public class ForecastProviderError extends Exception {

    public ForecastProviderError(String detailMessage) {
        super(detailMessage, null);
    }
}
