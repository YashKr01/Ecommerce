package com.shopping.bloom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.CustomImageView;

import java.util.ArrayList;
import java.util.List;

public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.ItemViewHolder> {

    private List<Product> list;
    private Context context;
    private ProductClickListener listener;

    public RecommendationsAdapter(Context context, ProductClickListener recommendedProductClickLister) {
        this.context = context;
        this.listener = recommendedProductClickLister;
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_product,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Product currentItem = list.get(position);
        holder.setUpData(context, currentItem);

        holder.imageView.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                listener.onProductClicked(currentItem);
            }
        });
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CustomImageView imageView;
        TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_wishlist);
            textView = itemView.findViewById(R.id.txt_wishlist_name);
        }

        void setUpData(Context context, Product product) {
            CommonUtils.loadImageWithGlide(
                    context,
                    Const.GET_BASE_URL + product.getPrimary_image(),
                    imageView,
                    false
            );
            textView.setText(CommonUtils.getSignedAmount(product.getPrice()));
        }

    }

    @Override
    public int getItemCount() {
        if(list == null) return 0;
        return list.size();
    }

    public void updateList(List<Product > products) {
        if(list == null || list.isEmpty()) {
            list = new ArrayList<>(products);
            notifyDataSetChanged();
            return;
        }
        int insertAt = list.size()-1;
        list.addAll(products);
        notifyItemRangeInserted(insertAt, products.size());
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
