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
import com.shopping.bloom.restService.callback.LoadMoreItems;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class RandomProductAdapter extends RecyclerView.Adapter<RandomProductAdapter.RandomProductViewHolder> {
    private static final String TAG = RandomProductAdapter.class.getName();

    Context context;
    ArrayList<Product> randomProductList;
    ProductClickListener mListener;
    LoadMoreItems loadMoreItems;
    private int LAST_ITEM = -1;

    public RandomProductAdapter(Context context, List<Product> randomProductList, ProductClickListener mListener, LoadMoreItems loadMoreItems) {
        this.context = context;
        this.randomProductList = new ArrayList<>(randomProductList);
        this.mListener = mListener;
        this.loadMoreItems = loadMoreItems;
    }

    //todo remove dummy list and image view image loading
    public void updateList(List<Product> list) {
        if (randomProductList == null) {
            randomProductList = new ArrayList<>(list);
        } else {
            randomProductList.clear();
            randomProductList.addAll(list);
        }
        Product dummyItem = new Product(LAST_ITEM, "", "", "", ""
                , "", "", "", "","","",
                "","","","","",false);
        randomProductList.add(dummyItem);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RandomProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == LAST_ITEM) {
            Log.d(TAG, "onCreateViewHolder: ");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_see_more, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_random_product, parent, false);
        }
        return new RandomProductViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Product product = randomProductList.get(position);
        if (product.getId() == LAST_ITEM && position == getItemCount() - 1) return LAST_ITEM;
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RandomProductViewHolder holder, int position) {
        Product product = getItemAt(position);

        if (getItemViewType(position) == LAST_ITEM) {
            if (loadMoreItems != null) {
                holder.rootView.setOnClickListener((view -> loadMoreItems.loadMore()));
            }
        } else {
            holder.setUpData(context, product);
            holder.rootView.setOnClickListener((view -> mListener.onProductClicked(product)));
        }
    }

    public static class RandomProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productPrice;      //could be product name
        ConstraintLayout rootView;

        public RandomProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.img_wishlist);
            productPrice = itemView.findViewById(R.id.txt_wishlist_name);
            rootView = itemView.findViewById(R.id.clProductRootView);
        }

        public void setUpData(Context context, Product subCategory) {
            String price = CommonUtils.getSignedAmount(subCategory.getPrice());
            productPrice.setText(price);
            String imageURL = "http://bloomapp.in" + subCategory.getPrimary_image();
            Log.d(TAG, "onBindViewHolder: imageURL " + imageURL);
            CommonUtils.loadImageWithGlide(context, imageURL, productImage, false);
        }
    }

    @Override
    public int getItemCount() {
        return randomProductList.size();
    }

    private Product getItemAt(int position) {
        if (position < 0 || position > randomProductList.size()) {
            Log.d(TAG, "getItemAt: INVALID POSITION");
        }
        return randomProductList.get(position);
    }
}
