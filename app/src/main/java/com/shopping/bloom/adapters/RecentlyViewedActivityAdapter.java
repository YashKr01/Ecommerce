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

public class RecentlyViewedActivityAdapter extends RecyclerView.Adapter<RecentlyViewedActivityAdapter.ItemViewHolder> {

    private List<Product> list;
    private Context context;
    private ProductClickListener listener;

    public RecentlyViewedActivityAdapter(Context context, ProductClickListener listener) {
        this.context = context;
        this.listener = listener;
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_recently_viewed_activity,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Product currentItem = list.get(position);

        holder.setUpData(context, currentItem);

        holder.imageView.setOnClickListener(new DebouncedOnClickListener(300) {
            @Override
            public void onDebouncedClick(View v) {
                listener.onProductClicked(currentItem);
            }
        });

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_recently_viewed);
            textView = itemView.findViewById(R.id.txt_recent_name);
        }

        void setUpData(Context context, Product product) {
            textView.setText(CommonUtils.getSignedAmount(product.getPrice()));
            String imageURL = Const.GET_BASE_URL + product.getPrimary_image();
            CommonUtils.loadImageWithGlide(
                    context,
                    imageURL,
                    imageView,
                    true
            );
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<Product> products) {
        if(list == null) {
            list = new ArrayList<>(products);
        } else {
            list.clear();
            list.addAll(products);
        }
        notifyDataSetChanged();
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
