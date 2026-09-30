package com.example.smartpantry;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// Shared screen helpers so every Activity behaves the same way.
public final class NavHelper {

    private NavHelper() {
    }

    // Connects the bottom bar (Pantry / Recipes / Settings) to the three main screens.
    // FLAG_ACTIVITY_REORDER_TO_FRONT re-uses a screen that is already open
    // instead of stacking a new copy every time a tab is tapped.
    public static void setupBottomNav(Activity activity, BottomNavigationView nav, int currentItemId) {
        nav.setSelectedItemId(currentItemId);          // highlight this screen's tab
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == currentItemId) {
                return true;                           // already on this screen
            }
            Class<?> target;
            if (id == R.id.nav_pantry) {
                target = PantryListActivity.class;
            } else if (id == R.id.nav_recipes) {
                target = SuggestedRecipesActivity.class;
            } else if (id == R.id.nav_settings) {
                target = SettingsActivity.class;
            } else {
                return false;
            }
            Intent intent = new Intent(activity, target);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            activity.startActivity(intent);
            activity.overridePendingTransition(0, 0);  // switch tabs without a slide animation
            return true;
        });
    }

    // New Android versions draw apps "edge to edge" (behind the status bar).
    // This adds padding so our content isn't hidden under the status/navigation bars.
    // padBottom = false on screens with a bottom bar, because the bar pads itself.
    public static void applySystemBarPadding(View root, boolean padBottom) {
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, padBottom ? bars.bottom : 0);
            return insets;
        });
    }
}