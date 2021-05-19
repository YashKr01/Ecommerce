package com.shopping.bloom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.ShoppingProductAdapter;
import com.shopping.bloom.bottomSheet.CheckoutProductBottomSheet;
import com.shopping.bloom.model.CartItem;
import com.shopping.bloom.model.PostCartProduct;
import com.shopping.bloom.model.ResponseCheckoutData;
import com.shopping.bloom.restService.callback.CheckoutResponseListener;
import com.shopping.bloom.restService.response.GetCheckoutResponse;
import com.shopping.bloom.restService.response.PostCheckoutData;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.Const;
import com.shopping.bloom.utils.LoginManager;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.ShoppingBagViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.shopping.bloom.utils.Const.ADD_ADDRESS_ACTIVITY;

public class PlaceOrderActivity extends AppCompatActivity {

    private static final String TAG = PlaceOrderActivity.class.getName();

    TextView tvDiscountPrice, tvSubTotal, tvShippingCharge, tvTotal;
    TextView tvCouponText;
    TextView tvErrorAddress, tvErrorCoupon;
    ImageView tvRemoveCoupon;
    TextView tvWalletBalance, shippingAddress, tvDiscountSaved, tvChangeAddress;
    RecyclerView productsRecyclerView;
    LinearLayout llApplyCoupon, llGiftCard, llUseWallet;
    LinearLayout pmCard, pmNetBanking, pmUPI;   //pm = payment method
    AppCompatCheckBox cbUseWallet;
    Button btPlaceOrder;
    RelativeLayout progressBar;
    ShoppingProductAdapter adapter;
    NestedScrollView mainScrollView;

