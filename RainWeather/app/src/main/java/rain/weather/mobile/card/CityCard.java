package rain.weather.mobile.card;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import it.gmariotti.cardslib.library.internal.Card;
import rain.weather.mobile.R;
import rain.weather.mobile.model.Forecast;

public class CityCard extends Card {
    private final static char DEGREES_SYMBOL = (char) 0x00B0;
    private final Forecast forecast;
    private TextView cityTextView;
    private TextView temperatureTextView;

    public CityCard(Context context, Forecast forecast) {
        super(context, R.layout.city_card_layout);
        this.forecast = forecast;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
        cityTextView          = (TextView) view.findViewById(R.id.cityTextView);
        temperatureTextView   = (TextView) view.findViewById(R.id.temperatureTextView);

        Log.d(TAG, "Current city is: " + forecast.getCity());

        cityTextView.setText(forecast.getCity());
        temperatureTextView.setText(String.format("%.02f "+ DEGREES_SYMBOL + "C", forecast.getTemperature()));

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        ((TextView)view.findViewById(R.id.updatedAtTextView)).setText(format.format(forecast.getLastUpdate()));
    }
}
