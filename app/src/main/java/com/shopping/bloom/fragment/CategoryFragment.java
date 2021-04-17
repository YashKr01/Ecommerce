package com.shopping.bloom.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.shopping.bloom.R;
import com.shopping.bloom.databinding.FragmentCategoryBinding;
import com.shopping.bloom.models.Product;
import com.shopping.bloom.restService.callback.CategoryResponseListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModel.CategoryViewModel;

import java.util.List;

public class CategoryFragment extends Fragment {

    private static final String TAG = CategoryFragment.class.getName();

    private CategoryViewModel viewModel;
    private FragmentCategoryBinding mainBinding;
    private int PAGE_NO = 0;
    private final int START_PAGE = 0;

    public CategoryFragment() {
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
        mainBinding = FragmentCategoryBinding.inflate(inflater, container, false);
        return mainBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().invalidateOptionsMenu();

        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        if (NetworkCheck.isConnect(getContext())) {
            getCategoryData();
        } else {
            noInternetAvailable(true);
        }

        mainBinding.swipeRefreshLayout.setOnRefreshListener(() -> {
            if (NetworkCheck.isConnect(getContext())) {
                noInternetAvailable(false);
                getCategoryData();
            } else {
                mainBinding.swipeRefreshLayout.setRefreshing(false);
                noInternetAvailable(true);
                Log.d(TAG, "onRefresh: No internet available");
            }
        });

    }

    private void getCategoryData() {
        viewModel.setResponseListener(responseListener);
        viewModel.fetchData("1", 20, PAGE_NO, "");
    }

    private CategoryResponseListener responseListener = new CategoryResponseListener() {
        @Override
        public void onSuccess(List<Product> product) {
            mainBinding.swipeRefreshLayout.setRefreshing(false);
            Log.d(TAG, "onSuccess: product " + product);
        }

        @Override
        public void onFailure(int errorCode, String errorMessage) {
            Log.d(TAG, "onFailure: errorCode" + errorCode + " errorMessage " + errorMessage);
            mainBinding.swipeRefreshLayout.setRefreshing(false);
            if (getContext() != null) {
                Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_settings).setVisible(true);
        MenuItem item = menu.getItem(0);
        item.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_fragment_menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: category " + menu.getItem(0).getTitle());
    }

    //Show the no internet image
    private void noInternetAvailable(boolean visible) {
        if (visible) {
            mainBinding.vsEmptyScreen.setVisibility(View.VISIBLE);
        } else {
            mainBinding.vsEmptyScreen.setVisibility(View.GONE);
        }
    }

}