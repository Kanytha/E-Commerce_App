package com.example.lalaecomerce;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String name;
    private double price;
    private String description;
    private Uri imageUri; // Changed from String to Uri
    private String category;
    private long id; // Unique identifier for the product

    public Product(String name, double price, String description, Uri imageUri, String category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUri = imageUri;
        this.category = category;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Uri getImageUri() { return imageUri; }
    public void setImageUri(Uri imageUri) { this.imageUri = imageUri; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    // Parcelable implementation
    protected Product(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        description = in.readString();
        imageUri = in.readParcelable(Uri.class.getClassLoader());
        category = in.readString();
        id = in.readLong(); // Read id from Parcel
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeString(description);
        dest.writeParcelable(imageUri, flags);
        dest.writeString(category);
        dest.writeLong(id); // Write id to Parcel
    }
}