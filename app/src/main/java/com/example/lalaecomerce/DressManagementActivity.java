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

public class DressManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Product> productList;
    private RecyclerAdapter adapter;

    // Define request code as a constant
    private static final int ADD_PRODUCT_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dress_management);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton addButton = findViewById(R.id.fabAdd);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load products from database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        productList = dbHelper.getProductsByCategory("Dress");

        // If no products in database, add sample products
        if (productList.isEmpty()) {
            productList = getSampleProducts();
            // Save sample products to database
            for (Product product : productList) {
                dbHelper.addProduct(product);
            }
        }

        Log.d("DressManagementActivity", "Product List Size: " + productList.size());
        for (Product product : productList) {
            Log.d("DressManagementActivity", "Product Name: " + product.getName());
        }

        adapter = new RecyclerAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(DressManagementActivity.this, AddProduct.class);
            intent.putExtra("category", "Dress");
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
                System.out.println("New product added: " + newProduct.getName());
            }
        }
    }

    // Sample data for testing
    private ArrayList<Product> getSampleProducts() {
        ArrayList<Product> list = new ArrayList<>();

        // Add sample products with drawable resources converted to Uri
        list.add(new Product(
                "White High-Low Dress",
                49.99,
                "Midi dress with a high-low hemline.",
                getDrawableUri(R.drawable.dress1),
                "Dress"
        ));
        list.add(new Product(
                "Sage Green Midi Dress",
                59.99,
                "Light sage green midi dress with tie straps.",
                getDrawableUri(R.drawable.dress2),
                "Dress"
        ));
        list.add(new Product(
                "Mini Dress",
                69.99,
                "Vintage black mesh embroidered bowknot strap A-line mini dress",
                getDrawableUri(R.drawable.dress3),
                "Dress"
        ));

        return list;
    }

    // Helper method to convert drawable resource to Uri
    private Uri getDrawableUri(int drawableResId) {
        try {
            return Uri.parse("android.resource://" + getPackageName() + "/" + drawableResId);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to parse drawable resource ID: " + drawableResId);
            return null;
        }
    }
}