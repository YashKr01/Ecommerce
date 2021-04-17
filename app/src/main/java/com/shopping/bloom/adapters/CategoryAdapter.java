package com.shopping.bloom.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.restService.callback.ProductClickListener;

import java.util.ArrayList;
import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private static final String TAG = CategoryAdapter.class.getName();

    Context context;
    ArrayList<Product> products;
    ProductClickListener mListener;

    public CategoryAdapter(Context context, List<Product> productList, ProductClickListener mListener) {
        this.context = context;
        this.products = new ArrayList<>(productList);
        this.mListener = mListener;
    }

    public void updateProductList(List<Product> productList){
        products.clear();
        products.addAll(productList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_products,parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Product product = getItemAt(position);
        Log.d(TAG, "onBindViewHolder: productDetail " + product);
        holder.tvProductCategory.setText(product.getCategory_name());
        NestedProductAdapter nestedProductAdapter = new NestedProductAdapter(context, product.getSub_category(), mListener);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
        holder.rvSubProduct.setLayoutManager(layoutManager);
        holder.rvSubProduct.setAdapter(nestedProductAdapter);
        nestedProductAdapter.notifyDataSetChanged();

        //attach the click listener to the product category
        holder.tvProductCategory.setOnClickListener((view -> mListener.onProductClick(product)));

    }

    private Product getItemAt(int position) {
        if(position < 0 || position > products.size()) {
            Log.d(TAG, "getItemAt: INVALID POSITION " + position);
        }
        return products.get(position);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductCategory;
        RecyclerView rvSubProduct;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
            rvSubProduct = itemView.findViewById(R.id.rv_Products);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
