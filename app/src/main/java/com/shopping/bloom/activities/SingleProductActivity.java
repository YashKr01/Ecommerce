package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.amar.library.ui.StickyScrollView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.shopping.bloom.R;
import com.shopping.bloom.adapters.singleproduct.ColorAdapter;
import com.shopping.bloom.adapters.singleproduct.ProductDescAdapter;
import com.shopping.bloom.adapters.singleproduct.SizeAdapter;
import com.shopping.bloom.adapters.singleproduct.ViewPagerImageAdapter;
import com.shopping.bloom.fragment.reviewsfragment.ReviewsFragment;
import com.shopping.bloom.model.ProductVariableResponse;
import com.shopping.bloom.model.SingleProductDataResponse;
import com.shopping.bloom.model.SingleProductDescResponse;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.SingleProductViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class SingleProductActivity extends AppCompatActivity {
    int i = 0;
    boolean colorClickable, sizeClickable;
    RecyclerView colorRecyclerView;
    RecyclerView sizeRecyclerView;
    SingleProductViewModel singleProductViewModel;
    ColorAdapter colorAdapter;
    SizeAdapter sizeAdapter;
    SingleProductDataResponse singleProductDataResponse;
    List<String> colorList, sizeList;
    LinearLayout linearLayout;
    TextView productName, price, viewReview, colorTextView, slideTextView;
    RatingBar ratingBar;
    ProgressDialog progressDialog;
    ViewPager viewPager;
    List<String> imageList;
    List<ProductVariableResponse> productVariableResponseList;
    ViewPagerImageAdapter viewPagerImageAdapter;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ViewStub viewStub;
    LinearLayout favLinearLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout relativeLayout;
    Toolbar toolbar, reviewToolbar;
    FrameLayout frameLayout;
    private Integer PRODUCT_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);


        /*
        Product ID received
         */
        if (getIntent() != null) {
            PRODUCT_ID = getIntent().getIntExtra("PRODUCT_ID", 1);
        }
        Log.d("SEND", "onCreate: " + PRODUCT_ID);


        productName = findViewById(R.id.product_name);
        price = findViewById(R.id.price);
        colorTextView = findViewById(R.id.color);
        ratingBar = findViewById(R.id.ratingBar4);
        viewReview = findViewById(R.id.viewAllReviewTextView);
        linearLayout = findViewById(R.id.linearLayout);
        favLinearLayout = findViewById(R.id.fav_layout);
        relativeLayout = findViewById(R.id.relative);
        viewPager = findViewById(R.id.viewpager);
        viewStub = findViewById(R.id.vsEmptyScreen);
        toolbar = findViewById(R.id.toolbar);
        reviewToolbar = findViewById(R.id.reviewToolbar);
        slideTextView = findViewById(R.id.slideTextView);


        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        frameLayout = findViewById(R.id.fragment);
        collapsingToolbarLayout = findViewById(R.id.collapseToolbar);

        swipeRefreshLayout.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);


        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        reviewToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkConnectivity);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Data");
        progressDialog.setCancelable(true);
        progressDialog.show();

        colorRecyclerView = findViewById(R.id.colorRecyclerView);
        sizeRecyclerView = findViewById(R.id.sizeRecyclerView);
        singleProductDataResponse = new SingleProductDataResponse();

        //color
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        colorList = new ArrayList<>();
        colorRecyclerView.setLayoutManager(linearLayoutManager);
        colorAdapter = new ColorAdapter(this, colorList);
        colorRecyclerView.setAdapter(colorAdapter);

        //size
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        sizeList = new ArrayList<>();
        sizeRecyclerView.setLayoutManager(linearLayoutManager2);
        sizeAdapter = new SizeAdapter(this, sizeList);
        sizeRecyclerView.setAdapter(sizeAdapter);

        productVariableResponseList = new ArrayList<>();
        imageList = new ArrayList<>();
        viewPagerImageAdapter = new ViewPagerImageAdapter(imageList);
        viewPager.setAdapter(viewPagerImageAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        double heightInDp = height * 0.4;

        int h = (int) Math.round(heightInDp);

        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        layoutParams.height = h;
        collapsingToolbarLayout.setLayoutParams(layoutParams);

        singleProductViewModel = ViewModelProviders.of(this).get(SingleProductViewModel.class);
        singleProductViewModel.getMutableLiveData().observe(this, singleProductDataResponse -> {

            this.singleProductDataResponse = singleProductDataResponse;

            if (this.singleProductDataResponse != null) {
                progressDialog.dismiss();

                String primary = this.singleProductDataResponse.getPrimary_image();
                productVariableResponseList = this.singleProductDataResponse.getProductVariableResponses();

                imageList.add(primary);
                for (ProductVariableResponse productVariableResponse : productVariableResponseList) {
                    imageList.add(productVariableResponse.getPrimary_image());
                    colorList.add(productVariableResponse.getColor());
                    sizeList.add(productVariableResponse.getSize());
                }

                slideTextView.setText(1 + "/" + imageList.size());

                //System.out.println(colorList);
                HashSet<String> colorSet = new LinkedHashSet<>(colorList);
                // System.out.println(colorSet);
                colorList.clear();
                colorList.addAll(colorSet);

                if (colorList.size() == 0) {
                    colorClickable = false;
                    String colors = this.singleProductDataResponse.getAvailable_colors();
                    String[] colorArray = colors.split(",");
                    colorList.addAll(Arrays.asList(colorArray));
                } else {
                    colorClickable = true;
                }

                HashSet<String> sizeSet = new LinkedHashSet<>(sizeList);
                sizeList.clear();
                sizeList.addAll(sizeSet);

                if (sizeList.size() == 0) {
                    sizeClickable = false;
                    String size = this.singleProductDataResponse.getAvailable_sizes();
                    String[] sizeArray = size.split(",");
                    sizeList.addAll(Arrays.asList(sizeArray));
                } else {
                    sizeClickable = true;
                }

                viewPagerImageAdapter.setImageList(imageList);
                viewPagerImageAdapter.notifyDataSetChanged();

                colorAdapter.setColorList(colorList, colorClickable);
                colorAdapter.notifyDataSetChanged();

                sizeAdapter.setSizeList(sizeList, sizeClickable);
                sizeAdapter.notifyDataSetChanged();

                productName.setText(this.singleProductDataResponse.getProduct_name());
                price.setText(getString(R.string.rupee).concat(" ").concat(this.singleProductDataResponse.getPrice()));

                ratingBar.setRating(Float.parseFloat(this.singleProductDataResponse.getAvg_rating()));

                linearLayout.setOnClickListener(v -> {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
                    View bottomSheet = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_productdesc, findViewById(R.id.bottomSheet));

                    ImageButton imageButton = bottomSheet.findViewById(R.id.imgClose);
                    imageButton.setOnClickListener(v1 -> bottomSheetDialog.dismiss());

                    TextView textView = bottomSheet.findViewById(R.id.description);
                    textView.setText(this.singleProductDataResponse.getDescription());

                    RecyclerView recyclerView = bottomSheet.findViewById(R.id.recyclerView);
                    LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager3);

                    List<SingleProductDescResponse> list = this.singleProductDataResponse.getSingleProductDescResponseList();
                    ProductDescAdapter productDescAdapter = new ProductDescAdapter(this, list);
                    productDescAdapter.setSingleProductDescResponseList(list);
                    recyclerView.setAdapter(productDescAdapter);

                    bottomSheetDialog.setContentView(bottomSheet);
                    bottomSheetDialog.show();

                });
            }
        });
        if (PRODUCT_ID != null) {
            singleProductViewModel.makeApiCall(PRODUCT_ID, getApplication());
        }

        viewReview.setOnClickListener(debouncedOnClickListener);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(singleProductDataResponse.getProduct_name());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                slideTextView.setText((position+1) + "/" + imageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private final DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            if (v.getId() == R.id.viewAllReviewTextView) {
                frameLayout.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                collapsingToolbarLayout.setVisibility(View.GONE);
                favLinearLayout.setVisibility(View.GONE);
                reviewToolbar.setVisibility(View.VISIBLE);

                Bundle bundle = new Bundle();
                bundle.putString("PRODUCT_ID", String.valueOf(PRODUCT_ID));

                Fragment fragment = new ReviewsFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

            }
        }
    };


    private void checkNetworkConnectivity() {
        if (!NetworkCheck.isConnect(this)) {
            viewStub.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
        } else {
            viewStub.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    public void setViewPagerCurrentItem(int pos) {
        String color = colorList.get(pos).trim();
        if (!color.isEmpty()) {
            colorTextView.setVisibility(View.VISIBLE);
            colorTextView.setText("Color: ".concat(color));

            for (int i = 0; i < singleProductDataResponse.getProductVariableResponses().size(); i++) {
                if (color.equals(singleProductDataResponse.getProductVariableResponses().get(i).getColor())) {
                    viewPager.setCurrentItem(i + 1, true);
                    price.setText(getString(R.string.rupee).concat(" ").concat(singleProductDataResponse.getProductVariableResponses().get(i).getPrice()));
                    break;
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        swipeRefreshLayout.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        collapsingToolbarLayout.setVisibility(View.VISIBLE);
        favLinearLayout.setVisibility(View.VISIBLE);
    }
}