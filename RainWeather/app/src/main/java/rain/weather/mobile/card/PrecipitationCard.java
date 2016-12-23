package rain.weather.mobile.card;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.prototypes.CardWithList;
import rain.weather.mobile.R;
import rain.weather.mobile.model.Forecast;
import rain.weather.mobile.model.RainData;

public class PrecipitationCard extends CardWithList {
    private final Forecast currentForecast;

    public PrecipitationCard(Context context, Forecast currentForecast) {
        super(context);
        this.currentForecast = currentForecast;
        init();
    }

    @Override
    protected CardHeader initCardHeader() {
        CardHeader cardHeader = new CardHeader(getContext());
        cardHeader.setTitle(getContext().getString(R.string.precipitation_card_title));
        return cardHeader;
    }

    @Override
    protected void initCard() {

    }

    @Override
    protected List<ListObject> initChildren() {
        ArrayList<ListObject> objects = new ArrayList<ListObject>();

        for (RainData rd : currentForecast.getRainData()) {
            PrecipitationListObject listObject = new PrecipitationListObject(this, rd);
            objects.add(listObject);
        }

        return objects;
    }

    @Override
    public View setupChildView(int i, ListObject listObject, View view, ViewGroup viewGroup) {
        PrecipitationListObject item  = (PrecipitationListObject)listObject;
        SimpleDateFormat format       = new SimpleDateFormat("hh:mm a");
        ((TextView)view.findViewById(R.id.timeTextView)).setText(format.format(item.data.getAt()));
        ((TextView)view.findViewById(R.id.humidityTextView)).setText("Humidity: "+ item.data.getHumidity()+" %");

        if (item.data.isRaining()) {
            ((ImageView)view.findViewById(R.id.weatherImageView)).setImageResource(R.drawable.status_raining);
        } else {
            ((ImageView)view.findViewById(R.id.weatherImageView)).setImageResource(R.drawable.status_sun);
        }

        return view;
    }

    @Override
    public int getChildLayoutId() {
        return R.layout.card_precipitation_list_view;
    }

    public class PrecipitationListObject extends DefaultListObject {
        private final RainData data;

        public PrecipitationListObject(Card parentCard, RainData data) {
            super(parentCard);
            this.data = data;
        }
    }
}
