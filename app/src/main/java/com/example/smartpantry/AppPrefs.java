package com.example.smartpantry;

import android.content.Context;
import android.content.SharedPreferences;

// Settings saved with SharedPreferences (small key/value pairs that survive app restarts).
// Keeping the keys in one class avoids typos like "expiryAlerts" vs "expiry_alerts".
public final class AppPrefs {

    private static final String PREFS_NAME = "smart_pantry_settings";

    public static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    public static final String KEY_SHOW_ALMOST_THERE = "show_almost_there";
    public static final String KEY_SORT_BY_EXPIRY = "sort_by_expiry";

    // An item counts as "expiring soon" if it expires within this many days
    public static final int EXPIRING_SOON_DAYS = 3;

    private AppPrefs() {
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static boolean expiryAlertsOn(Context context) {
        return prefs(context).getBoolean(KEY_EXPIRY_ALERTS, true);
    }

    public static boolean showAlmostThere(Context context) {
        return prefs(context).getBoolean(KEY_SHOW_ALMOST_THERE, true);
    }

    public static boolean sortByExpiry(Context context) {
        return prefs(context).getBoolean(KEY_SORT_BY_EXPIRY, false);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        prefs(context).edit().putBoolean(key, value).apply();   // apply() saves in the background
    }
}