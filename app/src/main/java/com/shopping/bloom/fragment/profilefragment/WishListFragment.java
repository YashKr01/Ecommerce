package com.shopping.bloom.fragment.profilefragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shopping.bloom.adapters.profilefragment.WishListAdapter;
import com.shopping.bloom.databinding.FragmentWishListBinding;
import com.shopping.bloom.model.wishlist.WishList;
import com.shopping.bloom.model.wishlist.WishListData;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.wishlist.WishListViewModel;


import java.util.ArrayList;
import java.util.List;

public class WishListFragment extends Fragment {

    private FragmentWishListBinding binding;
    private WishListAdapter adapter;
    private List<WishListData> list;
    private WishListViewModel viewModel;
    private String PAGE_NO = "0", LIMIT = "8";

    public WishListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWishListBinding.inflate(inflater, container, false);
        // initialise view model here
        viewModel = new ViewModelProvider(this).get(WishListViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setup list, recyclerview and adapter
        list = new ArrayList<>();
        adapter = new WishListAdapter(list, getContext());
        binding.wishListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.wishListRecyclerView.setAdapter(adapter);

        // get wish list
        getWishList(PAGE_NO, LIMIT);

    }

    private void getWishList(String page_no, String limit) {


        if (NetworkCheck.isConnect(getContext())) {
            if (viewModel.getWishList(page_no, limit) == null) {
                // null received
                Toast.makeText(getContext(), "Server Error", Toast.LENGTH_SHORT).show();
            } else {

                viewModel.getWishList(PAGE_NO, LIMIT).observe(getViewLifecycleOwner(), wishListData -> {

                    if (wishListData == null || wishListData.size() == 0) {

                    } else {
                        // non null response
                        list.clear();
                        list.addAll(wishListData);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

        } else {
            //handle no connection
            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}