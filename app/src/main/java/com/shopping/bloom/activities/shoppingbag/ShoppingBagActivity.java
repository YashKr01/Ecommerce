package com.shopping.bloom.activities.shoppingbag;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.shoppingbag.ShoppingBagAdapter;
import com.shopping.bloom.databinding.ActivityShoppingBagBinding;
import com.shopping.bloom.model.shoppingbag.ProductEntity;
import com.shopping.bloom.restService.callback.ShoppingBagItemListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.shoppingbag.ShoppingBagViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingBagActivity extends AppCompatActivity implements ShoppingBagItemListener {

    private ActivityShoppingBagBinding binding;
    private ShoppingBagViewModel viewModel;
    private List<ProductEntity> list;
    private ShoppingBagAdapter adapter;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingBagBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialise view model
        viewModel = new ViewModelProvider(this).get(ShoppingBagViewModel.class);

        list = new ArrayList<>();
        adapter = new ShoppingBagAdapter(this, list, this);
        binding.shoppingBagRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.shoppingBagRecyclerView.setAdapter(adapter);

        getShoppingBagItems();
    }

    private void getShoppingBagItems() {
        binding.progressBar5.setVisibility(View.VISIBLE);

        if (NetworkCheck.isConnect(this)) {
            viewModel.getShoppingBagItems().observe(this, productEntities -> {
                if (productEntities != null && !productEntities.isEmpty()) {
                    list.clear();
                    list.addAll(productEntities);
                    adapter.notifyDataSetChanged();
                    binding.progressBar5.setVisibility(View.INVISIBLE);
                } else {
                    // handle empty list
                    binding.progressBar5.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            // handle no connection
            binding.progressBar5.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void btnRemoveClickListener(ProductEntity productEntity) {

        builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to remove this item ?");
        builder.setCancelable(false);
        builder.setNegativeButton("NO", (dialog, which) -> {
            dialog.cancel();
        });

        builder.setPositiveButton("YES", (dialog, which) -> {
            viewModel.deleteFromShoppingBag(productEntity);
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}