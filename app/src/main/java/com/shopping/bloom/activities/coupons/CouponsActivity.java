package com.shopping.bloom.activities.coupons;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.coupons.CouponsViewPagerAdapter;
import com.shopping.bloom.databinding.ActivityCouponsBinding;
import com.shopping.bloom.fragment.coupons.ExpiredCouponsFragment;
import com.shopping.bloom.fragment.coupons.UnusedCouponsFragment;

import java.util.Objects;

public class CouponsActivity extends AppCompatActivity {

    private ActivityCouponsBinding binding;
    private CouponsViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCouponsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewPagerAdapter = new CouponsViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new UnusedCouponsFragment(), "Unused Coupons");
        viewPagerAdapter.addFragment(new ExpiredCouponsFragment(), "Expired Coupons");

        binding.couponsTabLayout.setupWithViewPager(binding.couponsViewPager);
        binding.couponsViewPager.setAdapter(viewPagerAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return true;
    }
}