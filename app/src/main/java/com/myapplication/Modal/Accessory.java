package com.myapplication.Modal;

public class Accessory {
    private int id;
    private String nameAccessory;
    private String imageResIdAccessory;       // Image resource ID from drawable
    private String description;      // Description of the accessory
    private String compatibleBrand;  // Compatible phone brand
    private String type;
    private double priceAccessory;
    public Accessory(int id, String name, String imageResId, String description, String compatibleBrand,String type, double price) {
        this.id = id;
        this.nameAccessory = name;
        this.imageResIdAccessory = imageResId;
        this.description = description;
        this.compatibleBrand = compatibleBrand;
        this.type=type;
        this.priceAccessory=price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return nameAccessory;
    }

    public String getImageResId() {
        return imageResIdAccessory;
    }

    public String getDescription() {
        return description;
    }

    public String getCompatibleBrand() {
        return compatibleBrand;
    }
    public String getTypeAccessory(){
        return type;
    }
    public double getPrice() { return priceAccessory; }
}
