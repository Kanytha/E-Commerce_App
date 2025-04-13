package com.example.lalaecomerce.cart;

public class CartItem {
    private int itemId;
    private String name;
    private int imageResource;
    private double price;
    private int quantity;

    public CartItem(int itemId, String name, int imageResource, double price, int quantity) {
        this.itemId = itemId;
        this.name = name;
        this.imageResource = imageResource;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getImageResource() { return imageResource; }
    public void setImageUrl(String imageUrl) { this.imageResource = imageResource; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() {
        return price * quantity;
    }
}
