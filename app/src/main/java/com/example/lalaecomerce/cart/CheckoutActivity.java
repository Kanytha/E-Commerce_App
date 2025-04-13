package com.example.lalaecomerce.cart;// CheckoutActivity.java

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.lalaecomerce.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private TextView textViewOrderSummary;
    private TextView textViewSubtotal;
    private TextView textViewShipping;
    private TextView textViewTax;
    private TextView textViewTotal;
    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextCity;
    private EditText editTextZipCode;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private EditText editTextCardNumber;
    private EditText editTextCardExpiry;
    private EditText editTextCardCvv;
    private Button buttonPlaceOrder;

    private CartManager cartManager;
    private NumberFormat currencyFormatter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize views
        textViewOrderSummary = findViewById(R.id.textViewOrderSummary);
        textViewSubtotal = findViewById(R.id.textViewSubtotal);
        textViewShipping = findViewById(R.id.textViewShipping);
        textViewTax = findViewById(R.id.textViewTax);
        textViewTotal = findViewById(R.id.textViewTotal);

        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextCity = findViewById(R.id.editTextCity);
        editTextZipCode = findViewById(R.id.editTextZipCode);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);

        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextCardExpiry = findViewById(R.id.editTextCardExpiry);
        editTextCardCvv = findViewById(R.id.editTextCardCvv);

        buttonPlaceOrder = findViewById(R.id.buttonPlaceOrder);

        // Initialize cart manager
        cartManager = CartManager.getInstance();
        currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

        // Display order summary
        displayOrderSummary();

        // Calculate and display totals
        calculateTotals();

        // Set up place order button click listener
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    processOrder();
                }
            }
        });
    }

    private void displayOrderSummary() {
        List<CartItem> items = cartManager.getCartItems();
        StringBuilder summary = new StringBuilder();

        for (CartItem item : items) {
            summary.append(item.getName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append(" - ")
                    .append(currencyFormatter.format(item.getTotalPrice()))
                    .append("\n");
        }

        textViewOrderSummary.setText(summary.toString());
    }

    private void calculateTotals() {
        double subtotal = cartManager.getCartTotal();
        double shipping = 5.99; // Fixed shipping cost
        double taxRate = 0.08; // 8% tax rate
        double tax = subtotal * taxRate;
        double total = subtotal + shipping + tax;

        textViewSubtotal.setText(currencyFormatter.format(subtotal));
        textViewShipping.setText(currencyFormatter.format(shipping));
        textViewTax.setText(currencyFormatter.format(tax));
        textViewTotal.setText(currencyFormatter.format(total));
    }

    private boolean validateInputs() {
        // Validate shipping information
        if (isEmpty(editTextName) || isEmpty(editTextAddress) ||
                isEmpty(editTextCity) || isEmpty(editTextZipCode) ||
                isEmpty(editTextPhone) || isEmpty(editTextEmail)) {
            Toast.makeText(this, "Please fill in all shipping information", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate payment information
        if (isEmpty(editTextCardNumber) || isEmpty(editTextCardExpiry) || isEmpty(editTextCardCvv)) {
            Toast.makeText(this, "Please fill in all payment information", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate email format
        String email = editTextEmail.getText().toString().trim();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email address");
            return false;
        }

        // Validate card number
        String cardNumber = editTextCardNumber.getText().toString().trim().replace(" ", "");
        if (cardNumber.length() < 13 || cardNumber.length() > 19) {
            editTextCardNumber.setError("Invalid card number");
            return false;
        }

        return true;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

    private void processOrder() {
        // Here you would typically connect to a payment gateway
        // For this example, we'll just simulate a successful order

        // Show processing message
        Toast.makeText(this, "Processing your order...", Toast.LENGTH_SHORT).show();

        // Simulate network delay
        buttonPlaceOrder.setEnabled(false);
        buttonPlaceOrder.setText("Processing...");

        buttonPlaceOrder.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Clear the cart
                cartManager.clearCart();

                // Navigate to order confirmation
//                Intent intent = new Intent(CheckoutActivity.this, OrderConfirmationActivity.class);
//                startActivity(intent);

                // Finish checkout activity
                finish();
            }
        }, 2000); // 2-second delay to simulate processing
    }
}