package com.shopping.bloom.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shopping.bloom.R;
import com.shopping.bloom.activities.ConnectToUsActivity;
import com.shopping.bloom.activities.LoginActivity;
import com.shopping.bloom.activities.CouponsActivity;
import com.shopping.bloom.activities.GiftCardActivity;
import com.shopping.bloom.activities.MyOrdersActivity;
import com.shopping.bloom.adapters.ProfileViewPagerAdapter;
import com.shopping.bloom.databinding.FragmentProfileBinding;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;

import static com.shopping.bloom.utils.Const.LOGIN_ACTIVITY;

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getName();

    private FragmentProfileBinding binding;
    private ProfileViewPagerAdapter viewPagerAdapter;
    LoginManager loginManager;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        loginManager = new LoginManager(getContext());
        getActivity().invalidateOptionsMenu();

        //Changing textView text if user is Logged in
        setName();


        binding.nestscrollview.setNestedScrollingEnabled(true);

        // Setup ViewPager Adapter
        setViewPagerAdapter();

        // Setting up tabLayout and viewpager
        setViewPager();

        binding.imgCoupons.setOnClickListener(new DebouncedOnClickListener(1000) {
            @Override
            public void onDebouncedClick(View v) {
                startActivity(new Intent(getContext(), CouponsActivity.class));
            }
        });

        /**
         *  Orders On click and sending header for recyclerview scroll
         */

        binding.imgUnpaid.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                sendIntent(0);
            }
        });
        binding.imgProcessing.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                sendIntent(1);
            }
        });
        binding.imgShipped.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                sendIntent(2);
            }
        });
        binding.imgReturns.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                sendIntent(3);
            }
        });
        binding.imgSupport.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                startActivity(new Intent(getContext(), ConnectToUsActivity.class));
            }
        });

        binding.imgGiftCard.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                startActivity(new Intent(getContext(), GiftCardActivity.class));
            }
        });

        return binding.getRoot();
    }

    private void setViewPager() {
        binding.profileTabLayout.setupWithViewPager(binding.profileViewPager);
        binding.profileViewPager.setAdapter(viewPagerAdapter);
    }

    private void setName() {
        String name = loginManager.getname();
        if (!loginManager.getEmailid().equals("NA")) {
            binding.textView.setText("Hello, " + name);
        } else { //OnClickListener on textView when user isn't logged in
            binding.textView.setText(getString(R.string.sign_in_register));
            binding.textView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intent, LOGIN_ACTIVITY);
            });
        }
    }

    private void setViewPagerAdapter(){
        viewPagerAdapter = new ProfileViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new WishListFragment(), "WishList");
        viewPagerAdapter.addFragment(new RecentlyViewedFragment(), "Recently Viewed");
    }

    private void sendIntent(Integer integer) {
        Intent intent = new Intent(getActivity(), MyOrdersActivity.class);
        intent.putExtra("RECYCLER_VIEW_POSITION", integer);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_fragment_menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: profile" + menu.getItem(0).getTitle());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onDetach();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_ACTIVITY && resultCode == Activity.RESULT_OK){
            setName();
            setViewPager();
            setViewPagerAdapter();
        }
    }
}