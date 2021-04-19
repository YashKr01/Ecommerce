package com.shopping.bloom.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.models.Product;
import com.shopping.bloom.restService.callback.CategoryImageClickListener;
import com.shopping.bloom.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoryImagesAdapter extends RecyclerView.Adapter<CategoryImagesAdapter.CategoryImageViewHolder> {
    private static final String TAG = CategoryImagesAdapter.class.getName();

    private ArrayList<Product> products;
    private Context mContext;
    private CategoryImageClickListener mListener;

    public CategoryImagesAdapter(Context mContext, CategoryImageClickListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        products = new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoryImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_image, parent, false);
        return new CategoryImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryImageViewHolder holder, int position) {
        Product product = getItemAt(position);

        String IMAGE_URL = "http://bloomapp.in" + product.getCategory_thumbnail();
        Log.d(TAG, "onBindViewHolder: imageURL "+ IMAGE_URL);
        CommonUtils.loadImageWithGlide(mContext, IMAGE_URL, holder.imgCategoryImage, true);

        holder.imgCategoryImage.setOnClickListener((view -> mListener.onClick(product)));
    }

    private Product getItemAt(int position) {
        if(position < 0 || position > products.size()) {
            Log.d(TAG, "getItemAt: INVALID POSITION");
        }
        return products.get(position);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void updateList(List<Product> productList) {
        if(products == null) {
            products = new ArrayList<>(productList);
        } else {
            products.clear();
            products.addAll(productList);
        }
        notifyDataSetChanged();
    }

    static class CategoryImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategoryImage;

        public CategoryImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategoryImage = itemView.findViewById(R.id.imgCategoryImage);
        }
    }

}
