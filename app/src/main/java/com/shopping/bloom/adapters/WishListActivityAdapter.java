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
import com.shopping.bloom.restService.callback.WishListItemClickListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.ArrayList;
import java.util.List;

public class WishListActivityAdapter extends RecyclerView.Adapter<WishListActivityAdapter.ItemViewHolder> {

    private Context context;
    private List<Product> list;
    private WishListItemClickListener listener;

    public WishListActivityAdapter(Context context, WishListItemClickListener listener) {
        this.context = context;
        this.listener = listener;
        list = new ArrayList<>();
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

        Product currentItem = list.get(position);

        holder.textView.setText(CommonUtils.getSignedAmount(currentItem.getPrice()));

        String imagePath = Const.GET_BASE_URL + currentItem.getPrimary_image();
        CommonUtils.loadImageWithGlide(context,
                imagePath,
                holder.imageView,
                true
        );

        holder.imgDelete.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                listener.onItemDelete(currentItem, position);
            }
        });

        holder.imageView.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                listener.onItemClick(currentItem);
            }
        });
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, imgDelete;
        TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_wish_list_product);
            textView = itemView.findViewById(R.id.txt_wish_list_price);
            imgDelete = itemView.findViewById(R.id.image_delete);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<Product> products) {
        if(list == null || list.isEmpty()) {
            list = new ArrayList<>(products);
            notifyDataSetChanged();
        } else {
            list.addAll(products);
            notifyItemRangeInserted(list.size()-1, products.size());
        }
    }

    public void removeItem(int position) {
        if(list == null || list.size() < position) return;
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void clearList() {
        if(list == null) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        notifyDataSetChanged();
    }
}
