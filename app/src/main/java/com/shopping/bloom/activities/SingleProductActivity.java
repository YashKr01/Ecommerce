package com.shopping.bloom.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shopping.bloom.BuildConfig;
import com.shopping.bloom.R;
import com.shopping.bloom.adapters.ColorAdapter;
import com.shopping.bloom.adapters.ProductDescAdapter;
import com.shopping.bloom.adapters.RandomImageAdapter;
import com.shopping.bloom.adapters.SizeAdapter;
import com.shopping.bloom.adapters.ViewPagerImageAdapter;
import com.shopping.bloom.database.EcommerceDatabase;
import com.shopping.bloom.fragment.ReviewsFragment;
import com.shopping.bloom.model.CartItem;
import com.shopping.bloom.model.Product;
import com.shopping.bloom.model.ProductVariableResponse;
import com.shopping.bloom.model.SingleProductDataResponse;
import com.shopping.bloom.model.SingleProductDescResponse;
import com.shopping.bloom.model.SingleProductImageResponse;
import com.shopping.bloom.model.WishListItem;
import com.shopping.bloom.restService.callback.AddToCartCallback;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewModels.SingleProductViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import static com.shopping.bloom.utils.Const.LOGIN_ACTIVITY;

public class SingleProductActivity extends AppCompatActivity {

    private static final String TAG = SingleProductActivity.class.getName();

    boolean colorClickable, sizeClickable;
    NestedScrollView scrollView;
    RecyclerView colorRecyclerView, sizeRecyclerView, randomRecyclerView;
    SingleProductViewModel singleProductViewModel;
    ColorAdapter colorAdapter;
    SizeAdapter sizeAdapter;
    SingleProductDataResponse singleProductDataResponse;
    List<String> colorList, sizeList, imageList;
    List<Product> randomImageList;
    List<String> selectedSizeList, selectedColorList;
    int pos, categoryId;
    LinearLayout linearLayout;
    TextView productName, price, viewReview, colorTextView, slideTextView,
            desc, salePrice, salePercentage, deliverStatusTv;
    EditText pincodeEditText;
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
    private String CATEGORY_ID;
    Button changePinCode;
    Dialog dialog;
    ImageButton wishListButton, selectedWishListButton;
    ImageView deliveryStatusIv;
    Button btnAddToBag;
    RandomImageAdapter randomImageAdapter;
    List<SingleProductDescResponse> list;
    int limit = 21, pageNo = 0;
    String SELECTED_COLOR = "";
    String SELECTED_SIZE = "";
    View inflated;
    List<String> wishList;
    String token;
    WishListItem wishListItem;
    String CALLING_ACTIVITY = "";
    boolean isLiked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        getIntentData();

        initViews();

        checkNetworkConnectivity();

        wishList = new ArrayList<>();

        //not required with new json response
        EcommerceDatabase.databaseWriteExecutor.execute(() -> {
            wishList = EcommerceDatabase.getInstance().wishListProductDao().getAllItem();
        });

        LoginManager loginManager = new LoginManager(SingleProductActivity.this);
        if (!loginManager.isLoggedIn()) {
            token = loginManager.gettoken();
        } else {
            token = loginManager.getGuest_token();
        }

        wishListItem = new WishListItem(String.valueOf(PRODUCT_ID), token);

        singleProductDataResponse = new SingleProductDataResponse();


        //view pager
        productVariableResponseList = new ArrayList<>();
        imageList = new ArrayList<>();
        viewPagerImageAdapter = new ViewPagerImageAdapter(imageList, this);
        viewPager.setAdapter(viewPagerImageAdapter);


