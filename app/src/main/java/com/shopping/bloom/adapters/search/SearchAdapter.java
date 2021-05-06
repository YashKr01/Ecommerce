package com.shopping.bloom.adapters.search;

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
import com.shopping.bloom.model.search.SearchProduct;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ItemViewHolder> {

    private Context context;
    private List<SearchProduct> list;

    public SearchAdapter(Context context, List<SearchProduct> list) {
        this.context = context;
        this.list = list;
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
        holder.textView.setText(currentItem.getPrice());

        CommonUtils.loadImageWithGlide(
                context,
                Const.GET_CATEGORY_DATA + currentItem.getProductImage(),
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
            imageView = itemView.findViewById(R.id.search_product_image);
            textView = itemView.findViewById(R.id.searched_product_name);
        }
    }
}
