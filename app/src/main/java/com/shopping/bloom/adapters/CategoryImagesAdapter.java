package com.shopping.bloom.adapters;

import android.content.Context;
import android.support.v4.media.session.IMediaControllerCallback;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.restService.RetrofitBuilder;
import com.shopping.bloom.restService.callback.CategoryImageClickListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoryImagesAdapter extends RecyclerView.Adapter<CategoryImagesAdapter.CategoryImageViewHolder> {
    private static final String TAG = CategoryImagesAdapter.class.getName();

    private ArrayList<Product> products;
    private Context mContext;
    private CategoryImageClickListener mListener;
    private int LARGE_IMAGE = -1;

    public CategoryImagesAdapter(Context mContext, CategoryImageClickListener mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
        products = new ArrayList<>();
    }

    /*Below comparator is used to sort the list to lift the Large screen thumbnails*/
    private final Comparator<Product> COMP = (product, t1) -> {
        return t1.getIs_bigthumbnail_show().compareTo(product.getIs_bigthumbnail_show());
    };

    @NonNull
    @Override
    public CategoryImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater =  LayoutInflater.from(parent.getContext());
        if(viewType == LARGE_IMAGE){
            view = inflater.inflate(R.layout.item_category_large_image, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_category_image, parent, false);
        }
        return new CategoryImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryImageViewHolder holder, int position) {
        Product product = getItemAt(position);
        String IMAGE_URL = "http://bloomapp.in";

        if(getItemViewType(position) == LARGE_IMAGE) {
            IMAGE_URL = IMAGE_URL + product.getBig_thumbnail();
        } else {
            IMAGE_URL = IMAGE_URL + product.getSquare_thumbnail();
        }

        holder.setUpData(mContext, IMAGE_URL);

        holder.imgCategoryImage.setOnClickListener((view -> mListener.onClick(product)));
    }

    static class CategoryImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategoryImage;

        public CategoryImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategoryImage = itemView.findViewById(R.id.imgCategoryImage);
        }

        public void setUpData(Context context,String imageUrl) {
            Log.d(TAG, "onBindViewHolder: imageURL "+ imageUrl);
            CommonUtils.loadImageWithGlide(context, imageUrl, imgCategoryImage, true);
        }

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

    /*
    *   @params sort is used to lift the Large thumbnail at top
    *       like in home screen Shop Fragment.
    * */
    public void updateList(List<Product> productList, boolean sort) {
        if (sort) {
            Collections.sort(productList, COMP);
        }
        if(products == null) {
            products = new ArrayList<>(productList);
        } else {
            products.clear();
            products.addAll(productList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Product product = getItemAt(position);
        String isLargeIcon = product.getIs_bigthumbnail_show();
        if(isLargeIcon.equals("1")) return LARGE_IMAGE;
        return super.getItemViewType(position);
    }
}
