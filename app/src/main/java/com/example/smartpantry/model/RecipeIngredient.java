package com.example.smartpantry.model;

public class RecipeIngredient {
    private final String name;
    private final double quantity;
    private final String unit;

    //no setter functions here as the variables are already set as final. adding setter will cause programme to not compile.

    public RecipeIngredient(String name, double quantity, String unit){
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    //getter functions
    public String getName(){
        return name;
    }

    public double getQuantity(){
        return quantity;
    }

    public String getUnit(){
        return unit;
    }

}
