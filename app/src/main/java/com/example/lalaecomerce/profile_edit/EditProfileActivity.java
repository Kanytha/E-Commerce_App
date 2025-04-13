package com.example.lalaecomerce.profile_edit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lalaecomerce.R;
import com.example.lalaecomerce.dashboard.DatabaseHelper;



public class EditProfileActivity extends AppCompatActivity {

    private ImageButton backButton;
    private EditText fullNameInput, emailInput, passwordInput;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize UI elements
        backButton = findViewById(R.id.backButton);
        fullNameInput = findViewById(R.id.fullNameInput);
        emailInput = findViewById(R.id.emailInput);
        saveButton = findViewById(R.id.saveButton);

        // Get current name and email from Intent
        String currentName = getIntent().getStringExtra("current_name");
        String currentEmail = getIntent().getStringExtra("current_email");

        if (currentName != null && currentEmail != null) {
            // Pre-fill EditText fields with current values
            fullNameInput.setText(currentName);
            emailInput.setText(currentEmail);
        } else {
            // Handle the case where currentName or currentEmail is null
            Toast.makeText(this, "Error: Unable to load user information", Toast.LENGTH_SHORT).show();
        }

        // Back button action
        backButton.setOnClickListener(v -> finish());

        // Save button action
        saveButton.setOnClickListener(v -> {
            String fullName = fullNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();

            // Check if any fields are empty
            if ( fullName.isEmpty() || email.isEmpty()) {
                Toast.makeText(EditProfileActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {

                SharedPreferences sharedPref = getSharedPreferences("user_profile", MODE_PRIVATE);
                String oldName = sharedPref.getString("user_name", fullName); // Get old name instead of email
                String oldEmail = sharedPref.getString("user_email", email); // Still keeping old email reference if needed
                Log.d("DEBUG", "Old Name: " + oldName);
                Log.d("DEBUG", "Old Email: " + oldEmail);
                DatabaseHelper dbHelper = new DatabaseHelper(EditProfileActivity.this);
                boolean emailUpdated = dbHelper.updateUserEmail(oldEmail, email);
                boolean nameUpdated = dbHelper.updateUserName(oldName, fullName);



                if (!emailUpdated) {
                    Toast.makeText(EditProfileActivity.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                }
                if (!nameUpdated) {
                    Toast.makeText(EditProfileActivity.this, "Failed to update name", Toast.LENGTH_SHORT).show();
                }

                if (emailUpdated && nameUpdated) {
                    // Save the updated name and email in SharedPreferences
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("user_name", fullName);
                    editor.putString("user_email", email);
                    editor.apply();


                    // Simulate saving user data (Replace with actual database or API update logic)
                    Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                    // Return to main profile screen after saving
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    intent.putExtra("updated_name", fullName);
                    intent.putExtra("updated_email", email);
                    setResult(RESULT_OK, intent);
                    finish(); // Close EditProfileActivity
                }
            }

        });
    }
}



