package com.example.lalaecomerce.order_history;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;


import com.example.lalaecomerce.R;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {

    private ListView orderHistoryListView;
    private ArrayList<OrderItem> orderItems;
    private OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        orderHistoryListView = findViewById(R.id.orderHistoryListView);

        // Load order items (replace with actual data from API or database)
        orderItems = loadOrderItems();

        // Set up the adapter
        orderHistoryAdapter = new OrderHistoryAdapter(this, orderItems);
        orderHistoryListView.setAdapter(orderHistoryAdapter);

        // Back button handling
        ImageButton backButton = findViewById(R.id.left);
        backButton.setOnClickListener(v -> super.onBackPressed()); // Use super.onBackPressed() for back navigation
    }

    private ArrayList<OrderItem> loadOrderItems() {
        // Mock order data (Replace with actual database or API call)
        ArrayList<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem("ORD123", "T-Shirt", 2, 15.99));
        items.add(new OrderItem("ORD124", "Jeans", 1, 29.99));

        return items;
    }
}
