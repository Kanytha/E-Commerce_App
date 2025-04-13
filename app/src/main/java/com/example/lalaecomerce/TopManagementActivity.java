package com.example.lalaecomerce;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TopManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter; // Use your existing RecyclerAdapter
    private ArrayList<Product> productList; // Use the refactored Product class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_management);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton addButton = findViewById(R.id.fabAdd);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load products from database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        productList = dbHelper.getProductsByCategory("Top");

        // If no products in database, add sample products
        if (productList.isEmpty()) {
            productList = getSampleProducts();
            // Save sample products to database
            for (Product product : productList) {
                dbHelper.addProduct(product);
            }
        }

        adapter = new RecyclerAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        // Set click listener for the Add Button
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(TopManagementActivity.this, AddProduct.class);
            intent.putExtra("category", "Top");
            startActivityForResult(intent, 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // Retrieve the new product from the intent
            Product newProduct = data.getParcelableExtra("PRODUCT"); // Use "PRODUCT" key

            if (newProduct != null) {
                productList.add(newProduct);  // Add to list
                adapter.notifyDataSetChanged();  // Refresh RecyclerView
                System.out.println("New product added: " + newProduct.getName());

                // Save the new product to the database
                DatabaseHelper dbHelper = new DatabaseHelper(this);
                dbHelper.addProduct(newProduct);
            }
        }
    }

    // Sample data for testing
    private ArrayList<Product> getSampleProducts() {
        ArrayList<Product> list = new ArrayList<>();

        // Add sample products with drawable resources converted to Uri
        list.add(new Product(
                "Sweater",
                19.99,
                "Grey off shoulder sweater",
                getDrawableUri(R.drawable.top1),
                "Top"));
        list.add(new Product(
                "Top",
                29.99,
                "Grey off shoulder top",
                getDrawableUri(R.drawable.top2),
                "Top"));
        list.add(new Product(
                "T shirt",
                15.99,
                "Black and white shirt with star light",
                getDrawableUri(R.drawable.top3),
                "Top"));
        list.add(new Product(
                "Tank Top",
                15.99,
                "White tank top with flora design",
                getDrawableUri(R.drawable.top4),
                "Top"));
        list.add(new Product(
                "Top",
                15.99,
                "Retro double loop design drop earring gold color",
                getDrawableUri(R.drawable.top5),
                "Top"));
        list.add(new Product(
                "Top",
                15.99,
                "Dark girl off shoulder strap cinched waist",
                getDrawableUri(R.drawable.top6),
                "Top"));
        list.add(new Product(
                "Top",
                15.99,
                "Print shirt flora design in yellow",
                getDrawableUri(R.drawable.top7),
                "Top"));

        return list;
    }

    // Helper method to convert drawable resource to Uri
    private Uri getDrawableUri(int drawableResId) {
        return Uri.parse("android.resource://" + getPackageName() + "/" + drawableResId);
    }
}