package com.example.lalaecomerce.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lalaecomerce.R;

import java.util.List;

public class BannerSliderAdapter extends RecyclerView.Adapter<BannerSliderAdapter.BannerViewHolder> {

    private List<Banner> banners;

    public BannerSliderAdapter(List<Banner> banners) {
        this.banners = banners;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_slider_item, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banner banner = banners.get(position);
        holder.bannerImage.setImageResource(banner.getImageResource());
        holder.bannerTitle.setText(banner.getTitle());
        holder.bannerDescription.setText(banner.getDescription());
    }

    @Override
    public int getItemCount() {
        return banners.size();
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImage;
        TextView bannerTitle;
        TextView bannerDescription;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerImage = itemView.findViewById(R.id.bannerImage);
            bannerTitle = itemView.findViewById(R.id.bannerTitle);
            bannerDescription = itemView.findViewById(R.id.bannerDescription);
        }
    }
}
