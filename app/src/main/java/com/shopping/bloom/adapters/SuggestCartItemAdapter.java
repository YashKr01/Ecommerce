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
import com.shopping.bloom.model.SuggestedCartProduct;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SuggestCartItemAdapter extends RecyclerView.Adapter<SuggestCartItemAdapter.SuggestedCartItemViewHolder> {

    private static final String TAG = SuggestCartItemAdapter.class.getName();

    private List<SuggestedCartProduct> suggestedCartProducts;
    private Context context;

    public SuggestCartItemAdapter(Context context) {
        suggestedCartProducts = new ArrayList<>();
        this.context = context;
    }

    @Override
    public @NotNull SuggestedCartItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggest_cart_item, parent, false);
        return new SuggestedCartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SuggestCartItemAdapter.SuggestedCartItemViewHolder holder, int position) {
        SuggestedCartProduct product = suggestedCartProducts.get(position);
        holder.setUpData(context, product);
        holder.viewAddToCart.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                Log.d(TAG, "onDebouncedClick: ");
            }
        });
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


        public void setUpData(Context context, SuggestedCartProduct product) {
            CommonUtils.loadImageWithGlide(
                    context, product.getImageUrl(), imgProductImage, false
            );
            tvProductName.setText(product.getProductName());
        }
    }

    @Override
    public int getItemCount() {
        return suggestedCartProducts.size();
    }

    public void updateList(List<SuggestedCartProduct> list) {
        if(suggestedCartProducts == null) {
            suggestedCartProducts = new ArrayList<>(list);
        } else {
            suggestedCartProducts.clear();
            suggestedCartProducts.addAll(list);
        }
        notifyDataSetChanged();
    }

}
