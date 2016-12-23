package rain.weather.mobile;

import android.app.Application;

import rain.weather.mobile.manager.AlarmManager;
import rain.weather.mobile.manager.IntentManager;
import rain.weather.mobile.manager.NotificationManager;
import rain.weather.mobile.manager.ServiceManager;
import rain.weather.mobile.manager.StoreManager;

public class RainWeatherApplication extends Application{
    public ServiceManager services;
    public IntentManager intents;
    public StoreManager store;
    public AlarmManager alarms;
    public NotificationManager notifications;


    @Override
    public void onCreate() {
        super.onCreate();
        services      = new ServiceManager(this);
        intents       = new IntentManager(this);
        store         = new StoreManager(this);
        alarms        = new AlarmManager(this);
        notifications = new NotificationManager(this);

        alarms.setup();
    }
}
