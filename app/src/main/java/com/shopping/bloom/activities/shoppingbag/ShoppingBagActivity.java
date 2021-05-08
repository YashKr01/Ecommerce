package com.shopping.bloom.activities.shoppingbag;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.shoppingbag.ShoppingBagAdapter;
import com.shopping.bloom.databinding.ActivityShoppingBagBinding;
import com.shopping.bloom.model.CartItem;
import com.shopping.bloom.restService.callback.ShoppingBagItemListener;
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

    private void subscribeToUI(LiveData<List<CartItem>> cartItems) {
        cartItems.observe(this, cartItems1 -> {
            cartItemList = cartItems1;
            if (cartItemList != null) {
                Log.d(TAG, "subscribeToUI: " + cartItemList.toString());
                adapter.updateList(cartItemList);
            } else {
                adapter.clearAll();
            }
            binding.progressBar5.setVisibility(View.INVISIBLE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shopping_bag_menu, menu);
        return true;
    }

    @Override
    public void removeCartItem(CartItem cartItem) {
        if (cartItem.getQuantity() == 1) {
            //ToDo: remove this item
        } else {
            //ToDo: show popup with how many item to delete
        }
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
}