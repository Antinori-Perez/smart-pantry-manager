package com.example.smartpantry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.adapter.PantryAdapter;
import com.example.smartpantry.data.PantryDataSource;
import com.example.smartpantry.logic.DateUtils;
import com.example.smartpantry.model.PantryItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class PantryListActivity extends AppCompatActivity implements PantryAdapter.OnPantryItemListener {

    private PantryDataSource dataSource;
    private PantryAdapter adapter;
    private TextView tvEmpty;
    private TextView tvExpiryBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);
        NavHelper.applySystemBarPadding(findViewById(R.id.main), false);

        dataSource = new PantryDataSource(this);
        tvEmpty = findViewById(R.id.tvEmptyPantry);
        tvExpiryBanner = findViewById(R.id.tvExpiryBanner);

        RecyclerView rvPantry = findViewById(R.id.rvPantry);
        rvPantry.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(this);
        rvPantry.setAdapter(adapter);

        Button btnAdd = findViewById(R.id.btnAddItem);
        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddEditIngredientActivity.class)));   // no id sent = new item

        NavHelper.setupBottomNav(this, findViewById(R.id.bottomNav), R.id.nav_pantry);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadPantry();
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.getMenu().findItem(R.id.nav_pantry).setChecked(true);
    }

    private void loadPantry() {
        boolean alertsOn = AppPrefs.expiryAlertsOn(this);
        List<PantryItem> items = dataSource.getAllItems(AppPrefs.sortByExpiry(this));
        adapter.setItems(items, alertsOn);

        tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);

        int expiringCount = 0;
        long today = DateUtils.startOfToday();
        for (PantryItem item : items) {
            if (item.hasExpiryDate()) {
                long days = DateUtils.daysUntil(item.getExpiryDate(), today);
                if (days >= 0 && days <= AppPrefs.EXPIRING_SOON_DAYS) {
                    expiringCount++;
                }
            }
        }
        if (alertsOn && expiringCount > 0) {
            tvExpiryBanner.setText(getResources().getQuantityString(
                    R.plurals.expiring_banner, expiringCount, expiringCount));
            tvExpiryBanner.setVisibility(View.VISIBLE);
        } else {
            tvExpiryBanner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClicked(PantryItem item) {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(getString(R.string.delete_message, item.getName()))
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    if (dataSource.deleteItem(item.getId())) {
                        Toast.makeText(this, getString(R.string.deleted, item.getName()), Toast.LENGTH_SHORT).show();
                        loadPantry();
                    } else {
                        Toast.makeText(this, R.string.error_delete, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}