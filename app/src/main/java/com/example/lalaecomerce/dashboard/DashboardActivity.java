package com.example.lalaecomerce.dashboard;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;


import com.example.lalaecomerce.R;
import com.example.lalaecomerce.cart.CartActivity;
import com.example.lalaecomerce.profile_edit.ProfileActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardActivity extends AppCompatActivity {

    private ViewPager2 bannerViewPager;
    private RecyclerView categoryRecyclerView;
    private RecyclerView productsRecyclerView;
    private TabLayout bannerIndicator;
    private Handler sliderHandler = new Handler(Looper.getMainLooper());
    private ImageButton userIcon;
    private ImageButton cartIcon;
    private ImageButton HomeIcon;
    private List<Product> productList = new ArrayList<>(); // Store all products
    private ProductAdapter productAdapter;



    private int currentPage = 0;
    private Timer timer;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;
    private int itemCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        bannerViewPager = findViewById(R.id.bannerViewPager);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        bannerIndicator = findViewById(R.id.bannerIndicator);

        // Set up banner slider
        setupBannerSlider();

        // Set up categories
        setupCategories();

        // Set up products
        userIcon = findViewById(R.id.userIcon);

        setupProducts();
        userIcon.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        cartIcon = findViewById(R.id.favicon);

        cartIcon.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, CartActivity.class);
            startActivity(intent);
        });
        HomeIcon = findViewById(R.id.homeButton);
        HomeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
        SearchView searchView = findViewById(R.id.searchView); // Make sure you have this in your layout
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.filter(newText);
                return false;
            }
        });



    }

    private void setupBannerSlider() {
        // Create banner items
        List<Banner> banners = new ArrayList<>();
        banners.add(new Banner(R.drawable.banner1, "New Summer Collection", "Up to 30% off"));
        banners.add(new Banner(R.drawable.banner2, "Exclusive purchase", "purchase now"));
        banners.add(new Banner(R.drawable.banner3, "Flash Sale", "Limited time offers"));

        // Set up the adapter
        BannerSliderAdapter bannerAdapter = new BannerSliderAdapter(banners);
        bannerViewPager.setAdapter(bannerAdapter);

        // Set up the indicator
        new TabLayoutMediator(bannerIndicator, bannerViewPager,
                (tab, position) -> {
                    // No text for the tabs
                }).attach();

        // Auto-sliding for the banner
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sliderHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPage == banners.size()) {
                            currentPage = 0;
                        }
                        bannerViewPager.setCurrentItem(currentPage++, true);
                    }
                });
            }
        }, DELAY_MS, PERIOD_MS);
    }

    private void setupCategories() {
        // Create category items
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(R.drawable.cutout_hoodie_crop, "Tops"));
        categories.add(new Category(R.drawable.jean, "Bottom"));
        categories.add(new Category(R.drawable.lolita_dress, "Dress"));
        categories.add(new Category(R.drawable.star_crop_top, "Y2k"));
        categories.add(new Category(R.drawable.coquette_tank_top, "Coquette"));
        categories.add(new Category(R.drawable.wool_crop, "Comfy"));

        // Set up the adapter
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setupProducts() {
        // Create product items
        productList.add(new Product(R.drawable.crop, "Top", "comfy", 19.99, 4.5f));
        productList.add(new Product(R.drawable.coquette_tank_top, "Bow tank top", "the combination of girly and chic", 29.99, 4.5f));
        productList.add(new Product(R.drawable.tanktop, "Tank top", "Great for everyday wear", 20.99, 4.5f));
        productList.add(new Product(R.drawable.offshoulder, "Off shoulder top", " wear on a date", 30.99, 4.5f));
        productList.add(new Product(R.drawable.jean, "Jean", "Good quality", 39.99, 4.5f));
        productList.add(new Product(R.drawable.cutout_hoodie_crop, "Cut out hoodie", "Good quality", 10.99, 4.5f));
        productList.add(new Product(R.drawable.coquette_bluepattern_crop, "Blue Corset top", "Good quality", 13.99, 4.5f));

        // Set up the adapter
        productAdapter = new ProductAdapter(productList);
        productsRecyclerView.setAdapter(productAdapter);
        productsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

}

