package rain.weather.mobile.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import rain.weather.mobile.manager.IntentManager;
import rain.weather.mobile.service.WeatherService;

public abstract class SyncStatusBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WeatherService.SyncStatus status  = (WeatherService.SyncStatus)intent.getSerializableExtra(IntentManager.EXTRA_SYNC_STATUS);
        onSyncStatus(status);
    }

    public abstract void onSyncStatus(WeatherService.SyncStatus status);
}
