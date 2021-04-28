package com.shopping.bloom.fragment;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.R;
import com.shopping.bloom.activities.ViewCategoryActivity;
import com.shopping.bloom.adapters.CategoryAdapter;
import com.shopping.bloom.databinding.FragmentCategoryBinding;
import com.shopping.bloom.model.Category;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.SubCategory;
import com.shopping.bloom.restService.callback.CategoryResponseListener;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.CategoryViewModel;

import java.util.List;

public class CategoryFragment extends Fragment implements ProductClickListener {

    private static final String TAG = CategoryFragment.class.getName();

    private CategoryViewModel viewModel;
    private FragmentCategoryBinding mainBinding;
    private CategoryAdapter categoryImagesAdapter;

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
        initRecyclerView();

        checkNetworkAndFetchData();
        mainBinding.swipeRefreshLayout.setOnRefreshListener(swipeRefreshListener);
    }

    private void initRecyclerView() {
        categoryImagesAdapter = new CategoryAdapter(getContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mainBinding.rvCategory.setLayoutManager(layoutManager);
        mainBinding.rvCategory.setHasFixedSize(true);
        mainBinding.rvCategory.setAdapter(categoryImagesAdapter);
    }

    private final SwipeRefreshLayout.OnRefreshListener swipeRefreshListener = this::checkNetworkAndFetchData;

    private void checkNetworkAndFetchData() {
        PAGE_NO = START_PAGE;
        if (NetworkCheck.isConnect(getContext())) {
            viewModel.setResponseListener(responseListener);
            viewModel.fetchData("1", 20, PAGE_NO, "");
            noInternetAvailable(false);
        } else {
            Log.d(TAG, "checkNetworkAndFetchData: No internet Available");
            mainBinding.swipeRefreshLayout.setRefreshing(false);
            noInternetAvailable(true);
        }
    }

    private final CategoryResponseListener responseListener = new CategoryResponseListener() {
        @Override
        public void onSuccess(List<Category> category) {
            Log.d(TAG, "onSuccess: productSize " + category);
            Log.d(TAG, "onSuccess: productSize " + category.size());
            categoryImagesAdapter.updateProductList(category);
            mainBinding.swipeRefreshLayout.setRefreshing(false);
            noInternetAvailable(false);
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


    //when product header/category is clicked
    @Override
    public void onProductClick(Category categoryCategory) {
        Log.d(TAG, "onProductClick: product clicked " + categoryCategory.toString());
        gotoProductScreen(String.valueOf(categoryCategory.getId()));
    }

    //When particular product image is clicked
    @Override
    public void onSubProductClick(SubCategory product) {
        Log.d(TAG, "onSubProductClick: "+ product.toString());
        if(getContext() != null) {
            gotoProductScreen(product.getParent_id());
            Toast.makeText(getContext(), product.getCategory_name(), Toast.LENGTH_SHORT).show();
        }
    }

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

    private void gotoProductScreen(String categoryId) {
        String ARG_CATEGORY = "category_id";
        Intent intent = new Intent(getActivity(), ViewCategoryActivity.class);
        intent.putExtra(ARG_CATEGORY, categoryId);
        startActivity(intent);
    }

    //set NO INTERNET Image to visible
    private void noInternetAvailable(boolean visible) {
        if (visible) {
            mainBinding.vsEmptyScreen.setVisibility(View.VISIBLE);
        } else {
            mainBinding.vsEmptyScreen.setVisibility(View.GONE);
        }
    }

}