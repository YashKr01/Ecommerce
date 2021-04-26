package com.shopping.bloom.adapters.profilefragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.Category;
import com.shopping.bloom.model.wishlist.WishList;
import com.shopping.bloom.model.wishlist.WishListData;
import com.shopping.bloom.utils.CommonUtils;


import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {

    private List<WishListData> wishList;
    private Context context;

    public WishListAdapter(List<WishListData> wishList, Context context) {
        this.wishList = wishList;
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

        WishListData currentItem = wishList.get(position);
        holder.textView.setText(currentItem.getProductName());

        String image_path = "http://bloomapp.in" + currentItem.getImage();
        CommonUtils.loadImageWithGlide(context, image_path,
                holder.imageView, false);

    }

    @Override
    public int getItemCount() {
        return wishList.size();
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
