package com.example.smartpantry.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Database files
//   pantry_item        - These are the ingredients that can be added, or deleted.
//   recipe             - The full collection of recipes.
//   recipe_ingredient  - requirements of ingredients needed for the recipes.
public class PantryDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "smartpantry.db";
    public static final int DATABASE_VERSION = 1;

    // Item table for the pantry.
    public static final String TABLE_PANTRY = "pantry_item";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry_date";

    // ---- recipe table ----
    public static final String TABLE_RECIPE = "recipe";
    public static final String COL_PREPARATION_TIME = "preparation_time";
    public static final String COL_STEPS = "steps";

    // ---- recipe_ingredient table ----
    public static final String TABLE_RECIPE_INGREDIENT = "recipe_ingredient";
    public static final String COL_RECIPE_ID = "recipe_id";

    private static final String CREATE_PANTRY =
            "CREATE TABLE " + TABLE_PANTRY + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_NAME + " TEXT NOT NULL, "
                    + COL_QUANTITY + " REAL NOT NULL, "
                    + COL_UNIT + " TEXT NOT NULL, "
                    + COL_EXPIRY + " INTEGER)";

    private static final String CREATE_RECIPE =
            "CREATE TABLE " + TABLE_RECIPE + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_NAME + " TEXT NOT NULL, "
                    + COL_PREPARATION_TIME + " INTEGER NOT NULL, "
                    + COL_STEPS + " TEXT NOT NULL)";

    private static final String CREATE_RECIPE_INGREDIENT =
            "CREATE TABLE " + TABLE_RECIPE_INGREDIENT + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_RECIPE_ID + " INTEGER NOT NULL REFERENCES " + TABLE_RECIPE + "(" + COL_ID + ") ON DELETE CASCADE, "
                    + COL_NAME + " TEXT NOT NULL, "
                    + COL_QUANTITY + " REAL NOT NULL, "
                    + COL_UNIT + " TEXT NOT NULL)";

    // One helper created for app to use.
    private static PantryDbHelper instance;

    public static synchronized PantryDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new PantryDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private PantryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);    // SQLite has foreign keys OFF by default
    }

    // Runs ONLY ONCE: Used when APp opens database.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PANTRY);
        db.execSQL(CREATE_RECIPE);
        db.execSQL(CREATE_RECIPE_INGREDIENT);
        RecipeSeeder.seed(db);
    }

    // Runs when DATABASE_VERSION is updated in future.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Version 1 is the first release, nothing to change yet.
        // Later we'd use ALTER TABLE here so the user's pantry isn't lost.
    }
}