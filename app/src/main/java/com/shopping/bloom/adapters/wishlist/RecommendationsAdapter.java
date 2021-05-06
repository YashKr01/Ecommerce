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
import com.shopping.bloom.model.wishlist.recommendations.RecommendationItem;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;

import java.util.List;

public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.ItemViewHolder> {

    private List<RecommendationItem> list;
    private Context context;

    public RecommendationsAdapter(List<RecommendationItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_recommendation,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        RecommendationItem currentItem = list.get(position);

        holder.textView.setText(currentItem.getPrice());

        CommonUtils.loadImageWithGlide(
                context,
                Const.GET_CATEGORY_DATA + currentItem.getImagePath(),
                holder.imageView,
                false
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
            imageView = itemView.findViewById(R.id.img_recommended_product);
            textView = itemView.findViewById(R.id.txt_recommended_product_price);
        }
    }
}
