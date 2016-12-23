package rain.weather.mobile.model;


import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Forecast {
    private static final String TAG = "Forecast";
    private ArrayList<RainData> data = new ArrayList<RainData>();
    private Date fromDate;
    private Date toDate;
    private Date lastUpdate;
    private String city;
    private float totalRainVolume;
    public String getCity() {
        return city;
    }
    public Date getLastUpdate() {
        return lastUpdate;
    }
    public ArrayList<RainData> getRainData() {
        return data;
    }

    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    public boolean isHeavyRain() {
        return totalRainVolume > 0.0;
    }

    public void parse(JSONObject object) {
        data.clear();
        totalRainVolume = 0;
        fromDate        = null;
        toDate          = null;
        lastUpdate      = new Date();
        try {
            getAllInfo(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAllInfo(JSONObject root) throws JSONException {
        JSONObject cityJson = root.getJSONObject("city");
        this.city           = cityJson.getString("name");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentDay    = calendar.get(Calendar.DAY_OF_YEAR);

        JSONArray listJSON  = root.getJSONArray("list");
        for(int i = 0; i < Math.min(listJSON.length(), 8); i++) {
            RainData rainData      = new RainData();

            JSONObject rawRainData = listJSON.getJSONObject(i);
            rainData.setAt(rawRainData.getInt("dt"));
            calendar.setTime(rainData.getAt());

            //if (calendar.get(Calendar.DAY_OF_YEAR) == currentDay) {
                if (rawRainData.has("rain")) {
                    JSONObject rain = rawRainData.getJSONObject("rain");
                    rainData.setVolume((float)rain.getDouble("3h"));
                } else {
                    rainData.setVolume(0);
                }

                if (rawRainData.has("main")) {
                    JSONObject main = rawRainData.getJSONObject("main");
                    rainData.setTemperature((float)main.getDouble("temp"));
                    rainData.setHumidity(main.getInt("humidity"));
                } else {
                    rainData.setTemperature(0);
                    rainData.setHumidity(0);
                }

                totalRainVolume += rainData.getVolume();

                if (fromDate == null || rainData.getAt().before(fromDate)) {
                    fromDate = rainData.getAt();
                }

                if (toDate == null || rainData.getAt().after(toDate)) {
                    toDate   = rainData.getAt();
                }

                data.add(rainData);
//            } else {
//                //Log.i(TAG, "Date: "+currentDay+" != "+calendar.get(Calendar.DAY_OF_YEAR));
//            }
        }

        calendar.setTime(fromDate);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        fromDate = calendar.getTime();

        calendar.setTime(toDate);

        calendar.set(Calendar.HOUR, calendar.getActualMaximum(Calendar.HOUR));
        calendar.set(Calendar.MINUTE, calendar.getActualMaximum(Calendar.MINUTE));
        toDate = calendar.getTime();

        Log.i(TAG, "From: "+fromDate.toString()+" to "+toDate.toString());
    }

    public float getTemperature() {
        Date currentDate = new Date();

        for(RainData rd : data) {
            if(currentDate.before(rd.getAt())) {
                return rd.getTemperature();
            }
        }
        return 0.0f;
    }

    public boolean isFresh() {
        Date currentDate = new Date();
        if (fromDate == null || toDate == null)
            return false;
        return currentDate.after(fromDate) && currentDate.before(toDate);
    }

    public boolean isNotFresh() {
        return !isFresh();
    }
    public void setCityIfEmpty(String currentCity) {
        if (city == null || city.length() <= 1) {
            city = currentCity;
        }
    }
}
