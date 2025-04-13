package com.example.lalaecomerce;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ProductViewHolder> {

    private ArrayList<Product> productList;
    private Context context;

    public RecyclerAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Bind product name, price, and image
        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice());

        Glide.with(context)
                .load(product.getImageUri())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .into(holder.productImage);

        // Set up edit button click listener
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditProduct.class);
            intent.putExtra("product", product); // Pass the product object
            ((Activity) context).startActivityForResult(intent, 100); // Use request code 100
        });

        // Add click listener for the product image
        holder.productImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditProduct.class);
            intent.putExtra("product", product); // Pass the product object
            ((Activity) context).startActivityForResult(intent, 100); // Use request code 100
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productPrice;
        ImageView editButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            editButton = itemView.findViewById(R.id.editIcon);
        }
    }
}