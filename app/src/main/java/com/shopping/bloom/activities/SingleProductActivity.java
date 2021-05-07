package com.shopping.bloom.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amar.library.ui.StickyScrollView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.shopping.bloom.App;
import com.shopping.bloom.BuildConfig;
import com.shopping.bloom.R;
import com.shopping.bloom.adapters.singleproduct.ColorAdapter;
import com.shopping.bloom.adapters.singleproduct.ProductDescAdapter;
import com.shopping.bloom.adapters.singleproduct.RandomImageAdapter;
import com.shopping.bloom.adapters.singleproduct.SizeAdapter;
import com.shopping.bloom.adapters.singleproduct.ViewPagerImageAdapter;
import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.fragment.reviewsfragment.ReviewsFragment;
import com.shopping.bloom.model.ProductVariableResponse;
import com.shopping.bloom.model.RandomImageDataResponse;
import com.shopping.bloom.model.SingleProductDataResponse;
import com.shopping.bloom.model.SingleProductDescResponse;
import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.model.shoppingbag.ProductEntity;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewModels.SingleProductViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class SingleProductActivity extends AppCompatActivity {
    boolean colorClickable, sizeClickable;
    NestedScrollView scrollView;
    RecyclerView colorRecyclerView, sizeRecyclerView, randomRecyclerView;
    SingleProductViewModel singleProductViewModel;
    ColorAdapter colorAdapter;
    SizeAdapter sizeAdapter;
    SingleProductDataResponse singleProductDataResponse;
    List<String> colorList, sizeList, imageList;
    List<RandomImageDataResponse> randomImageList;
    LinearLayout linearLayout;
    TextView productName, price, viewReview, colorTextView, slideTextView, desc;
    RatingBar ratingBar;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    ViewPager viewPager;
    List<ProductVariableResponse> productVariableResponseList;
    ViewPagerImageAdapter viewPagerImageAdapter;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ViewStub viewStub;
    LinearLayout favLinearLayout, linearLayoutDesc;
    RelativeLayout relativeLayout, hideRelativeLayout;
    Toolbar toolbar, reviewToolbar;
    FrameLayout frameLayout;
    private Integer PRODUCT_ID;
    String CATEGORY_ID;
    Button changePinCode;
    Dialog dialog;
    ImageButton wishListButton, selectedWishListButton;
    Button btnAddToBag;
    RandomImageAdapter randomImageAdapter;
    List<SingleProductDescResponse> list;
    int limit = 21, pageNo = 0;
    View inflated;
    List<String> wishList;
    String selectedColor, selectedSize, token;
    WishListItem wishListItem;

    //todo collapsing issue with toolbar when scrolling
    // done add price in recommended section

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        if (getIntent() != null) {
            PRODUCT_ID = getIntent().getIntExtra("PRODUCT_ID", 1);
            CATEGORY_ID = getIntent().getStringExtra("CATEGORY_ID");
        }
        Log.d("SEND", "onCreate: " + PRODUCT_ID);
        Log.d("SEND", "onCreate: " + CATEGORY_ID);

        wishList = new ArrayList<>();

        EcommerceDatabase.databaseWriteExecutor.execute(() -> {
            wishList = EcommerceDatabase.getInstance().wishListProductDao().getAllItem();
            System.out.println(wishList);
        });

        LoginManager loginManager = new LoginManager(SingleProductActivity.this);

        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }

        wishListItem = new WishListItem(String.valueOf(PRODUCT_ID), token);

        productName = findViewById(R.id.product_name);
        scrollView = findViewById(R.id.scrollView);
        price = findViewById(R.id.price);
        colorTextView = findViewById(R.id.color);
        ratingBar = findViewById(R.id.ratingBar4);
        viewReview = findViewById(R.id.viewAllReviewTextView);
        linearLayout = findViewById(R.id.linearLayout);
        favLinearLayout = findViewById(R.id.fav_layout);
        linearLayoutDesc = findViewById(R.id.linearLayoutDescription);
        relativeLayout = findViewById(R.id.relative);
        viewPager = findViewById(R.id.viewpager);
        viewStub = findViewById(R.id.vsEmptyScreen);
        toolbar = findViewById(R.id.toolbar);
        desc = findViewById(R.id.textViewDesc);
        reviewToolbar = findViewById(R.id.reviewToolbar);
        slideTextView = findViewById(R.id.slideTextView);
        changePinCode = findViewById(R.id.changePinCode);
        wishListButton = findViewById(R.id.wishListButton);
        selectedWishListButton = findViewById(R.id.selectWishListButton);
        randomRecyclerView = findViewById(R.id.randomRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        hideRelativeLayout = findViewById(R.id.hideRelative);

        inflated = viewStub.inflate();
        //swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        frameLayout = findViewById(R.id.fragment);
        collapsingToolbarLayout = findViewById(R.id.collapseToolbar);

        hideRelativeLayout.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        btnAddToBag = findViewById(R.id.btn_add_to_bag);

        toolbar.setNavigationIcon(R.drawable.ic_back_background);
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);

        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        reviewToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        //swipeRefreshLayout.setOnRefreshListener(this::checkNetworkConnectivity);

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

        //view pager
        productVariableResponseList = new ArrayList<>();
        imageList = new ArrayList<>();
        viewPagerImageAdapter = new ViewPagerImageAdapter(imageList, this);
        viewPager.setAdapter(viewPagerImageAdapter);

        //random Product
        LinearLayoutManager randomLinearLayoutManager = new GridLayoutManager(this, 3);
        randomImageList = new ArrayList<>();
        randomRecyclerView.setLayoutManager(randomLinearLayoutManager);
        randomImageAdapter = new RandomImageAdapter(this, randomImageList);
        randomRecyclerView.setAdapter(randomImageAdapter);

        TextView textView = findViewById(R.id.description);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDescription);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager3);

        list = new ArrayList<>();
        ProductDescAdapter productDescAdapter = new ProductDescAdapter(this, list);
        recyclerView.setAdapter(productDescAdapter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        double heightInDp = height * 0.5;

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

                HashSet<String> colorSet = new LinkedHashSet<>(colorList);
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

                textView.setText(this.singleProductDataResponse.getDescription());
                list = this.singleProductDataResponse.getSingleProductDescResponseList();
                productDescAdapter.setSingleProductDescResponseList(list);
                productDescAdapter.notifyDataSetChanged();

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
                    if (linearLayoutDesc.getVisibility() == View.VISIBLE) {
                        desc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
                        linearLayoutDesc.setVisibility(View.GONE);
                    } else {
                        desc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);
                        linearLayoutDesc.setVisibility(View.VISIBLE);
                    }
                });

                if (linearLayoutDesc.getVisibility() == View.VISIBLE) {
                    desc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);
                } else {
                    desc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
                }

            }
        });

        singleProductViewModel.getRandomImageDataResponseMutableLiveData().observe(this, randomImageDataResponses -> {
            if (randomImageDataResponses == null) {
                System.out.println("Empty");
            } else {
                randomImageList.addAll(randomImageDataResponses);
                randomImageAdapter.setImageList(randomImageList);
                randomImageAdapter.notifyDataSetChanged();
            }
        });

        if (PRODUCT_ID != null) {
            singleProductViewModel.makeApiCall(PRODUCT_ID, getApplication());
        }

        singleProductViewModel.makeApiCallCreateUserActivity(String.valueOf(PRODUCT_ID), CATEGORY_ID, getApplication());

        singleProductViewModel.getLoginResponseModelMutableLiveData().observe(this, loginResponseModel -> {
            if (loginResponseModel != null) {
                ShowToast.showToast(this, loginResponseModel.getMessage());
            }
        });


        singleProductViewModel.makeApiCallRandomImage(limit, pageNo, getApplication());

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = scrollView.getChildAt(scrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                        .getScrollY()));

                if (diff == 0) {
                    pageNo++;
                    System.out.println(pageNo);
                    singleProductViewModel.makeApiCallRandomImage(limit, pageNo, getApplication());
                }
            }
        });

        viewReview.setOnClickListener(debouncedOnClickListener);

        AppBarLayout appBarLayout = findViewById(R.id.appbarLayout);
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
                slideTextView.setText((position + 1) + "/" + imageList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_change_pincode);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        EditText editText = dialog.findViewById(R.id.pinCodeEditText);
        Button button = dialog.findViewById(R.id.changePinCodeButton);
        button.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                Toast.makeText(SingleProductActivity.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        changePinCode.setOnClickListener(debouncedOnClickListener);
        wishListButton.setOnClickListener(debouncedOnClickListener);
        selectedWishListButton.setOnClickListener(debouncedOnClickListener);
        btnAddToBag.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                addToShoppingBag();
            }
        });

        checkNetworkConnectivity();

        TextView textViewVS = inflated.findViewById(R.id.tvSwipeToRefresh);
        textViewVS.setText("Click to Refresh");
        ConstraintLayout constraintLayout = inflated.findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(new DebouncedOnClickListener(150) {
            @Override
            public void onDebouncedClick(View v) {
                checkNetworkConnectivity();
            }
        });

        for(String s: wishList){
            if(s.equals(String.valueOf(PRODUCT_ID))){
                System.out.println("Visible");
                wishListButton.setVisibility(View.GONE);
                selectedWishListButton.setVisibility(View.VISIBLE);
                break;
            }
        }

    }

    private void addToShoppingBag() {

        Integer id = singleProductDataResponse.getId();
        String name = singleProductDataResponse.getProduct_name();
        String image = singleProductDataResponse.getPrimary_image();
        String price = singleProductDataResponse.getPrice();
        ProductEntity productEntity = new ProductEntity(id, image, name, null, price, null);

        singleProductViewModel.addToShoppingBag(productEntity);

    }


    private final DebouncedOnClickListener debouncedOnClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            if (v.getId() == R.id.viewAllReviewTextView) {
                hideRelativeLayout.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                collapsingToolbarLayout.setVisibility(View.GONE);
                favLinearLayout.setVisibility(View.GONE);
                reviewToolbar.setVisibility(View.VISIBLE);

                Bundle bundle = new Bundle();
                bundle.putString("PRODUCT_ID", String.valueOf(PRODUCT_ID));

                Fragment fragment = new ReviewsFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

            } else if (v.getId() == R.id.changePinCode) {
                dialog.show();
            } else if (v.getId() == R.id.wishListButton) {
                selectedWishListButton.setVisibility(View.VISIBLE);
                wishListButton.setVisibility(View.GONE);
                EcommerceDatabase.databaseWriteExecutor.execute(() -> { EcommerceDatabase.getInstance().wishListProductDao().addToWishList(wishListItem);
                });
            } else if (v.getId() == R.id.selectWishListButton) {
                wishListButton.setVisibility(View.VISIBLE);
                selectedWishListButton.setVisibility(View.GONE);
                EcommerceDatabase.databaseWriteExecutor.execute(() -> { EcommerceDatabase.getInstance().wishListProductDao().delete(wishListItem);});
            }
        }
    };

    private void checkNetworkConnectivity() {
        if (!NetworkCheck.isConnect(this)) {

            viewStub.setVisibility(View.VISIBLE);

            relativeLayout.setVisibility(View.GONE);
            favLinearLayout.setVisibility(View.GONE);
        } else {
            viewStub.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            favLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setViewPagerCurrentItem(int pos) {
        selectedColor = colorList.get(pos).trim();
        if (!selectedColor.isEmpty()) {
            colorTextView.setVisibility(View.VISIBLE);
            colorTextView.setText("Color: ".concat(selectedColor));

            for (int i = 0; i < singleProductDataResponse.getProductVariableResponses().size(); i++) {
                if (selectedColor.equals(singleProductDataResponse.getProductVariableResponses().get(i).getColor())) {
                    viewPager.setCurrentItem(i + 1, true);
                    //viewPagerImageAdapter.notifyDataSetChanged();
                    price.setText(getString(R.string.rupee).concat(" ").concat(singleProductDataResponse.getProductVariableResponses().get(i).getPrice()));
                    break;
                }
            }
            sizeList.clear();
            for (ProductVariableResponse productVariableResponse : singleProductDataResponse.getProductVariableResponses()) {
                if (productVariableResponse.getColor().equals(selectedColor)) {
                    System.out.println(productVariableResponse.getSize());
                    sizeList.add(productVariableResponse.getSize());
                }
            }
            HashSet<String> hashSet = new LinkedHashSet<>(sizeList);
            sizeList.clear();
            sizeList.addAll(hashSet);
            sizeAdapter.setSizeList(sizeList, sizeClickable);
            sizeAdapter.notifyDataSetChanged();
        }
    }

    public void setSizeCurrentItem(int position) {
        selectedSize = sizeList.get(position);
        if (!selectedSize.isEmpty()) {
            colorList.clear();
            for (ProductVariableResponse productVariableResponse : singleProductDataResponse.getProductVariableResponses()) {
                if (productVariableResponse.getSize().equals(selectedSize)) {
                    System.out.println(productVariableResponse.getColor());
                    colorList.add(productVariableResponse.getColor());
                }
            }
            HashSet<String> hashSet = new LinkedHashSet<>(colorList);
            colorList.clear();
            colorList.addAll(hashSet);
            colorAdapter.setColorList(colorList, colorClickable);
            colorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_single_product, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.share){
            share();
        }

            return super.onOptionsItemSelected(item);

    }
    Bitmap bitmap1;

    public void share() {
        try {
            ImageView imageView = (ImageView)viewPager.findViewWithTag(viewPager.getCurrentItem());
            bitmap1 = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } catch (Exception e) {
            Toast.makeText(this, "No image available", Toast.LENGTH_SHORT).show();
        }
        try {
            File file = new File(this.getExternalCacheDir(), File.separator + "image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            file.setReadable(true, false);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(SingleProductActivity.this,
                    BuildConfig.APPLICATION_ID + ".fileprovider", file));
            intent.putExtra(Intent.EXTRA_TEXT, singleProductDataResponse.getProduct_name());
            intent.setType("image/*");
            startActivity(Intent.createChooser(intent, "Share Image via.."));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "No image available2", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        hideRelativeLayout.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        collapsingToolbarLayout.setVisibility(View.VISIBLE);
        favLinearLayout.setVisibility(View.VISIBLE);
    }

}