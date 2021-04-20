package com.shopping.bloom.adapters.profilefragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shopping.bloom.R;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.fragmentshop.RecentlyViewed;


import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {

    private List<RecentlyViewed> productList;
    private Context context;

    public WishListAdapter(List<RecentlyViewed> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_wishlist
                , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        RecentlyViewed curr = productList.get(position);
        holder.textView.setText(String.valueOf(curr.getPrice()));
        holder.imageView.setBackgroundResource(curr.getBackground());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_wish_list);
            textView = itemView.findViewById(R.id.txt_wish_list_name);
        }
    }
}
