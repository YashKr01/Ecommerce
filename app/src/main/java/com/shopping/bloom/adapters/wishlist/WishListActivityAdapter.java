package com.shopping.bloom.adapters.wishlist;

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
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.List;

public class WishListActivityAdapter extends RecyclerView.Adapter<WishListActivityAdapter.ItemViewHolder> {

    private Context context;
    private List<WishListData> list;
    private WishListProductListener listener;

    public WishListActivityAdapter(Context context, List<WishListData> list, WishListProductListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_wishlist_activity,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        WishListData currentItem = list.get(position);

        holder.textView.setText(currentItem.getProductName());

        String imagePath = "http://bloomapp.in" + currentItem.getImage();
        CommonUtils.loadImageWithGlide(context,
                imagePath,
                holder.imageView,
                true
        );

        holder.imgDelete.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                listener.wishListItemDelete(currentItem,position);
            }
        });

        holder.imageView.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                listener.wishListItemCLicked(currentItem);
            }
        });

    }

    public void setList(List<WishListData> wishListData) {
        this.list = wishListData;
        notifyDataSetChanged();
    }

    public void addAll(List<WishListData> newList) {
        int lastIndex = list.size() - 1;
        list.addAll(newList);
        notifyItemRangeInserted(lastIndex, newList.size());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imgDelete;
        TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_wish_list_product);
            textView = itemView.findViewById(R.id.txt_wish_list_price);
            imgDelete = itemView.findViewById(R.id.image_delete);
        }
    }

}
