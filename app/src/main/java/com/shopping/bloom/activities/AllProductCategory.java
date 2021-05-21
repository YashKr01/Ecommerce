package com.shopping.bloom.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.AllProductsAdapter;
import com.shopping.bloom.adapters.FilterItemAdapter;
import com.shopping.bloom.adapters.PaginationListener;
import com.shopping.bloom.bottomSheet.SortBottomSheet;
import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.database.repository.ProductRepository;
import com.shopping.bloom.model.FilterArrayValues;
import com.shopping.bloom.model.FilterItem;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.ProductFilter;
import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.restService.callback.FetchFilterListener;
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
    private TextView tvCategory, tvType, tvColor, tvSize, tvDiscount, tvPrice;

    private final int START_PAGE = 0;
    private int CURRENT_PAGE = 0;
    private String PARENT_ID = "";
    private String SUB_CATEGORY_ID = "";
    private boolean IS_LOADING = false, IS_LAST_PAGE = false;
    private boolean WISHLIST_CHANGED = false;
    private boolean IS_FILTER_FETCH_COMPLETE = false;
    final int SORT_BOTTOM_SHEET = 0;
    final int FILTER_BOTTOM_SHEET = 1;
    List<FilterItem> flCategory, flColor, flSize, flType, flDiscount, flPrice;
    List<FilterItem> savedCategory, savedColor, savedSize, savedType, savedDiscount, savedPrice;
    ProductFilter MAIN_FILTER = new ProductFilter();
    FilterArrayValues filterArrayValues = null;
    SORT_BY DEFAULT_SORT_VALUE = null;
    final int REQ_SINGLE_PRODUCT = 202;


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
        Log.d(TAG, "onCreate: ");
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
        tvDiscount = findViewById(R.id.tvDiscount);
        tvPrice = findViewById(R.id.tvPrice);

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
        flDiscount = new ArrayList<>();
        flPrice = new ArrayList<>();
        /*
         *   persistence filter list
         *      when user open the filter bottom sheet
         *       populate the last saved filter
         * */
        savedCategory = new ArrayList<>();
        savedType = new ArrayList<>();
        savedSize = new ArrayList<>();
        savedColor = new ArrayList<>();
        savedDiscount = new ArrayList<>();
        savedPrice = new ArrayList<>();

        findViewById(R.id.tvClearAll).setOnClickListener(clickListener);
        findViewById(R.id.tvApplyFilter).setOnClickListener(clickListener);
        findViewById(R.id.tvClose).setOnClickListener(clickListener);
        llSort.setOnClickListener(clickListener);
        llFilter.setOnClickListener(clickListener);

        tvCategory.setOnClickListener(changeCategory);
        tvType.setOnClickListener(changeCategory);
        tvColor.setOnClickListener(changeCategory);
        tvSize.setOnClickListener(changeCategory);
        tvDiscount.setOnClickListener(changeCategory);
        tvPrice.setOnClickListener(changeCategory);
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
            String title = bundle.getString(ARG_CATEGORY_NAME, "ECOMMERCE");
            this.setTitle(title);
            flCategory = bundle.getParcelableArrayList(ARG_SUB_CATEGORY_LIST);
            if (flCategory != null && flCategory.size() > 0) {
                changeSelectedTextTo(tvCategory.getId());
                filterItemAdapter.updateList(flCategory);
                findViewById(R.id.llCategory).setVisibility(View.VISIBLE);
            } else {
                flCategory = new ArrayList<>();
                changeSelectedTextTo(tvSize.getId());
                filterItemAdapter.updateList(flSize);
                findViewById(R.id.llCategory).setVisibility(View.GONE);
            }
        } else {
            String ARG_LIKED_BUNDLE = "app_liked_bundle";
            bundle = getIntent().getBundleExtra(ARG_BUNDLE);
            if (bundle != null) {
                Log.d(TAG, "getIntentData: NOT NULL");
            } else {
                Log.d(TAG, "getIntentData: NULL");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == REQ_SINGLE_PRODUCT) {
            Log.d(TAG, "onActivityResult: ");
            if (data == null) Log.d(TAG, "onActivityResult: NULL");
            if (resultCode == RESULT_OK && data != null) {
                int isLiked = data.getIntExtra("IS_LIKED", 0);
                Log.d(TAG, "onActivityResult: " + isLiked);
            }
        }
    }

    private void setUpRecycleView() {
        productAdapter = new AllProductsAdapter(this, updateWishList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setHasFixedSize(true);
        rvProducts.setAdapter(productAdapter);

        filterItemAdapter = new FilterItemAdapter(this, (filterItem) -> {
            changeBackground();
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

    /*
    *   Change the background color of the apply filter button
    * */
    private void changeBackground() {
        if(isFilterAdded()) {
            findViewById(R.id.tvClearAll).setVisibility(View.VISIBLE);
            findViewById(R.id.tvApplyFilter).setBackgroundColor(ContextCompat.getColor(this, R.color.blue_grey_900));
        } else {
            findViewById(R.id.tvClearAll).setVisibility(View.GONE);
            findViewById(R.id.tvApplyFilter).setBackgroundColor(ContextCompat.getColor(this, R.color.grey_400));
        }
    }

    private boolean isFilterAdded() {
        for (FilterItem filterItem : flPrice) { if(filterItem.isSelected()) return true; }
        for (FilterItem filterItem : flDiscount) { if(filterItem.isSelected()) return true; }
        for (FilterItem filterItem : flType) { if(filterItem.isSelected()) return true; }
        for (FilterItem filterItem : flSize) { if(filterItem.isSelected()) return true; }
        for (FilterItem filterItem : flCategory) { if(filterItem.isSelected()) return true; }
        for (FilterItem filterItem : flColor) { if(filterItem.isSelected()) return true; }
        return false;
    }

    private final DebouncedOnClickListener clickListener = new DebouncedOnClickListener(200) {
        @Override
        public void onDebouncedClick(View v) {
            Log.d(TAG, "onDebouncedClick: ");
            int viewId = v.getId();
            if (viewId == R.id.llSortLayout) {
                openBottomSheet(SORT_BOTTOM_SHEET);
            }
            if (viewId == R.id.llFilterLayout) {
                updateSelection();
                changeBackground();
                openBottomSheet(FILTER_BOTTOM_SHEET);
            }
            if (viewId == R.id.tvClearAll) {
                clearAllFilter();
                updateFilter(DEFAULT_SORT_VALUE);
                Toast.makeText(AllProductCategory.this, getString(R.string.filter_reset), Toast.LENGTH_SHORT)
                        .show();
                //showOrHideSheet(cltFilter, false);
            }
            if (viewId == R.id.tvApplyFilter) {
                //save list data
                saveSelectionData();
                updateFilter(SORT_BY.FILTERS);
                showOrHideSheet(cltFilter, false);
            }
            if (viewId == R.id.tvClose) {
                showOrHideSheet(cltFilter, false);
            }
        }
    };

    private void clearAllFilter() {
        filterItemAdapter.clearAllSelection(flCategory);
        filterItemAdapter.clearAllSelection(flSize);
        filterItemAdapter.clearAllSelection(flType);
        filterItemAdapter.clearAllSelection(flColor);
        filterItemAdapter.clearAllSelection(flDiscount);
        filterItemAdapter.clearAllSelection(flPrice);
        saveSelectionData();
        changeBackground();
    }

    private void changeCartIcon(LiveData<Integer> cartSize) {
        cartSize.observe(this, integer -> {
            Log.d(TAG, "changeCartIcon: ");
            int size = 0;
            try {
                size = integer;
            } catch (NullPointerException e) {
                size = 0;
                Log.d(TAG, "onChanged: ");
            }
            Log.d(TAG, "changeCartIcon: menu size " + toolbar.getMenu().size());
            MenuItem cartIcon = toolbar.getMenu().findItem(R.id.action_cart);
            if (cartIcon != null) {
                if (size == 0) {
                    cartIcon.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_cart));
                } else {
                    cartIcon.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_cart_red));
                }
            } else {
                Log.d(TAG, "changeCartIcon: NULL CART ICON please check the cartIcon ID for this screen");
            }

        });
    }

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

        if (savedPrice.isEmpty()) {
            for (FilterItem filterItem : flPrice) {
                filterItem.setSelected(false);
            }
        } else {
            createADeepCopy(savedPrice, flPrice);
        }

        if (savedDiscount.isEmpty()) {
            for (FilterItem filterItem : flDiscount) {
                filterItem.setSelected(false);
            }
        } else {
            createADeepCopy(savedDiscount, flDiscount);
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
        createADeepCopy(flDiscount, savedDiscount);
        createADeepCopy(flPrice, savedPrice);
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
            if (viewId == R.id.tvDiscount) {
                filterItemAdapter.updateList(flDiscount);
            }
            if (viewId == R.id.tvPrice) {
                filterItemAdapter.updateList(flPrice);
            }
        }
    };

    private void changeSelectedTextTo(int viewId) {
        int[] ids = {R.id.tvCategory, R.id.tvColor, R.id.tvSize, R.id.tvType, R.id.tvDiscount, R.id.tvPrice};
        int[] selectedLineIds = {R.id.lineCategorySelected, R.id.lineColorSelected,
                R.id.lineSizeSelected, R.id.lineTypeSelected, R.id.lineDiscountSelected,
                R.id.linePriceSelected};
        int idx = 0;
        for (int id : ids) {
            TextView textView = findViewById(id);
            View selectedLine = findViewById(selectedLineIds[idx]);
            if (id == viewId) {
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                selectedLine.setVisibility(View.VISIBLE);
            } else {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                textView.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_50));
                selectedLine.setVisibility(View.INVISIBLE);
            }
            idx++;
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

        if (filterValues.getSizes() != null && !filterValues.getSizes().isEmpty()) {
            flSize.clear();
            for (String size : filterValues.getSizes()) {
                FilterItem item = new FilterItem(size, "", "", false, Const.FILTER.LENGTH);
                flSize.add(item);
            }
            findViewById(R.id.llSize).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.llSize).setVisibility(View.GONE);
        }

        if (filterValues.getTypes() != null && filterValues.getTypes() != null && !filterValues.getTypes().isEmpty()) {
            flType.clear();
            for (String type : filterValues.getTypes()) {
                FilterItem item = new FilterItem(type, "", "", false, Const.FILTER.TYPE);
                flType.add(item);
            }
            findViewById(R.id.llType).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.llType).setVisibility(View.GONE);
        }

        if (filterValues.getColors() != null && !filterValues.getColors().isEmpty()) {
            flColor.clear();
            for (String color : filterValues.getColors()) {
                FilterItem item = new FilterItem(color, "", "", false, Const.FILTER.COLOR);
                flColor.add(item);
            }
            findViewById(R.id.llColor).setVisibility(View.VISIBLE);
        } else {
            tvColor.setVisibility(View.GONE);
            findViewById(R.id.llColor).setVisibility(View.GONE);
        }

        int minPrice = -1, maxPrice = -1;
        int minSale = -1, maxSale = -1;
        try {
            float fPriceMin = Float.parseFloat(filterValues.getMinPrice());
            float fPriceMax = Float.parseFloat(filterValues.getMaxPrice());
            minPrice = (int) fPriceMin;
            maxPrice = (int) fPriceMax;
            float fMinSale = Float.parseFloat(filterValues.getMinSalePercentage());
            float fMaxSale = Float.parseFloat(filterValues.getMaxSalePercentage());
            minSale = (int) fMinSale;
            maxSale = (int) fMaxSale;
        } catch (NumberFormatException e) {
            Log.d(TAG, "updateFilterList: Unable to parse the float value to int");
        }

        if (minPrice != -1 && maxPrice != -1) {
            tvPrice.setVisibility(View.VISIBLE);
            createPriceFilterList(minPrice, maxPrice+1);
            //createPriceFilterList(10, 5000); //FOR testing
        } else {
            tvPrice.setVisibility(View.GONE);
        }

        if (minPrice != -1 && maxPrice != -1) {
            tvDiscount.setVisibility(View.VISIBLE);
            createSalePercentageList(minSale, maxSale+1);
            //createSalePercentageList(10, 50);  //FOR testing
        } else {
            tvDiscount.setVisibility(View.GONE);
        }
    }

    private void createPriceFilterList(int minPrice, int maxPrice) {
        final int DIFF = 500;
        int price = 500;
        boolean firstTag = true;
        flPrice.clear();
        for (; price <= maxPrice; price += DIFF) {
            String text;
            if (firstTag) {
                text = price + " or below";
                FilterItem filterItem = new FilterItem(text, "0", "10", false, FILTER.PRICE);
                flPrice.add(filterItem);
                firstTag = false;
            } else {
                int from = price - DIFF;
                int to = price - 1;
                text = from + " - " + to;
                FilterItem filterItem = new FilterItem(text, from + "", to + "", false, FILTER.PRICE);
                flPrice.add(filterItem);
            }
        }
        if (flPrice.isEmpty()) {
            findViewById(R.id.llPrice).setVisibility(View.GONE);
        } else {
            findViewById(R.id.llPrice).setVisibility(View.VISIBLE);
        }
    }

    private void createSalePercentageList(int min, int max) {
        final int DIFF = 10;
        int sale = 10;
        boolean firstTag = true;
        flDiscount.clear();
        for (; sale <= max; sale += DIFF) {
            String text;
            if (firstTag) {
                text = sale + "% and below";
                FilterItem filterItem = new FilterItem(text, "0", "10", false, FILTER.DISCOUNT);
                flDiscount.add(filterItem);
                text = sale + "% and more";
                FilterItem filterItem2 = new FilterItem(text, "10", max + "", false, FILTER.DISCOUNT);
                flDiscount.add(filterItem2);
                firstTag = !firstTag;
                continue;
            } else {
                text = sale + "% or more";
            }
            FilterItem filterItem = new FilterItem(text, sale + "", max + "", false, FILTER.DISCOUNT);
            flDiscount.add(filterItem);
        }
        if (flDiscount.isEmpty()) {
            findViewById(R.id.llDiscount).setVisibility(View.GONE);
        } else {
            findViewById(R.id.llDiscount).setVisibility(View.VISIBLE);
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

    /*
     *   Update the filter and check for sort by option
     * */
    private void updateFilter(SORT_BY sortBy) {
        Log.d(TAG, "updateFilter: updating filter... " + sortBy);
        CURRENT_PAGE = 0;
        RETRY_ATTEMPT = 0;
        IS_LAST_PAGE = false;
        ProductFilter newFilter = new ProductFilter();
        String categoryIds = getQueryStringFor(flCategory, FILTER.CATEGORY);
        if (!categoryIds.isEmpty()) {
            newFilter.setSubCategoryIds(categoryIds);
        }

        String colors = getQueryStringFor(flColor, FILTER.COLOR);
        if (!colors.isEmpty()) {
            newFilter.setColors(colors);
        }

        String type = getQueryStringFor(flType, FILTER.TYPE);
        if (!type.isEmpty()) {
            newFilter.setTypes(type);
        }

        String size = getQueryStringFor(flSize, FILTER.LENGTH);
        if (!size.isEmpty()) {
            newFilter.setSizes(size);
        }
        Log.d(TAG, "updateFilter: MAIN filter " + MAIN_FILTER.toString());

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
        String priceLow = getMinOrMaxPrice(flPrice, true);
        String priceHigh = getMinOrMaxPrice(flPrice, false);
        if (!priceLow.isEmpty() && !priceHigh.isEmpty()) {
            newFilter.setPriceRangeLow(priceLow);
            newFilter.setPriceRangeHigh(priceHigh);
        }
        String minSalePercentage = getMinOrMaxSalePercentage(flDiscount, true);
        String maxSalePercentage = getMinOrMaxSalePercentage(flDiscount, false);
        if (!minSalePercentage.isEmpty() && !maxSalePercentage.isEmpty()) {
            newFilter.setSalePercentageRangeLow(minSalePercentage);
            newFilter.setSalePercentageRangeHigh(maxSalePercentage);
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

    private String getMinOrMaxPrice(List<FilterItem> filterItems, boolean minimum) {
        if (filterItems == null || filterItems.isEmpty()) return "";
        if (minimum) {
            for (FilterItem item : filterItems) {
                if (item.isSelected()) {
                    return item.getCategoryId();
                }
            }
        } else {
            String max = "";
            for (FilterItem item : filterItems) {
                if (item.isSelected()) {
                    max = item.getParentId();
                }
            }
            return max;
        }
        return "";
    }

    private String getMinOrMaxSalePercentage(List<FilterItem> filterItems, boolean minimum) {
        if (filterItems == null || filterItems.isEmpty()) return "";
        if (minimum) {
            for (FilterItem item : filterItems) {
                if (item.isSelected()) {
                    return item.getCategoryId();
                }
            }
        } else {
            String max = "";
            for (FilterItem item : filterItems) {
                if (item.isSelected()) {
                    max = item.getParentId();
                }
            }
            return max;
        }
        return "";
    }


    // reset filter, set CURRENT PAGE = 0
    private void reset() {
        Toast.makeText(this, "Reset Everything", Toast.LENGTH_SHORT)
                .show();
        clearAllFilter();
        MAIN_FILTER = new ProductFilter();
        CURRENT_PAGE = START_PAGE;
        IS_LAST_PAGE = false;
        RETRY_ATTEMPT = 0;
    }


    private void openSingleProductActivity(Product product) {
        String CALLING_ACTIVITY = AllProductCategory.class.getName();
        String ARG_CALLING_ACTIVITY = "CALLING_ACTIVITY";
        Intent intent = new Intent(this, SingleProductActivity.class);
        intent.putExtra(ARG_CALLING_ACTIVITY, CALLING_ACTIVITY);
        intent.putExtra("PRODUCT_ID", product.getId());
        intent.putExtra("CATEGORY_ID", PARENT_ID);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, REQ_SINGLE_PRODUCT);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_category, menu);
        changeCartIcon(EcommerceDatabase.getInstance().cartItemDao().changeCartIcon());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ID = item.getItemId();
        if (ID == android.R.id.home) {
            // app icon in action bar clicked; go home
            Log.d(TAG, "onBackPressed: uploading...");
            ProductRepository.getInstance().uploadWishListOnServer(this.getApplication(), null);
            super.onBackPressed();
            return true;
        } else if (ID == R.id.action_cart) {
            startActivity(new Intent(getApplicationContext(), ShoppingBagActivity.class));
            return true;
        } else if (ID == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}