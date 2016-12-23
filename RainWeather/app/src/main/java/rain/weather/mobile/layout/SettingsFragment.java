package rain.weather.mobile.layout;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;

import rain.weather.mobile.R;
import rain.weather.mobile.RainWeatherApplication;
import rain.weather.mobile.manager.StoreManager;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "SettingsFragment";
    private RainWeatherApplication app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (RainWeatherApplication)getActivity().getApplication();
        addPreferencesFromResource(R.xml.settings);

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        PreferenceScreen screen         = getPreferenceScreen();
        Preference syncEveryPreference  = screen.findPreference(StoreManager.KEY_SYNC_EVERY);
        syncEveryPreference.setSummary(app.store.getHumanReadableSynceEvery());
    }

    @Override
    public void onDestroy() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "Key changed: " + key);
        updateUI();
        app.alarms.setup();
    }
}
