package com.shopping.bloom.fragment.newfragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.newfragment.NewAdapter;
import com.shopping.bloom.databinding.FragmentNewBinding;

import com.shopping.bloom.firebaseConfig.RemoteConfig;
import com.shopping.bloom.model.newfragment.NewFragmentConfig;
import com.shopping.bloom.model.newfragment.NewProductCategory;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.newfragment.NewFragmentViewModel;

import java.util.ArrayList;
import java.util.List;


public class NewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = NewFragment.class.getName();

    private FragmentNewBinding binding;
    private NewAdapter adapter;
    private NewFragmentViewModel viewModel;
    private List<NewProductCategory> list;

    private NewFragmentConfig config;
    private String imagePath1;
    private String imagePath2;

    public NewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNewBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(NewFragmentViewModel.class);

        if (!isConnectionEnabled(getContext())) {
            showNoConnectionLayout(true);
        }

        // for remote config
        config = RemoteConfig.getNewFragmentConfig(getContext());

        imagePath1 = config.getImage1();
        imagePath2 = config.getImage2();

        Log.d(TAG, "onCreateView: " + imagePath1 + "\n" + imagePath2);

        // swipe refresh listener
        binding.newTrendsSwipeRefresh.setOnRefreshListener(this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().invalidateOptionsMenu();

        // load remote config images
        loadImages(imagePath1, imagePath2);

        // setup list, adapter and recyclerview
        list = new ArrayList<>();
        adapter = new NewAdapter(getContext(), list);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setAdapter(adapter);

        getNewProductsList();

    }

    // method to get new category list
    private void getNewProductsList() {
        binding.newTrendsSwipeRefresh.setRefreshing(true);

        viewModel.getNewProducts().observe(getViewLifecycleOwner(), newProductCategories -> {

            if (newProductCategories == null || newProductCategories.size() == 0) {
                // empty or null list received
            } else {
                list.clear();
                list.addAll(newProductCategories);
                adapter.notifyDataSetChanged();
            }
        });

        binding.newTrendsSwipeRefresh.setRefreshing(false);
    }

    // load image from remote config in first two images
    private void loadImages(String path1, String path2) {
        if (path1 != null && path2 != null) {
            CommonUtils.loadImageWithGlide(getContext(), path1, binding.image1, true);
            CommonUtils.loadImageWithGlide(getContext(), path2, binding.image2, true);
        }
    }

    // check device connection
    private boolean isConnectionEnabled(Context context) {
        if (NetworkCheck.isConnect(context)) {
            Log.d(TAG, "isConnectionEnabled: " + " TRUE");
            return true;
        } else {
            Log.d(TAG, "isConnectionEnabled: " + " FALSE");
            Toast.makeText(context, R.string.no_internet_connected, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // show no connection layout
    private void showNoConnectionLayout(boolean show) {
        if (show) {
            binding.newFragmentNoConnectionLayout.setVisibility(View.VISIBLE);
        } else {
            binding.newFragmentNoConnectionLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_settings).setVisible(true);
        MenuItem item = menu.findItem(R.id.menu_settings);
        item.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_fragment_menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: new" + menu.getItem(0).getTitle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRefresh() {

        if (!NetworkCheck.isConnect(getContext())) {
            showNoConnectionLayout(true);
        } else {
            getNewProductsList();
        }

        binding.newTrendsSwipeRefresh.setRefreshing(false);
    }

}