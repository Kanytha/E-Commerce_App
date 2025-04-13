package com.example.lalaecomerce.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lalaecomerce.R;


import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private CartManager cartManager;
    private NumberFormat currencyFormatter;

    public CartItemAdapter(Context context, List<CartItem> cartItems, CartManager cartManager) {
        this.context = context;
        this.cartItems = cartItems;
        this.cartManager = cartManager;
        this.currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
    }

    public void updateCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartItemViewHolder(view);
    }
    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItem;
        TextView textViewName;
        TextView textViewPrice;
        TextView textViewQuantity;
        TextView textViewTotalPrice;
        ImageButton buttonRemove;
        ImageButton buttonDecrease;
        ImageButton buttonIncrease;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewItem = itemView.findViewById(R.id.imageViewItem);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewQuantity);
            textViewTotalPrice = itemView.findViewById(R.id.textViewTotalPrice);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        // Set item details
        holder.textViewName.setText(item.getName());
        holder.textViewPrice.setText(currencyFormatter.format(item.getPrice()));
        holder.textViewQuantity.setText(String.valueOf(item.getQuantity()));
        holder.textViewTotalPrice.setText(currencyFormatter.format(item.getTotalPrice()));

        // Load image with Glide
        Glide.with(context)
                .load(item.getImageResource())
                .placeholder(R.drawable.placholder)
                .error(R.drawable.error)
                .into(holder.imageViewItem);

        // Set click listeners for quantity adjustment
        holder.buttonRemove.setOnClickListener(v -> {
            cartManager.removeItem(item.getItemId());
        });

        holder.buttonDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                cartManager.updateItemQuantity(item.getItemId(), item.getQuantity() - 1);
            } else {
                cartManager.removeItem(item.getItemId());
            }
        });

        holder.buttonIncrease.setOnClickListener(v -> {
            cartManager.updateItemQuantity(item.getItemId(), item.getQuantity() + 1);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }


}

