package com.example.smartpantry.model;

public class PantryItem {

    private long id =-1;
    private String name;
    private double quantity;
    private String unit;
    private Long expiryDate;

    public PantryItem() {

    }

    public PantryItem(long id, String name, double quantity, String unit, Long expiryDate){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    //Getter and Setter for the ID;
    public long getId(){
        return id;
    }
    public void setId( long id){
        this.id = id;
    }

    //Getter and Setter for the name;
    public String getName(){
        return name;
    }
    public void setName( String name){
        this.name = name;
    }

    //Geetter and Setter for quantity;
    public double getQuantity(){
        return quantity;
    }
    public void setQuantity( double quantity){
        this.quantity = quantity;
    }

    //getter and setter for unit;
    public String getUnit(){
        return unit;
    }
    public void setUnit( String unit){
        this.unit = unit;
    }

    //getter and setter for expiration date;
    public Long getExpiryDate(){
        return expiryDate;
    }
    public void setExpiryDate( Long expiryDate){
        this.expiryDate = expiryDate;
    }

    public boolean hasExpiryDate() {
        return expiryDate != null;
    }

}
