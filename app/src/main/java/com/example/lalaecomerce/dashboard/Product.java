package com.example.lalaecomerce.dashboard;


public class Product {
    private static int idCounter = 0;  // Static variable to keep track of IDs
    private int id;
    private int imageResource;
    private String name;
    private String description;
    private double price;
    private float rating;

    public Product(int imageResource, String name, String description, double price, float rating) {
        this.id = ++idCounter;  // Increment the counter each time a new Product is created
        this.imageResource = imageResource;
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
    }
    public int getId() {
        return id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }
}
