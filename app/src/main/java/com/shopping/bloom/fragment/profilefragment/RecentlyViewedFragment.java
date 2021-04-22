package com.shopping.bloom.fragment.profilefragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopping.bloom.adapters.profilefragment.WishListAdapter;
import com.shopping.bloom.databinding.FragmentRecentlyViewedBinding;
import com.shopping.bloom.model.Category;

import java.util.ArrayList;
import java.util.List;

public class RecentlyViewedFragment extends Fragment {

    private FragmentRecentlyViewedBinding binding;
    private List<Category> categoryList;
    private WishListAdapter adapter;

    // URL for loading temporary image
    public static final String IMAGE_URL = "http://bloomapp.in/images/product/product_image_3.png";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecentlyViewedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();

    }

    private void initRecyclerView() {
        categoryList = new ArrayList<>();
        getMockData();
        adapter = new WishListAdapter(categoryList, getContext());
        binding.recentlyViewedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2
                , RecyclerView.VERTICAL, false));
        binding.recentlyViewedRecyclerView.setHasFixedSize(true);
        binding.recentlyViewedRecyclerView.setNestedScrollingEnabled(false);
        binding.recentlyViewedRecyclerView.setAdapter(adapter);
    }

    private List<Category> getMockData() {

        for (int i = 0; i < 10; i++) {
            categoryList.add(new Category(1, null, null, null,
                    null, IMAGE_URL, "1999", null, null
                    , null));
        }
        return categoryList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}