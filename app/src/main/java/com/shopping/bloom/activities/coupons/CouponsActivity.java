package com.shopping.bloom.activities.coupons;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.coupons.CouponsViewPagerAdapter;
import com.shopping.bloom.databinding.ActivityCouponsBinding;
import com.shopping.bloom.fragment.coupons.ExpiredCouponsFragment;
import com.shopping.bloom.fragment.coupons.UnusedCouponsFragment;
import com.shopping.bloom.utils.NetworkCheck;

import java.util.Objects;

public class CouponsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ActivityCouponsBinding binding;
    private boolean isViewPagerInitialised = false;

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

        // SWIPE REFRESH LAYOUT
        binding.swipeRefresh.setOnRefreshListener(this);

        // TOOLBAR SETUP
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


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
}