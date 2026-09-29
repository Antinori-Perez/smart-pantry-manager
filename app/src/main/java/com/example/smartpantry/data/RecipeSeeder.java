package com.example.smartpantry.data;

import static com.example.smartpantry.data.PantryDbHelper.COL_NAME;
import static com.example.smartpantry.data.PantryDbHelper.COL_PREPARATION_TIME;
import static com.example.smartpantry.data.PantryDbHelper.COL_QUANTITY;
import static com.example.smartpantry.data.PantryDbHelper.COL_RECIPE_ID;
import static com.example.smartpantry.data.PantryDbHelper.COL_STEPS;
import static com.example.smartpantry.data.PantryDbHelper.COL_UNIT;
import static com.example.smartpantry.data.PantryDbHelper.TABLE_RECIPE;
import static com.example.smartpantry.data.PantryDbHelper.TABLE_RECIPE_INGREDIENT;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

// Loads the recipe collection into the database on the app's first run.
// Ingredient format: "name|quantity|unit"   (g / ml for weight & volume, pcs for things you count)
final class RecipeSeeder {

    private RecipeSeeder() {
    }

    static void seed(SQLiteDatabase db) {
        // ===== My own recipe collection =====

        add(db, "Eggs Benedict", 20,
                "Melt the butter and whisk it slowly into 1 egg yolk and the lemon juice to make hollandaise.\nToast the English muffin and fry the ham.\nPoach the other 2 eggs in simmering water for 3 minutes.\nStack muffin, ham and poached egg, then pour the hollandaise over.",
                "egg|3|pcs", "english muffin|1|pcs", "ham|50|g", "butter|50|g", "lemon|1|pcs");

        add(db, "Spam in Chutney", 20,
                "Cook the rice.\nSlice the Spam and the onion.\nFry the Spam until crispy, then add the onion and soften.\nStir in the chutney and simmer for 5 minutes.\nServe on the rice.",
                "spam|340|g", "chutney|150|ml", "onion|1|pcs", "rice|200|g");

        add(db, "Mutton Curry", 90,
                "Fry the chopped onions and garlic in the oil until golden.\nAdd the curry powder and cook for 1 minute.\nAdd the mutton and brown on all sides.\nAdd the chopped tomatoes and 500 ml water, cover and simmer for 1 hour.\nAdd the cubed potatoes and cook for 20 more minutes.",
                "mutton|500|g", "onion|2|pcs", "garlic|3|pcs", "tomato|2|pcs", "potato|2|pcs", "curry powder|20|g", "cooking oil|30|ml");

        add(db, "Margherita Pizza", 30,
                "Mix the flour, yeast, olive oil and 150 ml warm water into a dough and rest for 1 hour.\nRoll the dough out into a round base.\nSpread the tomato sauce over the base.\nTear the mozzarella over the top.\nBake at 220 C for 12 minutes and finish with fresh basil.",
                "flour|250|g", "yeast|7|g", "olive oil|15|ml", "tomato sauce|100|ml", "mozzarella|125|g", "basil|5|g");

        add(db, "Lemon Meringue Pie", 60,
                "Rub the flour and butter together, press into a pie dish and bake at 180 C for 15 minutes.\nHeat the lemon juice, half the sugar and the cornflour until thick, then beat in the egg yolks.\nPour the lemon filling into the pastry case.\nWhisk the egg whites with the rest of the sugar until stiff and spread on top.\nBake for 15 minutes until golden.",
                "flour|200|g", "butter|100|g", "sugar|200|g", "egg|3|pcs", "lemon|3|pcs", "cornflour|40|g");

        add(db, "Cheesecake", 60,
                "Crush the biscuits and mix with the melted butter.\nPress into a tin to make the base.\nBeat the cream cheese, sugar, eggs and lemon juice until smooth.\nPour over the base and bake at 160 C for 45 minutes.\nCool, then chill for at least 4 hours.",
                "biscuits|200|g", "butter|100|g", "cream cheese|500|g", "sugar|100|g", "egg|2|pcs", "lemon|1|pcs");

        add(db, "Lasagna", 75,
                "Brown the mince with the chopped onion, then add the chopped tomatoes and simmer for 20 minutes.\nMelt the butter, stir in the flour, then whisk in the milk to make a white sauce.\nLayer lasagne sheets, meat sauce and white sauce in a dish, three times.\nTop with the cheese.\nBake at 180 C for 40 minutes.",
                "lasagne sheets|250|g", "beef mince|500|g", "onion|1|pcs", "tomato|4|pcs", "milk|500|ml", "flour|40|g", "butter|40|g", "cheese|100|g");

        add(db, "Chicken Tacos", 25,
                "Slice the chicken and fry with the taco seasoning until cooked.\nWarm the taco shells in the oven.\nShred the lettuce and chop the tomatoes.\nFill each shell with chicken, lettuce, tomato and cheese.",
                "chicken breast|2|pcs", "taco shells|6|pcs", "taco seasoning|20|g", "lettuce|1|pcs", "tomato|2|pcs", "cheese|60|g");

        add(db, "Beef Tacos", 25,
                "Brown the mince with the chopped onion.\nStir in the taco seasoning and 100 ml water and simmer for 5 minutes.\nWarm the taco shells in the oven.\nFill each shell with the beef, chopped tomato and cheese.",
                "beef mince|400|g", "onion|1|pcs", "taco seasoning|20|g", "taco shells|6|pcs", "tomato|2|pcs", "cheese|60|g");

        add(db, "Quesadillas", 15,
                "Slice the green pepper and onion and fry until soft.\nLay 2 tortillas flat and cover with the cheese and vegetables.\nTop with the other 2 tortillas.\nFry in the butter for 2 minutes each side until the cheese melts.\nCut into wedges.",
                "tortilla|4|pcs", "cheese|150|g", "green pepper|1|pcs", "onion|1|pcs", "butter|10|g");

        add(db, "Burritos", 30,
                "Cook the rice.\nBrown the mince, add the drained kidney beans and chopped tomatoes and simmer for 10 minutes.\nWarm the tortillas in a dry pan.\nFill each tortilla with rice, the beef and bean mix and cheese.\nFold in the sides and roll up tightly.",
                "tortilla|4|pcs", "rice|200|g", "beef mince|400|g", "kidney beans|400|g", "tomato|2|pcs", "cheese|100|g");

        // ===== Everyday basics (quick meals from common leftovers) =====

        add(db, "Scrambled Eggs", 10,
                "Whisk the eggs and milk together.\nMelt the butter in a pan on low heat.\nAdd the eggs and stir gently until just set.",
                "egg|3|pcs", "milk|50|ml", "butter|10|g");

        add(db, "Cheese Omelette", 10,
                "Whisk the eggs.\nMelt the butter in a pan and pour in the eggs.\nSprinkle the cheese over, fold in half and serve.",
                "egg|3|pcs", "cheese|50|g", "butter|10|g");

        add(db, "French Toast", 15,
                "Whisk the eggs, milk and sugar in a bowl.\nDip each bread slice in the mixture.\nFry in butter until golden on both sides.",
                "bread|4|pcs", "egg|2|pcs", "milk|100|ml", "sugar|10|g", "butter|15|g");

        add(db, "Toasted Cheese Sandwich", 10,
                "Butter the outside of both bread slices.\nPut the cheese between the slices.\nToast in a pan until golden and the cheese melts.",
                "bread|2|pcs", "cheese|60|g", "butter|15|g");

        add(db, "Beans on Toast", 10,
                "Heat the baked beans in a small pot.\nToast and butter the bread.\nPour the beans over the toast.",
                "baked beans|400|g", "bread|2|pcs", "butter|10|g");

        add(db, "Pancakes", 25,
                "Mix the flour and sugar.\nWhisk in the eggs and milk to a smooth batter.\nMelt a little butter in a pan and cook thin pancakes on both sides.",
                "flour|200|g", "egg|2|pcs", "milk|300|ml", "butter|20|g", "sugar|20|g");

        add(db, "Chakalaka", 30,
                "Fry the onion and green pepper until soft.\nAdd the curry powder and cook for 1 minute.\nAdd the grated carrots and chopped tomatoes and cook for 10 minutes.\nStir in the baked beans and heat through.",
                "onion|1|pcs", "tomato|2|pcs", "carrot|2|pcs", "green pepper|1|pcs", "baked beans|400|g", "curry powder|10|g");

        add(db, "Stiff Pap", 25,
                "Bring 750 ml salted water to the boil.\nSlowly stir in the maize meal to avoid lumps.\nCover and steam on low heat for 20 minutes, stirring now and then.\nStir in the butter before serving.",
                "maize meal|250|g", "salt|5|g", "butter|10|g");

        add(db, "Savoury Mince", 35,
                "Brown the mince in a pot.\nAdd the chopped onion and grated carrot and fry for 5 minutes.\nAdd the chopped tomatoes and crumbled stock cube.\nSimmer for 20 minutes.",
                "beef mince|500|g", "onion|1|pcs", "tomato|2|pcs", "carrot|1|pcs", "stock cube|1|pcs");
    }

    // Saves ONE recipe, then saves each of its ingredients linked to that recipe's id
    private static void add(SQLiteDatabase db, String name, int minutes, String steps, String... ingredients) {
        ContentValues recipe = new ContentValues();
        recipe.put(COL_NAME, name);
        recipe.put(COL_PREPARATION_TIME, minutes);
        recipe.put(COL_STEPS, steps);
        long recipeId = db.insert(TABLE_RECIPE, null, recipe);   // the new recipe's id

        for (String line : ingredients) {
            String[] parts = line.split("\\|");       // "egg|3|pcs" -> [egg, 3, pcs]
            ContentValues ing = new ContentValues();
            ing.put(COL_RECIPE_ID, recipeId);          // links this ingredient to its recipe
            ing.put(COL_NAME, parts[0]);
            ing.put(COL_QUANTITY, Double.parseDouble(parts[1]));
            ing.put(COL_UNIT, parts[2]);
            db.insert(TABLE_RECIPE_INGREDIENT, null, ing);
        }
    }
}
