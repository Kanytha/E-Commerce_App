package com.example.lalaecomerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText searchEditText;
    private RecyclerView recyclerViewOfficial, recyclerViewPopular;
    private TextView officialBrandText, popularProductsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        searchEditText = findViewById(R.id.editTextText);
        recyclerViewOfficial = findViewById(R.id.recyclerViewOfficial);
        recyclerViewPopular = findViewById(R.id.recyclerViewPopular);
        officialBrandText = findViewById(R.id.textView);
        popularProductsText = findViewById(R.id.textView3);

        // Get login data
        String email = getIntent().getStringExtra("USER_EMAIL");
        String role = getIntent().getStringExtra("USER_ROLE");
        if (email != null && role != null) {
            searchEditText.setHint("Welcome, " + email + " (" + role + ")");
        }

    }
}