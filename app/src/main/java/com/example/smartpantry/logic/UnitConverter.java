package com.example.smartpantry.logic;

import java.util.Locale;

// Converts amounts to a common base unit so "1 kg" and "500 g" can be compared.
//   MEASURE = weight or volume, base unit grams/millilitres (we treat 1 ml as about 1 g)
//   COUNT   = things you count (pcs): 3 eggs, 2 onions
// A COUNT is never compared with a MEASURE (3 onions vs 200 g onion) -> counts as "not enough".
public final class UnitConverter {

    public enum Bucket { MEASURE, COUNT }

    private UnitConverter() {
    }

    // Turns any spelling of a unit into one of: g, kg, ml, l, tsp, tbsp, cup, pcs
    public static String canonicalUnit(String unit) {
        if (unit == null) {
            return "pcs";
        }
        String u = unit.toLowerCase(Locale.ROOT).trim().replace(".", "");
        switch (u) {
            case "g": case "gram": case "grams": case "gr":
                return "g";
            case "kg": case "kilogram": case "kilograms": case "kgs":
                return "kg";
            case "ml": case "millilitre": case "millilitres": case "milliliter": case "milliliters":
                return "ml";
            case "l": case "litre": case "litres": case "liter": case "liters":
                return "l";
            case "tsp": case "teaspoon": case "teaspoons":
                return "tsp";
            case "tbsp": case "tablespoon": case "tablespoons":
                return "tbsp";
            case "cup": case "cups":
                return "cup";
            default:
                return "pcs";   // "pcs", "piece", "pieces", "whole"... all counted
        }
    }

    public static Bucket bucketOf(String unit) {
        return "pcs".equals(canonicalUnit(unit)) ? Bucket.COUNT : Bucket.MEASURE;
    }

    // Returns the amount in grams/millilitres (MEASURE) or pieces (COUNT)
    public static double toBase(double quantity, String unit) {
        switch (canonicalUnit(unit)) {
            case "kg":
            case "l":
                return quantity * 1000;
            case "tsp":
                return quantity * 5;
            case "tbsp":
                return quantity * 15;
            case "cup":
                return quantity * 250;
            default:            // g, ml, pcs
                return quantity;
        }
    }
}