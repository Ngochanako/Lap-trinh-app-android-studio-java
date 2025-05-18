package com.myapplication.Modal;

public class DienThoai {
    private int id;
    private String name;
    private double price;
    private String brand;
    private String imageResId; // ID ảnh từ drawable

    public DienThoai(int id, String name, double price, String brand, String imageResId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
        this.imageResId = imageResId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getBrand() { return brand; }
    public String getImageResId() { return imageResId; }
}
