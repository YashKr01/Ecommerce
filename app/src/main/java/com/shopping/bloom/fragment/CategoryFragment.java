package com.shopping.bloom.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.activities.AllProductCategory;
import com.shopping.bloom.adapters.CategoryAdapter;
import com.shopping.bloom.databinding.FragmentCategoryBinding;
import com.shopping.bloom.model.Category;
import com.shopping.bloom.model.FilterItem;
import com.shopping.bloom.model.SubCategory;
import com.shopping.bloom.restService.callback.CategoryClickListener;
import com.shopping.bloom.restService.callback.CategoryResponseListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.ShopViewModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment implements CategoryClickListener {

    private static final String TAG = CategoryFragment.class.getName();

    private ShopViewModel viewModel;
    private FragmentCategoryBinding mainBinding;
    private CategoryAdapter categoryImagesAdapter;

    private int PAGE_NO = 0;
    private final int START_PAGE = 0;

    /*
     *   RETRY POLICY
     *       MAXIMUM Retry attempt = 3
     *          1. First check if (WISHLIST_CHANGE == true) if so then
     *               upload the wishlist to the server and fetch the data again
     *           otherWish fetch the data.
     *       if the request fails then check for (RETRY_ATTEMPT < MAX_RETRY_ATTEMPT) if so then
     *           Request again.
     * */
    private int RETRY_ATTEMPT = 0;
    private final int MAX_RETRY_ATTEMPT = 3;

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

        viewModel = new ViewModelProvider(this).get(ShopViewModel.class);
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
            viewModel.fetchCategoryItems("1", 20, PAGE_NO, "");
        } else {
            Log.d(TAG, "checkNetworkAndFetchData: No internet Available");
            setNoInternetLayout(true);
        }
    }

    private final CategoryResponseListener responseListener = new CategoryResponseListener() {
        @Override
        public void onSuccess(List<Category> category) {
            categoryImagesAdapter.updateProductList(category);
            showEmptyScreen(category.isEmpty());
            setNoInternetLayout(false);
        }

        @Override
        public void onFailure(int errorCode, String errorMessage) {
            Log.d(TAG, "onFailure: errorCode" + errorCode + " errorMessage " + errorMessage);
            RETRY_ATTEMPT++;
            if (RETRY_ATTEMPT < MAX_RETRY_ATTEMPT) {
                Log.d(TAG, "onFailure: RETRYING request... " + RETRY_ATTEMPT);
                checkNetworkAndFetchData();
            } else {
                RETRY_ATTEMPT = 0;
                showEmptyScreen(true);
                setNoInternetLayout(false);
            }
        }
    };


    //when product header/category is clicked
    @Override
    public void onCategoryClicked(Category categoryCategory) {
        Log.d(TAG, "onProductClick: product clicked " + categoryCategory.toString());
        openAllProductScreen(categoryCategory);
    }

    //When particular product image is clicked
    @Override
    public void onSubCategoryClicked(SubCategory product) {
        Log.d(TAG, "onSubProductClick: " + product.toString());
        if (getContext() != null) {
            openAllProductScreen(product);
        }
    }

    private void openAllProductScreen(Category product) {
        /*
         *  send the parent ID and subCategory name to populate in
         *   filter section of the ViewCategory Activity.
         * */

        String ARG_CATEGORY_ID = "category_id";
        String ARG_CATEGORY_NAME = "category_name";
        String ARG_SUB_CATEGORY_LIST = "sub_category_list";
        String ARG_BUNDLE = "app_bundle_name";
        Intent intent = new Intent(getActivity(), AllProductCategory.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARG_CATEGORY_ID, String.valueOf(product.getId()));
        bundle.putString(ARG_CATEGORY_NAME, product.getCategory_name());
        ArrayList<FilterItem> list = new ArrayList<>();
        if (product.getSub_category() != null && !product.getSub_category().isEmpty()) {
            for (int i = 0; i < product.getSub_category().size(); i++) {
                SubCategory subCategory = product.getSub_category().get(i);
                FilterItem filterItem = new FilterItem(
                        subCategory.getCategory_name(),
                        String.valueOf(subCategory.getId()),
                        subCategory.getParent_id());
                list.add(filterItem);
            }
            bundle.putParcelableArrayList(ARG_SUB_CATEGORY_LIST, list);
        }
        intent.putExtra(ARG_BUNDLE, bundle);
        startActivity(intent);
    }

    private void openAllProductScreen(SubCategory subCategory) {
        String ARG_CATEGORY_ID = "category_id";
        String ARG_CATEGORY_NAME = "category_name";
        String ARG_SUB_CATEGORY_LIST = "sub_category_list";
        String ARG_BUNDLE = "app_bundle_name";
        Intent intent = new Intent(getActivity(), AllProductCategory.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARG_CATEGORY_ID, String.valueOf(subCategory.getParent_id()));
        bundle.putString(ARG_CATEGORY_NAME, subCategory.getCategory_name());
        bundle.putParcelableArrayList(ARG_SUB_CATEGORY_LIST, null);
        intent.putExtra(ARG_BUNDLE, bundle);
        startActivity(intent);
    }

    //set NO INTERNET Image to visible
    private void setNoInternetLayout(boolean visible) {
        mainBinding.swipeRefreshLayout.setRefreshing(false);
        if (visible) {
            mainBinding.vsEmptyScreen.setVisibility(View.VISIBLE);
        } else {
            mainBinding.vsEmptyScreen.setVisibility(View.GONE);
        }
    }

    private void showEmptyScreen(boolean show) {
        //TODO: set empty screen
    }

}