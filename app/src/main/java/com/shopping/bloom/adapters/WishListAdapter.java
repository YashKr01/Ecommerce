package com.shopping.bloom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.DebouncedOnClickListener;


import java.util.ArrayList;
import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {

    private List<Product> wishList;
    private final Context context;
    private final ProductClickListener listener;

    public WishListAdapter(Context context, ProductClickListener listener) {
        this.context = context;
        this.listener = listener;
        wishList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wishlist, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product item = wishList.get(position);

        holder.setupData(context, item);

        holder.imageView.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                listener.onProductClicked(item);
            }
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_recently_viewed);
            textView = itemView.findViewById(R.id.txt_recent_name);
        }

        void setupData(Context context, Product data) {
            textView.setText(CommonUtils.getSignedAmount(data.getPrice()));
            String imageURL = Const.GET_BASE_URL + data.getPrimary_image();
            CommonUtils.loadImageWithGlide(context, imageURL,
                    imageView, true);
        }
    }

    public void updateList(List<Product> list) {
        if(wishList == null) wishList = new ArrayList<>(list);
        else {
            wishList.clear();
            wishList.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }

}
