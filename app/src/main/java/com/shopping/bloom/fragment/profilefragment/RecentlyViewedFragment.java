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

import com.shopping.bloom.activities.SingleProductActivity;
import com.shopping.bloom.adapters.profilefragment.RecentlyViewedAdapter;
import com.shopping.bloom.databinding.FragmentRecentlyViewedBinding;
import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;
import com.shopping.bloom.model.wishlist.WishListData;
import com.shopping.bloom.restService.callback.WishListProductListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewModels.recentlyviewed.RecentlyViewedViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecentlyViewedFragment extends Fragment implements WishListProductListener {

    private FragmentRecentlyViewedBinding binding;
    private RecentlyViewedAdapter adapter;
    private List<RecentlyViewedItem> list;
    private RecentlyViewedViewModel viewModel;

    private String PAGE_NO = "0", LIMIT = "8";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRecentlyViewedBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(RecentlyViewedViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = new ArrayList<>();
        adapter = new RecentlyViewedAdapter(list, getContext(),this);
        binding.recentlyViewedRecyclerView.setNestedScrollingEnabled(false);
        binding.recentlyViewedRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recentlyViewedRecyclerView.setAdapter(adapter);

        getRecentlyViewedList();

        if (list.isEmpty()) {
            binding.viewMore.setVisibility(View.INVISIBLE);
            binding.txtEmpty.setVisibility(View.VISIBLE);
        }

    }

    private void getRecentlyViewedList() {

        if (NetworkCheck.isConnect(getContext())) {
            viewModel.getRecentlyViewedList(PAGE_NO, LIMIT).observe(getViewLifecycleOwner(), recentlyViewedItems -> {

                if (recentlyViewedItems != null && recentlyViewedItems.size() > 0) {
                    binding.txtEmpty.setVisibility(View.INVISIBLE);
                    binding.viewMore.setVisibility(View.VISIBLE);
                    list.clear();
                    list.addAll(recentlyViewedItems);
                    adapter.notifyDataSetChanged();
                } else {
                    binding.txtEmpty.setVisibility(View.VISIBLE);
                }
            });
        } else {
            ShowToast.showToast(getContext(), "Connection not available");
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void wishListItemDelete(WishListData wishListData, int position) {
        //
    }

    @Override
    public void wishListItemCLicked(WishListData wishListData) {
        //
    }

    @Override
    public void recentlyViewedOnClicked(RecentlyViewedItem recentlyViewedItem) {
        Intent intent = new Intent(getContext(), SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", recentlyViewedItem.getId());
        startActivity(intent);
    }
}