package com.example.lalaecomerce;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BottomManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<Product> productList;

    // Define request code as a constant
    private static final int ADD_PRODUCT_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_management);

        Log.d("BottomManagementActivity", "Activity started");

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton addButton = findViewById(R.id.fabAdd);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load products from database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        productList = dbHelper.getProductsByCategory("Bottom");

        // If no products in database, add sample products
        if (productList.isEmpty()) {
            productList = getSampleProducts();
            // Save sample products to database
            for (Product product : productList) {
                dbHelper.addProduct(product);
            }
        }

        Log.d("BottomManagementActivity", "Product List Size: " + productList.size());
        for (Product product : productList) {
            Log.d("BottomManagementActivity", "Product Name: " + product.getName());
        }

        adapter = new RecyclerAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        // Set click listener for the Add Button
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(BottomManagementActivity.this, AddProduct.class);
            intent.putExtra("category", "Bottom");
            startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // Retrieve the updated product from the intent
            Product updatedProduct = data.getParcelableExtra("updatedProduct");

            if (updatedProduct != null) {
                // Find and replace the old product in the list
                for (int i = 0; i < productList.size(); i++) {
                    if (productList.get(i).getId() == updatedProduct.getId()) {
                        productList.set(i, updatedProduct);
                        break;
                    }
                }

                // Notify the adapter to refresh the RecyclerView
                adapter.notifyDataSetChanged();

                Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Sample data for testing
    private ArrayList<Product> getSampleProducts() {
        ArrayList<Product> list = new ArrayList<>();

        // Add sample products with drawable resources converted to Uri
        list.add(new Product(
                "Jeans",
                39.99,
                "Black pant with white bows",
                getDrawableUri(R.drawable.jean1),
                "Bottom"
        ));
        list.add(new Product(
                "Jeans",
                49.99,
                "Light blue star jeans",
                getDrawableUri(R.drawable.jean2),
                "Bottom"
        ));
        list.add(new Product(
                "Jeans",
                29.99,
                "Blue pants",
                getDrawableUri(R.drawable.jean3),
                "Bottom"
        ));
        list.add(new Product(
                "Jeans",
                29.99,
                "Black pants",
                getDrawableUri(R.drawable.jean4),
                "Bottom"
        ));
        list.add(new Product(
                "Pants",
                29.99,
                "Brown pants with waist tie",
                getDrawableUri(R.drawable.jean5),
                "Bottom"
        ));
        list.add(new Product(
                "Shorts",
                29.99,
                "Beige shorts",
                getDrawableUri(R.drawable.jean6),
                "Bottom"
        ));
        list.add(new Product(
                "Shorts",
                29.99,
                "Pink shorts with waist design",
                getDrawableUri(R.drawable.jean7),
                "Bottom"
        ));

        return list;
    }

    // Helper method to convert drawable resource to Uri
    private Uri getDrawableUri(int drawableResId) {
        try {
            return Uri.parse("android.resource://" + getPackageName() + "/" + drawableResId);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("BottomManagementActivity", "Failed to parse drawable resource ID: " + drawableResId);
            return null;
        }
    }
}