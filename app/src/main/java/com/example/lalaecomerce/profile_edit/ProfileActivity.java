package com.example.lalaecomerce.profile_edit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lalaecomerce.R;
import com.example.lalaecomerce.cart.CartActivity;
import com.example.lalaecomerce.dashboard.DashboardActivity;
import com.example.lalaecomerce.fav_item.FavoritesActivity;
import com.example.lalaecomerce.order_history.OrderHistoryActivity;


public class ProfileActivity extends AppCompatActivity {

    private TextView userName, userEmail;
    private ImageView profileImage;
    private Button editProfile, logoutButton;
    private ImageButton backButton;
    private RelativeLayout cartMenu, favoritesMenu, orderHistoryMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Ensure XML file exists

        // Initialize UI elements
        backButton = findViewById(R.id.backButton);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        profileImage = findViewById(R.id.profileImage);
        editProfile = findViewById(R.id.editProfile);
        logoutButton = findViewById(R.id.logoutButton);
        cartMenu = findViewById(R.id.cartMenu);
        favoritesMenu = findViewById(R.id.favoritesMenu);
        orderHistoryMenu = findViewById(R.id.orderHistoryMenu);

        // Load user data from SharedPreferences
        loadUserData();

        // Back button action
        backButton.setOnClickListener(v -> onBackPressed());

        // Edit Profile button
        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        // Cart menu action
        cartMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Favorites menu action
        favoritesMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // Order History menu action
        orderHistoryMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // Logout button
        logoutButton.setOnClickListener(v -> logoutUser());
    }

    private void loadUserData() {
        // Retrieve data from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_profile", MODE_PRIVATE);
        String savedName = prefs.getString("user_name", "Default Name");
        String savedEmail = prefs.getString("user_email", "123@gmail.com");

        // Set user information
        userName.setText(savedName);
        userEmail.setText(savedEmail);
    }

    private void logoutUser() {
        // Clear stored user data
        SharedPreferences prefs = getSharedPreferences("user_profile", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        // Redirect to login screen
        Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                String updatedName = data.getStringExtra("updated_name");
                String updatedEmail = data.getStringExtra("updated_email");

                if (updatedName != null) userName.setText(updatedName);
                if (updatedEmail != null) userEmail.setText(updatedEmail);
            }
        }
    }
}
