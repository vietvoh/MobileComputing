package rain.weather.mobile.model;


import java.util.Date;

public class RainData {
    private float volume;
    private Date at;
    private float temperature;
    private int humidity = 0;

    public RainData() {

    }

    public Date getAt() {
        return at;
    }

    public void setAt(Date at) {
        this.at = at;
    }

    public void setAt(int t) {
        at = new Date();
        at.setTime((long)t*1000);
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public boolean isRaining() {
        return volume > 0;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature - 273.15f; // Convert to kelvins
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
