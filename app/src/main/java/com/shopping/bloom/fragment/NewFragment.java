package com.shopping.bloom.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.activities.SingleProductActivity;
import com.shopping.bloom.activities.AllProductCategory;
import com.shopping.bloom.adapters.NewAdapter;
import com.shopping.bloom.databinding.FragmentNewBinding;

import com.shopping.bloom.firebaseConfig.RemoteConfig;
import com.shopping.bloom.model.newfragment.NewFragmentConfig;
import com.shopping.bloom.model.newfragment.NewProduct;
import com.shopping.bloom.model.newfragment.NewProductCategory;
import com.shopping.bloom.restService.callback.NewProductOnClick;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.NewFragmentViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, NewProductOnClick {
    private static final String TAG = NewFragment.class.getName();

    private FragmentNewBinding binding;
    private NewAdapter adapter;
    private NewFragmentViewModel viewModel;
    private List<NewProductCategory> list;

    private NewFragmentConfig config;
    private String imagePath1;
    private String imagePath2;

    //todo need to see api working

    public NewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
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
        adapter = new NewAdapter(getContext(), list, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setNestedScrollingEnabled(false);
        binding.recyclerView.setAdapter(adapter);

        getNewProductsList();

    }

    // method to get new category list
    private void getNewProductsList() {

        binding.progressBar2.setVisibility(View.VISIBLE);

        viewModel.getNewProducts().observe(getViewLifecycleOwner(), newProductCategories -> {

            if (newProductCategories == null || newProductCategories.size() == 0) {
                // empty or null list received
                binding.emptyText.setVisibility(View.VISIBLE);
                binding.progressBar2.setVisibility(View.INVISIBLE);
            } else {
                list.clear();
                list.addAll(newProductCategories);
                adapter.notifyDataSetChanged();
                binding.emptyText.setVisibility(View.INVISIBLE);
                binding.progressBar2.setVisibility(View.INVISIBLE);
            }
        });

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

   /* @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_settings).setVisible(true);
        MenuItem item = menu.findItem(R.id.menu_settings);
        item.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_fragment_menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: new" + menu.getItem(0).getTitle());
    }*/

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
            showNoConnectionLayout(false);
            getNewProductsList();
        }

        binding.newTrendsSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void newProductListener(NewProduct newProduct) {
        Intent intent = new Intent(getActivity(), SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", newProduct.getId());
        startActivity(intent);
    }

    @Override
    public void newBannerListener(NewProductCategory newProductCategory) {
        String ARG_CATEGORY_ID = "category_id";
        String ARG_CATEGORY_NAME = "category_name";
        String ARG_SUB_CATEGORY_LIST = "sub_category_list";
        String ARG_BUNDLE = "app_bundle_name";
        Intent intent = new Intent(getContext(), AllProductCategory.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARG_CATEGORY_ID, String.valueOf(newProductCategory.getId()));
        bundle.putString(ARG_CATEGORY_NAME, newProductCategory.getCategoryName());
        bundle.putParcelableArrayList(ARG_SUB_CATEGORY_LIST, null);
        intent.putExtra(ARG_BUNDLE, bundle);
        startActivity(intent);
    }
}