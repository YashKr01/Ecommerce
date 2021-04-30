package com.shopping.bloom.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.CategoryTypes;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.ArrayList;
import java.util.List;


public class CategoryTypesAdapter extends RecyclerView.Adapter<CategoryTypesAdapter.CategoryTypesViewHolder> {
    private static final String TAG = CategoryTypesAdapter.class.getName();

    private List<CategoryTypes> categoryTypesList;
    Context context;

    public CategoryTypesAdapter(Context context) {
        this.context = context;
        categoryTypesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoryTypesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_category_type, parent, false
        );
        return new CategoryTypesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryTypesViewHolder holder, int position) {
        CategoryTypes categoryTypes = categoryTypesList.get(position);

        holder.setUpData(categoryTypes, context);
        holder.clRootLayout.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                boolean isSelected = categoryTypesList.get(position).isSelected();
                categoryTypesList.get(position).setSelected(!isSelected);
                holder.changeBackground(!isSelected, context);
            }
        });
    }


    static class CategoryTypesViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout clRootLayout;
        TextView tvCategoryText;
        public CategoryTypesViewHolder(@NonNull View itemView) {
            super(itemView);
            clRootLayout = itemView.findViewById(R.id.clRootLayout);
            tvCategoryText = itemView.findViewById(R.id.tvCategoryType);
        }

        void setUpData(CategoryTypes categoryTypes, Context context) {
            tvCategoryText.setText(categoryTypes.getCategoryName());
            changeBackground(categoryTypes.isSelected(), context);
            if (categoryTypes.isSelected()) {
                clRootLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_category_type_selected));
            } else {
                clRootLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_category_type));
            }
        }

        void changeBackground(boolean value, Context context) {
            if (value) {
                clRootLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_category_type_selected));
            } else {
                clRootLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_category_type));
            }
        }
    }

    public void clearAllSelection() {
        if (categoryTypesList == null || categoryTypesList.size() == 0) return;
        for (int i = 0; i < categoryTypesList.size(); i++) {
            categoryTypesList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    public List<CategoryTypes> getSelectedItems() {
        List<CategoryTypes> selectedItems = new ArrayList<>();
        if (categoryTypesList == null || categoryTypesList.size() == 0) return selectedItems;
        for (int i = 0; i < categoryTypesList.size(); i++) {
            if (categoryTypesList.get(i).isSelected()) {
                selectedItems.add(categoryTypesList.get(i));
            }
        }
        return selectedItems;
    }

    public String getSelectedItemsString() {
        StringBuilder selectedItems = new StringBuilder();
        if (categoryTypesList == null || categoryTypesList.size() == 0) return "";
        for (int i = 0; i < categoryTypesList.size(); i++) {
            if (categoryTypesList.get(i).isSelected()) {
                selectedItems.append(categoryTypesList.get(i).getCategoryId());
                selectedItems.append(",");
            }
        }
        if (selectedItems.toString().isEmpty()) return "";
        return selectedItems.toString().substring(0, selectedItems.length() - 1);
    }

    public void updateList(List<CategoryTypes> list) {
        if (list == null) return;
        categoryTypesList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categoryTypesList.size();
    }


}
