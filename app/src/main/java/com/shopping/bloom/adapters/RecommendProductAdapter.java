package com.shopping.bloom.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class RecommendProductAdapter extends RecyclerView.Adapter<RecommendProductAdapter.RecommendProductViewHolder> {
    private static final String TAG = RecommendProductAdapter.class.getName();

    Context context;
    List<Product> products;
    ProductClickListener mListener;
    private int LAST_ITEM = -1;

    public RecommendProductAdapter(Context context, ProductClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
        this.products = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecommendProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new RecommendProductViewHolder(view);
    }

    public void updateList(List<Product> newList) {
        if (products == null || products.isEmpty()) {
            products = new ArrayList<>(newList);
        } else {
            products.addAll(newList);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        if(products == null) return;
        products.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendProductViewHolder holder, int position) {
        Product product = getItemAt(position);
        holder.setUpData(context, product);
        holder.rootView.setOnClickListener((view -> mListener.onProductClicked(product)));
    }

    public static class RecommendProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productPrice;      //could be product name
        ConstraintLayout rootView;

        public RecommendProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.img_wishlist);
            productPrice = itemView.findViewById(R.id.txt_wishlist_name);
            rootView = itemView.findViewById(R.id.clProductRootView);
        }

        public void setUpData(Context context, Product product) {
            String price = CommonUtils.getSignedAmount(product.getPrice());
            productPrice.setText(price);
            String imageURL = "http://bloomapp.in" + product.getPrimary_image();
            Log.d(TAG, "onBindViewHolder: imageURL " + imageURL);
            CommonUtils.loadImageWithGlide(context, imageURL, productImage, false);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private Product getItemAt(int position) {
        if (position < 0 || position > products.size()) {
            Log.d(TAG, "getItemAt: INVALID POSITION");
        }
        return products.get(position);
    }

}
