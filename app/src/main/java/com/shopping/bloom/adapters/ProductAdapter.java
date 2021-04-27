package com.shopping.bloom.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.ColorImageArray;
import com.shopping.bloom.model.LoginWithPassData;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.restService.callback.WishListListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private static final String TAG = ProductAdapter.class.getName();
    private List<Product> productList;
    private WishListListener wishListListener;
    private Context context;

    public ProductAdapter(Context context, WishListListener wishListListener) {
        this.context = context;
        this.wishListListener = wishListListener;
        productList = new ArrayList<>();
    }

    public void updateList(List<Product> products) {
        if(products == null) return ;
        this.productList = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_view_category, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = getItemAt(position);

        holder.setUpData(context, product);

        holder.viewFavorites.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                Log.d(TAG, "onDebouncedClick: ADD to fav");
                boolean isLiked = product.isInUserWishList();
                wishListListener.updateWishList(position, !isLiked);
            }
        });
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProductImage;
        View viewFavorites;
        TextView tvPrice;
        LinearLayout parentColorsLayout;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProductImage = itemView.findViewById(R.id.imgProductImage);
            viewFavorites = itemView.findViewById(R.id.viewFavorites);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            parentColorsLayout = itemView.findViewById(R.id.llColorOptions);
        }

        public void setUpData(Context context, Product product) {
            //1. show primary image
            //2. Change wishListIcon (if already added to wishList)
            //3. Populate color option
            String imageURL = "http://bloomapp.in" + product.getPrimary_image();
            Log.d(TAG, "setUpData: imageURL "+imageURL);
            CommonUtils.loadImageWithGlide(context, imageURL, imgProductImage, true);
            tvPrice.setText(product.getPrice());

            setLiked(context, product.isInUserWishList());

            // Populate the color array and pass the callbackListener to change the Image
            List<ColorImageArray> colors = product.getColorsImageArray();
            if (colors != null && !colors.isEmpty()) {
                addColors(context,colors, (color -> changeImage(context, color)));
            } else {
                parentColorsLayout.removeAllViews();
                parentColorsLayout.removeAllViewsInLayout();
            }
        }

        public void changeImage(Context context, String imagePath) {
            String imageURL = "http://bloomapp.in" + imagePath;
            Log.d(TAG, "ChangeImage: imageURL "+imageURL);
            CommonUtils.loadImageWithGlide(context, imageURL, imgProductImage, true);
        }

        public void setLiked(Context context, boolean isLiked) {
            if (isLiked) {
                viewFavorites.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_fav_added));
            } else {
                viewFavorites.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_favourite_product));
            }
        }

        /*
        *  @params colors is the list of ColorImageArray which contain color, imageURL
        *  @params callback is used to change the image upon clicking the color
        */
        public void addColors(Context context, List<ColorImageArray> colors, ColorClickListener callback) {
            Log.d(TAG, "addColors: arraySize " + colors.size());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            parentColorsLayout.removeAllViewsInLayout();
            parentColorsLayout.removeAllViews();
            for (ColorImageArray color : colors) {
                TextView textView = new TextView(context);
                textView.setText(color.getColor());
                textView.setTextColor(ContextCompat.getColor(context, R.color.blue_grey_900));
                params.rightMargin = 12;
                textView.setTextSize(12f);
                textView.setLayoutParams(params);
                textView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_color_choices));
                textView.setOnClickListener(new DebouncedOnClickListener(200) {
                    @Override
                    public void onDebouncedClick(View v) {
                        callback.onColorSelected(color.getImagePath());
                    }
                });
                parentColorsLayout.addView(textView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private interface ColorClickListener {
        void onColorSelected(String color);
    }

    private List<String> getAllColors(String available_colors) {
        Log.d(TAG, "getAllColors: " + available_colors.toString());
        if (available_colors.isEmpty()) {
            Log.d(TAG, "getAllColors: EMPTY COLOR ARRAY");
            return null;
        }
        List<String> colors;
        colors = Arrays.asList(available_colors.split(","));
        return colors;
    }

    public void updateItem(int position, boolean isAdded) {
        Log.d(TAG, "updateItem: " + isAdded);
        productList.get(position).setInUserWishList(isAdded);
        notifyItemChanged(position);
    }

    public Product getItemAt(int position) {
        if (position < 0 || position > productList.size()) {
            Log.d(TAG, "getItemAt: INVALID POSITION");
        }
        return productList.get(position);
    }

}
