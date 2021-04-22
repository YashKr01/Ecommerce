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
import com.shopping.bloom.model.Category;
import com.shopping.bloom.restService.callback.LoadMoreItems;
import com.shopping.bloom.restService.callback.ProductClickListener;

import java.util.ArrayList;
import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private static final String TAG = CategoryAdapter.class.getName();

    Context context;
    ArrayList<Category> categories;
    ProductClickListener mListener;

    public CategoryAdapter(Context context, ProductClickListener mListener) {
        this.context = context;
        this.mListener = mListener;
        categories = new ArrayList<>();
    }

    public void updateProductList(List<Category> categoryList){
        if(categories == null) {
            categories = new ArrayList<>(categoryList);
        } else {
            categories.clear();
            categories.addAll(categoryList);
        }
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
        Category category = getItemAt(position);
        Log.d(TAG, "onBindViewHolder: productDetail " + category);

        holder.setUpData(context, category, mListener, null);
        //attach the click listener to the category category
        holder.tvProductCategory.setOnClickListener((view -> mListener.onProductClick(category)));
    }

    private Category getItemAt(int position) {
        if(position < 0 || position > categories.size()) {
            Log.d(TAG, "getItemAt: INVALID POSITION " + position);
        }
        return categories.get(position);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductCategory;
        RecyclerView rvSubProduct;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductCategory = itemView.findViewById(R.id.tvProductCategory);
            rvSubProduct = itemView.findViewById(R.id.rv_Products);
        }

        public void setUpData(Context context, Category category, ProductClickListener mListener, LoadMoreItems listener) {
            tvProductCategory.setText(category.getCategory_name());

            NestedProductAdapter nestedProductAdapter = new NestedProductAdapter(context, category.getSub_category(),
                    mListener, null);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 3);
            rvSubProduct.setLayoutManager(layoutManager);
            rvSubProduct.setAdapter(nestedProductAdapter);
            nestedProductAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
