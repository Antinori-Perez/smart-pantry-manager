package com.example.smartpantry.model;

    import java.util.ArrayList;
    import java.util.List;


public class Recipe {

    private long id = -1;
    private String name;
    private int preparationTime;
    private String steps;

    private final List<RecipeIngredient> ingredients = new ArrayList<>();

    public Recipe(){

    }

    public Recipe(long id, String name, int preparationTime, String steps) {
        this.id =id;
        this.name = name;
        this.preparationTime = preparationTime;
        this.steps = steps;
    }

    public long getId() {
        return id;
        }
    public void setId(long id){
        this.id =id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public int getPreparationTime(){
        return preparationTime;
    }
    public void setPreparationTime(int preparationTime){
        this.preparationTime = preparationTime;
    }

    public String getSteps(){
        return steps;
    }
    public void setSteps( String steps){
        this.steps=steps;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(RecipeIngredient ingredient) {
        ingredients.add(ingredient);
    }

}
