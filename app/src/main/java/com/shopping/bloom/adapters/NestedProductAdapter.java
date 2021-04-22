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
import com.shopping.bloom.model.SubCategory;
import com.shopping.bloom.restService.callback.LoadMoreItems;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class NestedProductAdapter extends RecyclerView.Adapter<NestedProductAdapter.NestedProductViewHolder> {
    private static final String TAG = NestedProductAdapter.class.getName();

    Context context;
    ArrayList<SubCategory> subCategories;
    ProductClickListener mListener;
    LoadMoreItems loadMoreItems;
    private int LAST_ITEM = -1;

    public NestedProductAdapter(Context context, List<SubCategory> subCategories, ProductClickListener mListener, LoadMoreItems loadMoreItems) {
        this.context = context;
        this.subCategories = new ArrayList<>(subCategories);
        this.mListener = mListener;
        this.loadMoreItems = loadMoreItems;
    }

    @NonNull
    @Override
    public NestedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == LAST_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_see_more_products, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        }
        return new NestedProductViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        SubCategory product = subCategories.get(position);
        if (product.getId() == LAST_ITEM && position == getItemCount()-1) return LAST_ITEM;
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedProductViewHolder holder, int position) {
        SubCategory subCategory = getItemAt(position);

        if (getItemViewType(position) == LAST_ITEM) {
            if(loadMoreItems != null) {
                holder.rootView.setOnClickListener((view -> loadMoreItems.loadMore()));
            }
        } else {
            holder.setUpData(context, subCategory);
            holder.rootView.setOnClickListener((view -> mListener.onSubProductClick(subCategory)));
        }
    }

    public static class NestedProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productPrice;      //could be product name
        ConstraintLayout rootView;

        public NestedProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.img_wishlist);
            productPrice = itemView.findViewById(R.id.txt_wishlist_name);
            rootView = itemView.findViewById(R.id.clProductRootView);
        }

        public void setUpData(Context context, SubCategory subCategory) {
            productPrice.setText(subCategory.getCategory_name());
            String imageURL = "http://bloomapp.in" + subCategory.getCategory_thumbnail();
            Log.d(TAG, "onBindViewHolder: imageURL " + imageURL);
            CommonUtils.loadImageWithGlide(context, imageURL, productImage, false);
        }
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    private SubCategory getItemAt(int position) {
        if (position < 0 || position > subCategories.size()) {
            Log.d(TAG, "getItemAt: INVALID POSITION");
        }
        return subCategories.get(position);
    }
}
