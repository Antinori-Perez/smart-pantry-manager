package com.example.smartpantry.logic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Small helpers for expiry dates
public final class DateUtils {

    public static final long ONE_DAY_MS = 24L * 60 * 60 * 1000;

    private DateUtils() {
    }

    // Midnight at the start of today, in milliseconds
    public static long startOfToday() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    // Midnight of a given date (month starts at 0, like the DatePicker)
    public static long toMillis(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(year, month, day);
        return c.getTimeInMillis();
    }

    // Whole days from today until expiry: 0 = today, negative = already expired
    public static long daysUntil(long expiryMillis, long todayMillis) {
        return Math.round((expiryMillis - todayMillis) / (double) ONE_DAY_MS);
    }

    // e.g. "30 Sep 2026"
    public static String format(long millis) {
        return new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(millis));
    }
}