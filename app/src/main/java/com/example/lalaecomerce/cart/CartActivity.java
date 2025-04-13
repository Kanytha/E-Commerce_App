package com.example.lalaecomerce.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lalaecomerce.R;

import java.text.NumberFormat;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartManager.CartUpdateListener {
    private RecyclerView recyclerViewCart;
    private CartItemAdapter cartAdapter;
    private TextView textViewTotal;
    private TextView textViewEmpty;
    private Button buttonCheckout;
    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize views
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        textViewTotal = findViewById(R.id.textViewTotal);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        buttonCheckout = findViewById(R.id.buttonCheckout);

        // Setup cart manager
        cartManager = CartManager.getInstance();
        cartManager.addCartUpdateListener(this);

        // Setup RecyclerView
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartItemAdapter(this, cartManager.getCartItems(), cartManager);
        recyclerViewCart.setAdapter(cartAdapter);

        // Setup checkout button
        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartManager.getItemCount() > 0) {
                    Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CartActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Update UI
        updateCartUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cartManager.removeCartUpdateListener(this);
    }

    @Override
    public void onCartUpdated() {
        updateCartUI();
    }

    private void updateCartUI() {
        if (cartManager.getItemCount() == 0) {
            recyclerViewCart.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
            buttonCheckout.setEnabled(false);
        } else {
            recyclerViewCart.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
            buttonCheckout.setEnabled(true);

            // Update adapter
            cartAdapter.updateCartItems(cartManager.getCartItems());

            // Format and display total
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
            String formattedTotal = currencyFormatter.format(cartManager.getCartTotal());
            textViewTotal.setText("Total: " + formattedTotal);
        }
    }
}