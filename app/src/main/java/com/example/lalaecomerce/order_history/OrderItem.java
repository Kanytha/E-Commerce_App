package com.example.lalaecomerce.order_history;


public class OrderItem {
    private String orderID;
    private String itemName;
    private int quantity;
    private double price;

    public OrderItem(String orderID, String itemName, int quantity, double price) {
        this.orderID = orderID;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}

