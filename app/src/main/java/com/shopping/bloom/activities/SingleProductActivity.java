package com.shopping.bloom.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shopping.bloom.R;
import com.shopping.bloom.adapters.singleproduct.ColorAdapter;
import com.shopping.bloom.adapters.singleproduct.ProductDescAdapter;
import com.shopping.bloom.adapters.singleproduct.SizeAdapter;
import com.shopping.bloom.adapters.singleproduct.ViewPagerImageAdapter;
import com.shopping.bloom.model.EmailVerificationModel;
import com.shopping.bloom.model.ProductVariableResponse;
import com.shopping.bloom.model.SingleProductDataResponse;
import com.shopping.bloom.model.SingleProductDescResponse;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.utils.ShowToast;
import com.shopping.bloom.viewModels.SingleProductViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SingleProductActivity extends AppCompatActivity {

    RecyclerView colorRecyclerView;
    RecyclerView sizeRecyclerView;
    SingleProductViewModel singleProductViewModel;
    ColorAdapter colorAdapter;
    SizeAdapter sizeAdapter;
    SingleProductDataResponse singleProductDataResponse;
    List<String> colorList;
    List<String> sizeList;
    LinearLayout linearLayout;
    TextView productName, price;
    RatingBar ratingBar;
    ProgressDialog progressDialog;
    ViewPager viewPager;
    List<String> imageList;
    List<ProductVariableResponse> productVariableResponseList;
    ViewPagerImageAdapter viewPagerImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_product);

        productName = findViewById(R.id.product_name);
        price = findViewById(R.id.price);
        ratingBar = findViewById(R.id.ratingBar4);
        linearLayout = findViewById(R.id.linearLayout);
        viewPager = findViewById(R.id.viewpager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        colorRecyclerView = findViewById(R.id.colorRecyclerView);
        sizeRecyclerView = findViewById(R.id.sizeRecyclerView);
        singleProductDataResponse = new SingleProductDataResponse();

        //color
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        colorRecyclerView.setLayoutManager(linearLayoutManager);
        colorAdapter = new ColorAdapter(this, colorList);
        colorRecyclerView.setAdapter(colorAdapter);
        colorList = new ArrayList<>();

        //size
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        sizeRecyclerView.setLayoutManager(linearLayoutManager2);
        sizeAdapter = new SizeAdapter(this, colorList);
        sizeRecyclerView.setAdapter(sizeAdapter);
        sizeList = new ArrayList<>();

        productVariableResponseList = new ArrayList<>();
        imageList = new ArrayList<>();
        viewPagerImageAdapter = new ViewPagerImageAdapter(imageList);
        viewPager.setAdapter(viewPagerImageAdapter);

        singleProductViewModel = ViewModelProviders.of(this).get(SingleProductViewModel.class);
        singleProductViewModel.getMutableLiveData().observe(this, singleProductDataResponse -> {

            this.singleProductDataResponse = singleProductDataResponse;

            if (this.singleProductDataResponse != null) {
                progressDialog.dismiss();

                String primary = this.singleProductDataResponse.getPrimary_image();
                productVariableResponseList = this.singleProductDataResponse.getProductVariableResponses();

                imageList.add(primary);
                for(ProductVariableResponse productVariableResponse: productVariableResponseList){
                    //
                    imageList.add(productVariableResponse.getPrimary_image());
                }
                System.out.println(imageList.size());

                viewPagerImageAdapter.setImageList(imageList);
                viewPagerImageAdapter.notifyDataSetChanged();

                //Glide.with(this).load("http://bloomapp.in" + primary).into(imageView);

                //colors
                String colors = this.singleProductDataResponse.getAvailable_colors();
                String[] colorArray = colors.split(",");
                colorList.addAll(Arrays.asList(colorArray));

                colorAdapter.setColorList(colorList);
                colorAdapter.notifyDataSetChanged();

                //size
                String size = this.singleProductDataResponse.getAvailable_sizes();
                String[] sizeArray = size.split(",");
                sizeList.addAll(Arrays.asList(sizeArray));

                sizeAdapter.setSizeList(sizeList);
                sizeAdapter.notifyDataSetChanged();

                productName.setText(this.singleProductDataResponse.getProduct_name());
                price.setText(getString(R.string.rupee).concat(" ").concat(this.singleProductDataResponse.getPrice()));

                ratingBar.setRating(Float.parseFloat(this.singleProductDataResponse.getAvg_rating()));

                linearLayout.setOnClickListener(v -> {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
                    View bottomSheet = LayoutInflater.from(this).inflate(R.layout.layout_bottom_sheet_productdesc, findViewById(R.id.bottomSheet));

                    ImageButton imageButton = bottomSheet.findViewById(R.id.imgClose);
                    imageButton.setOnClickListener(v1 -> bottomSheetDialog.dismiss());

                    RecyclerView recyclerView = bottomSheet.findViewById(R.id.recyclerView);
                    LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                    List<SingleProductDescResponse> list;
                    list = this.singleProductDataResponse.getSingleProductDescResponseList();
                    recyclerView.setLayoutManager(linearLayoutManager3);
                    ProductDescAdapter productDescAdapter = new ProductDescAdapter(this, list);
                    productDescAdapter.setSingleProductDescResponseList(list);
                    recyclerView.setAdapter(productDescAdapter);

                    bottomSheetDialog.setContentView(bottomSheet);
                    bottomSheetDialog.show();


                });
            }
        });


        singleProductViewModel.makeApiCall(getApplication());

    }
}