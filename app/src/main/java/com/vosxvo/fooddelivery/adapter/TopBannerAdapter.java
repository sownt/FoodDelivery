package com.vosxvo.fooddelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.api.API;

public class TopBannerAdapter extends RecyclerView.Adapter<TopBannerAdapter.TopBannerViewHolder> {
    public static final String[] banners = {
            "/static/food-pictures/98aa45f6-884f-4c52-80b2-a957c769fa39.png",
            "/static/food-pictures/2edb1f73-2f0d-408c-81bb-6183dc299565.png",
            "/static/food-pictures/91cce645-523f-41f8-b9cb-136add0f7f87.png",
            "/static/food-pictures/fbbc70ca-60f9-4f98-a530-f2ef18716445.png",
            "/static/food-pictures/ed12dda2-891e-435a-b636-e7bfadc67661.png",
            "/static/food-pictures/2b7b742a-dfb8-40ca-afea-c1460313d7d0.png",
            "/static/food-pictures/b69899d4-584e-4234-86d7-7b4c431be139.png",
            "/static/food-pictures/87ef101d-0182-4f35-a340-a0f3cee20809.png"
    };

    public TopBannerAdapter() {
    }

    @NonNull
    @Override
    public TopBannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopBannerViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_banner, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TopBannerViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(API.MAIN_API_BASE_URL + banners[position])
                .centerCrop()
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class TopBannerViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;

        public TopBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.top_banner_img);
        }
    }
}
