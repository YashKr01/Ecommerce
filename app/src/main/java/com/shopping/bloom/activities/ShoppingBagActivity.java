package com.shopping.bloom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.ShoppingBagAdapter;
import com.shopping.bloom.adapters.SuggestCartItemAdapter;
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
import com.shopping.bloom.viewModels.ShoppingBagViewModel;

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
    private SwipeRefreshLayout srlNoInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingBagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpToolBar();

        // initialise view model
        viewModel = new ViewModelProvider(this).get(ShoppingBagViewModel.class);
        setUpRecyclerView();

        checkNetworkAndFetchData();
        getPromoOffer();
        binding.srlNoInternet.setOnRefreshListener(this::checkNetworkAndFetchData);
        binding.btCheckOut.setOnClickListener(view -> gotoCheckOutScreen());
    }

    /*
     *   Check network
     *       if connected: show the list of cart items and update the total number of Items
     *       else show the no internet screen with pull down to refresh option
     * */
    private void checkNetworkAndFetchData() {
        binding.srlNoInternet.setRefreshing(false);
        showOrHideNoInternet(!NetworkCheck.isConnect(this));
        if (!NetworkCheck.isConnect(this)) return;
        subscribeToUI(viewModel.getAllCartItem());
        getTotalCartItem(viewModel.getTotalCartItems());
    }

    private void setUpToolBar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        binding.toolbar.setNavigationOnClickListener((v) -> onBackPressed());
    }

    private void gotoCheckOutScreen() {
        if(!NetworkCheck.isConnect(this)) {
            showOrHideNoInternet(false);
            return;
        }
        Intent intent = new Intent(this, PlaceOrderActivity.class);
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
                if (cartItemList.isEmpty()) {
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
            try {
                totalCartItem = integer;
            } catch (NullPointerException c) {
                Log.d(TAG, "getTotalCartItem: NULL pointer");
            }
            if (totalCartItem == 0) {
                binding.btCheckOut.setText("CHECKOUT");
            } else {
                String total = "CHECKOUT(" + totalCartItem + ")";
                binding.btCheckOut.setText(total);
            }
        });
    }

    private void getCartValue(List<CartItem> cartItems) {
        showProgressBar(true);
        if (!NetworkCheck.isConnect(this)) {
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
            showProgressBar(false);
            Log.d(TAG, "onFailed: ");
        }
    };

    //Update the price
    private void updateUI(ResponseCartData data) {
        if (data == null) {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int ID = item.getItemId();
        if(ID == R.id.action_wishList) {
            gotoWishListActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoWishListActivity() {
        Intent intent = new Intent(this, WishListActivity.class);
        startActivity(intent);
    }

    @Override
    public void removeCartItem(CartItem cartItem) {
        showDeleteConfirmation(cartItem);
    }

    @Override
    public void updateCartItem(CartItem cartItem) {
        viewModel.updateCartItem(cartItem);
    }

    @Override
    public void maxItemAdded() {
        Toast.makeText(this, getString(R.string.can_not_add_more_items), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void noInternet() {
        showOrHideNoInternet(true);
    }

    private void showOrHideNoInternet(final boolean show) {
        if (show) {
            binding.srlNoInternet.setVisibility(View.VISIBLE);
        } else {
            binding.srlNoInternet.setVisibility(View.GONE);
        }
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
        if (show) {
            binding.rlProgressBar.setVisibility(View.VISIBLE);
        } else {
            binding.rlProgressBar.setVisibility(View.GONE);
        }
    }

    private void showEmptyCart(boolean show) {
        if (show) {
            binding.rlEmptyCart.setVisibility(View.VISIBLE);
        } else {
            binding.rlEmptyCart.setVisibility(View.GONE);
        }
    }

    private List<SuggestedCartProduct> getDummyData() {
        List<SuggestedCartProduct> list = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            SuggestedCartProduct p1 = new SuggestedCartProduct("", "Dummy Data");
            list.add(p1);
        }
        return list;
    }

}