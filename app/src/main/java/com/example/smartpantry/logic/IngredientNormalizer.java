package com.example.smartpantry.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

// Turns messy, user-typed ingredient names into one normalized ones.
public final class IngredientNormalizer {

    // Describing forms of words that dont change ingredient.
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "fresh", "large", "small", "medium", "big", "chopped", "sliced", "diced",
            "grated", "ripe", "whole", "raw", "free", "range", "organic", "of", "a", "some"));

    // Different names for the same ingredient
    private static final Map<String, String> ALIASES = new HashMap<>();

    static {
        ALIASES.put("yogurt", "yoghurt");
        ALIASES.put("capsicum", "green pepper");
        ALIASES.put("bell pepper", "green pepper");
        ALIASES.put("pepper green", "green pepper");
        ALIASES.put("scallion", "spring onion");
        ALIASES.put("green onion", "spring onion");
        ALIASES.put("mince", "beef mince");
        ALIASES.put("minced beef", "beef mince");
        ALIASES.put("ground beef", "beef mince");
        ALIASES.put("mealie meal", "maize meal");
        ALIASES.put("cheddar", "cheese");
        ALIASES.put("cheddar cheese", "cheese");
        ALIASES.put("spaghetti", "pasta");
        ALIASES.put("macaroni", "pasta");
        ALIASES.put("penne", "pasta");
        ALIASES.put("white rice", "rice");
        ALIASES.put("mayo", "mayonnaise");
        ALIASES.put("chicken fillet", "chicken breast");
        ALIASES.put("tinned tuna", "tuna");
        ALIASES.put("canned tuna", "tuna");
        ALIASES.put("stock", "stock cube");
        ALIASES.put("garlic clove", "garlic");
        ALIASES.put("clove garlic", "garlic");
        ALIASES.put("feta cheese", "feta");
        ALIASES.put("celery stick", "celery");
        ALIASES.put("bread slice", "bread");
        ALIASES.put("slice bread", "bread");
        ALIASES.put("white bread", "bread");
        ALIASES.put("brown bread", "bread");
        ALIASES.put("white sugar", "sugar");
        ALIASES.put("brown sugar", "sugar");
        ALIASES.put("cake flour", "flour");
        ALIASES.put("plain flour", "flour");
        ALIASES.put("full cream milk", "milk");
        ALIASES.put("low fat milk", "milk");
        ALIASES.put("mozzarella cheese", "mozzarella");
        ALIASES.put("flour tortilla", "tortilla");
        ALIASES.put("wrap", "tortilla");
        ALIASES.put("lasagna sheet", "lasagne sheet");
        ALIASES.put("red kidney bean", "kidney bean");
        ALIASES.put("mutton chop", "mutton");
        ALIASES.put("corn flour", "cornflour");
        ALIASES.put("maizena", "cornflour");
    }

    private IngredientNormalizer() {
    }

    public static String normalize(String rawName) {
        if (rawName == null) {
            return "";
        }
        // 1. lower-case, and replace anything that isn't a letter with a space
        String cleaned = rawName.toLowerCase(Locale.ROOT).replaceAll("[^a-z]+", " ").trim();
        if (cleaned.isEmpty()) {
            return "";
        }

        // 2. drop describing words and make every word singular
        List<String> words = new ArrayList<>();
        for (String word : cleaned.split(" ")) {
            if (!STOP_WORDS.contains(word)) {
                words.add(singularize(word));
            }
        }
        String key = String.join(" ", words);

        // 3. swap known synonyms for one standard name
        String alias = ALIASES.get(key);
        return alias != null ? alias : key;
    }

    // A few simple English plural -> singular rules (good enough for food words)
    static String singularize(String word) {
        if (word.length() <= 3) {
            return word;
        }
        if (word.endsWith("ies") && word.length() > 4) {
            return word.substring(0, word.length() - 3) + "y";
        }
        if (word.endsWith("oes")) {
            return word.substring(0, word.length() - 2);
        }
        if (word.endsWith("ches") || word.endsWith("shes") || word.endsWith("sses")
                || word.endsWith("xes") || word.endsWith("zes")) {
            return word.substring(0, word.length() - 2);
        }
        if (word.endsWith("s") && !word.endsWith("ss") && !word.endsWith("us") && !word.endsWith("is")) {
            return word.substring(0, word.length() - 1);
        }
        return word;
    }
}