    List<CartItem> cartItemList;
    ShoppingBagViewModel viewModel;
    boolean FIRST_TIME = true;
    final int REQ_COUPON_CODE = 200;
    final int REQ_ADDRESS_CODE = 201;
    String promoCode = null;
    String promoOffer = "";
    String addressID = "";
    String address = "";
    private LoginManager loginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        viewModel = ViewModelProviders.of(this).get(ShoppingBagViewModel.class);
        loginManager = new LoginManager(PlaceOrderActivity.this);
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
                }
                if (FIRST_TIME) {
                    checkNetworkAndFetchData();
                    FIRST_TIME = !FIRST_TIME;
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
        tvRemoveCoupon = findViewById(R.id.imgRemoveCoupon);
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
        mainScrollView = findViewById(R.id.nestedScrollView);
        tvErrorAddress = findViewById(R.id.tvErrorAddress);
        tvErrorCoupon = findViewById(R.id.tvErrorCoupon);

        setDefaultAddress();

        //Attach Click listeners
        pmCard.setOnClickListener(paymentMethod);
        pmNetBanking.setOnClickListener(paymentMethod);
        pmUPI.setOnClickListener(paymentMethod);
        llApplyCoupon.setOnClickListener(discountClickListener);
        llGiftCard.setOnClickListener(discountClickListener);
        llUseWallet.setOnClickListener(discountClickListener);
        cbUseWallet.setOnClickListener(discountClickListener);

        btPlaceOrder.setOnClickListener(view -> {
            if (!NetworkCheck.isConnect(this)) {
                Toast.makeText(PlaceOrderActivity.this, getString(R.string.no_internet_connected), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if(!validateData()) return ;
            if (loginManager.getIs_primary_address_available()) {
                placeOrder();
            } else {
                Toast.makeText(PlaceOrderActivity.this, "No Address", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), AddShippingAddressActivity.class);
                startActivityForResult(intent, ADD_ADDRESS_ACTIVITY);
            }
        });
        cbUseWallet.setOnClickListener(view -> checkNetworkAndFetchData());
        tvChangeAddress.setOnClickListener(view -> changeAddress());
        tvRemoveCoupon.setOnClickListener(view -> {
            applyOrRemoveCoupon(false);
            checkNetworkAndFetchData();
        });
    }

    private void setDefaultAddress() {
        address = LoginManager.getInstance().getPrimary_address();
        addressID = LoginManager.getInstance().getPrimary_address_id();
        if (address == null || address.isEmpty() ||
                addressID == null || addressID.isEmpty()) {
            shippingAddress.setText("ADD ADDRESS");
            addressID = "";
            return;
        }
        address = address.replaceAll(",", "\n");
        shippingAddress.setText(address);
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
            Log.d(TAG, "checkNetworkAndFetchData: NULL checkout data");
            showProgressBar(false);
            return;
        }
        resetError();
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
                Toast.makeText(PlaceOrderActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
            if (errorCode == -1) {
                //unknown error
            }
            showError(errorCode, errorMessage);
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
        if (addressID == null || addressID.isEmpty() || addressID.equals("NA")) {
            Log.d(TAG, "getPostCheckoutData: AddressID");
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
            intent.putExtra(ARG_ACTIVITY_NAME, PlaceOrderActivity.class.getName());
            startActivityForResult(intent, REQ_COUPON_CODE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADDRESS_CODE) {
            if (resultCode == RESULT_CANCELED) {
                shippingAddress.setText("");
                return;
            }

            if (resultCode == RESULT_OK) {
                String ARG_ADDRESS_ID = "ADDRESS_ID";
                String ARG_ADDRESS = "ADDRESS";
                if (data != null) {
                    address = String.valueOf(data.getStringExtra(ARG_ADDRESS));
                    addressID = String.valueOf(data.getStringExtra(ARG_ADDRESS_ID));
                    if (addressID == null || address == null ||
                            addressID.isEmpty() || address.isEmpty()) {
                        showError(REQ_ADDRESS_CODE, "");
                        return;
                    }
                    address = address.replaceAll(",", "\n");
                    shippingAddress.setText(address);
                } else {
                    Log.d(TAG, "onActivityResult: NULL address");
                    showError(REQ_ADDRESS_CODE, "");
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == REQ_COUPON_CODE) {
            if (data != null) {
                promoCode = data.getStringExtra("PROMOCODE");
                promoOffer = String.valueOf(data.getDoubleExtra("PROMO_OFFER", 0));
                applyOrRemoveCoupon(true);
                checkNetworkAndFetchData();
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
        if (!NetworkCheck.isConnect(this)) {
            Toast.makeText(this, "No internet!!", Toast.LENGTH_SHORT)
                    .show();
            apply = false;
        }
        if (apply) {
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
        tvErrorAddress.setVisibility(View.GONE);
        String CALLING_ACTIVITY = "CALLING_ACTIVITY";
        Intent intent = new Intent(this, MyAddressActivity.class);
        intent.putExtra(CALLING_ACTIVITY, PlaceOrderActivity.class.getName());
        startActivityForResult(intent, REQ_ADDRESS_CODE);
    }

    private void showNoInternetScreen(boolean show) {
        if (show) {

        } else {

        }
    }

    private boolean validateData() {
        return address != null && !address.isEmpty();
    }

    private void showError(int error_code, String errorMessage) {
        switch (error_code) {
            case REQ_ADDRESS_CODE: {
                Log.d(TAG, "showError:L address error");
                break;
            }
            case REQ_COUPON_CODE: {
                Log.d(TAG, "showError:L invalid coupon code");
                break;
            }
            case Const.ERROR_NO_ADDRESS_FOUND: {
                Log.d(TAG, "showError: no address found");
                mainScrollView.smoothScrollTo(0,0);
                tvErrorAddress.setVisibility(View.VISIBLE);
                tvErrorAddress.setText("No address found. Try changing the PinCode");
                break;
            }
            case Const.ERROR_INVALID_PROMO_CODE: {
                Log.d(TAG, "showError: invalid promo code");
                tvErrorAddress.setVisibility(View.VISIBLE);
                tvErrorAddress.setText(errorMessage);
                break;
            }
            case Const.ERROR_NO_DELIVERY_AVAILABLE: {
                Log.d(TAG, "showError: no delivery at this pin code");
                tvErrorAddress.setVisibility(View.VISIBLE);
                tvErrorAddress.setText(R.string.error_no_delivery);
                break;
            }
        }
    }

    private void resetError() {
        tvErrorAddress.setVisibility(View.GONE);
        tvErrorCoupon.setVisibility(View.GONE);
    }

}