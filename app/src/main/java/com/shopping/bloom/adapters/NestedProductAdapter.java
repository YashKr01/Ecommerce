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

import com.bumptech.glide.Glide;
import com.shopping.bloom.R;
import com.shopping.bloom.models.SubProduct;
import com.shopping.bloom.restService.callback.ProductClickListener;

import java.util.ArrayList;
import java.util.List;

public class NestedProductAdapter extends RecyclerView.Adapter<NestedProductAdapter.NestedProductViewHolder> {
    private static final String TAG = NestedProductAdapter.class.getName();

    Context context;
    ArrayList<SubProduct> subProducts;
    ProductClickListener mListener;

    public NestedProductAdapter(Context context, List<SubProduct> subProducts, ProductClickListener mListener) {
        this.context = context;
        this.subProducts = new ArrayList<>(subProducts);
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public NestedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new NestedProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedProductViewHolder holder, int position) {
        SubProduct subProduct = getItemAt(position);
        holder.productPrice.setText(subProduct.getCategory_name());

        Glide.with(context)
                .load(subProduct.getCategory_thumbnail())
                .into(holder.productImage);

        //Attach ClickListenerTo product image
        holder.rootView.setOnClickListener((view -> mListener.onSubProductClick(subProduct)));
    }

    public static class NestedProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productPrice;      //could be product name
        ConstraintLayout rootView;
        public NestedProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.imgProductImage);
            productPrice = itemView.findViewById(R.id.tvProductPrice);
            rootView = itemView.findViewById(R.id.clProductRootView);
        }
    }

    @Override
    public int getItemCount() {
        return subProducts.size();
    }

    private SubProduct getItemAt(int position) {
        if(position < 0 || position > subProducts.size()) {
            Log.d(TAG, "getItemAt: INVALID POSITION");
        }
        return subProducts.get(position);
    }

}
