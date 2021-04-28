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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.shopping.bloom.R;
import com.shopping.bloom.adapters.CategoryTypesAdapter;
import com.shopping.bloom.adapters.PaginationListener;
import com.shopping.bloom.adapters.ProductAdapter;
import com.shopping.bloom.database.repository.ProductRepository;
import com.shopping.bloom.model.CategoryTypes;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.restService.callback.WishListListener;
import com.shopping.bloom.utils.DebouncedOnClickListener;
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
    private RelativeLayout rlSort, rlCategory, rlFilter;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView rvProducts, rvCategoryTypes;
    private ProductAdapter productAdapter;
    private CategoryTypesAdapter categoryNamesAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private ImageView imgCloseNavigationView;
    private LinearLayout sortOptionSheet, categoryOptionSheet;
    private TextView sortMostPopular, sortNewArrival, sortPriceLtoH, sortPriceHtoL;
    private ImageView sortArrow, categoryArrow;
    private Button btApply, btClear;

    private final int START_PAGE = 0;
    private int CURRENT_PAGE = 0;
    private final int ITEM_LIMIT = 14;
    private int PARENT_ID = -1;
    private boolean IS_LOADING = false, IS_LAST_PAGE = false;
    List<CategoryTypes> filterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        initViews();
        getIntentData();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener((view -> onBackPressed()));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setUpRecycleView();
        viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        checkNetworkAndFetchData();

        refreshLayout.setOnRefreshListener(() -> {
            CURRENT_PAGE = START_PAGE;
            IS_LAST_PAGE = false;
            checkNetworkAndFetchData();
        });
    }

    private void initViews() {
        rlSort = findViewById(R.id.rlSort);
        rlCategory = findViewById(R.id.rlCategory);
        rlFilter = findViewById(R.id.rlFilter);
        rvProducts = findViewById(R.id.rvViewCategory);
        rvCategoryTypes = findViewById(R.id.rv_CategoryTypes);
        toolbar = findViewById(R.id.toolbar);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        imgCloseNavigationView = findViewById(R.id.imgClose);
        sortOptionSheet = findViewById(R.id.llSortOptionsSheet);
        categoryOptionSheet = findViewById(R.id.llCategoryOptionsSheet);
        sortArrow = findViewById(R.id.imgSortArrow);
        categoryArrow = findViewById(R.id.imgCategoryArrow);
        btApply = findViewById(R.id.btApply);
        btClear = findViewById(R.id.btClearAll);

        sortMostPopular = findViewById(R.id.textView1);
        sortNewArrival = findViewById(R.id.textView2);
        sortPriceLtoH = findViewById(R.id.textView3);
        sortPriceHtoL = findViewById(R.id.textView4);


        //Lock the drawer layout so that it won't open with left swipe
        lockDrawerLayout();

        //Attack Click Listeners
        rlSort.setOnClickListener(optionsClickListener);
        rlCategory.setOnClickListener(optionsClickListener);
        rlFilter.setOnClickListener(optionsClickListener);
        imgCloseNavigationView.setOnClickListener(optionsClickListener);

        sortMostPopular.setOnClickListener(optionsClickListener);
        sortNewArrival.setOnClickListener(optionsClickListener);
        sortPriceLtoH.setOnClickListener(optionsClickListener);
        sortPriceHtoL.setOnClickListener(optionsClickListener);
        btApply.setOnClickListener(optionsClickListener);
        btClear.setOnClickListener(optionsClickListener);
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

    private void setUpRecycleView() {
        productAdapter = new ProductAdapter(this, wishListListener);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setHasFixedSize(true);
        rvProducts.setAdapter(productAdapter);

        categoryNamesAdapter = new CategoryTypesAdapter(this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayout.HORIZONTAL);
        rvCategoryTypes.setLayoutManager(staggeredGridLayoutManager);
        rvCategoryTypes.setAdapter(categoryNamesAdapter);
        categoryNamesAdapter.updateList(getDummyCategories());
        categoryNamesAdapter.notifyDataSetChanged();

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
                boolean isVisible = sortOptionSheet.getVisibility() == View.GONE;
                if (!isVisible) {
                    rotateUpArrow(sortArrow,false);
                    showOrHideSheet(sortOptionSheet,false);
                }
                return IS_LOADING;
            }
        });
    }

    private final WishListListener wishListListener = (position, isAdded) -> {
        Log.d(TAG, "updateWishList: " + isAdded);
        Product product = productAdapter.getItemAt(position);
        productAdapter.updateItem(position, isAdded);
        WishListItem wishListItem = new WishListItem(String.valueOf(product.getId()), LoginManager.getInstance().gettoken());
        if (isAdded) {
            viewModel.addToWishList(wishListItem);
        } else {
            viewModel.removeFromWishList(wishListItem);
        }
    };

    private void checkNetworkAndFetchData() {
        if (NetworkCheck.isConnect(this)) {
            IS_LOADING = true;
            viewModel.setResponseListener(responseListener);
            viewModel.fetchData("2", ITEM_LIMIT, CURRENT_PAGE);       // Temporary category
        } else {
            showNoInternetImage(true);
        }
    }

    private final ProductResponseListener responseListener = new ProductResponseListener() {
        @Override
        public void onSuccess(List<Product> products) {
            IS_LOADING = false;
            refreshLayout.setRefreshing(false);
            if(CURRENT_PAGE == 0) {
                productAdapter.updateList(products);
            } else {
                productAdapter.addProductList(products);
            }
            CURRENT_PAGE++;
            showNoInternetImage(false);
        }

        @Override
        public void onFailure(int errorCode, String message) {
            Log.d(TAG, "onFailure: errorCode " + errorCode + " message " + message);
            refreshLayout.setRefreshing(false);
            if(errorCode == 200) {
                IS_LAST_PAGE = true;
            }
            IS_LOADING = false;
        }
    };

    private final DebouncedOnClickListener optionsClickListener = new DebouncedOnClickListener(200) {
        @Override
        public void onDebouncedClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.rlSort) {
                selectText(0);  //ignore
                boolean isOtherSheetVisible = categoryOptionSheet.getVisibility() == View.VISIBLE;
                if(isOtherSheetVisible) { //close filter sheet
                    rotateUpArrow(categoryArrow, false);
                    showOrHideSheet(categoryOptionSheet, false);
                }


                boolean isVisible = sortOptionSheet.getVisibility() == View.GONE;
                showOrHideSheet(sortOptionSheet, isVisible);
                rotateUpArrow(sortArrow, isVisible);
                return;
            }
            if (viewId == R.id.rlCategory) {    //close sort sheet
                boolean isOtherSheetVisible = sortOptionSheet.getVisibility() == View.VISIBLE;
                if(isOtherSheetVisible) {
                    rotateUpArrow(sortArrow, false);
                    showOrHideSheet(sortOptionSheet, false);
                }


                boolean isVisible = categoryOptionSheet.getVisibility() == View.GONE;
                showOrHideSheet(categoryOptionSheet, isVisible);
                rotateUpArrow(categoryArrow, isVisible);
                return;
            }
            if (viewId == R.id.rlFilter) {
                drawerLayout.openDrawer(GravityCompat.END);
                return;
            }
            if (viewId == R.id.imgClose) {
                drawerLayout.closeDrawer(GravityCompat.END);
                return;
            }

            //if any sort option is selected
            if (viewId == R.id.textView1) {  //Most popular
                selectText(viewId);
                return;
            }
            if (viewId == R.id.textView2) { //New arrival
                selectText(viewId);
                return;
            }
            if (viewId == R.id.textView3) { //Price Low to High
                selectText(viewId);
                return;
            }
            if (viewId == R.id.textView4) { //Price High to Low
                selectText(viewId);
                return;
            }

            if(viewId == R.id.btClearAll) {
                showOrHideSheet(categoryOptionSheet, false);  // close filter sheet
                categoryNamesAdapter.clearAllSelection();
                return;
            }

            if(viewId == R.id.btApply) {
                List<CategoryTypes> list = categoryNamesAdapter.getSelectedItems();
                Log.d(TAG, "onDebouncedClick: list " + list.toString());
                showOrHideSheet(categoryOptionSheet, false);
                return;
            }

        }
    };

    //animate Filter sheet or Sort layout sheet to get an dropDown effects
    // Pass the parent layout LinearLayout
    private void showOrHideSheet(LinearLayout sheetLayout, final boolean show) {
        long ANIMATION_DURATION = 300L;
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
                    .translationY(-sheetLayout.getHeight())
                    .setDuration(ANIMATION_DURATION + 100)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            sheetLayout.setVisibility(View.GONE);
                        }
                    });
        }
    }

    //This will animate the arrow icon on opening or closing the tab
    private void rotateUpArrow(ImageView arrowIcon, boolean show) {
        if (show) {
            arrowIcon.animate()
                    .setDuration(300)
                    .rotation(0)
                    .start();
        } else {
            arrowIcon.animate()
                    .setDuration(300)
                    .rotation(180)
                    .start();
        }
    }

    private void selectText(int viewId) {
        int[] optionList = {R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4};
        for(int i = 0; i < 4; i++) {
            TextView textView = findViewById(optionList[i]);
            if(optionList[i] == viewId){
                textView.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
    }

    private void showNoInternetImage(boolean show) {

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
        ProductRepository.getInstance().uploadWishListOnServer(this.getApplication());
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Log.d(TAG, "onBackPressed: uploading...");
                ProductRepository.getInstance().uploadWishListOnServer(this.getApplication());
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
            CategoryTypes product1 = new CategoryTypes("Dummy shoes", "2","1");
            CategoryTypes product2 = new CategoryTypes("dummy HipHop", "2","5");
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