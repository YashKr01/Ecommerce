package com.shopping.bloom.activities.shoppingbag;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.shoppingbag.ShoppingBagAdapter;
import com.shopping.bloom.databinding.ActivityShoppingBagBinding;
import com.shopping.bloom.model.CartItem;
import com.shopping.bloom.model.PostCartProduct;
import com.shopping.bloom.model.ResponseCartData;
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
    private ShoppingBagAdapter adapter;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingBagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        // initialise view model
        viewModel = new ViewModelProvider(this).get(ShoppingBagViewModel.class);

        cartItemList = new ArrayList<>();
        adapter = new ShoppingBagAdapter(this, this);
        binding.shoppingBagRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.shoppingBagRecyclerView.setAdapter(adapter);

        Log.d(TAG, "onCreate: ");
        subscribeToUI(viewModel.getAllCartItem());
    }

    private void subscribeToUI(LiveData<List<CartItem>> liveCartItems) {
        liveCartItems.observe(this, cartItems1 -> {
            cartItemList = cartItems1;
            if (cartItemList != null) {
                adapter.updateList(cartItemList);
                if(cartItemList.isEmpty()) {
                    updateUI(null);
                } else {
                    getCartValue(cartItemList);
                }
            } else {
                updateUI(null);
                adapter.clearAll();
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
            binding.tvCartPrice.setVisibility(View.INVISIBLE);
            return;
        }
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

}