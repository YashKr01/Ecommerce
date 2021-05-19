package com.shopping.bloom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.shopping.bloom.activities.RecentlyViewedActivity;
import com.shopping.bloom.activities.SingleProductActivity;
import com.shopping.bloom.adapters.RecentlyViewedAdapter;
import com.shopping.bloom.databinding.FragmentRecentlyViewedBinding;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.recentlyviewed.RecentlyViewedItem;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewModels.RecentlyViewedViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecentlyViewedFragment extends Fragment implements ProductClickListener {

    private FragmentRecentlyViewedBinding binding;
    private RecentlyViewedAdapter adapter;
    private List<RecentlyViewedItem> list;
    private RecentlyViewedViewModel viewModel;

    private int PAGE_NO = 0, LIMIT = 8;

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

        viewModel = new ViewModelProvider(requireActivity()).get(RecentlyViewedViewModel.class);
        list = new ArrayList<>();
        setupRecyclerView();


        getRecentlyViewedList();

        if (list.isEmpty()) {
            binding.tvViewMore.setVisibility(View.INVISIBLE);
            binding.tvEmpty.setVisibility(View.VISIBLE);
        }

        binding.tvViewMore.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                startActivity(new Intent(getContext(), RecentlyViewedActivity.class));
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new RecentlyViewedAdapter(getContext(), this);
        binding.rvRecentlyView.setNestedScrollingEnabled(false);
        binding.rvRecentlyView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.rvRecentlyView.setAdapter(adapter);
    }

    private void getRecentlyViewedList() {
        if (!NetworkCheck.isConnect(getContext())) {
            ShowToast.showToast(getContext(), "Connection not available");
            return;
        }

        viewModel.getRecentlyViewedList(PAGE_NO, LIMIT, recentProductCallBack);
    }

    private final ProductResponseListener recentProductCallBack = new ProductResponseListener() {
        @Override
        public void onSuccess(List<Product> products) {
            if(products != null && !products.isEmpty()) {
                binding.tvEmpty.setVisibility(View.INVISIBLE);
                binding.tvViewMore.setVisibility(View.VISIBLE);
                adapter.updateList(products);
                binding.tvViewMore.setVisibility(View.VISIBLE);
                binding.tvEmpty.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onFailure(int errorCode, String message) {
            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.tvViewMore.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onProductClicked(Product product) {
        Intent intent = new Intent(getContext(), SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", product.getId());
        startActivity(intent);
    }
}