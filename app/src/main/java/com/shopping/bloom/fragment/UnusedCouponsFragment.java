package com.shopping.bloom.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopping.bloom.adapters.UnusedCouponAdapter;
import com.shopping.bloom.databinding.FragmentUnusedCouponsBinding;
import com.shopping.bloom.model.coupons.Coupon;
import com.shopping.bloom.restService.callback.CouponClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.UnusedCouponsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UnusedCouponsFragment extends Fragment {
    private static final String TAG = "UnusedCouponsFragment";

    private FragmentUnusedCouponsBinding binding;
    private List<Coupon> list;
    private UnusedCouponAdapter adapter;
    private UnusedCouponsViewModel viewModel;
    private CouponClickListener clickListener;

    public UnusedCouponsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUnusedCouponsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(UnusedCouponsViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = new ArrayList<>();
        adapter = new UnusedCouponAdapter(list, getContext(), this::pickCoupon);
        binding.unusedCouponsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.unusedCouponsRecyclerView.setAdapter(adapter);

        getCouponsList();
    }

    /**
     * Adding a Coupon item to the list only if it is active,
     * rest of the coupons will be displayed in ExpiredCouponsFragment
     */

    private void getCouponsList() {
        binding.progressBar.setVisibility(View.VISIBLE);

        if (NetworkCheck.isConnect(getContext())) {
            viewModel.getCouponsList().observe(getViewLifecycleOwner(), coupons -> {

                if (coupons != null && coupons.size() > 0) {
                    for (Coupon c : coupons) {
                        if (c.getIsActive().equals("1")) {
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
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        if(context instanceof CouponClickListener){
            clickListener = (CouponClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CouponClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clickListener = null;
    }

    private void pickCoupon(Coupon coupon){
        if(clickListener != null) {
            Log.d(TAG, "pickCoupon: UnusedFragment " + coupon.getDate());
            clickListener.onCouponClickListener(coupon);
        } else {
            Log.d(TAG, "pickCoupon: NULL UnusedFragment ");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}