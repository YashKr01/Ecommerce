package com.shopping.bloom.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.profilefragment.WishListAdapter;
import com.shopping.bloom.databinding.FragmentRecentlyViewedBinding;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.fragmentshop.RecentlyViewed;

import java.util.ArrayList;
import java.util.List;

public class RecentlyViewedFragment extends Fragment {

    private FragmentRecentlyViewedBinding binding;
    private List<RecentlyViewed> productList;
    private WishListAdapter adapter;

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
        productList = new ArrayList<>();
        initList(productList);
        adapter = new WishListAdapter(productList, getContext());
        binding.recentlyViewedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2
                , RecyclerView.VERTICAL, false));
        binding.recentlyViewedRecyclerView.setHasFixedSize(true);
        binding.recentlyViewedRecyclerView.setNestedScrollingEnabled(false);
        binding.recentlyViewedRecyclerView.setAdapter(adapter);
    }

    private void initList(List<RecentlyViewed> list) {

        list.add(new RecentlyViewed(null, R.color.grey_300, 1000));
        list.add(new RecentlyViewed(null, R.color.grey_300, 300));
        list.add(new RecentlyViewed(null, R.color.grey_300, 349));
        list.add(new RecentlyViewed(null, R.color.grey_300, 899));
        list.add(new RecentlyViewed(null, R.color.grey_300, 229));
        list.add(new RecentlyViewed(null, R.color.grey_300, 1999));
        list.add(new RecentlyViewed(null, R.color.grey_300, 1900));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}