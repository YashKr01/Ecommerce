package com.shopping.bloom.fragment.profilefragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shopping.bloom.activities.SingleProductActivity;
import com.shopping.bloom.activities.wishlist.WishListActivity;
import com.shopping.bloom.adapters.profilefragment.WishListAdapter;
import com.shopping.bloom.databinding.FragmentWishListBinding;
import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;
import com.shopping.bloom.model.wishlist.WishListData;
import com.shopping.bloom.restService.callback.WishListProductListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.wishlist.WishListViewModel;


import java.util.ArrayList;
import java.util.List;

public class WishListFragment extends Fragment implements WishListProductListener {

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
        adapter = new WishListAdapter(list, getContext(), this);
        binding.wishListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.wishListRecyclerView.setAdapter(adapter);
        binding.wishListRecyclerView.setNestedScrollingEnabled(false);

        // navigate to wish list activity
        binding.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), WishListActivity.class));
                getParentFragment().getActivity().finish();
            }
        });

        // get wish list
        getWishList(PAGE_NO, LIMIT);

        if (list.isEmpty()) {
            binding.viewMore.setVisibility(View.INVISIBLE);
            binding.txtEmpty.setVisibility(View.VISIBLE);
        }

    }

    private void getWishList(String page_no, String limit) {

        if (NetworkCheck.isConnect(getContext())) {
            viewModel.getWishList(page_no, limit).observe(getViewLifecycleOwner(), wishListData -> {
                if (wishListData != null && wishListData.size() > 0) {
                    binding.txtEmpty.setVisibility(View.INVISIBLE);
                    binding.viewMore.setVisibility(View.VISIBLE);
                    list.clear();
                    list.addAll(wishListData);
                    adapter.notifyDataSetChanged();
                } else {
                    binding.txtEmpty.setVisibility(View.VISIBLE);
                }
            });
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

    @Override
    public void wishListItemDelete(WishListData wishListData, int position) {
    }

    @Override
    public void wishListItemCLicked(WishListData wishListData) {
        Intent intent = new Intent(getActivity(), SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", wishListData.getId());
        startActivity(intent);
    }

    @Override
    public void recentlyViewedOnClicked(RecentlyViewedItem recentlyViewedItem) {

    }

}