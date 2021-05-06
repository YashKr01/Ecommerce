package com.shopping.bloom.adapters.singleproduct;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shopping.bloom.R;
import com.shopping.bloom.activities.SingleProductActivity;
import com.shopping.bloom.model.RandomImageDataResponse;

import java.util.List;

public class RandomImageAdapter extends RecyclerView.Adapter<RandomImageAdapter.ViewHolder>{

    Context context;
    List<RandomImageDataResponse> imageList;

    public RandomImageAdapter(Context context, List<RandomImageDataResponse> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    public void setImageList(List<RandomImageDataResponse> imageList) {
        this.imageList = imageList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RandomImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_random_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RandomImageAdapter.ViewHolder holder, int position) {
        Glide.with(context).load("http://www.bloomapp.in" + imageList.get(position).getPrimary_image())
                .placeholder(R.drawable.ic_placeholder_product).into(holder.imageView);
        holder.imageView.setOnClickListener(v ->{
            Intent intent = new Intent(context, SingleProductActivity.class);
            intent.putExtra("PRODUCT_ID", imageList.get(position).getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (this.imageList != null) {
            return this.imageList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
