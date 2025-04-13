package com.example.lalaecomerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize views
        ImageView adminIcon = findViewById(R.id.adminIcon);

        // Find CardViews by their IDs
        CardView cardSkirt = findViewById(R.id.cardSkirt);
        CardView cardDress = findViewById(R.id.cardDress);
        CardView cardTop = findViewById(R.id.cardTop);
        CardView cardBottom = findViewById(R.id.cardBottom);

        // Set click listener for Skirts
        cardSkirt.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, SkirtManagementActivity.class);
            startActivity(intent);
        });

        // Set click listener for Dresses
        cardDress.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, DressManagementActivity.class);
            startActivity(intent);
        });

        // Set click listener for Tops
        cardTop.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, TopManagementActivity.class);
            startActivity(intent);
        });

        // Set click listener for Bottoms
        cardBottom.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, BottomManagementActivity.class);
            startActivity(intent);
        });

        // Set click listener for Admin Icon
        adminIcon.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, AdminProfileActivity.class); // Navigate to AdminProfileActivity
            intent.putExtra("admin_EMAIL", getIntent().getStringExtra("adminexample@gamil.com")); // Pass the admin's email
            startActivity(intent);
        });
    }
}