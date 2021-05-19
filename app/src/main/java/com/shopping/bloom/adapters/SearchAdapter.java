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
import com.shopping.bloom.model.search.SearchProduct;
import com.shopping.bloom.restService.callback.SearchProductClickListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemViewHolder> {

    private Context context;
    private List<SearchProduct> list;
    private SearchProductClickListener listener;

    public SearchAdapter(Context context, List<SearchProduct> list, SearchProductClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_searched_product,
                parent,
                false
        ));
    }

    public void updateList(List<SearchProduct> searchProducts) {
        if (searchProducts == null) return;
        this.list = searchProducts;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        SearchProduct currentItem = list.get(position);
        holder.textView.setText(CommonUtils.getSignedAmount(currentItem.getPrice()));

        CommonUtils.loadImageWithGlide(
                context,
                Const.GET_BASE_URL + currentItem.getProductImage(),
                holder.imageView,
                true
        );

        holder.imageView.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                listener.onSearchClickListener(currentItem);
            }
        });

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
            imageView = itemView.findViewById(R.id.search_product_image);
            textView = itemView.findViewById(R.id.searched_product_name);
        }
    }
}
