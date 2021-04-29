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
import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;

import java.util.List;

public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.ItemViewHolder> {

    private List<RecentlyViewedItem> list;
    private Context context;

    public RecentlyViewedAdapter(List<RecentlyViewedItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_recently_viewed,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        RecentlyViewedItem currentItem = list.get(position);

        holder.textView.setText(currentItem.getPrice());

        CommonUtils.loadImageWithGlide(
                context,
                Const.GET_CATEGORY_DATA + currentItem.getImagePath(),
                holder.imageView,
                true
        );

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_recently_viewed);
            textView = itemView.findViewById(R.id.txt_recent_name);
        }
    }
}
