package com.shopping.bloom.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import com.shopping.bloom.model.FilterArrayValues;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.ProductFilter;
import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.restService.callback.FetchFilterListener;
import com.shopping.bloom.restService.callback.ProductResponseListener;
import com.shopping.bloom.restService.callback.WishListListener;
import com.shopping.bloom.restService.callback.WishListUploadedCallback;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.Const.SORT_BY;
import com.shopping.bloom.utils.Const.FILTER;
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
    private SwipeRefreshLayout refreshLayout, retryConnecting;
    private RecyclerView rvProducts, rvCategoryTypes;
    private ProductAdapter productAdapter;
    private CategoryTypesAdapter categoryNamesAdapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private ImageView imgCloseNavigationView;
    private LinearLayout sortOptionSheet, categoryOptionSheet;
    private LinearLayout dummyLayout;
    private TextView sortMostPopular, sortNewArrival, sortPriceLtoH, sortPriceHtoL;
    private ImageView sortArrow, categoryArrow;
    private Button btApply, btClear;
    private Button navBtApply, navBtClear;
    private ViewStub noInternetLayout;

    private LinearLayout filterType, filterColor, filterSize;
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
        rlSort = findViewById(R.id.rlSort);
        rlCategory = findViewById(R.id.rlCategory);
        rlFilter = findViewById(R.id.rlFilter);
        rvProducts = findViewById(R.id.rvViewCategory);
        rvCategoryTypes = findViewById(R.id.rv_CategoryTypes);
        toolbar = findViewById(R.id.toolbar);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);
        retryConnecting = findViewById(R.id.swipeNoInternet);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        imgCloseNavigationView = findViewById(R.id.imgClose);
        sortOptionSheet = findViewById(R.id.llSortOptionsSheet);
        categoryOptionSheet = findViewById(R.id.llCategoryOptionsSheet);
        sortArrow = findViewById(R.id.imgSortArrow);
        categoryArrow = findViewById(R.id.imgCategoryArrow);
        btApply = findViewById(R.id.btApply);
        btClear = findViewById(R.id.btClearAll);
        navBtApply = findViewById(R.id.btApplyFilter);
        navBtClear = findViewById(R.id.btClearAllFilter);
        filterType = findViewById(R.id.filterType);
        filterColor = findViewById(R.id.filterColor);
        filterSize = findViewById(R.id.filterLength);
        dummyLayout = findViewById(R.id.llDummyLayout);
        noInternetLayout = findViewById(R.id.vsEmptyScreen);

        sortMostPopular = findViewById(R.id.textView1);
        sortNewArrival = findViewById(R.id.textView2);
        sortPriceLtoH = findViewById(R.id.textView3);
        sortPriceHtoL = findViewById(R.id.textView4);

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

        //Attack Click Listeners
        rlSort.setOnClickListener(optionsClickListener);
        rlCategory.setOnClickListener(optionsClickListener);
        rlFilter.setOnClickListener(optionsClickListener);
        imgCloseNavigationView.setOnClickListener(optionsClickListener);

        sortMostPopular.setOnClickListener(sortOptionClickListener);
        sortNewArrival.setOnClickListener(sortOptionClickListener);
        sortPriceLtoH.setOnClickListener(sortOptionClickListener);
        sortPriceHtoL.setOnClickListener(sortOptionClickListener);
        btApply.setOnClickListener(optionsClickListener);
        btClear.setOnClickListener(optionsClickListener);
        navBtApply.setOnClickListener(optionsClickListener);
        navBtClear.setOnClickListener(optionsClickListener);
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

        categoryNamesAdapter = new CategoryTypesAdapter(this);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayout.HORIZONTAL);
        rvCategoryTypes.setLayoutManager(staggeredGridLayoutManager);
        rvCategoryTypes.setAdapter(categoryNamesAdapter);
        //categoryNamesAdapter.updateList(getDummyCategories());
        if(filterList != null) {
            categoryNamesAdapter.updateList(filterList);
        }

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


        /*
        *   Dummy layout for identifying the clicks
        *       1. If any dialog is open sort/category then close the dialog and consume the click.
        *       2. If no dialog is open then return false.
        * */
        dummyLayout.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_UP) {
                boolean isCategorySheetVisible = categoryOptionSheet.getVisibility() == View.VISIBLE;
                boolean isSortSheetVisible = sortOptionSheet.getVisibility() == View.VISIBLE;
                if(isCategorySheetVisible || isSortSheetVisible) {
                    closeDialog();
                    return true;
                }
            }
            return false;
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
            addFilterToNavigationSheet(filterColor ,filterValues.getColors(), FILTER.COLOR);
            addFilterToNavigationSheet(filterType ,filterValues.getTypes(), FILTER.TYPE);
            addFilterToNavigationSheet(filterSize ,filterValues.getSizes(), FILTER.LENGTH);
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
        String subCategory = categoryNamesAdapter.getSelectedItemsString();
        if (!subCategory.isEmpty()) {
            newFilter.setSubCategoryIds(subCategory);
        } else {
            newFilter.setSubCategoryIds(null);
        }
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

    private boolean closeDialog() {
        boolean isCategorySheetVisible = categoryOptionSheet.getVisibility() == View.VISIBLE;
        boolean isSortSheetVisible = sortOptionSheet.getVisibility() == View.VISIBLE;
        if (isCategorySheetVisible) { //close filter sheet
            rotateUpArrow(categoryArrow, false);
            showOrHideSheet(categoryOptionSheet, false);
        }
        if (isSortSheetVisible) {
            rotateUpArrow(sortArrow, false);
            showOrHideSheet(sortOptionSheet, false);
        }
        return isCategorySheetVisible||isSortSheetVisible;
    }

    private final DebouncedOnClickListener optionsClickListener = new DebouncedOnClickListener(200) {
        @Override
        public void onDebouncedClick(View v) {
            int viewId = v.getId();
            if (viewId == R.id.rlSort) {
                boolean isOtherSheetVisible = categoryOptionSheet.getVisibility() == View.VISIBLE;
                if (isOtherSheetVisible) { //close filter sheet
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
                if (isOtherSheetVisible) {
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

            if (viewId == R.id.btClearAll) {
                categoryNamesAdapter.clearAllSelection();
                return;
            }

            if (viewId == R.id.btApply) {
                showOrHideSheet(categoryOptionSheet, false);
                rotateUpArrow(categoryArrow, false);
                updateFilter(SORT_BY.SUB_CATEGORY);
                return ;
            }

            /*
            *   Navigation filter option buttons
            * */
            if (viewId == R.id.btClearAllFilter) {
                if(filterArrayValues == null) return ;
                colorFilterList.clear();
                typeFilterList.clear();
                sizeFilterList.clear();
                addFilterToNavigationSheet(filterColor ,filterArrayValues.getColors(), FILTER.COLOR);
                addFilterToNavigationSheet(filterType ,filterArrayValues.getTypes(), FILTER.TYPE);
                addFilterToNavigationSheet(filterSize ,filterArrayValues.getSizes(), FILTER.LENGTH);
                updateFilter(SORT_BY.NAV_FILTER);
                drawerLayout.closeDrawer(GravityCompat.END);
                return;
            }

            if (viewId == R.id.btApplyFilter) {
                updateFilter(SORT_BY.NAV_FILTER);
                drawerLayout.closeDrawer(GravityCompat.END);
                return ;
            }
            //Navigation filter buttons ENDS

        }
    };

    //This Listener ONLY used for the click items
    private final DebouncedOnClickListener sortOptionClickListener = new DebouncedOnClickListener(200) {
        @Override
        public void onDebouncedClick(View v) {
            int viewId = v.getId();
            selectText(viewId);     // change the typeface to bold for the selected text
            //if any sort option is selected
            if (viewId == R.id.textView1) {  //Most popular
                updateFilter(SORT_BY.MOST_POPULAR);
            }
            if (viewId == R.id.textView2) { //New arrival
                updateFilter(SORT_BY.NEW_ARRIVAL);
            }
            if (viewId == R.id.textView3) { //Price Low to High
                updateFilter(SORT_BY.PRICE_LOW_TO_HIGH);
            }
            if (viewId == R.id.textView4) { //Price High to Low
                updateFilter(SORT_BY.PRICE_HIGH_TO_LOW);
            }
            showOrHideSheet(sortOptionSheet, false);
            rotateUpArrow(sortArrow, false);
        }
    };

    private void addFilterToNavigationSheet(LinearLayout parentView, List<String> values, FILTER tag) {
        Log.d(TAG, "addFilterToNavigationSheet: TAG: " + tag);
        if(values == null || values.isEmpty()) {
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
        currentRow.setPadding(10,10,10,10);
        int rowItemCount = 0;
        for (String value: values) {
            TextView textView = new TextView(this);
            textView.setText(value);
            textView.setTextColor(ContextCompat.getColor(this, R.color.blue_grey_900));
            textView.setTextSize(14f);
            textView.setLayoutParams(params);
            textView.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_color_choices));
            textView.setOnClickListener(new DebouncedOnClickListener(200) {
                @Override
                public void onDebouncedClick(View v) {
                    addToFilterToList(textView, tag);
                }
            });
            if(rowItemCount%3 == 0 && rowItemCount != 0) {
                parentView.addView(currentRow);
                currentRow = new LinearLayout(this);
                currentRow.setOrientation(LinearLayout.HORIZONTAL);
                currentRow.setWeightSum(3.0f);
                currentRow.setPadding(10,10,10,10);
            }
            LinearLayout tvParentLayout = new LinearLayout(this);
            tvParentLayout.setOrientation(LinearLayout.VERTICAL);
            tvParentLayout.setLayoutParams(tvParams);
            tvParentLayout.addView(textView);
            currentRow.addView(tvParentLayout);
            rowItemCount++;
            Log.d(TAG, "addFilterToNavigationSheet: adding...");
        }
        if(currentRow.getChildCount() > 0)
            parentView.addView(currentRow);
    }


    /*
    *   @param tag will is used to identify if Filter type
    *       COLOR, TYPE, SIZE
    * */
    private void addToFilterToList(TextView textView, FILTER tag) {
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
    }

    // reset filter, set CURRENT PAGE = 0
    private void reset() {
        Toast.makeText(this, "Reset Everything", Toast.LENGTH_SHORT)
                .show();
        selectText(-1); //reset sort options
        MAIN_FILTER = new ProductFilter();
        CURRENT_PAGE = START_PAGE;
        IS_LAST_PAGE = false;
        RETRY_ATTEMPT = 0;
        if(IS_FILTER_FETCH_COMPLETE) {
            colorFilterList.clear();
            typeFilterList.clear();
            sizeFilterList.clear();
            addFilterToNavigationSheet(filterColor ,filterArrayValues.getColors(), FILTER.COLOR);
            addFilterToNavigationSheet(filterType ,filterArrayValues.getTypes(), FILTER.TYPE);
            addFilterToNavigationSheet(filterSize ,filterArrayValues.getSizes(), FILTER.LENGTH);
        }
        //categoryNamesAdapter.clearAllSelection(); //optional
    }

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

    private void openSingleProductActivity(Product product) {
        //TODO: send product id with intent
        Intent intent = new Intent(this, SingleProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void selectText(int viewId) {
        int[] optionList = {R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4};
        for (int i = 0; i < 4; i++) {
            TextView textView = findViewById(optionList[i]);
            if (optionList[i] == viewId) {
                textView.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
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
        //If any dialog is in expanded state then close it
        if(closeDialog()) return ;

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