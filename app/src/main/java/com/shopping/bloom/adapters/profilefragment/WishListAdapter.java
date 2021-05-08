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
import com.shopping.bloom.model.wishlist.WishListData;
import com.shopping.bloom.restService.callback.WishListProductListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.DebouncedOnClickListener;


import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {

    private List<WishListData> wishList;
    private Context context;
    private WishListProductListener listener;

    public WishListAdapter(List<WishListData> wishList, Context context, WishListProductListener listener) {
        this.wishList = wishList;
        this.context = context;
        this.listener = listener;
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
        holder.textView.setText(CommonUtils.getSignedAmount(currentItem.getPrice()));

        CommonUtils.loadImageWithGlide(context, Const.GET_CATEGORY_DATA + currentItem.getImage(),
                holder.imageView, true);

        holder.imageView.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                listener.wishListItemCLicked(currentItem);
            }
        });

    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_recently_viewed);
            textView = itemView.findViewById(R.id.txt_recent_name);
        }
    }
}
