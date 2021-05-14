package com.shopping.bloom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.activities.coupons.CouponsActivity;
import com.shopping.bloom.adapters.ShoppingProductAdapter;
import com.shopping.bloom.bottomSheet.CheckoutProductBottomSheet;
import com.shopping.bloom.model.CartItem;
import com.shopping.bloom.model.PostCartProduct;
import com.shopping.bloom.model.ResponseCheckoutData;
import com.shopping.bloom.restService.callback.CheckoutResponseListener;
import com.shopping.bloom.restService.response.GetCheckoutResponse;
import com.shopping.bloom.restService.response.PostCheckoutData;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.Tools;
import com.shopping.bloom.viewModels.shoppingbag.ShoppingBagViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.shopping.bloom.utils.Const.ADD_ADDRESS_ACTIVITY;
import static com.shopping.bloom.utils.Const.LOGIN_ACTIVITY;

public class CheckoutActivity extends AppCompatActivity {

    private static final String TAG = CheckoutActivity.class.getName();

    TextView tvDiscountPrice, tvSubTotal, tvShippingCharge, tvTotal;
    TextView tvCouponText, tvRemoveCoupon;
    TextView tvWalletBalance, shippingAddress, tvDiscountSaved, tvChangeAddress;
    RecyclerView productsRecyclerView;
    LinearLayout llApplyCoupon, llGiftCard, llUseWallet;
    LinearLayout pmCard, pmNetBanking, pmUPI;   //pm = payment method
    AppCompatCheckBox cbUseWallet;
    Button btPlaceOrder;
    RelativeLayout progressBar;
    ShoppingProductAdapter adapter;

