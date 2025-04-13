package com.example.lalaecomerce;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lalaecomerce.Admin;
import com.example.lalaecomerce.DatabaseHelper;
import com.example.lalaecomerce.LoginActivity;

public class AdminProfileActivity extends AppCompatActivity {

    private EditText etAdminName, etAdminEmail, etAdminPassword;
    private Button btnSaveProfile, logoutButton;
    private ImageButton backButton; // Declare backButton
    private ImageView adminIcon; // Declare adminIcon
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        // Retrieve the admin's email from the intent
        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        // Initialize views
        etAdminName = findViewById(R.id.etAdminName);
        etAdminEmail = findViewById(R.id.etAdminEmail);
        etAdminPassword = findViewById(R.id.etAdminPassword);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        logoutButton = findViewById(R.id.logoutButton);
        backButton = findViewById(R.id.backButton); // Initialize backButton
        adminIcon = findViewById(R.id.adminIcon); // Initialize adminIcon

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Load admin details
        if (userEmail != null) {
            loadAdminDetails(userEmail);
        } else {
            Toast.makeText(this, "User email not provided", Toast.LENGTH_SHORT).show();
        }

        // Set click listener for Save button
        btnSaveProfile.setOnClickListener(v -> saveAdminDetails());

        // Set click listener for Logout button
        logoutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class); // Navigate to Login screen
            startActivity(intent);
            finish(); // Close the current activity
        });

        // Set click listener for Back button
        backButton.setOnClickListener(v -> onBackPressed()); // Navigate back

        // Set click listener for Admin Icon
        adminIcon.setOnClickListener(v -> {
            Intent intent = new Intent(AdminProfileActivity.this, AdminActivity.class); // Navigate to AdminActivity
            startActivity(intent);
        });
    }

    private void loadAdminDetails(String adminEmail) {
        Admin admin = databaseHelper.getAdminByEmail(adminEmail);

        if (admin != null) {
            etAdminName.setText(admin.getName());
            etAdminEmail.setText(admin.getEmail());
            etAdminPassword.setText(admin.getPassword());
        } else {
            Log.e("AdminProfileActivity", "Admin not found for email: " + adminEmail);
            Toast.makeText(this, "Admin not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAdminDetails() {
        String name = etAdminName.getText().toString().trim();
        String email = etAdminEmail.getText().toString().trim();
        String password = etAdminPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update admin details in the database
        boolean isUpdated = databaseHelper.updateAdmin(name, email, password);

        if (isUpdated) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("AdminProfileActivity", "Failed to update admin profile");
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }
}