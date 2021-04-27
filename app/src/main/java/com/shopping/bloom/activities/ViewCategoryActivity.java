package com.shopping.bloom.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.shopping.bloom.R;
import com.shopping.bloom.adapters.PaginationListener;
import com.shopping.bloom.adapters.ProductAdapter;
import com.shopping.bloom.database.repository.ProductRepository;
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
    private TextView tvSort, tvCategory;
    private RelativeLayout rlSort, rlCategory, rlFilter;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imgCloseNavigationView;
    private Toolbar toolbar;
    private LinearLayout sortOptionSheet, categoryOptionSheet;
    private TextView sortMostPopular, sortNewArrival, sortPriceLtoH, sortPriceHtoL;
    //animate these arrow images
    private ImageView sortArrow, categoryArrow;

    private final int START_PAGE = 0;
    private int CURRENT_PAGE = 0;
    private final int ITEM_LIMIT = 14;
    private int ITEM_CATEGORY = -1;
    private String ARG_CATEGORY = "category_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ITEM_CATEGORY = bundle.getInt(ARG_CATEGORY);
        } else {
            Log.d(TAG, "onCreate: Empty bundle");
        }

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
            checkNetworkAndFetchData();
        });
    }

    private void initViews() {
        tvSort = findViewById(R.id.tvSort);
        tvCategory = findViewById(R.id.tvCategory);
        rlSort = findViewById(R.id.rlSort);
        rlCategory = findViewById(R.id.rlCategory);
        rlFilter = findViewById(R.id.rlFilter);
        rvProducts = findViewById(R.id.rvViewCategory);
        toolbar = findViewById(R.id.toolbar);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        imgCloseNavigationView = findViewById(R.id.imgClose);
        sortOptionSheet = findViewById(R.id.llSortOptionsSheet);
        categoryOptionSheet = findViewById(R.id.llCategoryOptionsSheet);
        sortArrow = findViewById(R.id.imgSortArrow);
        categoryArrow = findViewById(R.id.imgCategoryArrow);

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
    }

    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //TODO : get bundle data
        }
    }

    private void setUpRecycleView() {
        adapter = new ProductAdapter(this, wishListListener);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setHasFixedSize(true);
        rvProducts.setAdapter(adapter);

        //Disable blink animation when updating the items
        ((SimpleItemAnimator) rvProducts.getItemAnimator()).setSupportsChangeAnimations(false);

        rvProducts.addOnScrollListener(new PaginationListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                checkNetworkAndFetchData();
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                boolean isVisible = sortOptionSheet.getVisibility() == View.GONE;
                if (!isVisible) showSortMenu(false);
                return false;
            }
        });
    }

    private final WishListListener wishListListener = (position, isAdded) -> {
        Log.d(TAG, "updateWishList: " + isAdded);
        Product product = adapter.getItemAt(position);
        adapter.updateItem(position, isAdded);
        WishListItem wishListItem = new WishListItem(String.valueOf(product.getId()), LoginManager.getInstance().gettoken());
        if (isAdded) {
            viewModel.addToWishList(wishListItem);
        } else {
            viewModel.removeFromWishList(wishListItem);
        }
    };

    private void checkNetworkAndFetchData() {
        if (NetworkCheck.isConnect(this)) {
            viewModel.setResponseListener(responseListener);
            viewModel.fetchData("2", ITEM_LIMIT, CURRENT_PAGE);       // Temporary category
        } else {
            showNoInternetImage(true);
        }
    }

    private final ProductResponseListener responseListener = new ProductResponseListener() {
        @Override
        public void onSuccess(List<Product> products) {
            refreshLayout.setRefreshing(false);
            adapter.updateList(products);
            CURRENT_PAGE++;
            showNoInternetImage(false);
        }

        @Override
        public void onFailure(int errorCode, String message) {
            refreshLayout.setRefreshing(false);
            Log.d(TAG, "onFailure: errorCode " + errorCode + " message " + message);
        }
    };

    private final DebouncedOnClickListener optionsClickListener = new DebouncedOnClickListener(200) {
        @Override
        public void onDebouncedClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.rlSort) {
                boolean isVisible = sortOptionSheet.getVisibility() == View.GONE;
                showSortMenu(isVisible);
                return;
            }
            if (viewId == R.id.rlCategory) {
                boolean isVisible = categoryOptionSheet.getVisibility() == View.GONE;
                showFilterMenu(isVisible);
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

            //if any sort option selected
            if (viewId == R.id.textView1) {  //Most popular
                selectText(sortMostPopular, viewId);
                return;
            }
            if (viewId == R.id.textView2) { //New arrival
                selectText(sortNewArrival, viewId);
                return;
            }
            if (viewId == R.id.textView3) { //Price Low to High
                selectText(sortPriceLtoH, viewId);
                return;
            }
            if (viewId == R.id.textView4) { //Price High to Low
                selectText(sortPriceHtoL, viewId);
                return;
            }
        }
    };

    private void showFilterMenu(boolean show) {
        rotateUpArrow(categoryArrow, show);
    }

    private void showSortMenu(final boolean show) {
        rotateUpArrow(sortArrow, show);
        long ANIMATION_DURATION = 300L;
        if (show) {
            //show navigation bar with animation
            sortOptionSheet.animate().setListener(null);
            sortOptionSheet.clearAnimation();
            sortOptionSheet.animate()
                    .alpha(1.0f).translationY(0f).setDuration(ANIMATION_DURATION);
            sortOptionSheet.setVisibility(View.VISIBLE);
        } else {
            //hide bottom navigation bar with animation
            sortOptionSheet.clearAnimation();
            sortOptionSheet.animate()
                    .alpha(0.0f)
                    .translationY(-sortOptionSheet.getHeight())
                    .setDuration(ANIMATION_DURATION + 200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            sortOptionSheet.setVisibility(View.GONE);
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

    private void selectText(TextView sortOption, int viewId) {
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