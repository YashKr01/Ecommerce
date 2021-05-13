package com.shopping.bloom.bottomSheet;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shopping.bloom.R;
import com.shopping.bloom.adapters.ProductsOverViewAdapter;
import com.shopping.bloom.model.CartItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CheckoutProductBottomSheet extends BottomSheetDialog {

    private Context context;
    RecyclerView recyclerView;
    ImageView imgClose;
    List<CartItem> cartItemList;

    public CheckoutProductBottomSheet(@NonNull @NotNull Context context, List<CartItem> cartItems) {
        super(context);
        this.context = context;
        this.cartItemList = new ArrayList<>(cartItems);

        final View view = getLayoutInflater().inflate(R.layout.bottom_sheet_products, null);
        setContentView(view);
        initViews(view);
        setUpRecyclerView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rvProducts);
        imgClose = view.findViewById(R.id.imgClose);

        imgClose.setOnClickListener((v)->{
            dismiss();
        });
    }

    private void setUpRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        ProductsOverViewAdapter adapter = new ProductsOverViewAdapter(context, cartItemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
