package com.example.lalaecomerce;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout tilEmail, tilPassword, tilFullName;
    private TextInputEditText etEmail, etPassword, etFullName;
    private Button btnSignUp;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize views
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilFullName = findViewById(R.id.tilFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFullName = findViewById(R.id.etFullName);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Set click listener
        btnSignUp.setOnClickListener(v -> signUp());
    }

    private void signUp() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email is required");
            return;
        } else {
            tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Password is required");
            return;
        } else {
            tilPassword.setError(null);
        }

        if (TextUtils.isEmpty(fullName)) {
            tilFullName.setError("Full name is required");
            return;
        } else {
            tilFullName.setError(null);
        }

        // Insert user into database
        if (databaseHelper.insertUser(email, password, fullName)) {
            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
        }
    }
}