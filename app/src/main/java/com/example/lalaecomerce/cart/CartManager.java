package com.example.lalaecomerce.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartManager {
    private static CartManager instance;
    private Map<Integer, CartItem> cartItems;
    private List<CartUpdateListener> listeners;

    private CartManager() {
        cartItems = new HashMap<>();
        listeners = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addItem(CartItem item) {
        int itemId = item.getItemId();
        if (cartItems.containsKey(itemId)) {
            CartItem existingItem = cartItems.get(itemId);
            existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
        } else {
            cartItems.put(itemId, item);
        }
        notifyListeners();
    }

    public void updateItemQuantity(int itemId, int quantity) {
        if (cartItems.containsKey(itemId)) {
            if (quantity <= 0) {
                removeItem(itemId);
            } else {
                cartItems.get(itemId).setQuantity(quantity);
                notifyListeners();
            }
        }
    }

    public void removeItem(int itemId) {
        cartItems.remove(itemId);
        notifyListeners();
    }

    public void clearCart() {
        cartItems.clear();
        notifyListeners();
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems.values());
    }

    public int getItemCount() {
        int count = 0;
        for (CartItem item : cartItems.values()) {
            count += item.getQuantity();
        }
        return count;
    }

    public double getCartTotal() {
        double total = 0;
        for (CartItem item : cartItems.values()) {
            total += item.getTotalPrice();
        }
        return total;
    }

    // Observer pattern for cart updates
    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public void addCartUpdateListener(CartUpdateListener listener) {
        listeners.add(listener);
    }

    public void removeCartUpdateListener(CartUpdateListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (CartUpdateListener listener : listeners) {
            listener.onCartUpdated();
        }
    }
}
