package com.example.lalaecomerce;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lalaecomerce.dashboard.DashboardActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Set click listeners
        btnLogin.setOnClickListener(v -> login()); // Call the login() method

        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String email = etEmail.getText().toString().trim().toLowerCase(); // Convert email to lowercase
        String password = etPassword.getText().toString().trim();

        Log.d("LoginActivity", "Attempting login with email: " + email);

        // --- Validation ---
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email is required");
            Log.e("LoginActivity", "Email is empty");
            return;
        } else {
            tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password is required");
            Log.e("LoginActivity", "Password is empty");
            return;
        } else {
            tilPassword.setError(null);
        }

        // Check if user exists
        if (!databaseHelper.checkUser(email)) {
            Toast.makeText(this, "Email does not exist", Toast.LENGTH_SHORT).show();
            Log.e("LoginActivity", "User does not exist for email: " + email);
            return;
        }

        // Validate credentials
        if (databaseHelper.validateLogin(email, password)) {
            Log.d("LoginActivity", "Login successful for email: " + email);

            // Get user role
            String role = databaseHelper.getUserRole(email);
            Log.d("LoginActivity", "User role: " + role);

            // Handle null role
            if (role == null) {
                Log.e("LoginActivity", "Role not found for email: " + email);
                Toast.makeText(this, "Role not found", Toast.LENGTH_SHORT).show();
                return;
            }

            // Redirect based on role
            Intent intent;
            if ("admin".equals(role)) {
                intent = new Intent(this, AdminActivity.class);
            } else if ("user".equals(role)) {
                intent = new Intent(this, DashboardActivity.class);
            } else {
                Log.e("LoginActivity", "Invalid role: " + role);
                Toast.makeText(this, "Invalid user role", Toast.LENGTH_SHORT).show();
                return; // Exit the method to prevent further execution
            }

            // Add extras to the intent
            intent.putExtra("USER_EMAIL", email);
            intent.putExtra("USER_ROLE", role);

            // Start the activity and close the current one
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
            Log.e("LoginActivity", "Invalid password for email: " + email);
        }
    }
}