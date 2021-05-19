package com.shopping.bloom.adapters;

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
import com.shopping.bloom.R;
import com.shopping.bloom.activities.SingleProductActivity;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.List;

public class RandomImageAdapter extends RecyclerView.Adapter<RandomImageAdapter.ViewHolder> {

    Context context;
    List<Product> imageList;

    public RandomImageAdapter(Context context, List<Product> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    public void setImageList(List<Product> imageList) {
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
        Glide.with(context).load(Const.GET_BASE_URL + imageList.get(position).getPrimary_image())
                .placeholder(R.drawable.ic_placeholder_product).into(holder.imageView);

        holder.imageView.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                Intent intent = new Intent(context, SingleProductActivity.class);
                intent.putExtra("PRODUCT_ID", imageList.get(position).getId());
                System.out.println("PRODUCT_ID = " + imageList.get(position).getId());
                context.startActivity(intent);
            }
        });
        holder.textView.setText(CommonUtils.getSignedAmount(imageList.get(position).getPrice()));
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
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.product_name);
        }
    }
}
