package com.example.lalaecomerce;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SkirtManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Product> productList;
    private RecyclerAdapter adapter;

    // Define request code as a constant
    private static final int ADD_PRODUCT_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skirt_management);

        Log.d("SkirtManagementActivity", "Activity started");

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton addButton = findViewById(R.id.fabAdd);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load products from database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        productList = dbHelper.getProductsByCategory("Skirt");

        // If no products in database, add sample products
        if (productList.isEmpty()) {
            productList = getSampleProducts();
            // Save sample products to database
            for (Product product : productList) {
                dbHelper.addProduct(product);
            }
        }

        Log.d("SkirtManagementActivity", "Product List Size: " + productList.size());
        for (Product product : productList) {
            Log.d("SkirtManagementActivity", "Product Name: " + product.getName());
        }

        adapter = new RecyclerAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(SkirtManagementActivity.this, AddProduct.class);
            intent.putExtra("category", "Skirt");
            startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PRODUCT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Retrieve the new product from the intent
            Product newProduct = data.getParcelableExtra("PRODUCT"); // Ensure the key matches what you pass in AddProduct

            if (newProduct != null) {
                // Add the new product to the list
                productList.add(newProduct);

                // Notify the adapter to refresh the RecyclerView
                adapter.notifyDataSetChanged();

                // Save the new product to the database
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                dbHelper.addProduct(newProduct);

                // Log for debugging
                Log.d("SkirtManagementActivity", "New product added: " + newProduct.getName());
            } else {
                Log.e("SkirtManagementActivity", "No product received from AddProduct activity");
            }
        } else {
            Log.e("SkirtManagementActivity", "Invalid request code or result code");
        }
    }

    // Sample data for testing
    private ArrayList<Product> getSampleProducts() {
        ArrayList<Product> list = new ArrayList<>();

        // Add sample products with drawable resources converted to Uri
        list.add(new Product(
                "Denim mini skirt",
                29.99,
                "Denim Mini Skirt",
                getDrawableUri(R.drawable.skirt1),
                "Skirt"
        ));
        list.add(new Product(
                "Beige corduroy pleated mini skirt",
                39.99,
                "Beige corduroy pleated mini skirt with a lace trim at the hem",
                getDrawableUri(R.drawable.skirt2),
                "Skirt"
        ));
        list.add(new Product(
                "Brown corduroy pleaded mini skirt",
                49.99,
                "Brown corduroy pleaded mini skirt",
                getDrawableUri(R.drawable.skirt3),
                "Skirt"
        ));
        list.add(new Product(
                "Black Ruched Midi Skirt",
                49.99,
                "Long with a ruffled hem",
                getDrawableUri(R.drawable.skirt4),
                "Skirt"
        ));
        list.add(new Product(
                "Denim Belted Maxi Skirt",
                49.99,
                "Blue long denim skirt",
                getDrawableUri(R.drawable.skirt5),
                "Skirt"
        ));
        list.add(new Product(
                "Maxi Skirt",
                49.99,
                "White long skirts with a maxi silhouette",
                getDrawableUri(R.drawable.skirt6),
                "Skirt"
        ));
        list.add(new Product(
                "Pleated Mini skirt",
                49.99,
                "Dark gray pleated mini skirt with lace-up detailing and a lace hem",
                getDrawableUri(R.drawable.skirt7),
                "Skirt"
        ));

        return list;
    }

    // Helper method to convert drawable resource to Uri
    private Uri getDrawableUri(int drawableResId) {
        try {
            return Uri.parse("android.resource://" + getPackageName() + "/" + drawableResId);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SkirtManagementActivity", "Failed to parse drawable resource ID: " + drawableResId);
            return null;
        }
    }
}