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
import android.view.ViewGroup;
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
import com.shopping.bloom.adapters.ProductAdapter;
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
    private ProductAdapter productAdapter;
    private RecyclerView rvFilter;
    private FilterItemAdapter filterItemAdapter;
    private Toolbar toolbar;
    private LinearLayout llSort, llFilter;
    private ConstraintLayout cltFilter;
    private TextView tvSortBy, tvFilterBy;
    private TextView tvCategory, tvType, tvColor, tvSize;

    private final int START_PAGE = 0;
    private int CURRENT_PAGE = 0;
    private String PARENT_ID = "";
    private String SUB_CATEGORY_ID = "";
    private boolean IS_LOADING = false, IS_LAST_PAGE = false;
    private boolean WISHLIST_CHANGED = false;
    private boolean IS_FILTER_FETCH_COMPLETE = false;
    List<FilterItem> subCategoryList, flColor, flSize, flType;
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
        tvFilterBy = findViewById(R.id.tvFilterBy);

        tvCategory = findViewById(R.id.tvCategory);
        tvType = findViewById(R.id.tvType);
        tvColor = findViewById(R.id.tvColor);
        tvSize = findViewById(R.id.tvSize);

        //SetUpToolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener((view -> onBackPressed()));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //init List

        flColor = new ArrayList<>();
        flSize = new ArrayList<>();
        flType = new ArrayList<>();


        findViewById(R.id.btClose).setOnClickListener(clickListener);
        findViewById(R.id.btApplyFilter).setOnClickListener(clickListener);
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
            subCategoryList = bundle.getParcelableArrayList(ARG_SUB_CATEGORY_LIST);
            if(subCategoryList != null && subCategoryList.size() > 0) {
                changeSelectedTextTo(tvCategory.getId());
                filterItemAdapter.updateList(subCategoryList);
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
        productAdapter = new ProductAdapter(this, wishListListener);
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
            int SORT_BOTTOM_SHEET = 0;
            int FILTER_BOTTOM_SHEET = 1;
            int viewId = v.getId();
            if (viewId == R.id.llSortLayout) {
                openBottomSheet(SORT_BOTTOM_SHEET);
            }
            if (viewId == R.id.llFilterLayout) {
                openBottomSheet(FILTER_BOTTOM_SHEET);
            }
            if(viewId == R.id.btClose) {
                showOrHideSheet(cltFilter,false);
            }
            if(viewId == R.id.btApplyFilter) {
                updateFilter(SORT_BY.FILTERS);
                showOrHideSheet(cltFilter, false);
            }
        }
    };

    private final DebouncedOnClickListener changeCategory = new DebouncedOnClickListener(300) {
        @Override
        public void onDebouncedClick(View v) {
            int viewId = v.getId();
            Log.d(TAG, "onDebouncedClick: change category");
            changeSelectedTextTo(viewId);
            if(viewId == R.id.tvCategory) {
                filterItemAdapter.updateList(subCategoryList);
            }
            if(viewId == R.id.tvColor) {
                filterItemAdapter.updateList(flColor);
            }
            if(viewId == R.id.tvSize) {
                filterItemAdapter.updateList(flSize);
            }
            if(viewId == R.id.tvType) {
                filterItemAdapter.updateList(flType);
            }
        }
    };

    private void changeSelectedTextTo(int viewId) {
        int[] ids = {R.id.tvCategory, R.id.tvColor, R.id.tvSize, R.id.tvType};
        for(int id : ids) {
            TextView textView = findViewById(id);
            if(id == viewId) {
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            } else {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                textView.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_50));
            }
        }
    }

    private final WishListListener wishListListener = new WishListListener() {
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
        viewModel.getAvailableColorAndSize(filterListener);
    }

    private final FetchFilterListener filterListener = new FetchFilterListener() {
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
        if(filterValues == null) return ;

        if (!filterValues.getSizes().isEmpty()) {
            flSize.clear();
            for(String size: filterValues.getSizes()) {
                FilterItem item = new FilterItem(size, "", "", false, Const.FILTER.LENGTH);
                flSize.add(item);
            }
        }
        if (filterValues.getTypes() != null && !filterValues.getTypes().isEmpty()) {
            flType.clear();
            for(String type: filterValues.getTypes()) {
                FilterItem item = new FilterItem(type, "","", false, Const.FILTER.TYPE);
                flType.add(item);
            }

        }
        if (!filterValues.getColors().isEmpty()) {
            flColor.clear();
            for(String color: filterValues.getColors()) {
                FilterItem item = new FilterItem(color, "","", false, Const.FILTER.COLOR);
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
        final int SORT_BOTTOM_SHEET = 0;
        final int FILTER_BOTTOM_SHEET = 1;
        switch (bottomSheetType) {
            case SORT_BOTTOM_SHEET: {
                SortBottomSheet sortBottomSheet = new SortBottomSheet(this);
                sortBottomSheet.setListenerAndDefaultValue((sortBy, value) -> {
                    DEFAULT_SORT_VALUE = sortBy;
                    tvSortBy.setText(value);
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

    private void addFilterToNavigationSheet(LinearLayout parentView, List<String> values, Const.FILTER tag) {
        Log.d(TAG, "addFilterToNavigationSheet: TAG: " + tag);
        if (values == null || values.isEmpty()) {
            Log.d(TAG, "addFilterToNavigationSheet: NULL Values for TAG: " + tag);
            parentView.removeAllViews();
            return;
        }
        parentView.removeAllViews();

        //Param for the textView
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //params for the LinearLayout which will wrap the text view
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1.0f
        );

        LinearLayout currentRow = new LinearLayout(this);
        currentRow.setOrientation(LinearLayout.HORIZONTAL);
        currentRow.setWeightSum(3.0f);
        currentRow.setPadding(10, 10, 10, 10);
        int rowItemCount = 0;
        for (String value : values) {
            TextView textView = new TextView(this);
            textView.setText(value);
            textView.setTextColor(ContextCompat.getColor(this, R.color.blue_grey_900));
            textView.setTextSize(14f);
            textView.setLayoutParams(params);
            textView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_color_choices));
            textView.setOnClickListener(new DebouncedOnClickListener(200) {
                @Override
                public void onDebouncedClick(View v) {
                    //addToFilterToList(textView, tag);
                }
            });
            if (rowItemCount % 3 == 0 && rowItemCount != 0) {
                parentView.addView(currentRow);
                currentRow = new LinearLayout(this);
                currentRow.setOrientation(LinearLayout.HORIZONTAL);
                currentRow.setWeightSum(3.0f);
                currentRow.setPadding(10, 10, 10, 10);
            }
            LinearLayout tvParentLayout = new LinearLayout(this);
            tvParentLayout.setOrientation(LinearLayout.VERTICAL);
            tvParentLayout.setLayoutParams(tvParams);
            tvParentLayout.addView(textView);
            currentRow.addView(tvParentLayout);
            rowItemCount++;
            Log.d(TAG, "addFilterToNavigationSheet: adding...");
        }
        if (currentRow.getChildCount() > 0)
            parentView.addView(currentRow);
    }

    /*
     *   @param tag will is used to identify if Filter type
     *       COLOR, TYPE, SIZE
     * */
    /*private void addToFilterToList(TextView textView, FILTER tag) {
        Log.d(TAG, "filterItem: TAG: " + tag.toString());
        Drawable background = textView.getBackground();
        if (background == null) return;
        String value = textView.getText().toString();
        if (background.getConstantState() ==
                ContextCompat.getDrawable(this, R.drawable.bg_color_choices).getConstantState()) {
            textView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_color_choices_selected));

            //Add item to the filter list based on tag
            if (tag == FILTER.COLOR) {
                colorFilterList.add(value);
            } else if (tag == FILTER.TYPE) {
                typeFilterList.add(value);
            } else if (tag == FILTER.LENGTH) {
                sizeFilterList.add(value);
            } else {
                Log.d(TAG, "filterItem: UNIDENTIFIED VALUE");
            }
        } else {
            textView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_color_choices));

            //Remove item to the filter list based on tag
            if (tag == FILTER.COLOR) {
                colorFilterList.remove(value);
            } else if (tag == FILTER.TYPE) {
                typeFilterList.remove(value);
            } else if (tag == FILTER.LENGTH) {
                sizeFilterList.remove(value);
            } else {
                Log.d(TAG, "filterItem: UNIDENTIFIED VALUE");
            }
        }
    }*/

    private void updateFilter(SORT_BY sortBy) {
        Log.d(TAG, "updateFilter: updating filter... " + sortBy);
        CURRENT_PAGE = 0;
        RETRY_ATTEMPT = 0;
        IS_LAST_PAGE = false;
        ProductFilter newFilter = new ProductFilter();
        if (sortBy == SORT_BY.FILTERS) {
            String categoryIds = getItems(subCategoryList, FILTER.CATEGORY);
            if(!categoryIds.isEmpty()) {
                MAIN_FILTER.setSubCategoryIds(categoryIds);
            } else {
                MAIN_FILTER.setSubCategoryIds(null);
            }

            String colors = getItems(flColor, FILTER.COLOR);
            if(!colors.isEmpty()) {
                MAIN_FILTER.setColors(colors);
            } else {
                MAIN_FILTER.setColors(null);
            }

            String type = getItems(flType, FILTER.TYPE);
            if(!type.isEmpty()) {
                MAIN_FILTER.setTypes(type);
            } else {
                MAIN_FILTER.setTypes(null);
            }

            String size = getItems(flSize, FILTER.LENGTH);
            if(!size.isEmpty()) {
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
            newFilter.setNewArrival("0");
        }
        MAIN_FILTER = newFilter;
        Log.d(TAG, "updateFilter: MAIN filter " + MAIN_FILTER.toString());
        checkNetworkAndFetchData();
    }

    private String getItems(List<FilterItem> filterItems, FILTER filter) {
        if(filterItems == null || filterItems.isEmpty()) return "";
        List<String> newList = new ArrayList<>();
        for(FilterItem item: filterItems) {
            if(item.isSelected()) {
                if(filter == FILTER.CATEGORY) {
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
        //TODO: send product id with intent
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
        if(cltFilter.getVisibility() == View.VISIBLE) {
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
                checkNetworkAndFetchData();
            } else {
                WISHLIST_CHANGED = false;
                RETRY_ATTEMPT = 0;
                checkNetworkAndFetchData();
            }
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

    private List<FilterItem> getDummyCategories() {
        List<FilterItem> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            FilterItem product = new FilterItem("Dummy", "2", "");
            FilterItem product1 = new FilterItem("Dummy shoes", "2", "1");
            FilterItem product2 = new FilterItem("dummy HipHop", "2", "5");
            list.add(product);
            list.add(product1);
            list.add(product2);
        }
        return list;
    }

    private List<Product> getDummyData() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i < 99; i++) {
            Product product = new Product(1, "Dummy Name", "", "100", "1000", "", "WHITE, YELLOW, BLACK, BLUE, YELLOW, BLACK, BLUE, YELLOW, BLACK, BLUE",
                    "/images/product/product1618639820.png", "", "",
                    "100", "", "", "", "",
                    "", false);
            list.add(product);
        }
        return list;
    }

}