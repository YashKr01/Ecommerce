package com.shopping.bloom.fragment.coupons;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopping.bloom.adapters.coupons.CouponAdapter;
import com.shopping.bloom.databinding.FragmentExpiredCouponsBinding;
import com.shopping.bloom.model.coupons.Coupon;

import java.util.ArrayList;
import java.util.List;

public class ExpiredCouponsFragment extends Fragment {

    private FragmentExpiredCouponsBinding binding;
    private CouponAdapter adapter;
    private List<Coupon> list;

    public ExpiredCouponsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExpiredCouponsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = new ArrayList<>();
        adapter = new CouponAdapter(list, getContext());
        binding.expiredCouponsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.expiredCouponsRecyclerView.setAdapter(adapter);

        list.addAll(mockList());
        adapter.notifyDataSetChanged();

    }

    private List<Coupon> mockList() {

        List<Coupon> temp = new ArrayList<>();
        temp.add(new Coupon("Expires soon", "15.00% OFF", "CODE:saleoctober"
                , "For orders over 1500", "3/13/2021", "For all products"));

        return temp;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}