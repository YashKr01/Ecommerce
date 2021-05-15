package com.shopping.bloom.activities.coupons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.activities.CheckoutActivity;
import com.shopping.bloom.adapters.coupons.CouponsViewPagerAdapter;
import com.shopping.bloom.databinding.ActivityCouponsBinding;
import com.shopping.bloom.fragment.coupons.ExpiredCouponsFragment;
import com.shopping.bloom.fragment.coupons.UnusedCouponsFragment;
import com.shopping.bloom.model.coupons.Coupon;
import com.shopping.bloom.restService.callback.CouponClickListener;
import com.shopping.bloom.utils.NetworkCheck;

import java.util.Objects;

public class CouponsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CouponClickListener {

    private static final String TAG = "CouponsActivity";

    private ActivityCouponsBinding binding;
    private boolean isViewPagerInitialised = false;
    private String CALLING_ACTIVITY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCouponsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!NetworkCheck.isConnect(this)) {
            showNoConnectionLayout(true);
        } else {
            setupViewPager();
        }
        getIntentData();
        // SWIPE REFRESH LAYOUT
        binding.swipeRefresh.setOnRefreshListener(this);

        // TOOLBAR SETUP
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        String ARG_ACTIVITY_NAME = "calling_activity_name";
        if (intent != null) {
            CALLING_ACTIVITY = intent.getStringExtra(ARG_ACTIVITY_NAME);
        }
    }

    private void setupViewPager() {
        // VIEWPAGER & TAB LAYOUT SETUP
        CouponsViewPagerAdapter viewPagerAdapter = new CouponsViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new UnusedCouponsFragment(), "Unused Coupons");
        viewPagerAdapter.addFragment(new ExpiredCouponsFragment(), "Expired Coupons");
        binding.couponsTabLayout.setupWithViewPager(binding.couponsViewPager);
        binding.couponsViewPager.setAdapter(viewPagerAdapter);
        isViewPagerInitialised = true;
    }

    private void showNoConnectionLayout(boolean show) {
        if (show) binding.noConnectionLayout.setVisibility(View.VISIBLE);
        else binding.noConnectionLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return true;
    }

    @Override
    public void onRefresh() {
        if (NetworkCheck.isConnect(this)) {
            showNoConnectionLayout(false);
            if (!isViewPagerInitialised) {
                setupViewPager();
            }
        } else {
            showNoConnectionLayout(true);
        }
        binding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onCouponClickListener(Coupon coupon) {
        if(CALLING_ACTIVITY == null || CALLING_ACTIVITY.isEmpty()) return ;
        Intent returnIntent = new Intent();
        if (CALLING_ACTIVITY.equals(CheckoutActivity.class.getName())) {
            Log.d(TAG, "onCouponClickListener: " + coupon.getPromoCode());
            returnIntent.putExtra("PROMOCODE", coupon.getPromoCode());
            returnIntent.putExtra("PROMO_OFFER", coupon.getDiscount());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}