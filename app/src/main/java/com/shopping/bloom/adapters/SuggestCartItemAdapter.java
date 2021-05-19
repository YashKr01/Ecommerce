package com.shopping.bloom.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.ProductSuggestion;
import com.shopping.bloom.restService.callback.SuggestedProductClickListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SuggestCartItemAdapter extends RecyclerView.Adapter<SuggestCartItemAdapter.SuggestedCartItemViewHolder> {

    private static final String TAG = SuggestCartItemAdapter.class.getName();

    private List<ProductSuggestion> suggestedCartProducts;
    private SuggestedProductClickListener mListener;
    private Context context;

    public SuggestCartItemAdapter(Context context, SuggestedProductClickListener listener) {
        this.context = context;
        mListener = listener;
        suggestedCartProducts = new ArrayList<>();
    }

    @Override
    public @NotNull SuggestedCartItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggest_cart_item, parent, false);
        return new SuggestedCartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SuggestCartItemAdapter.SuggestedCartItemViewHolder holder, int position) {
        ProductSuggestion product = suggestedCartProducts.get(position);
        holder.setUpData(context, product);
        holder.viewAddToCart.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                Log.d(TAG, "onDebouncedClick: " + position);
                Log.d(TAG, "onDebouncedClick: " + product.getId());
                mListener.onProductAdd(product, position);
            }
        });
    }

    public void removeItem(int position) {
        if(suggestedCartProducts != null && !suggestedCartProducts.isEmpty()) {
            if(position < suggestedCartProducts.size()) {
                suggestedCartProducts.remove(position);
                notifyDataSetChanged();
            }
        }
    }


    static class SuggestedCartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProductImage;
        TextView tvProductName;
        View viewAddToCart;

        public SuggestedCartItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            viewAddToCart = itemView.findViewById(R.id.viewAddToCart);
            imgProductImage = itemView.findViewById(R.id.imgProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
        }

        public void setUpData(Context context, ProductSuggestion product) {
            String imageURL = Const.GET_BASE_URL + product.getPrimary_image();
            if(product.getColorsImageArray() != null && !product.getColorsImageArray().isEmpty()) {
                imageURL = Const.GET_BASE_URL + product.getColorsImageArray().get(0).getPrimary_image();
            }
            CommonUtils.loadImageWithGlide(
                    context, imageURL, imgProductImage, false
            );
            String price = "";
            if (product.is_on_sale.equals("1")) {
                price = product.sale_price;
            } else {
                price = product.price;
            }
            tvProductName.setText(price);
        }
    }

    @Override
    public int getItemCount() {
        if(suggestedCartProducts == null) return 0;
        return suggestedCartProducts.size();
    }

    public void updateList(List<ProductSuggestion> list) {
        if (suggestedCartProducts == null) {
            suggestedCartProducts = new ArrayList<>(list);
        } else {
            suggestedCartProducts.clear();
            suggestedCartProducts.addAll(list);
        }
        notifyDataSetChanged();
    }

}
