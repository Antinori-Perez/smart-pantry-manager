package com.example.smartpantry.data;

import static com.example.smartpantry.data.PantryDbHelper.COL_EXPIRY;
import static com.example.smartpantry.data.PantryDbHelper.COL_ID;
import static com.example.smartpantry.data.PantryDbHelper.COL_NAME;
import static com.example.smartpantry.data.PantryDbHelper.COL_QUANTITY;
import static com.example.smartpantry.data.PantryDbHelper.COL_UNIT;
import static com.example.smartpantry.data.PantryDbHelper.TABLE_PANTRY;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantry.model.PantryItem;

import java.util.ArrayList;
import java.util.List;

/**
 * All CRUD operations for pantry items (Create, Read, Update, Delete).
 * The screens never write SQL themselves - they call these methods.
 *
 * Every query uses "?" placeholders instead of gluing user text into the SQL
 * string, which protects against SQL injection and quote characters in names.
 */
public class PantryDataSource {

    private final PantryDbHelper dbHelper;

    public PantryDataSource(Context context) {
        dbHelper = PantryDbHelper.getInstance(context);
    }

    /** CREATE - returns the new row id, or -1 if the insert failed. */
    public long insertItem(PantryItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newId = db.insert(TABLE_PANTRY, null, toContentValues(item));
        if (newId != -1) {
            item.setId(newId);
        }
        return newId;
    }

    /** UPDATE - returns true if exactly one row was changed. */
    public boolean updateItem(PantryItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.update(TABLE_PANTRY, toContentValues(item),
                COL_ID + " = ?", new String[]{String.valueOf(item.getId())});
        return rows == 1;
    }

    /** DELETE - returns true if the row was removed. */
    public boolean deleteItem(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete(TABLE_PANTRY, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return rows == 1;
    }

    /** READ one item, or null if it doesn't exist. */
    public PantryItem getItemById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, COL_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        try {
            return cursor.moveToFirst() ? fromCursor(cursor) : null;
        } finally {
            cursor.close();                                  // ALWAYS close cursors
        }
    }

    /** READ all items, sorted by name or by expiry date (soonest first, no-date last). */
    public List<PantryItem> getAllItems(boolean sortByExpiry) {
        String orderBy = sortByExpiry
                ? COL_EXPIRY + " IS NULL, " + COL_EXPIRY + " ASC, " + COL_NAME + " COLLATE NOCASE ASC"
                : COL_NAME + " COLLATE NOCASE ASC";

        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, null, null, null, null, orderBy);
        try {
            while (cursor.moveToNext()) {
                items.add(fromCursor(cursor));
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    // ---------- helpers: object <-> database row ----------

    private ContentValues toContentValues(PantryItem item) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME, item.getName());
        values.put(COL_QUANTITY, item.getQuantity());
        values.put(COL_UNIT, item.getUnit());
        if (item.getExpiryDate() != null) {
            values.put(COL_EXPIRY, item.getExpiryDate());
        } else {
            values.putNull(COL_EXPIRY);
        }
        return values;
    }

    private PantryItem fromCursor(Cursor c) {
        // getColumnIndexOrThrow is safer than hard-coded column numbers
        int expiryIndex = c.getColumnIndexOrThrow(COL_EXPIRY);
        Long expiry = c.isNull(expiryIndex) ? null : c.getLong(expiryIndex);
        return new PantryItem(
                c.getLong(c.getColumnIndexOrThrow(COL_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_NAME)),
                c.getDouble(c.getColumnIndexOrThrow(COL_QUANTITY)),
                c.getString(c.getColumnIndexOrThrow(COL_UNIT)),
                expiry);
    }
}
