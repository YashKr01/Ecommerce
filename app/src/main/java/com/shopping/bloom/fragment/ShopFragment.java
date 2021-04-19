package com.shopping.bloom.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.CategoryImagesAdapter;
import com.shopping.bloom.adapters.NestedProductAdapter;
import com.shopping.bloom.adapters.ViewpagerAdapter;
import com.shopping.bloom.firebaseConfig.RemoteConfig;
import com.shopping.bloom.model.MainScreenConfig;
import com.shopping.bloom.model.*;
import com.shopping.bloom.model.MainScreenImageModel;
import com.shopping.bloom.restService.callback.CategoryResponseListener;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private static final String TAG = "ShopFragment";

    private ViewPager2 vpHeaderImages;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MainScreenConfig mainScreenConfig;
    private ViewpagerAdapter viewpagerAdapter;
    private ImageView imgHeaderImage;
    private LinearLayout vpIndicator;

    private CategoryViewModel viewModel;

    private RecyclerView rvCategoryImages;          //At Top Product suggestion Horizontal Scroll
    private RecyclerView rvTopProductSuggestion;    //Categories
    private RecyclerView rvBottomProductSuggestion; //At bottom Product suggestion

    private CategoryImagesAdapter categoryImagesAdapter;
    private NestedProductAdapter topProductSuggestionAdapter;
    private NestedProductAdapter bottomProductSuggestionAdapter;

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        mainScreenConfig = RemoteConfig.getMainScreenConfig(getContext());
        List<MainScreenImageModel> mainScreenImageModel = RemoteConfig.getMainScreenConfig(getContext()).getViewpager_image();
        Log.d(TAG, mainScreenImageModel.get(0).getImagepath() + "\n ");
        Log.d(TAG, RemoteConfig.getMainScreenConfig(getContext()).getSaleimagepath());
        Log.d(TAG, "onCreateView: mainScreenImageModel " + mainScreenImageModel.size());

        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().invalidateOptionsMenu();
        initViews(view);
        loadViewPager();
        setUpRecyclerView();

        //Header image
        CommonUtils.loadImageWithGlide(getContext(), mainScreenConfig.getSaleimagepath(), imgHeaderImage, false);

        checkNetworkAndFetchData();
        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkAndFetchData);

    }

    private void initViews(View view) {
        vpHeaderImages = view.findViewById(R.id.vpHeaderImages);
        vpIndicator = view.findViewById(R.id.vpIndicator);
        imgHeaderImage = view.findViewById(R.id.imgNewArrival);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        //below are the three recyclerView
        rvTopProductSuggestion = view.findViewById(R.id.rvTopProductSuggestion);        //At Top Product suggestion Horizontal Scroll
        rvCategoryImages = view.findViewById(R.id.rvProductsCategory);                 //Categories
        rvBottomProductSuggestion = view.findViewById(R.id.rvBottomProductSuggestion);  //At bottom Product suggestion

        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        viewpagerAdapter = new ViewpagerAdapter(mainScreenConfig.getViewpager_image(), getContext(), imageModel -> {
            Log.d(TAG, "initViews: viewpager clicked " + imageModel.getImagepath() +
                    " ID: " + imageModel.getId());
        });
        setUpIndicator();   //View pager Indicators
    }

    private void loadViewPager() {
        if (!mainScreenConfig.getViewpager_image().isEmpty()) {
            vpHeaderImages.setAdapter(viewpagerAdapter);
            viewpagerAdapter.notifyDataSetChanged();
        }
        vpHeaderImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });
    }


    /*
     * Three recycler view
     * 1. Horizontal scrollable for top product suggestions
     * 2. Vertical scrollable for show category images
     * 3. Vertical scrollable for bottom product suggestions
     * */
    private void setUpRecyclerView() {
        categoryImagesAdapter = new CategoryImagesAdapter(getContext(), product -> {
            Log.d(TAG, "initViews: product " + product.getId());
        });
        rvCategoryImages.setHasFixedSize(true);
        rvCategoryImages.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvCategoryImages.setAdapter(categoryImagesAdapter);

        rvTopProductSuggestion.setHasFixedSize(true);
        rvTopProductSuggestion.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        topProductSuggestionAdapter = new NestedProductAdapter(getContext(), getDummyData(), suggestedProductClickListener);
        rvTopProductSuggestion.setAdapter(topProductSuggestionAdapter);

        rvBottomProductSuggestion.setHasFixedSize(true);
        rvBottomProductSuggestion.setLayoutManager(new GridLayoutManager(getContext(), 3));
        bottomProductSuggestionAdapter = new NestedProductAdapter(getContext(), getDummyData(), suggestedProductClickListener);
        rvBottomProductSuggestion.setAdapter(bottomProductSuggestionAdapter);
    }

    private void checkNetworkAndFetchData() {
        if (NetworkCheck.isConnect(getContext())) {
            int PAGE_NO = 0;
            viewModel.setResponseListener(responseListener);
            viewModel.fetchData("1", 10, PAGE_NO, "");
        } else {
            swipeRefreshLayout.setRefreshing(false);
            noInternetToast(true);
            Log.d(TAG, "onRefresh: NO INTERNET CONNECTION");
        }
    }

    private final CategoryResponseListener responseListener = new CategoryResponseListener() {
        @Override
        public void onSuccess(List<Product> product) {
            //Log.d(TAG, "onSuccess: productSize " + product);
            swipeRefreshLayout.setRefreshing(false);
            categoryImagesAdapter.updateList(product);
            Log.d(TAG, "onSuccess: productSize " + product.size());
        }

        @Override
        public void onFailure(int errorCode, String errorMessage) {
            Log.d(TAG, "onFailure: errorCode" + errorCode + " errorMessage " + errorMessage);
            swipeRefreshLayout.setRefreshing(false);
            noInternetToast(false);
        }
    };

    private void noInternetToast(boolean show) {
        //TODO: show No Internet error screen
        String error_text = "";
        if(show) {
            error_text = getString(R.string.no_internet_connected);
        } else {
            error_text = getString(R.string.something_went_wrong);
        }
        if(getContext() != null) {
            Toast.makeText(getContext(), error_text,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private final ProductClickListener suggestedProductClickListener = new ProductClickListener() {
        @Override
        public void onProductClick(Product productCategory) {
            Log.d(TAG, "onProductClick: " + productCategory);
        }

        @Override
        public void onSubProductClick(SubProduct product) {
            Log.d(TAG, "onSubProductClick: " + product);
        }
    };

    private void setUpIndicator() {
        ImageView[] indicator = new ImageView[viewpagerAdapter.getItemCount()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicator.length; i++) {
            indicator[i] = new ImageView(getContext().getApplicationContext());
            indicator[i].setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(),
                    R.drawable.vp_indicator_inactive));
            indicator[i].setLayoutParams(params);
            vpIndicator.addView(indicator[i]);
        }
    }

    private void setCurrentIndicator(int position) {
        int childViewCount = vpIndicator.getChildCount();
        for (int i = 0; i < childViewCount; i++) {
            ImageView imageView = (ImageView) vpIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(),
                        R.drawable.vp_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(),
                        R.drawable.vp_indicator_inactive));
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_settings).setVisible(true);
        MenuItem item = menu.getItem(0);
        item.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.profile_fragment_menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: shop" + menu.getItem(0).getTitle());
    }

    private List<SubProduct> getDummyData() {
        List<SubProduct> list = new ArrayList<>();
        //String thumbNail = "http://bloomapp.in/images/product/product_image_3.png";
        String thumbNail = "/images/product/product_image_3.png";
        for (int i = 0; i < 12; i++) {
            SubProduct subProduct = new SubProduct(1, "", "MockData", thumbNail, ""
                    , "", "", "", "");
            list.add(subProduct);
        }
        return list;
    }

}