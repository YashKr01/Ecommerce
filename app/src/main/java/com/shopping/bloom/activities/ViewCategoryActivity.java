package com.shopping.bloom.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.PaginationListener;
import com.shopping.bloom.adapters.ProductAdapter;
import com.shopping.bloom.database.repository.ProductRepository;
import com.shopping.bloom.model.CategoryTypes;
import com.shopping.bloom.model.FilterArrayValues;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.ProductFilter;
import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.restService.callback.FetchFilterListener;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.restService.callback.WishListListener;
import com.shopping.bloom.restService.callback.WishListUploadedCallback;
import com.shopping.bloom.utils.Const.SORT_BY;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.ProductsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewCategoryActivity extends AppCompatActivity {

    private static final String TAG = ViewCategoryActivity.class.getName();

    private ProductsViewModel viewModel;
    //views
    private SwipeRefreshLayout refreshLayout, retryConnecting;
    private RecyclerView rvProducts;
    private ProductAdapter productAdapter;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private final int START_PAGE = 0;
    private int CURRENT_PAGE = 0;
    private int PARENT_ID = -1;
    private boolean IS_LOADING = false, IS_LAST_PAGE = false;
    private boolean WISHLIST_CHANGED = false;
    private boolean IS_FILTER_FETCH_COMPLETE = false;
    List<CategoryTypes> filterList;     //subcategory list for filter/sorting
    ProductFilter MAIN_FILTER = new ProductFilter();
    FilterArrayValues filterArrayValues = null;
    List<String> colorFilterList, sizeFilterList, typeFilterList;

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
        setContentView(R.layout.activity_view_category);

        initViews();
        getIntentData();
        setUpRecycleView();

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
        toolbar = findViewById(R.id.toolbar);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        retryConnecting = findViewById(R.id.swipeNoInternet);
        drawerLayout = findViewById(R.id.drawerLayout);

        //SetUpToolbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener((view -> onBackPressed()));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        //Lock the drawer layout so that it won't open with left swipe
        lockDrawerLayout();

        //init List
        colorFilterList = new ArrayList<>();
        sizeFilterList = new ArrayList<>();
        typeFilterList = new ArrayList<>();
    }

    private void getIntentData() {
        String ARG_CATEGORY_ID = "category_id";
        String ARG_CATEGORY_NAMES = "category_names";
        String ARG_BUNDLE = "app_bundle_name";
        Bundle bundle = getIntent().getBundleExtra(ARG_BUNDLE);
        String parentId;

        if (bundle != null) {
            Log.d(TAG, "getIntentData: Not null");
            Log.d(TAG, "getIntentData: " + bundle.toString());
            parentId = bundle.getString(ARG_CATEGORY_ID, "");
            filterList = bundle.getParcelableArrayList(ARG_CATEGORY_NAMES);
            Log.d(TAG, "getIntentData: parentId " + parentId);
            Log.d(TAG, "getIntentData: categoryTypes " + filterList.toString());
        } else {
            Log.d(TAG, "getIntentData: NULL BUNDLE NO DATA RECEIVED");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpRecycleView() {
        productAdapter = new ProductAdapter(this, wishListListener);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setHasFixedSize(true);
        rvProducts.setAdapter(productAdapter);


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
            if(!IS_FILTER_FETCH_COMPLETE) {
                fetchFilterAndUpdateUI();
            }
            if(WISHLIST_CHANGED) {
                //Upload the wishListItems to the server
                viewModel.uploadWishListOnServer(this.getApplication(), wishListUploadedCallback);
            }
            IS_LOADING = true;
            viewModel.setResponseListener(responseListener);
            viewModel.fetchData("1", CURRENT_PAGE, MAIN_FILTER);
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
            IS_FILTER_FETCH_COMPLETE = true;
            filterArrayValues = filterValues;
        }

        @Override
        public void fetchOnFailed(int errorCode, String message) {
            Log.d(TAG, "fetchFailed: errorCode "+ errorCode + " message "+ message);
        }
    };

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
                return ;
            }
            IS_LOADING = false;
            RETRY_ATTEMPT++;
            if(RETRY_ATTEMPT < MAX_RETRY_ATTEMPT) {
                Log.d(TAG, "onFailure: RETRYING request... " + RETRY_ATTEMPT);
                checkNetworkAndFetchData();
            } else {
                RETRY_ATTEMPT = 0;
            }
        }
    };

    private void updateFilter(SORT_BY sortBy) {
        Log.d(TAG, "updateFilter: updating filter... " + sortBy);
        CURRENT_PAGE = 0;
        RETRY_ATTEMPT = 0;
        IS_LAST_PAGE = false;
        ProductFilter newFilter = new ProductFilter();
        if(sortBy == SORT_BY.SUB_CATEGORY) {
            //Just update the category items and return
            MAIN_FILTER.setSubCategoryIds(newFilter.getSubCategoryIds());
            Log.d(TAG, "updateFilter: MAIN filter " + MAIN_FILTER.toString());
            checkNetworkAndFetchData();
            return ;
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
        if(sortBy == SORT_BY.NAV_FILTER) {
            if(colorFilterList != null && colorFilterList.size() != 0)
                newFilter.setColors(ProductRepository.join(colorFilterList));
            if(sizeFilterList != null && sizeFilterList.size() != 0)
                newFilter.setSizes(ProductRepository.join(sizeFilterList));
            if(typeFilterList != null && typeFilterList.size() != 0)
                newFilter.setTypes(ProductRepository.join(typeFilterList));
            Log.d(TAG, "updateFilter: NAV_FILTER " + newFilter.toString());
        }
        MAIN_FILTER = newFilter;
        Log.d(TAG, "updateFilter: MAIN filter " + MAIN_FILTER.toString());
        checkNetworkAndFetchData();
    }

    // reset filter, set CURRENT PAGE = 0
    private void reset() {
        Toast.makeText(this, "Reset Everything", Toast.LENGTH_SHORT)
                .show();
        MAIN_FILTER = new ProductFilter();
        CURRENT_PAGE = START_PAGE;
        IS_LAST_PAGE = false;
        RETRY_ATTEMPT = 0;
        if(IS_FILTER_FETCH_COMPLETE) {
            colorFilterList.clear();
            typeFilterList.clear();
            sizeFilterList.clear();
        }
    }

    private void openSingleProductActivity(Product product) {
        //TODO: send product id with intent
        Intent intent = new Intent(this, SingleProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void showNoInternetImage(boolean show) {
        retryConnecting.setRefreshing(false);
        refreshLayout.setRefreshing(false);
        if(show) {
            retryConnecting.setVisibility(View.VISIBLE);
            return;
        }
        retryConnecting.setVisibility(View.GONE);
    }

    private void showEmptyScreen(boolean show) {
        if(show) {
            //TODO: show empty here screen when created
            return ;
        }

    }

    private void lockDrawerLayout() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_category, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
            return;
        }

        Log.d(TAG, "onBackPressed: uploading...");
        viewModel.uploadWishListOnServer(this.getApplication(), null);
        super.onBackPressed();
    }

    private final WishListUploadedCallback wishListUploadedCallback = new WishListUploadedCallback() {
        @Override
        public void onUploadSuccessful() {
            if(WISHLIST_CHANGED) {
                WISHLIST_CHANGED = false;
                checkNetworkAndFetchData();
            }
        }

        @Override
        public void onUploadFailed(int errorCode, String message) {
            Log.d(TAG, "onUploadFailed: errorCode " + errorCode);
            Log.d(TAG, "onUploadFailed: message " + message);
            if(RETRY_ATTEMPT < MAX_RETRY_ATTEMPT) {
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

    private List<CategoryTypes> getDummyCategories() {
        List<CategoryTypes> list = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            CategoryTypes product = new CategoryTypes("Dummy", "2", "");
            CategoryTypes product1 = new CategoryTypes("Dummy shoes", "2", "1");
            CategoryTypes product2 = new CategoryTypes("dummy HipHop", "2", "5");
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