package com.example.smartpantry.model;

import java.util.Collections;
import java.util.List;
public class MatchResult {
    private final Recipe recipe;
    private final List<RecipeIngredient> missing;

    public MatchResult(Recipe recipe, List<RecipeIngredient> missing) {
        this.recipe = recipe;
        // copy is read only to stop items being editted.
        this.missing = Collections.unmodifiableList(missing);
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public List<RecipeIngredient> getMissing() {
        return missing;
    }

    // STRICT RULE: suggested only if NOTHING is missing
    public boolean isFullMatch() {
        return missing.isEmpty();
    }
}
