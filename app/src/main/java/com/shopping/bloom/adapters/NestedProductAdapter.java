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
import com.shopping.bloom.model.SubProduct;
import com.shopping.bloom.restService.callback.LoadMoreItems;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class NestedProductAdapter extends RecyclerView.Adapter<NestedProductAdapter.NestedProductViewHolder> {
    private static final String TAG = NestedProductAdapter.class.getName();

    Context context;
    ArrayList<SubProduct> subProducts;
    ProductClickListener mListener;
    LoadMoreItems loadMoreItems;
    private int LAST_ITEM = -1;

    public NestedProductAdapter(Context context, List<SubProduct> subProducts, ProductClickListener mListener, LoadMoreItems loadMoreItems) {
        this.context = context;
        this.subProducts = new ArrayList<>(subProducts);
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
        SubProduct product = subProducts.get(position);
        if (product.getId() == LAST_ITEM && position == getItemCount()-1) return LAST_ITEM;
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedProductViewHolder holder, int position) {
        SubProduct subProduct = getItemAt(position);

        if (getItemViewType(position) == LAST_ITEM) {
            if(loadMoreItems != null) {
                holder.rootView.setOnClickListener((view -> loadMoreItems.loadMore()));
            }
        } else {
            holder.setUpData(context, subProduct);
            holder.rootView.setOnClickListener((view -> mListener.onSubProductClick(subProduct)));
        }
    }

    public static class NestedProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productPrice;      //could be product name
        ConstraintLayout rootView;

        public NestedProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imgCategoryImage);
            productPrice = itemView.findViewById(R.id.tvProductPrice);
            rootView = itemView.findViewById(R.id.clProductRootView);
        }

        public void setUpData(Context context, SubProduct subProduct) {
            productPrice.setText(subProduct.getCategory_name());
            String imageUrl = "http://bloomapp.in" + subProduct.getCategory_thumbnail();
            Log.d(TAG, "onBindViewHolder: imageURL " + imageUrl);
            CommonUtils.loadImageWithGlide(context, imageUrl, productImage, false);
        }
    }

    @Override
    public int getItemCount() {
        return subProducts.size();
    }

    private SubProduct getItemAt(int position) {
        if (position < 0 || position > subProducts.size()) {
            Log.d(TAG, "getItemAt: INVALID POSITION");
        }
        return subProducts.get(position);
    }
}
