package com.shopping.bloom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.model.FilterItem;
import com.shopping.bloom.restService.callback.FilterItemClicked;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.ArrayList;
import java.util.List;


public class FilterItemAdapter extends RecyclerView.Adapter<FilterItemAdapter.CategoryTypesViewHolder> {
    private static final String TAG = FilterItemAdapter.class.getName();

    private List<FilterItem> filterItemList;
    Context context;

    public FilterItemAdapter(Context context) {
        this.context = context;
        filterItemList = new ArrayList<>();
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
        FilterItem filterItem = filterItemList.get(position);

        holder.setUpData(filterItem);
        holder.clRootLayout.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                boolean isSelected = filterItemList.get(position).isSelected();
                filterItemList.get(position).setSelected(!isSelected);
                holder.changeBackground(!isSelected);
            }
        });
    }


    static class CategoryTypesViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout clRootLayout;
        TextView tvCategoryText;
        View isSelected;

        public CategoryTypesViewHolder(@NonNull View itemView) {
            super(itemView);
            clRootLayout = itemView.findViewById(R.id.clRootLayout);
            tvCategoryText = itemView.findViewById(R.id.tvCategoryType);
            isSelected = itemView.findViewById(R.id.isSelected);
        }

        void setUpData(FilterItem filterItem) {
            tvCategoryText.setText(filterItem.getCategoryName());

            changeBackground(filterItem.isSelected());
            if (filterItem.isSelected()) {
                isSelected.setVisibility(View.VISIBLE);
            } else {
                isSelected.setVisibility(View.GONE);
            }
        }

        void changeBackground(boolean value) {
            if (value) {
                isSelected.setVisibility(View.VISIBLE);
            } else {
                isSelected.setVisibility(View.GONE);
            }
        }
    }

    public void clearAllSelection() {
        if (filterItemList == null || filterItemList.size() == 0) return;
        for (int i = 0; i < filterItemList.size(); i++) {
            filterItemList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    public void updateList(List<FilterItem> list) {
        if (list == null) return;
        filterItemList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return filterItemList.size();
    }


}
