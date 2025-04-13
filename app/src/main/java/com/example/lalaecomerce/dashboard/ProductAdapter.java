package com.example.lalaecomerce.dashboard;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lalaecomerce.R;
import com.example.lalaecomerce.cart.CartItem;
import com.example.lalaecomerce.cart.CartManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private List<Product> productListFull; //


    public ProductAdapter(List<Product> products) {
        this.products = products;
        this.productListFull = new ArrayList<>(products); // Store original list

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_user, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productImage.setImageResource(product.getImageResource());
        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText(String.format(Locale.US, "$%.2f", product.getPrice()));
        holder.productRating.setRating(product.getRating());

        holder.addToCartButton.setOnClickListener(v -> {
            Product selectedProduct = products.get(position);

            // Convert Product to CartItem (assuming CartItem constructor takes Product details)
            CartItem cartItem = new CartItem(
                    selectedProduct.getId(),
                    selectedProduct.getName(),
                    selectedProduct.getImageResource(),
                    selectedProduct.getPrice(),
                    1 // Default quantity when adding
            );


            // Add to CartManager
            CartManager.getInstance().addItem(cartItem);

            // Optional: Show a confirmation message
            Toast.makeText(v.getContext(), selectedProduct.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            int itemCount = CartManager.getInstance().getItemCount();  // Get the updated item count
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productDescription;
        TextView productPrice;
        RatingBar productRating;
        Button addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productRating = itemView.findViewById(R.id.productRating);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
    public void filter(String text) {
        List<Product> filteredList = new ArrayList<>();

        if (text.isEmpty()) {
            filteredList.addAll(productListFull); // Restore full list
        } else {
            String filterPattern = text.toLowerCase().trim();
            for (Product item : productListFull) {
                if (item.getName().toLowerCase().contains(filterPattern)) {
                    filteredList.add(item);
                }
            }
        }

        products.clear();
        products.addAll(filteredList);
        notifyDataSetChanged(); // Refresh RecyclerView
    }

}
