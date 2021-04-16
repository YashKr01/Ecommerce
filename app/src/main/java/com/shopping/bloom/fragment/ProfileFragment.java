package com.shopping.bloom.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.shopping.bloom.R;
import com.shopping.bloom.adapters.ProfileViewPagerAdapter;
import com.shopping.bloom.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getName();

    private FragmentProfileBinding binding;
    private ProfileViewPagerAdapter viewPagerAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        // Setup ViewPager Adapter
        viewPagerAdapter = new ProfileViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new WishListFragment(), "WishList");
        viewPagerAdapter.addFragment(new RecentlyViewedFragment(), "Recently Viewed");

        // Setting up tabLayout and viewpager
        binding.profileTabLayout.setupWithViewPager(binding.profileViewPager);
        binding.profileViewPager.setAdapter(viewPagerAdapter);


        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_fragment_menu, menu);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}