    List<CartItem> cartItemList;
    ShoppingBagViewModel viewModel;
    boolean FIRST_TIME = true;
    final int REQ_COUPON_CODE = 200;
    String promoCode = null;
    String promoOffer = "";
    private LoginManager loginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        viewModel = ViewModelProviders.of(this).get(ShoppingBagViewModel.class);
        loginManager = new LoginManager(CheckoutActivity.this);
        initView();
        setUpRecyclerView();
        subscribeToUI(viewModel.getAllCartItem());
        cartItemList = new ArrayList<>();

    }

    private void subscribeToUI(LiveData<List<CartItem>> allCartItem) {
        allCartItem.observe(this, cartItems -> {
            Log.d(TAG, "subscribeToUI: ");
            if (cartItems != null) {
                if (!cartItems.isEmpty()) {
                    Log.d(TAG, "subscribeToUI: NOT EMPTY" +
                            cartItems.toString());
                    cartItemList = cartItems;
                    adapter.updateList(cartItemList);
                    if (FIRST_TIME) {
                        checkNetworkAndFetchData();
                        FIRST_TIME = !FIRST_TIME;
                    }
                } else {
                    Log.d(TAG, "subscribeToUI: EMPTY");
                }
            } else {
                Log.d(TAG, "subscribeToUI: NULL");
            }
        });
    }

    private void initView() {
        tvDiscountPrice = findViewById(R.id.tvDiscount);
        tvSubTotal = findViewById(R.id.tvSubTotal);
        tvShippingCharge = findViewById(R.id.tvShippingCharge);
        tvTotal = findViewById(R.id.tvTotalCartValue);
        tvDiscountSaved = findViewById(R.id.tvDiscountSaved);
        shippingAddress = findViewById(R.id.tvShippingAddress);
        tvWalletBalance = findViewById(R.id.tvWalletBalance);
        tvChangeAddress = findViewById(R.id.tvChangeAddress);
        tvRemoveCoupon = findViewById(R.id.tvRemoveCoupon);
        tvCouponText = findViewById(R.id.tvApplyCoupon);
        productsRecyclerView = findViewById(R.id.rvShoppingBag);
        llApplyCoupon = findViewById(R.id.llApplyCoupon);
        llGiftCard = findViewById(R.id.llGiftCard);
        llUseWallet = findViewById(R.id.llWalletBalance);
        pmCard = findViewById(R.id.llPaymentMethodCard);      //pm = paymentMethod
        pmNetBanking = findViewById(R.id.llPaymentMethodNetBanking);
        pmUPI = findViewById(R.id.llPaymentMethodUPI);
        cbUseWallet = findViewById(R.id.cbUseWallet);
        btPlaceOrder = findViewById(R.id.btPlaceOrder);
        progressBar = findViewById(R.id.rlProgressBar);

        String address = LoginManager.getInstance().getPrimary_address();
        if (!address.isEmpty() && !address.equals("NA")) {
            address = address.replaceAll(",", "\n");
        }
        shippingAddress.setText(address);

        //Attach Click listeners
        pmCard.setOnClickListener(paymentMethod);
        pmNetBanking.setOnClickListener(paymentMethod);
        pmUPI.setOnClickListener(paymentMethod);
        llApplyCoupon.setOnClickListener(discountClickListener);
        llGiftCard.setOnClickListener(discountClickListener);
        llUseWallet.setOnClickListener(discountClickListener);
        cbUseWallet.setOnClickListener(discountClickListener);

        btPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginManager.getIs_primary_address_available()) {
                    placeOrder();
                }else{
                    //todo user will go on add address screen with intent flag once he set the address will be return to this screen again

                    Toast.makeText(CheckoutActivity.this, "No Address" , Toast.LENGTH_SHORT) .show();
                    Intent intent = new Intent(getApplicationContext(), AddShippingAddressActivity.class);
                    startActivityForResult(intent, ADD_ADDRESS_ACTIVITY);
                }
            }
        });
        cbUseWallet.setOnClickListener(view -> checkNetworkAndFetchData());
        tvChangeAddress.setOnClickListener(view -> changeAddress());
        tvRemoveCoupon.setOnClickListener(view -> {
            applyOrRemoveCoupon(false);
            checkNetworkAndFetchData();
        });
    }

    private void checkNetworkAndFetchData() {
        showProgressBar(true);
        if (!NetworkCheck.isConnect(this)) {
            showNoInternetScreen(true);
            showProgressBar(false);
            return;
        }
        PostCheckoutData checkoutData = getPostCheckoutData();
        if (checkoutData == null) {
            return;
        }
        viewModel.getCheckoutData(checkoutData, responseListener);
    }

    private final CheckoutResponseListener responseListener = new CheckoutResponseListener() {
        @Override
        public void onSuccess(GetCheckoutResponse response) {
            updateUI(response);
            showProgressBar(false);
        }

        @Override
        public void onFailed(int errorCode, String errorMessage) {
            showProgressBar(false);
            if (errorCode == 200) {
                Toast.makeText(CheckoutActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private PostCheckoutData getPostCheckoutData() {
        PostCheckoutData checkoutData = new PostCheckoutData();
        List<PostCartProduct> productsList = new ArrayList<>();
        if (cartItemList != null) {
            for (CartItem cartItem : cartItemList) {
                PostCartProduct item = new PostCartProduct(cartItem.getChildId(),
                        String.valueOf(cartItem.getQuantity()));
                productsList.add(item);
            }
        }
        int useWalletBalance = 0;
        if (cbUseWallet.isChecked()) {
            useWalletBalance = 1;
        }
        String addressID = LoginManager.getInstance().getPrimary_address_id();
        if (addressID.isEmpty() || addressID.equals("NA")) {
            Toast.makeText(this, getString(R.string.invalid_address), Toast.LENGTH_SHORT)
                    .show();
            return null;
        }
        checkoutData.setProductList(productsList);
        checkoutData.setUseWalletBalance(useWalletBalance);
        checkoutData.setPromocode(promoCode);
        try {
            checkoutData.setAddressID(Integer.parseInt(addressID));
        } catch (NullPointerException e) {
            Log.d(TAG, "getPostCheckoutData: Invalid address ID, unable to parse");
            return null;
        }
        return checkoutData;
    }

    private void setUpRecyclerView() {
        adapter = new ShoppingProductAdapter(this, () -> openBottomSheet(cartItemList));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        productsRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerView.setAdapter(adapter);

    }

    private void openBottomSheet(List<CartItem> cartItemList) {
        CheckoutProductBottomSheet bottomSheet = new CheckoutProductBottomSheet(this, cartItemList);
        bottomSheet.show();
    }

    private final View.OnClickListener paymentMethod = view -> {

    };

    private final View.OnClickListener discountClickListener = view -> {
        int ID = view.getId();
        if (ID == R.id.llWalletBalance) {
            //Do nothing
        }
        if (ID == R.id.llApplyCoupon) {
            String ARG_ACTIVITY_NAME = "calling_activity_name";
            Intent intent = new Intent(this, CouponsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra(ARG_ACTIVITY_NAME, CheckoutActivity.class.getName());
            startActivityForResult(intent, REQ_COUPON_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_COUPON_CODE) {
            if (data != null) {
                promoCode = data.getStringExtra("PROMOCODE");
                promoOffer = String.valueOf(data.getDoubleExtra("PROMO_OFFER", 0));
                checkNetworkAndFetchData();
                applyOrRemoveCoupon(true);
            }
        }
    }

    private void updateUI(GetCheckoutResponse response) {
        ResponseCheckoutData data = response.getData();
        String wallet = "Use Wallet ";
        if (data.getWalletBalance() != 0) {
            wallet = wallet + CommonUtils.getSignedAmount("" + data.getWalletBalance());
        }
        tvWalletBalance.setText(wallet);
        String shippingCharges = CommonUtils.getSignedAmount(String.valueOf(data.getShippingCharges()));
        String subTotal = CommonUtils.getSignedAmount(String.valueOf(data.getSubTotal()));
        String total = CommonUtils.getSignedAmount(String.valueOf(data.getTotal()));
        String discount = "₹ 0";
        if (data.getDiscountAmount() != 0) {
            discount = "-" + CommonUtils.getSignedAmount(String.valueOf(data.getDiscountAmount()));
            String discountSaved = "You are saving ₹ " + data.getDiscountAmount() + " on this order";
            cbUseWallet.setChecked(true);
            tvDiscountSaved.setVisibility(View.VISIBLE);
            tvDiscountSaved.setText(discountSaved);
        } else {
            tvDiscountSaved.setVisibility(View.GONE);
        }

        tvShippingCharge.setText(shippingCharges);
        tvSubTotal.setText(subTotal);
        tvTotal.setText(total);
        tvDiscountPrice.setText(discount);
    }

    private void placeOrder() {

    }

    private void applyOrRemoveCoupon(boolean apply) {
        if(apply) {
            String text = "Promocode " + promoCode + " applied successfully";
            tvCouponText.setText(text);
            tvCouponText.setTextColor(ContextCompat.getColor(this, R.color.green_600));
            tvRemoveCoupon.setVisibility(View.VISIBLE);
        } else {
            String text = "Apply Coupon";
            tvCouponText.setText(text);
            tvCouponText.setTextColor(ContextCompat.getColor(this, R.color.blue_grey_900));
            tvRemoveCoupon.setVisibility(View.GONE);
        }
    }

    private void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void changeAddress() {
        Intent intent = new Intent(this, MyAddressActivity.class);
        startActivity(intent);
    }

    private void showNoInternetScreen(boolean show) {
        if (show) {

        } else {

        }
    }

}