        RecyclerView recyclerView = findViewById(R.id.recyclerViewDescription);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager3);

        list = new ArrayList<>();
        ProductDescAdapter productDescAdapter = new ProductDescAdapter(this, list);
        recyclerView.setAdapter(productDescAdapter);


        AppBarLayout.LayoutParams layoutParams = (AppBarLayout.LayoutParams) collapsingToolbarLayout.getLayoutParams();
        layoutParams.height = getRequiredHeight();
        collapsingToolbarLayout.setLayoutParams(layoutParams);

        selectedSizeList = new ArrayList<>();
        selectedColorList = new ArrayList<>();
        singleProductViewModel = ViewModelProviders.of(this).get(SingleProductViewModel.class);
        changeCartIcon(singleProductViewModel.getCartSize());

        singleProductViewModel.getMutableLiveData().observe(this, singleProductDataResponse -> {

            this.singleProductDataResponse = singleProductDataResponse;

            if (this.singleProductDataResponse != null) {
                progressDialog.dismiss();

                String primary = this.singleProductDataResponse.getPrimary_image();
                imageList.add(primary);

                for (SingleProductImageResponse imageResponse : this.singleProductDataResponse.getSingleProductImageResponses()) {
                    imageList.add(imageResponse.getImagePath());
                }

                productVariableResponseList = this.singleProductDataResponse.getProductVariableResponses();

                for (ProductVariableResponse productVariableResponse : productVariableResponseList) {
                    imageList.add(productVariableResponse.getPrimary_image());
                    colorList.add(productVariableResponse.getColor());
                    sizeList.add(productVariableResponse.getSize());
                }

                String isOnSale = this.singleProductDataResponse.getIs_on_sale();
                if (isOnSale.equals("1")) {
                    double percentage = Double.parseDouble(this.singleProductDataResponse.getSale_percentage());
                    int per = (int) percentage;
                    salePrice.setVisibility(View.VISIBLE);
                    salePercentage.setVisibility(View.VISIBLE);
                    price.setTextSize(14);
                    price.setText(getString(R.string.rupee).concat(" ").concat(this.singleProductDataResponse.getPrice()));
                    price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    salePrice.setText(CommonUtils.getSignedAmount(this.singleProductDataResponse.getSale_price()));
                    salePercentage.setText(String.valueOf(per).concat(" % OFF"));
                } else {
                    price.setVisibility(View.VISIBLE);
                    salePrice.setVisibility(View.GONE);
                    salePercentage.setVisibility(View.GONE);
                    price.setText(getString(R.string.rupee).concat(" ").concat(this.singleProductDataResponse.getPrice()));
                }

                slideTextView.setText(1 + "/" + imageList.size());

                HashSet<String> colorSet = new LinkedHashSet<>(colorList);
                colorList.clear();
                colorList.addAll(colorSet);

                selectedColorList.addAll(colorSet);

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

                selectedSizeList.addAll(sizeSet);

                if (sizeList.size() == 0) {
                    sizeClickable = false;
                    String size = this.singleProductDataResponse.getAvailable_sizes();
                    String[] sizeArray = size.split(",");
                    sizeList.addAll(Arrays.asList(sizeArray));
                } else {
                    sizeClickable = true;
                }

                TextView textView = findViewById(R.id.description);
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

                //wishlist button from api
                if(this.singleProductDataResponse.isInWishList()){
                    wishListButton.setVisibility(View.GONE);
                    selectedWishListButton.setVisibility(View.VISIBLE);
                }

            }

        });


        if (PRODUCT_ID != null) {
            singleProductViewModel.makeApiCall(PRODUCT_ID, getApplication());
        }
        if (CATEGORY_ID == null) {
            categoryId = -1;
        } else {
            categoryId = Integer.parseInt(CATEGORY_ID);
        }

        checkPinCode();

        viewReview.setOnClickListener(debouncedOnClickListener);

        changePinCode.setOnClickListener(debouncedOnClickListener);
        wishListButton.setOnClickListener(debouncedOnClickListener);
        selectedWishListButton.setOnClickListener(debouncedOnClickListener);
        btnAddToBag.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                //Check for internet connection and then
                //Check if user is logged in or not

                if(!checkNetworkConnectivity()){
                    return;
                }

                /*
                 *   below method returns false if User is logged in :/
                 * */
                if (!LoginManager.getInstance().isLoggedIn()) {
                    addToShoppingBag();
                } else {
                    Toast.makeText(SingleProductActivity.this, "Login and continue shopping", Toast.LENGTH_SHORT)
                            .show();
                    gotoLoginActivity();
                }
            }
        });


        //setting up no internet check
        TextView textViewVS = inflated.findViewById(R.id.tvSwipeToRefresh);
        textViewVS.setText(getString(R.string.tap_to_refresh));
        ConstraintLayout constraintLayout = inflated.findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(new DebouncedOnClickListener(150) {
            @Override
            public void onDebouncedClick(View v) {
                checkNetworkConnectivity();
            }
        });


        //not needed will be removed as new json response is there
