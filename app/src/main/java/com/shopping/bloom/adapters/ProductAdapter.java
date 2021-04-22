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
import com.shopping.bloom.model.Product;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private static final String TAG = ProductAdapter.class.getName();
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context) {
        this.context = context;
        productList = new ArrayList<>();
    }

    public void updateList(List<Product> products) {
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

        List<String> colors = getAllColors(product.getAvailable_colors().trim());
        if (colors != null) {
            holder.addColors(context, colors, colorOptionSelected);
        } else {
            Log.d(TAG, "onBindViewHolder: NULL COLOR value");
        }

        holder.viewFavorites.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                Log.d(TAG, "onDebouncedClick: ADD to fav");
                holder.setLiked(context, true);
            }
        });
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

    @Override
    public int getItemCount() {
        return productList.size();
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
            String imageURL = "http://bloomapp.in" + product.getPrimary_image();
            Log.d(TAG, "setUpData: imageURL "+imageURL);
            CommonUtils.loadImageWithGlide(context, imageURL, imgProductImage, true);
            tvPrice.setText(product.getPrice()); //TODO: change it to getProductPrice

        }

        public void setLiked(Context context, boolean isLiked) {
            if (isLiked) {
                viewFavorites.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_fav_added));
            } else {
                viewFavorites.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_favourite_product));
            }
        }

        public void addColors(Context context, List<String> colors, DebouncedOnClickListener colorOptionSelected) {
            Log.d(TAG, "addColors: arraySize " + colors.size());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (String color : colors) {
                TextView textView = new TextView(context);
                textView.setText(color);
                textView.setTextColor(ContextCompat.getColor(context, R.color.blue_grey_900));
                params.rightMargin = 12;
                textView.setTextSize(12f);
                textView.setLayoutParams(params);
                textView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_color_choices));
                textView.setOnClickListener(colorOptionSelected);
                parentColorsLayout.addView(textView);
            }
        }
    }

    private final DebouncedOnClickListener colorOptionSelected = new DebouncedOnClickListener(200) {
        @Override
        public void onDebouncedClick(View v) {
            Log.d(TAG, "onDebouncedClick: " + v.getId());
            if(v instanceof TextView) {
                Log.d(TAG, "onDebouncedClick: color " + ((TextView) v).getText());
            }
        }
    };

    private Product getItemAt(int position) {
        if (position < 0 || position > productList.size()) {
            Log.d(TAG, "getItemAt: INVALID POSITION");
        }
        return productList.get(position);
    }

}
