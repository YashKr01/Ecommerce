package com.shopping.bloom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
    private RelativeLayout rlFilter;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imgCloseNavigationView;
    private Toolbar toolbar;

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
        if(bundle != null) {
            ITEM_CATEGORY = bundle.getInt(ARG_CATEGORY);
        } else {
            Log.d(TAG, "onCreate: Empty bundle");
        }

        initViews();
        setSupportActionBar(toolbar);
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
        rlFilter = findViewById(R.id.rlFilter);
        rvProducts = findViewById(R.id.rvViewCategory);
        toolbar = findViewById(R.id.toolbar);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        imgCloseNavigationView = findViewById(R.id.imgClose);

        //Lock the drawer layout so that it won't open with left swipe
        lockDrawerLayout();

        //Attack Click Listeners
        tvSort.setOnClickListener(optionsClickListener);
        tvCategory.setOnClickListener(optionsClickListener);
        rlFilter.setOnClickListener(optionsClickListener);
        imgCloseNavigationView.setOnClickListener(optionsClickListener);
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
            //TODO: change the subcategory parameter with intent value ITEM_CATEGORY
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
            if (v.getId() == R.id.tvSort) {
                Log.d(TAG, "onDebouncedClick: sort");
            }
            if (v.getId() == R.id.tvCategory) {
                Log.d(TAG, "onDebouncedClick: Category");
            }
            if (v.getId() == R.id.rlFilter) {
                drawerLayout.openDrawer(GravityCompat.END);
                Log.d(TAG, "onDebouncedClick: filter");
            }
            if(v.getId() == R.id.imgClose) {
                if(drawerLayout.isDrawerVisible(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        }
    };

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
        Log.d(TAG, "onBackPressed: uploading...");
        ProductRepository.getInstance().uploadAutomationMessages(this.getApplication());
        super.onBackPressed();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Log.d(TAG, "onBackPressed: uploading...");
                ProductRepository.getInstance().uploadAutomationMessages(this.getApplication());
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