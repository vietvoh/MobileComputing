package rain.weather.mobile.layout;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import it.gmariotti.cardslib.library.view.CardView;
import rain.weather.mobile.R;
import rain.weather.mobile.RainWeatherApplication;
import rain.weather.mobile.card.CityCard;
import rain.weather.mobile.card.PrecipitationCard;
import rain.weather.mobile.model.Forecast;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {
    private ScrollView scrollView;
    private CardView cityCardView;
    private CardView chanceOfRain;

    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast,
                container, false);
//        CardView cityCardView = (CardView)view.findViewById(R.id.city_card_container_view);
//        cityCardView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
//                startActivity(browserIntent);
//                //startActivity(new Intent(this, SettingsActivity.class));
//            }
//        });
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        scrollView        = (ScrollView)activity.findViewById(R.id.card_scrollview);
        cityCardView      = (CardView)activity.findViewById(R.id.city_card_container_view);
        chanceOfRain      = (CardView)activity.findViewById(R.id.chance_of_rain_container_view);
    }


    @Override
    public void onResume() {
        super.onResume();
        loadForecast();
    }

    public void loadForecast() {
        RainWeatherApplication app = (RainWeatherApplication)getActivity().getApplication();

        Forecast currentForecast = app.store.getForecast();

        if (currentForecast != null) {
            cityCardView.setCard(new CityCard(getActivity(), currentForecast));
            chanceOfRain.setCard(new PrecipitationCard(getActivity(), currentForecast));
        }
    }
}