//        for (String s : wishList) {
//            if (s.equals(String.valueOf(PRODUCT_ID))) {
//                System.out.println("Visible");
//                wishListButton.setVisibility(View.GONE);
//                selectedWishListButton.setVisibility(View.VISIBLE);
//                break;
//            }
//        }

        SetupColorAndSizeList();
        GetRecommendProductList();
        CreateUserLogs();
    }

    private void getIntentData() {
        String ARG_CALLING_ACTIVITY = "CALLING_ACTIVITY";
        if (getIntent() != null) {
            CALLING_ACTIVITY = getIntent().getStringExtra(ARG_CALLING_ACTIVITY);
            PRODUCT_ID = getIntent().getIntExtra("PRODUCT_ID", 1);
            CATEGORY_ID = getIntent().getStringExtra("CATEGORY_ID");
        }
        Log.d("SEND", "onCreate: " + PRODUCT_ID);
        Log.d("SEND", "onCreate: " + CATEGORY_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_ACTIVITY && resultCode == RESULT_OK ){
    //Todo Do something when user returns from the login Activity

//            Toast.makeText(this, "Check", Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(SingleProductActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, LOGIN_ACTIVITY);
    }

    private void changeCartIcon(LiveData<Integer> cartSize) {
        cartSize.observe(this, integer -> {
            int size = 0;
            try {
                size = integer;
            } catch (NullPointerException e) {
                size = 0;
                Log.d(TAG, "onChanged: ");
            }
            Log.d(TAG, "changeCartIcon: ");
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

    private void addToShoppingBag() {
        if (isColorSizeSelected()) {
            Log.d(TAG, "addToShoppingBag: SELECTED size and color" + SELECTED_SIZE + ", " + SELECTED_COLOR);
            ProductVariableResponse product = getSelectedChildSKUObj(SELECTED_COLOR, SELECTED_SIZE);
            if (product == null) {
                Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (product.getQuantity().equals("0")) {
                // No product available
                Toast.makeText(this, "Out of stock", Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            String parentId = product.getParentId();
            String childId = product.getChildId();
            String primaryImage = product.getPrimary_image();
            String color = product.getColor();
            String size = product.getSize();
            String price = product.getPrice();
            if (product.getIs_on_sale().equals("1")) {
                price = product.getSale_price();
            }
            String name = singleProductDataResponse.getProduct_name();
            Log.d(TAG, "addToShoppingBag: product: " + product.toString());
            CartItem cartItem = new CartItem(parentId, childId, name, primaryImage, color, size, price, Integer.parseInt(product.getQuantity()));
            if (validateCartItem(cartItem, product.getQuantity())) {
                singleProductViewModel.addToShoppingBag(cartItem, product.getQuantity(), callback);
            } else {
                Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(TAG, "addToShoppingBag: color and size " + SELECTED_SIZE + ", " + SELECTED_COLOR);
            if (SELECTED_SIZE.isEmpty()) {
                Toast.makeText(this, getString(R.string.select_size), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.select_color), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private final AddToCartCallback callback = new AddToCartCallback() {
        @Override
        public void onAdded(int totalItems) {
            String text = "";
            if (totalItems == 1) {
                text = totalItems + " item added to the cart";
            } else {
                text = totalItems + " items into the cart";
            }
            Log.d(TAG, "onAdded: " + text);
            String finalText = text;
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(SingleProductActivity.this, finalText, Toast.LENGTH_SHORT)
                    .show());
        }

        @Override
        public void onItemLimitReached(int maxItems) {
            final String text = "You can not add more than " + maxItems + " item.";
            Log.d(TAG, "onItemLimitReached: " + text);
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(SingleProductActivity.this, text, Toast.LENGTH_SHORT)
                    .show());

        }
    };


    /*
       All ui about color and size will be handled here in adapters
     */
    private void SetupColorAndSizeList() {

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

    }

    private void checkPinCode() {
        singleProductViewModel.getPinCodeResponseMutableLiveData().observe(this, pinCodeResponse -> {
            if (pinCodeResponse != null) {
                if(pinCodeResponse.getSuccess().equals("false")){
                    deliverStatusTv.setText(pinCodeResponse.getMessage());
                    deliverStatusTv.setTextColor(Color.RED);
                    deliveryStatusIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_alert));
                }else{
                    deliverStatusTv.setText(pinCodeResponse.getMessage());
                    deliverStatusTv.setTextColor(Color.BLACK);
                    deliveryStatusIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_group_629));
                }

            }else{
                pincodeEditText.setError("Pincode Empty");
                pincodeEditText.requestFocus();
            }
        });
    }

    /*
       Recommended products api call and list handling
     */
    private void GetRecommendProductList() {
        LinearLayoutManager randomLinearLayoutManager = new GridLayoutManager(this, 3);
        randomImageList = new ArrayList<>();
        randomRecyclerView.setLayoutManager(randomLinearLayoutManager);
        randomImageAdapter = new RandomImageAdapter(this, randomImageList);
        randomRecyclerView.setAdapter(randomImageAdapter);


        singleProductViewModel.getRandomImageDataResponseMutableLiveData().observe(this, randomImageDataResponses -> {
            if (randomImageDataResponses == null) {
                System.out.println("Empty");
            } else {
                randomImageList.addAll(randomImageDataResponses);
                randomImageAdapter.setImageList(randomImageList);
                randomImageAdapter.notifyDataSetChanged();
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

    }

    /*
     *   Check if size or color is selected or not
     * */
    private boolean isColorSizeSelected() {
        return (SELECTED_COLOR.length() > 0 && SELECTED_SIZE.length() > 0);
    }

    private boolean validateCartItem(CartItem cartItem, String quantity) {
        if (cartItem.getParentId().isEmpty() || cartItem.getChildId().isEmpty() ||
                cartItem.getName().isEmpty() || cartItem.getPrimaryImage().isEmpty() ||
                cartItem.getProductPrice().isEmpty()) {
            //Something is wrong
            Log.d(TAG, "validateCartItem: SOMETHING IS WRONG INVALID data received");
            return false;
        }
        return true;
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
                String pinCode = pincodeEditText.getText().toString().trim();
                if (!pinCode.isEmpty()) {
                    singleProductViewModel.checkPinCode(pinCode, getApplication());
                }
            } else if (v.getId() == R.id.wishListButton) {
                selectedWishListButton.setVisibility(View.VISIBLE);
                wishListButton.setVisibility(View.GONE);
                EcommerceDatabase.databaseWriteExecutor.execute(() -> {
                    EcommerceDatabase.getInstance().wishListProductDao().addToWishList(wishListItem);
                });
            } else if (v.getId() == R.id.selectWishListButton) {
                wishListButton.setVisibility(View.VISIBLE);
                selectedWishListButton.setVisibility(View.GONE);
                EcommerceDatabase.databaseWriteExecutor.execute(() -> {
                    EcommerceDatabase.getInstance().wishListProductDao().delete(wishListItem);
                });
            }
        }
    };


    /*
        Dilaog for pincode
     */
    private void ShowPincodeDailog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_change_pincode);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
        EditText editText = dialog.findViewById(R.id.pinCodeEditText);
        Button button = dialog.findViewById(R.id.changePinCodeButton);
        button.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                Toast.makeText(SingleProductActivity.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean checkNetworkConnectivity() {
        if (!NetworkCheck.isConnect(this)) {
            viewStub.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);
            favLinearLayout.setVisibility(View.GONE);
            return false;
        } else {
            viewStub.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            favLinearLayout.setVisibility(View.VISIBLE);
            return true;
        }
    }

    public void setViewPagerCurrentItem(Integer pos) {
        try {
            System.out.println("color pos = " + pos);
            System.out.println("color size = " + selectedColorList.size());
            System.out.println("color size2 = " + colorList.size());
            Log.d("colorsize", "activiy = " + pos.toString());
            if (pos != -1) {
                SELECTED_COLOR = colorList.get(pos).trim();
                System.out.println("color = " + SELECTED_COLOR);
                if (!SELECTED_COLOR.isEmpty()) {
                    colorTextView.setVisibility(View.VISIBLE);
                    colorTextView.setText("Color: ".concat(SELECTED_COLOR));

                    for (int i = 0; i < singleProductDataResponse.getProductVariableResponses().size(); i++) {
                        if (SELECTED_COLOR.equals(singleProductDataResponse.getProductVariableResponses().get(i).getColor())) {
                            viewPager.setCurrentItem(i + 2, true);
                            String isOnSale = this.singleProductDataResponse.getProductVariableResponses().get(i).getIs_on_sale();
                            if (isOnSale.equals("1")) {
                                double percentage = Double.parseDouble(this.singleProductDataResponse.getProductVariableResponses().get(i).getSale_percentage());
                                int per = (int) percentage;
                                salePrice.setVisibility(View.VISIBLE);
                                salePercentage.setVisibility(View.VISIBLE);
                                price.setTextSize(14);
                                price.setText(getString(R.string.rupee).concat(" ").concat(this.singleProductDataResponse.getProductVariableResponses().get(i).getPrice()));
                                price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                salePrice.setText(CommonUtils.getSignedAmount(this.singleProductDataResponse.getProductVariableResponses().get(i).getSale_price()));
                                salePercentage.setText(String.valueOf(per).concat(" % OFF"));
                            } else {
                                price.setVisibility(View.VISIBLE);
                                salePrice.setVisibility(View.GONE);
                                salePercentage.setVisibility(View.GONE);
                                price.setTextSize(17);
                                price.setText(getString(R.string.rupee).concat(" ").concat(this.singleProductDataResponse.getProductVariableResponses().get(i).getPrice()));
                            }
                            break;
                        }
                    }

                    sizeList.clear();
                    for (ProductVariableResponse productVariableResponse : singleProductDataResponse.getProductVariableResponses()) {
                        if (productVariableResponse.getColor().equals(SELECTED_COLOR)) {

                            sizeList.add(productVariableResponse.getSize());
                        }
                    }
                    System.out.println("size = " + selectedSizeList.size());
                    HashSet<String> hashSet = new LinkedHashSet<>(sizeList);
                    sizeList.clear();
                    sizeList.addAll(hashSet);

                    if (sizeList.size() == 0) {
                        sizeClickable = false;
                        String size = this.singleProductDataResponse.getAvailable_sizes();
                        String[] sizeArray = size.split(",");
                        sizeList.addAll(Arrays.asList(sizeArray));
                    } else {
                        sizeClickable = true;
                    }

                    sizeAdapter.setSizeList(sizeList, sizeClickable);
                    sizeAdapter.notifyDataSetChanged();
                }
            } else {
                viewPager.setCurrentItem(0, true);
                price.setText(getString(R.string.rupee).concat(" ").concat(this.singleProductDataResponse.getPrice()));
                colorTextView.setVisibility(View.GONE);
                sizeList.clear();
                for (ProductVariableResponse productVariableResponse : singleProductDataResponse.getProductVariableResponses()) {
                    sizeList.add(productVariableResponse.getSize());
                }
                HashSet<String> hashSet = new LinkedHashSet<>(sizeList);
                sizeList.clear();
                sizeList.addAll(hashSet);

                if (sizeList.size() == 0) {
                    sizeClickable = false;
                    String size = this.singleProductDataResponse.getAvailable_sizes();
                    String[] sizeArray = size.split(",");
                    sizeList.addAll(Arrays.asList(sizeArray));
                } else {
                    sizeClickable = true;
                }

                sizeAdapter.setSizeList(sizeList, sizeClickable);
                sizeAdapter.notifyDataSetChanged();


            }
        } catch (Exception ignored) {
        }
    }

    public void setSizeCurrentItem(Integer position) {
        try {
            pos = position;

            if (position != -1) {
                SELECTED_SIZE = sizeList.get(position);

                colorList.clear();
                for (ProductVariableResponse productVariableResponse : singleProductDataResponse.getProductVariableResponses()) {
                    if (productVariableResponse.getSize().equals(SELECTED_SIZE)) {
                        colorList.add(productVariableResponse.getColor());
                    }
                }
                HashSet<String> hashSet = new LinkedHashSet<>(colorList);
                colorList.clear();
                colorList.addAll(hashSet);

                if (colorList.size() == 0) {
                    colorClickable = false;
                    String colors = this.singleProductDataResponse.getAvailable_colors();
                    String[] colorArray = colors.split(",");
                    colorList.addAll(Arrays.asList(colorArray));
                } else {
                    colorClickable = true;
                }

            } else {
                colorTextView.setVisibility(View.GONE);
                colorList.clear();
                for (ProductVariableResponse productVariableResponse : singleProductDataResponse.getProductVariableResponses()) {
                    colorList.add(productVariableResponse.getColor());
                }
                HashSet<String> hashSet = new LinkedHashSet<>(colorList);
                colorList.clear();
                colorList.addAll(hashSet);

                if (colorList.size() == 0) {
                    colorClickable = false;
                    String colors = this.singleProductDataResponse.getAvailable_colors();
                    String[] colorArray = colors.split(",");
                    colorList.addAll(Arrays.asList(colorArray));
                } else {
                    colorClickable = true;
                }

            }
            colorAdapter.setColorList(colorList, colorClickable);
            colorAdapter.notifyDataSetChanged();


        } catch (Exception ignored) {
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
        if (id == R.id.share) {
            share();
        }
        if (id == R.id.action_cart) {
            checkAndOpenShoppingBagActivity();
        }

        return super.onOptionsItemSelected(item);

    }

    private void checkAndOpenShoppingBagActivity() {
        if(!NetworkCheck.isConnect(this)) {
            Toast.makeText(this, getString(R.string.no_internet_connected), Toast.LENGTH_SHORT)
                    .show();
            return ;
        }
        if (!LoginManager.getInstance().isLoggedIn()) {
            startActivity(new Intent(this, ShoppingBagActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


    private Bitmap bitmap1;

    public void share() {
        try {
            ImageView imageView = (ImageView) viewPager.findViewWithTag(viewPager.getCurrentItem());
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

    /*
     *   This method is use to identify if the Product is already added into the
     *       Cart or Not. Just call once and update the UI from callBack method @itemAlreadyAddedToCart
     *   call this method after inflating the UI and update the
     *       add to shopping bag button in itemAlreadyAddedToCart CallBack function
     * */
    private void checkIfExist(String parentID, String childID) {
        EcommerceDatabase.databaseWriteExecutor.execute(() -> {
            List<CartItem> cartItems = EcommerceDatabase.getInstance().cartItemDao().getAllProductWith(parentID, childID);
            itemAlreadyAddedToCart(cartItems != null && !cartItems.isEmpty());
        });
    }

    private void itemAlreadyAddedToCart(boolean alreadyAdded) {
        //TODO: handle the UI accordingly
        if (alreadyAdded) {
            Log.d(TAG, "itemAlreadyAddedToCart: ADDED");
        } else {
            //Show add to cart button
            Log.d(TAG, "itemAlreadyAddedToCart: NOT ADDED");
        }
    }

    /*
     *   You can check if the item is already present with this overloaded function
     * */
    private void checkIfExist(CartItem cartItem) {
        checkIfExist(cartItem.getParentId(), cartItem.getChildId());
    }

    // Return productvairalable object of particular item which user select
    // need to check if mcolor and msize should not be empty or null when calling this method
    private ProductVariableResponse getSelectedChildSKUObj(String mcolor, String msize) {
        if (mcolor.isEmpty() || msize.isEmpty())
            return null;

        ProductVariableResponse mobj = null;
        for (ProductVariableResponse productVariableResponse : singleProductDataResponse.getProductVariableResponses()) {
            if (productVariableResponse.getColor().equalsIgnoreCase(mcolor) &&
                    productVariableResponse.getSize().equalsIgnoreCase(msize)) {
                mobj = productVariableResponse;
            }
        }
        return mobj;
    }

    private void CreateUserLogs() {
        singleProductViewModel.makeApiCallCreateUserActivity(String.valueOf(PRODUCT_ID), categoryId, getApplication());
        singleProductViewModel.getLoginResponseModelMutableLiveData().observe(this, loginResponseModel -> {
            if (loginResponseModel != null) {
                ShowToast.showToast(this, loginResponseModel.getMessage());
            }
        });


    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: " + CALLING_ACTIVITY);
        if(CALLING_ACTIVITY != null && !CALLING_ACTIVITY.isEmpty()) {
            if(CALLING_ACTIVITY.equals(AllProductCategory.class.getName())) {
                Log.d(TAG, "onBackPressed: NOT NULL");
                Intent resultIntent = new Intent();
                resultIntent.putExtra("IS_LIKED", 100);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        } else {
            super.onBackPressed();
        }

        hideRelativeLayout.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        collapsingToolbarLayout.setVisibility(View.VISIBLE);
        favLinearLayout.setVisibility(View.VISIBLE);
    }

    /*
     Return dynamic height for viewpager
     just change value in heightinDP according to screen % height of imageview
     */

    private int getRequiredHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        double heightInDp = height * 0.55;

        return (int) Math.round(heightInDp);
    }


    private void initViews() {

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
        pincodeEditText = findViewById(R.id.pincodeEditText);
        salePrice = findViewById(R.id.salePrice);
        salePercentage = findViewById(R.id.salePercentage);
        deliverStatusTv = findViewById(R.id.deliverStatusTv);
        deliveryStatusIv = findViewById(R.id.deliverStatusIv);

        inflated = viewStub.inflate();

        frameLayout = findViewById(R.id.fragment);
        collapsingToolbarLayout = findViewById(R.id.collapseToolbar);

        hideRelativeLayout.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
        btnAddToBag = findViewById(R.id.btn_add_to_bag);


        colorRecyclerView = findViewById(R.id.colorRecyclerView);
        sizeRecyclerView = findViewById(R.id.sizeRecyclerView);


        toolbar.setNavigationIcon(R.drawable.ic_back_background);
        toolbar.setOnMenuItemClickListener(this::onOptionsItemSelected);

        toolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        reviewToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        String styledText = "Usually Delivery in <b> 5 days </b>";
        deliverStatusTv.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Data");
        progressDialog.setCancelable(true);
        progressDialog.show();

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

    }


}