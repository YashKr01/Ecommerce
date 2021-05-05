package com.shopping.bloom.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.FilterItemAdapter;
import com.shopping.bloom.adapters.PaginationListener;
import com.shopping.bloom.adapters.AllProductsAdapter;
import com.shopping.bloom.bottomSheet.SortBottomSheet;
import com.shopping.bloom.database.repository.ProductRepository;
import com.shopping.bloom.model.FilterArrayValues;
import com.shopping.bloom.model.FilterItem;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.ProductFilter;
import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.restService.callback.FetchFilterListener;
import com.shopping.bloom.restService.callback.FilterItemClicked;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.restService.callback.WishListListener;
import com.shopping.bloom.restService.callback.WishListUploadedCallback;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.Const.FILTER;
import com.shopping.bloom.utils.Const.SORT_BY;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.ProductsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllProductCategory extends AppCompatActivity {

    private static final String TAG = AllProductCategory.class.getName();

    private ProductsViewModel viewModel;
    //views
    private SwipeRefreshLayout refreshLayout, retryConnecting;
    private RecyclerView rvProducts;
    private AllProductsAdapter productAdapter;
    private RecyclerView rvFilter;
    private FilterItemAdapter filterItemAdapter;
    private Toolbar toolbar;
    private LinearLayout llSort, llFilter;
    private ConstraintLayout cltFilter;
    private TextView tvSortBy;
    private TextView tvCategory, tvType, tvColor, tvSize;

    private final int START_PAGE = 0;
    private int CURRENT_PAGE = 0;
    private String PARENT_ID = "";
    private String SUB_CATEGORY_ID = "";
    private boolean IS_LOADING = false, IS_LAST_PAGE = false;
    private boolean WISHLIST_CHANGED = false;
    private boolean IS_FILTER_FETCH_COMPLETE = false;
    final int SORT_BOTTOM_SHEET = 0;
    final int FILTER_BOTTOM_SHEET = 1;
    List<FilterItem> flCategory, flColor, flSize, flType;
    List<FilterItem> savedCategory, savedColor, savedSize, savedType;
    ProductFilter MAIN_FILTER = new ProductFilter();
    FilterArrayValues filterArrayValues = null;
    SORT_BY DEFAULT_SORT_VALUE = null;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_product_category);

        initViews();
        setUpRecycleView();
        getIntentData();

        viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        checkNetworkAndFetchData();

        refreshLayout.setOnRefreshListener(() -> {
            reset();    //reset the UI and filters
            checkNetworkAndFetchData();
        });
        retryConnecting.setOnRefreshListener(this::checkNetworkAndFetchData);
    }


    private void initViews() {
        rvProducts = findViewById(R.id.rvViewCategory);
        rvFilter = findViewById(R.id.rvFilter);
        toolbar = findViewById(R.id.toolbar);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        retryConnecting = findViewById(R.id.swipeNoInternet);
        llSort = findViewById(R.id.llSortLayout);
        llFilter = findViewById(R.id.llFilterLayout);
        cltFilter = findViewById(R.id.cltFilterSheet);
        tvSortBy = findViewById(R.id.tvSortBy);

        tvCategory = findViewById(R.id.tvCategory);
        tvType = findViewById(R.id.tvType);
        tvColor = findViewById(R.id.tvColor);
        tvSize = findViewById(R.id.tvSize);

        //SetUpToolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener((view -> onBackPressed()));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        /*
         *   filter item list
         * */
        flCategory = new ArrayList<>();
        flColor = new ArrayList<>();
        flSize = new ArrayList<>();
        flType = new ArrayList<>();
        /*
         *   persistence filter list
         *      when user open the filter bottom sheet
         *       populate the last saved filter
         * */
        savedCategory = new ArrayList<>();
        savedType = new ArrayList<>();
        savedSize = new ArrayList<>();
        savedColor = new ArrayList<>();

        findViewById(R.id.btClose).setOnClickListener(clickListener);
        findViewById(R.id.btApplyFilter).setOnClickListener(clickListener);
        findViewById(R.id.imgClose).setOnClickListener(clickListener);
        llSort.setOnClickListener(clickListener);
        llFilter.setOnClickListener(clickListener);

        tvCategory.setOnClickListener(changeCategory);
        tvType.setOnClickListener(changeCategory);
        tvColor.setOnClickListener(changeCategory);
        tvSize.setOnClickListener(changeCategory);
    }

    private void getIntentData() {
        String ARG_CATEGORY_ID = "category_id";
        String ARG_CATEGORY_NAME = "category_name";
        String ARG_SUB_CATEGORY_LIST = "sub_category_list";
        String ARG_BUNDLE = "app_bundle_name";


        Bundle bundle = getIntent().getBundleExtra(ARG_BUNDLE);
        String parentId;

        if (bundle != null) {
            Log.d(TAG, "getIntentData: " + bundle.toString());

            parentId = bundle.getString(ARG_CATEGORY_ID, "1");
            PARENT_ID = parentId;
            String title = bundle.getString(ARG_CATEGORY_NAME, "ECOMMERCE..");
            this.setTitle(title);
            flCategory = bundle.getParcelableArrayList(ARG_SUB_CATEGORY_LIST);
            if (flCategory != null && flCategory.size() > 0) {
                changeSelectedTextTo(tvCategory.getId());
                filterItemAdapter.updateList(flCategory);
                tvCategory.setVisibility(View.VISIBLE);
            } else {
                changeSelectedTextTo(tvSize.getId());
                filterItemAdapter.updateList(flSize);
                tvCategory.setVisibility(View.GONE);
            }
        } else {
            Log.d(TAG, "getIntentData: NULL BUNDLE NO DATA RECEIVED");
        }
    }

    private void setUpRecycleView() {
        productAdapter = new AllProductsAdapter(this, updateWishList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setHasFixedSize(true);
        rvProducts.setAdapter(productAdapter);

        filterItemAdapter = new FilterItemAdapter(this, new FilterItemClicked() {
            @Override
            public void onItemClicked(FilterItem filterItem) {

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvFilter.setHasFixedSize(true);
        rvFilter.setLayoutManager(linearLayoutManager);
        rvFilter.setAdapter(filterItemAdapter);


        //Disable blink animation when updating the items
        ((SimpleItemAnimator) rvProducts.getItemAnimator()).setSupportsChangeAnimations(false);

        rvProducts.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                checkNetworkAndFetchData();
            }

            @Override
            public boolean isLastPage() {
                return IS_LAST_PAGE;
            }

            @Override
            public boolean isLoading() {
                return IS_LOADING;
            }
        });

    }

    private final DebouncedOnClickListener clickListener = new DebouncedOnClickListener(200) {
        @Override
        public void onDebouncedClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.llSortLayout) {
                openBottomSheet(SORT_BOTTOM_SHEET);
            }
            if (viewId == R.id.llFilterLayout) {
                updateSelection();
                openBottomSheet(FILTER_BOTTOM_SHEET);
            }
            if (viewId == R.id.btClose) {
                showOrHideSheet(cltFilter, false);
            }
            if (viewId == R.id.btApplyFilter) {
                //save list data
                saveSelectionData();
                updateFilter(SORT_BY.FILTERS);
                showOrHideSheet(cltFilter, false);
            }
            if (viewId == R.id.imgClose) {
                showOrHideSheet(cltFilter, false);
            }
        }
    };

    private void updateSelection() {
        Log.d(TAG, "updateSelection: ");
        if (flCategory != null) {
            if (savedCategory.isEmpty()) {
                for (FilterItem filterItem : flCategory) {
                    filterItem.setSelected(false);
                }
            } else {
                createADeepCopy(savedCategory, flCategory);
            }
        }

        if (savedColor.isEmpty()) {
            for (FilterItem filterItem : flColor) {
                filterItem.setSelected(false);
            }
        } else {
            createADeepCopy(savedColor, flColor);
        }

        if (savedSize.isEmpty()) {
            for (FilterItem filterItem : flSize) {
                filterItem.setSelected(false);
            }
        } else {
            createADeepCopy(savedSize, flSize);
        }
        if (savedType.isEmpty()) {
            for (FilterItem filterItem : flType) {
                filterItem.setSelected(false);
            }
        } else {
            createADeepCopy(savedType, flType);
        }

        filterItemAdapter.notifyDataSetChanged();
    }

    private void saveSelectionData() {
        if (flCategory != null) {
            createADeepCopy(flCategory, savedCategory);
        }
        createADeepCopy(flType, savedType);
        createADeepCopy(flSize, savedSize);
        createADeepCopy(flColor, savedColor);
    }

    private void createADeepCopy(List<FilterItem> from, List<FilterItem> to) {
        if (from == null || to == null) return;
        to.clear();
        for (FilterItem item : from) {
            FilterItem f = new FilterItem(item.getCategoryName(),
                    item.getCategoryId(), item.getParentId(),
                    item.isSelected(), item.getFilterType());
            to.add(f);
        }
    }

    private final DebouncedOnClickListener changeCategory = new DebouncedOnClickListener(300) {
        @Override
        public void onDebouncedClick(View v) {
            int viewId = v.getId();
            Log.d(TAG, "onDebouncedClick: change category");
            changeSelectedTextTo(viewId);
            if (viewId == R.id.tvCategory) {
                filterItemAdapter.updateList(flCategory);
            }
            if (viewId == R.id.tvColor) {
                filterItemAdapter.updateList(flColor);
            }
            if (viewId == R.id.tvSize) {
                filterItemAdapter.updateList(flSize);
            }
            if (viewId == R.id.tvType) {
                filterItemAdapter.updateList(flType);
            }
        }
    };

    private void changeSelectedTextTo(int viewId) {
        int[] ids = {R.id.tvCategory, R.id.tvColor, R.id.tvSize, R.id.tvType};
        for (int id : ids) {
            TextView textView = findViewById(id);
            if (id == viewId) {
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            } else {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                textView.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_50));
            }
        }
    }

    private final WishListListener updateWishList = new WishListListener() {
        @Override
        public void updateWishList(int position, boolean isAdded) {
            WISHLIST_CHANGED = true;
            Log.d(TAG, "updateWishList: " + isAdded);
            Product product = productAdapter.getItemAt(position);
            productAdapter.updateItem(position, isAdded);
            WishListItem wishListItem = new WishListItem(String.valueOf(product.getId()), LoginManager.getInstance().gettoken());
            if (isAdded) {
                viewModel.addToWishList(wishListItem);
            } else {
                viewModel.removeFromWishList(wishListItem);
            }
        }

        @Override
        public void productClicked(Product product) {
            //Open single product screen
            openSingleProductActivity(product);
        }
    };

    private void checkNetworkAndFetchData() {
        if (NetworkCheck.isConnect(this)) {
            if (!IS_FILTER_FETCH_COMPLETE) {
                fetchFilterAndUpdateUI();
            }
            if (WISHLIST_CHANGED) {
                //Upload the wishListItems to the server
                viewModel.uploadWishListOnServer(this.getApplication(), wishListUploadedCallback);
            }
            IS_LOADING = true;
            viewModel.setResponseListener(responseListener);
            viewModel.fetchData(PARENT_ID, CURRENT_PAGE, MAIN_FILTER);
        } else {
            showNoInternetImage(true);
        }
    }

    private void fetchFilterAndUpdateUI() {
        viewModel.getAvailableColorAndSize(fetchFilter);
    }

    private final FetchFilterListener fetchFilter = new FetchFilterListener() {
        @Override
        public void fetchOnSuccess(FilterArrayValues filterValues) {
            Log.d(TAG, "fetchOnSuccess: ");
            IS_FILTER_FETCH_COMPLETE = true;
            filterArrayValues = filterValues;
            updateFilterList(filterArrayValues);
        }

        @Override
        public void fetchOnFailed(int errorCode, String message) {
            Log.d(TAG, "fetchFailed: errorCode " + errorCode + " message " + message);
        }
    };

    private void updateFilterList(FilterArrayValues filterValues) {
        if (filterValues == null) return;

        if (!filterValues.getSizes().isEmpty()) {
            flSize.clear();
            for (String size : filterValues.getSizes()) {
                FilterItem item = new FilterItem(size, "", "", false, Const.FILTER.LENGTH);
                flSize.add(item);
            }
        }
        if (filterValues.getTypes() != null && !filterValues.getTypes().isEmpty()) {
            flType.clear();
            for (String type : filterValues.getTypes()) {
                FilterItem item = new FilterItem(type, "", "", false, Const.FILTER.TYPE);
                flType.add(item);
            }

        }
        if (!filterValues.getColors().isEmpty()) {
            flColor.clear();
            for (String color : filterValues.getColors()) {
                FilterItem item = new FilterItem(color, "", "", false, Const.FILTER.COLOR);
                flColor.add(item);
            }
        }
    }

    private final ProductResponseListener responseListener = new ProductResponseListener() {
        @Override
        public void onSuccess(List<Product> products) {
            IS_LOADING = false;
            RETRY_ATTEMPT = 0;
            refreshLayout.setRefreshing(false);
            showNoInternetImage(false);
            showEmptyScreen(products.isEmpty());
            if (CURRENT_PAGE == 0) {
                productAdapter.updateList(products);
                rvProducts.smoothScrollToPosition(0);       //CHECK for bugs
            } else {
                productAdapter.addProductList(products);
            }
            CURRENT_PAGE++;
        }

        @Override
        public void onFailure(int errorCode, String message) {
            Log.d(TAG, "onFailure: errorCode " + errorCode + " message " + message);
            refreshLayout.setRefreshing(false);
            if (errorCode == 200) {
                IS_LAST_PAGE = true;
                return;
            }
            IS_LOADING = false;
            RETRY_ATTEMPT++;
            if (RETRY_ATTEMPT < MAX_RETRY_ATTEMPT) {
                Log.d(TAG, "onFailure: RETRYING request... " + RETRY_ATTEMPT);
                checkNetworkAndFetchData();
            } else {
                RETRY_ATTEMPT = 0;
            }
        }
    };

    private void openBottomSheet(int bottomSheetType) {
        switch (bottomSheetType) {
            case SORT_BOTTOM_SHEET: {
                SortBottomSheet sortBottomSheet = new SortBottomSheet(this);
                sortBottomSheet.setListenerAndDefaultValue((sortBy, value) -> {
                    DEFAULT_SORT_VALUE = sortBy;
                    tvSortBy.setText(value);
                    tvSortBy.setVisibility(View.VISIBLE);
                    updateFilter(sortBy);
                    sortBottomSheet.dismiss();
                }, DEFAULT_SORT_VALUE);
                sortBottomSheet.show();
                return;
            }
            case FILTER_BOTTOM_SHEET: {
                showOrHideSheet(cltFilter, true);
                return;
            }
            default: {
                Log.d(TAG, "openBottomSheet: Invalid values " + bottomSheetType);
            }
        }
    }

    //animate Filter sheet or Sort layout sheet to get an dropDown effects
    // Pass the parent layout LinearLayout
    private void showOrHideSheet(ConstraintLayout sheetLayout, final boolean show) {
        long ANIMATION_DURATION = 250L;
        if (show) {
            //show navigation bar with animation
            sheetLayout.animate().setListener(null);
            sheetLayout.clearAnimation();
            sheetLayout.animate()
                    .alpha(1.0f).translationY(0f).setDuration(ANIMATION_DURATION);
            sheetLayout.setVisibility(View.VISIBLE);
        } else {
            //hide bottom navigation bar with animation
            sheetLayout.clearAnimation();
            sheetLayout.animate()
                    .alpha(0.0f)
                    .translationY(sheetLayout.getHeight())
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            sheetLayout.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void updateFilter(SORT_BY sortBy) {
        Log.d(TAG, "updateFilter: updating filter... " + sortBy);
        CURRENT_PAGE = 0;
        RETRY_ATTEMPT = 0;
        IS_LAST_PAGE = false;
        ProductFilter newFilter = new ProductFilter();
        if (sortBy == SORT_BY.FILTERS) {
            String categoryIds = getQueryStringFor(flCategory, FILTER.CATEGORY);
            if (!categoryIds.isEmpty()) {
                MAIN_FILTER.setSubCategoryIds(categoryIds);
            } else {
                MAIN_FILTER.setSubCategoryIds(null);
            }

            String colors = getQueryStringFor(flColor, FILTER.COLOR);
            if (!colors.isEmpty()) {
                MAIN_FILTER.setColors(colors);
            } else {
                MAIN_FILTER.setColors(null);
            }

            String type = getQueryStringFor(flType, FILTER.TYPE);
            if (!type.isEmpty()) {
                MAIN_FILTER.setTypes(type);
            } else {
                MAIN_FILTER.setTypes(null);
            }

            String size = getQueryStringFor(flSize, FILTER.LENGTH);
            if (!size.isEmpty()) {
                MAIN_FILTER.setSizes(size);
            } else {
                MAIN_FILTER.setSizes(null);
            }
            Log.d(TAG, "updateFilter: MAIN filter " + MAIN_FILTER.toString());
            checkNetworkAndFetchData();
            return;
        }

        if (sortBy == SORT_BY.NEW_ARRIVAL) {
            newFilter.setNewArrival("1");
        }
        if (sortBy == SORT_BY.MOST_POPULAR) {
            newFilter.setMostPopular("1");
        }
        if (sortBy == SORT_BY.PRICE_HIGH_TO_LOW) {
            newFilter.setPriceHtoL("1");
        }
        if (sortBy == SORT_BY.PRICE_LOW_TO_HIGH) {
            newFilter.setPriceHtoL("0");
        }
        MAIN_FILTER = newFilter;
        Log.d(TAG, "updateFilter: MAIN filter " + MAIN_FILTER.toString());
        checkNetworkAndFetchData();
    }

    private String getQueryStringFor(List<FilterItem> filterItems, FILTER filter) {
        if (filterItems == null || filterItems.isEmpty()) return "";
        List<String> newList = new ArrayList<>();
        for (FilterItem item : filterItems) {
            if (item.isSelected()) {
                if (filter == FILTER.CATEGORY) {
                    newList.add(item.getCategoryId());
                } else {
                    newList.add(item.getCategoryName());
                }
            }
        }
        return CommonUtils.getStringFromList(newList);
    }

    // reset filter, set CURRENT PAGE = 0
    private void reset() {
        Toast.makeText(this, "Reset Everything", Toast.LENGTH_SHORT)
                .show();
        MAIN_FILTER = new ProductFilter();
        CURRENT_PAGE = START_PAGE;
        IS_LAST_PAGE = false;
        RETRY_ATTEMPT = 0;
    }

    private void openSingleProductActivity(Product product) {
        Intent intent = new Intent(this, SingleProductActivity.class);
        intent.putExtra("PRODUCT_ID", product.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void showNoInternetImage(boolean show) {
        retryConnecting.setRefreshing(false);
        refreshLayout.setRefreshing(false);
        if (show) {
            retryConnecting.setVisibility(View.VISIBLE);
            return;
        }
        retryConnecting.setVisibility(View.GONE);
    }

    private void showEmptyScreen(boolean show) {
        if (show) {
            //TODO: show empty here screen when created
            return;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_category, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (cltFilter.getVisibility() == View.VISIBLE) {
            showOrHideSheet(cltFilter, false);
            return;
        }
        Log.d(TAG, "onBackPressed: uploading...");
        viewModel.uploadWishListOnServer(this.getApplication(), null);
        super.onBackPressed();
    }

    private final WishListUploadedCallback wishListUploadedCallback = new WishListUploadedCallback() {
        @Override
        public void onUploadSuccessful() {
            if (WISHLIST_CHANGED) {
                WISHLIST_CHANGED = false;
                checkNetworkAndFetchData();
            }
        }

        @Override
        public void onUploadFailed(int errorCode, String message) {
            Log.d(TAG, "onUploadFailed: errorCode " + errorCode);
            Log.d(TAG, "onUploadFailed: message " + message);
            if (RETRY_ATTEMPT < MAX_RETRY_ATTEMPT) {
                RETRY_ATTEMPT++;
            } else {
                WISHLIST_CHANGED = false;
                RETRY_ATTEMPT = 0;
            }
            checkNetworkAndFetchData();
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Log.d(TAG, "onBackPressed: uploading...");
                ProductRepository.getInstance().uploadWishListOnServer(this.getApplication(), null);
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}