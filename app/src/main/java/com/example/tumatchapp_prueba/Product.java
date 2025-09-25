package com.example.tumatchapp_prueba;

public class Product {
    public int id;
    public String title;
    public double price;
    public String description;
    public String category;
    public String image;

    public Product(int id, String title, double price, String description, String category, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
    }
}

