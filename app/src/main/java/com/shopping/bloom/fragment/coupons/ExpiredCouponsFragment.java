package com.shopping.bloom.fragment.coupons;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopping.bloom.adapters.coupons.ExpiredCouponAdapter;
import com.shopping.bloom.adapters.coupons.UnusedCouponAdapter;
import com.shopping.bloom.databinding.FragmentExpiredCouponsBinding;
import com.shopping.bloom.model.coupons.Coupon;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.coupon.ExpiredCouponViewModel;

import java.util.ArrayList;
import java.util.List;

public class ExpiredCouponsFragment extends Fragment {

    private FragmentExpiredCouponsBinding binding;
    private ExpiredCouponAdapter adapter;
    private List<Coupon> list;
    private ExpiredCouponViewModel viewModel;

    public ExpiredCouponsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExpiredCouponsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ExpiredCouponViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = new ArrayList<>();
        adapter = new ExpiredCouponAdapter(list, getContext());
        binding.expiredCouponsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.expiredCouponsRecyclerView.setAdapter(adapter);

        getCouponsList();

    }

    /**
     * Adding a Coupon item to the list only if it is not active,
     * rest of the coupons will be displayed in UnusedCouponsFragment
     */

    private void getCouponsList() {
        binding.progressBar.setVisibility(View.VISIBLE);

        if (NetworkCheck.isConnect(getContext())) {
            viewModel.getCouponsList().observe(getViewLifecycleOwner(), coupons -> {

                if (coupons != null && coupons.size() > 0) {
                    for (Coupon c : coupons) {
                        if (!c.getIsActive().equals("1")) {
                            list.add(c);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                if (list.isEmpty()) binding.txtEmptyList.setVisibility(View.VISIBLE);
                else binding.txtEmptyList.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
            });

        } else {
            binding.progressBar.setVisibility(View.GONE);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}