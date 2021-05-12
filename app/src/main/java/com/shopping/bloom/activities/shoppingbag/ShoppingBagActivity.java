package com.shopping.bloom.activities.shoppingbag;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.shopping.bloom.R;
import com.shopping.bloom.activities.CheckoutActivity;
import com.shopping.bloom.adapters.shoppingbag.ShoppingBagAdapter;
import com.shopping.bloom.adapters.shoppingbag.SuggestCartItemAdapter;
import com.shopping.bloom.databinding.ActivityShoppingBagBinding;
import com.shopping.bloom.firebaseConfig.RemoteConfig;
import com.shopping.bloom.model.CartItem;
import com.shopping.bloom.model.PostCartProduct;
import com.shopping.bloom.model.PromoConfig;
import com.shopping.bloom.model.ResponseCartData;
import com.shopping.bloom.model.SuggestedCartProduct;
import com.shopping.bloom.restService.callback.CartValueCallback;
import com.shopping.bloom.restService.callback.ShoppingBagItemListener;
import com.shopping.bloom.restService.response.GetCartValueResponse;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.shoppingbag.ShoppingBagViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingBagActivity extends AppCompatActivity implements ShoppingBagItemListener {
    private static final String TAG = ShoppingBagActivity.class.getName();

    private ActivityShoppingBagBinding binding;
    private ShoppingBagViewModel viewModel;
    private List<CartItem> cartItemList;
    private ShoppingBagAdapter cartItemAdapter;
    private SuggestCartItemAdapter suggestCartItemAdapter;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingBagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        binding.toolbar.setNavigationOnClickListener((v)-> onBackPressed());

        // initialise view model
        viewModel = new ViewModelProvider(this).get(ShoppingBagViewModel.class);
        setUpRecyclerView();

        subscribeToUI(viewModel.getAllCartItem());
        getTotalCartItem(viewModel.getTotalCartItems());
        getPromoOffer();

        binding.btCheckOut.setOnClickListener(view -> gotoCheckOutScreen());
    }

    private void gotoCheckOutScreen() {
        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void getPromoOffer() {
        PromoConfig config = RemoteConfig.getPromoConfig(this);
        String promoOffer = config.getPromo();
        binding.tvOffer.setText(promoOffer);
    }


    private void setUpRecyclerView() {
        cartItemList = new ArrayList<>();
        cartItemAdapter = new ShoppingBagAdapter(this, this);
        binding.shoppingBagRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.shoppingBagRecyclerView.setAdapter(cartItemAdapter);

        suggestCartItemAdapter = new SuggestCartItemAdapter(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        binding.rvSuggestionProduct.setLayoutManager(layoutManager);
        binding.rvSuggestionProduct.setAdapter(suggestCartItemAdapter);
        suggestCartItemAdapter.updateList(getDummyData());
    }

    private void subscribeToUI(LiveData<List<CartItem>> liveCartItems) {
        liveCartItems.observe(this, cartItems1 -> {
            cartItemList = cartItems1;
            if (cartItemList != null) {
                cartItemAdapter.updateList(cartItemList);
                if(cartItemList.isEmpty()) {
                    updateUI(null);
                } else {
                    getCartValue(cartItemList);
                }
            } else {
                updateUI(null);
                cartItemAdapter.clearAll();
            }
        });
    }

    private void getTotalCartItem(LiveData<Integer> totalCartItems) {
        totalCartItems.observe(this, integer -> {
            int totalCartItem = 0;
            try{
                totalCartItem = integer;
            } catch (NullPointerException c) {
                Log.d(TAG, "getTotalCartItem: NULL pointer");
            }
            if(totalCartItem == 0) {
                binding.btCheckOut.setText("CHECKOUT");
            } else {
                String total = "CHECKOUT("+totalCartItem+")";
                binding.btCheckOut.setText(total);
            }
        });
    }

    private void getCartValue(List<CartItem> cartItems) {
        showProgressBar(true);
        if(!NetworkCheck.isConnect(this)) {
            binding.tvDummyText.setText("No internet!!");
            showProgressBar(false);
            return;
        }
        List<PostCartProduct> postCartProducts = new ArrayList<>();
        for (CartItem c : cartItems) {
            PostCartProduct p1 = new PostCartProduct(c.getChildId(), String.valueOf((c.getQuantity())));
            postCartProducts.add(p1);
        }
        viewModel.getCartValue(postCartProducts, responseListener);
    }

    private final CartValueCallback responseListener = new CartValueCallback() {
        @Override
        public void onSuccess(GetCartValueResponse response) {
            showProgressBar(false);
            Log.d(TAG, "onSuccess: response" + response.toString());
            updateUI(response.getData());
            showProgressBar(false);
        }

        @Override
        public void onFailed(int errorCode, String message) {
            binding.tvDummyText.setText("Something went wrong!!");
            showProgressBar(false);
            Log.d(TAG, "onFailed: ");
        }
    };

    //Update the price
    private void updateUI(ResponseCartData data) {
        if(data == null) {
            showProgressBar(false);
            showEmptyCart(true);
            binding.tvCartPrice.setVisibility(View.INVISIBLE);
            return;
        }
        showEmptyCart(false);
        String totalCartValue = CommonUtils.getSignedAmount(String.valueOf(data.getSubTotal()));
        binding.tvCartPrice.setText(totalCartValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shopping_bag_menu, menu);
        return true;
    }

    @Override
    public void removeCartItem(CartItem cartItem) {
        showDeleteConfirmation(cartItem);
    }

    @Override
    public void updateCartItem(CartItem cartItem) {
        //getCartValue(cartItemList);
        viewModel.updateCartItem(cartItem);
    }

    @Override
    public void maxItemAdded() {
        Toast.makeText(this, "Can not add more item", Toast.LENGTH_SHORT)
                .show();
    }

    void showDeleteConfirmation(CartItem cartItem) {
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to remove " + cartItem.getName() + " ?");
        builder.setCancelable(false);
        builder.setNegativeButton("NO", (dialog, which) -> {
            dialog.cancel();
        });
        builder.setPositiveButton("Remove", (dialog, which) -> {
            viewModel.removeItemFromCart(cartItem);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showProgressBar(boolean show) {
        if(show) {
            binding.rlProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.rlProgressBar.setVisibility(View.GONE);
        }
    }

    private void showEmptyCart(boolean show) {
        if(show) {
            binding.rlEmptyCart.setVisibility(View.VISIBLE);
        } else {
            binding.rlEmptyCart.setVisibility(View.GONE);
        }
    }

    private List<SuggestedCartProduct> getDummyData() {
        List<SuggestedCartProduct> list = new ArrayList<>();
        for(int i = 0; i < 28; i++) {
            SuggestedCartProduct p1 = new SuggestedCartProduct("", "Dummy Data");
            list.add(p1);
        }
        return list;
    }

}