package com.example.lalaecomerce.dashboard;

public class Category {
    private int imageResource;
    private String name;

    public Category(int imageResource, String name) {
        this.imageResource = imageResource;
        this.name = name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }
}