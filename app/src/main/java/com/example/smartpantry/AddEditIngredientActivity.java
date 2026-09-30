package com.example.smartpantry;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.adapter.PantryAdapter;
import com.example.smartpantry.data.PantryDataSource;
import com.example.smartpantry.logic.DateUtils;
import com.example.smartpantry.logic.IngredientNormalizer;
import com.example.smartpantry.model.PantryItem;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "com.example.smartpantry.EXTRA_ITEM_ID";
    private static final String STATE_EXPIRY = "state_expiry";

    private static final double MAX_QUANTITY = 100000;
    private static final int MAX_NAME_LENGTH = 40;

    private PantryDataSource dataSource;
    private PantryItem currentItem;          // id -1 = new item

    private EditText etName;
    private EditText etQuantity;
    private Spinner spUnit;
    private TextView tvExpiry;
    private Button btnClearExpiry;
    private List<String> units;

    private Long selectedExpiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);
        NavHelper.applySystemBarPadding(findViewById(R.id.main), true);

        dataSource = new PantryDataSource(this);
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        spUnit = findViewById(R.id.spUnit);
        tvExpiry = findViewById(R.id.tvExpiry);
        btnClearExpiry = findViewById(R.id.btnClearExpiry);
        Button btnPickExpiry = findViewById(R.id.btnPickExpiry);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnDelete = findViewById(R.id.btnDelete);
        TextView tvTitle = findViewById(R.id.tvTitle);


        units = Arrays.asList(getResources().getStringArray(R.array.units));
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUnit.setAdapter(unitAdapter);

        long itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            currentItem = dataSource.getItemById(itemId);
        }
        if (currentItem != null && savedInstanceState == null) {
            fillForm(currentItem);
        }
        if (currentItem == null) {
            currentItem = new PantryItem();   // new item, id = -1
        }

        boolean editing = currentItem.getId() != -1;
        tvTitle.setText(editing ? R.string.title_edit_ingredient : R.string.title_add_ingredient);
        btnDelete.setVisibility(editing ? View.VISIBLE : View.GONE);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_EXPIRY)) {
            selectedExpiry = savedInstanceState.getLong(STATE_EXPIRY);
        }
        showExpiry();

        btnPickExpiry.setOnClickListener(v -> showDatePicker());
        btnClearExpiry.setOnClickListener(v -> {
            selectedExpiry = null;
            showExpiry();
        });
        btnSave.setOnClickListener(v -> saveItem());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedExpiry != null) {
            outState.putLong(STATE_EXPIRY, selectedExpiry);
        }
    }

    private void fillForm(PantryItem item) {
        etName.setText(item.getName());
        etQuantity.setText(PantryAdapter.formatQuantity(item.getQuantity()));
        int unitIndex = units.indexOf(item.getUnit());
        spUnit.setSelection(Math.max(unitIndex, 0));
        selectedExpiry = item.getExpiryDate();
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        if (selectedExpiry != null) {
            c.setTimeInMillis(selectedExpiry);
        }
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, day) -> {
                    selectedExpiry = DateUtils.toMillis(year, month, day);
                    showExpiry();
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        // Boundary rule: you can't pick a date before today
        dialog.getDatePicker().setMinDate(DateUtils.startOfToday());
        dialog.show();
    }

    private void showExpiry() {
        if (selectedExpiry == null) {
            tvExpiry.setText(R.string.no_expiry);
            btnClearExpiry.setVisibility(View.GONE);
        } else {
            tvExpiry.setText(getString(R.string.expires_on, DateUtils.format(selectedExpiry)));
            btnClearExpiry.setVisibility(View.VISIBLE);
        }
    }

    private boolean validate() {
        boolean valid = true;
        String name = etName.getText().toString().trim();
        String qtyText = etQuantity.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError(getString(R.string.error_name_required));
            valid = false;
        } else if (name.length() > MAX_NAME_LENGTH) {
            etName.setError(getString(R.string.error_name_too_long, MAX_NAME_LENGTH));
            valid = false;
        } else if (!name.matches("[A-Za-z][A-Za-z '\\-]*")) {
            etName.setError(getString(R.string.error_name_letters));
            valid = false;
        } else if (isDuplicateName(name)) {
            etName.setError(getString(R.string.error_name_duplicate));
            valid = false;
        }

        if (qtyText.isEmpty()) {
            etQuantity.setError(getString(R.string.error_quantity_required));
            valid = false;
        } else {
            try {
                double qty = Double.parseDouble(qtyText);
                if (qty <= 0) {
                    etQuantity.setError(getString(R.string.error_quantity_positive));
                    valid = false;
                } else if (qty > MAX_QUANTITY) {
                    etQuantity.setError(getString(R.string.error_quantity_too_large));
                    valid = false;
                }
            } catch (NumberFormatException e) {
                etQuantity.setError(getString(R.string.error_quantity_number));
                valid = false;
            }
        }
        return valid;
    }


    private boolean isDuplicateName(String name) {
        String key = IngredientNormalizer.normalize(name);
        for (PantryItem other : dataSource.getAllItems(false)) {
            if (other.getId() != currentItem.getId()
                    && IngredientNormalizer.normalize(other.getName()).equals(key)) {
                return true;
            }
        }
        return false;
    }

    private void saveItem() {
        if (!validate()) {
            return;
        }
        currentItem.setName(etName.getText().toString().trim());
        currentItem.setQuantity(Double.parseDouble(etQuantity.getText().toString().trim()));
        currentItem.setUnit(spUnit.getSelectedItem().toString());
        currentItem.setExpiryDate(selectedExpiry);

        boolean ok;
        if (currentItem.getId() == -1) {
            ok = dataSource.insertItem(currentItem) != -1;
        } else {
            ok = dataSource.updateItem(currentItem);
        }

        if (ok) {
            Toast.makeText(this, getString(R.string.saved, currentItem.getName()), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.error_save, Toast.LENGTH_LONG).show();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(getString(R.string.delete_message, currentItem.getName()))
                .setPositiveButton(R.string.delete, (d, w) -> {
                    if (dataSource.deleteItem(currentItem.getId())) {           // DELETE
                        Toast.makeText(this, getString(R.string.deleted, currentItem.getName()), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, R.string.error_delete, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}