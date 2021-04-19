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
import com.shopping.bloom.adapters.WishListAdapter;
import com.shopping.bloom.databinding.FragmentWishListBinding;
import com.shopping.bloom.model.Product;

import java.util.ArrayList;
import java.util.List;

public class WishListFragment extends Fragment {

    private FragmentWishListBinding binding;
    private List<Product> productList;
    private WishListAdapter adapter;

    public WishListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWishListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.progressBar.setVisibility(View.VISIBLE);
        // setup recycler view
        initRecyclerView();
    }

    private void initRecyclerView() {
        productList = new ArrayList<>();
        initList(productList);
        adapter = new WishListAdapter(productList, getContext());
        binding.wishListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2
                , RecyclerView.VERTICAL, false));
        binding.wishListRecyclerView.setHasFixedSize(true);
        binding.wishListRecyclerView.setNestedScrollingEnabled(false);
        binding.wishListRecyclerView.setAdapter(adapter);

        binding.progressBar.setVisibility(View.INVISIBLE);
    }

    // adding dummy data to recycler view
    private void initList(List<Product> list) {
        list.add(new Product(0, null, null, null,
                null, "https://img.faballey.com/Images/Product/SWT00080Z/3.jpg"
                , null, null, "1000", null));
        list.add(new Product(0, null, null, null,
                null, "https://img.faballey.com/Images/Product/SWT00080Z/3.jpg"
                , null, null, "1000", null));
        list.add(new Product(0, null, null, null,
                null, "https://img.faballey.com/Images/Product/SWT00080Z/3.jpg"
                , null, null, "1000", null));
        list.add(new Product(0, null, null, null,
                null, "https://img.faballey.com/Images/Product/SWT00080Z/3.jpg"
                , null, null, "1000", null));
        list.add(new Product(0, null, null, null,
                null, "https://img.faballey.com/Images/Product/SWT00080Z/3.jpg"
                , null, null, "1000", null));
        list.add(new Product(0, null, null, null,
                null, "https://img.faballey.com/Images/Product/SWT00080Z/3.jpg"
                , null, null, "1000", null));
        list.add(new Product(0, null, null, null,
                null, "https://img.faballey.com/Images/Product/SWT00080Z/3.jpg"
                , null, null, "1000", null));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}