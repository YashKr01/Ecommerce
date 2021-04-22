package com.shopping.bloom.fragment.profilefragment;

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
import android.widget.TextView;

import com.shopping.bloom.R;
import com.shopping.bloom.activities.LoginActivity;
import com.shopping.bloom.adapters.profilefragment.ProfileViewPagerAdapter;
import com.shopping.bloom.databinding.FragmentProfileBinding;
import com.shopping.bloom.utils.LoginManager;


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

        String name = loginManager.getname();

        //Changing textView text if user is Logged in
        if(!loginManager.getEmailid().equals("NA")){
            binding.textView.setText("Hello, " + name);
        }else{ //OnClickListener on textView when user isn't logged in
            binding.textView.setText(getString(R.string.sign_in_register));
            binding.textView.setOnClickListener(v ->{
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            });
        }

       binding.nestscrollview.setNestedScrollingEnabled(true);


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
        Log.d(TAG, "onCreateOptionsMenu: profile" + menu.getItem(0).getTitle());
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