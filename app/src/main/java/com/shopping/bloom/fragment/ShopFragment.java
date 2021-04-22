package com.shopping.bloom.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
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
import com.shopping.bloom.model.Category;
import com.shopping.bloom.model.MainScreenConfig;
import com.shopping.bloom.model.MainScreenImageModel;
import com.shopping.bloom.model.SubCategory;
import com.shopping.bloom.restService.callback.CategoryResponseListener;
import com.shopping.bloom.restService.callback.LoadMoreItems;
import com.shopping.bloom.restService.callback.ProductClickListener;
import com.shopping.bloom.utils.CommonUtils;
import com.shopping.bloom.utils.DebouncedOnClickListener;
import com.shopping.bloom.utils.NetworkCheck;
import com.shopping.bloom.viewModels.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private static final String TAG = "ShopFragment";

    private ViewPager2 vpHeaderImages;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout srlNoInternet;
    private MainScreenConfig mainScreenConfig;
    private ViewpagerAdapter viewpagerAdapter;
    private ImageView imgHeaderImage;
    private LinearLayout vpIndicator;
    private Runnable runnable;
    private Handler handler = new Handler();

    private CategoryViewModel viewModel;

    private RecyclerView rvCategoryImages;          //At Top Category suggestion Horizontal Scroll
    private RecyclerView rvTopProductSuggestion;    //Categories
    private RecyclerView rvBottomProductSuggestion; //At bottom Category suggestion

    private CategoryImagesAdapter categoryImagesAdapter;
    private NestedProductAdapter topProductSuggestionAdapter;
    private NestedProductAdapter bottomProductSuggestionAdapter;
    private ViewStub vsEmptyScreen;
    ImageView offerImage1;
    ImageView offerImage2;
    ImageView offerImage3;
    ImageView offerImage4;

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
        setUpViewPager();
        setUpRecyclerView();

        //Load banner and Offer images
        loadBannerAndOfferImages();

        checkNetworkAndFetchData();
        swipeRefreshLayout.setOnRefreshListener(this::checkNetworkAndFetchData);
        srlNoInternet.setOnRefreshListener(this::checkNetworkAndFetchData);
    }

    private void initViews(View view) {
        vpHeaderImages = view.findViewById(R.id.vpHeaderImages);
        vpIndicator = view.findViewById(R.id.vpIndicator);
        imgHeaderImage = view.findViewById(R.id.imgNewArrival);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        srlNoInternet = view.findViewById(R.id.srlNoInternet);

        //below are the three recyclerView used in shop fragment
        rvTopProductSuggestion = view.findViewById(R.id.rvTopProductSuggestion);        //At Top Category suggestion Horizontal Scroll
        rvCategoryImages = view.findViewById(R.id.rvProductsCategory);                  //Categories
        rvBottomProductSuggestion = view.findViewById(R.id.rvBottomProductSuggestion);  //At bottom Category suggestion
        offerImage1 = view.findViewById(R.id.imgOffer1);
        offerImage2 = view.findViewById(R.id.imgOffer2);
        offerImage3 = view.findViewById(R.id.imgOffer3);
        offerImage4 = view.findViewById(R.id.imgOffer4);

        offerImage1.setOnClickListener(offerClickListener);
        offerImage2.setOnClickListener(offerClickListener);
        offerImage3.setOnClickListener(offerClickListener);
        offerImage4.setOnClickListener(offerClickListener);

        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    private void setUpViewPager() {
        viewpagerAdapter = new ViewpagerAdapter(mainScreenConfig.getViewpager_image(), getContext(), imageModel -> {
            Log.d(TAG, "initViews: viewpager clicked " + imageModel.toString());
        });

        setUpIndicator();   //View pager Indicators

        if(mainScreenConfig == null) { return; }

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
        startAutoSlider(mainScreenConfig.getViewpager_image().size());
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
            Navigation.findNavController(requireActivity(), R.id.home_fragment).navigate(R.id.viewCategoryFragment);
        });
        rvCategoryImages.setHasFixedSize(true);
        rvCategoryImages.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvCategoryImages.setAdapter(categoryImagesAdapter);

        rvTopProductSuggestion.setHasFixedSize(true);
        rvTopProductSuggestion.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        topProductSuggestionAdapter = new NestedProductAdapter(getContext(), getDummyData(), suggestedProductClickListener, loadMoreItems);
        rvTopProductSuggestion.setAdapter(topProductSuggestionAdapter);

        rvBottomProductSuggestion.setHasFixedSize(true);
        rvBottomProductSuggestion.setLayoutManager(new GridLayoutManager(getContext(), 3));
        bottomProductSuggestionAdapter = new NestedProductAdapter(getContext(), getDummyData(), suggestedProductClickListener, null);
        rvBottomProductSuggestion.setAdapter(bottomProductSuggestionAdapter);
    }

    private void loadBannerAndOfferImages() {
        int OFFER_IMAGES_COUNT = 4;
        if (getContext() == null || mainScreenConfig == null) return;

        if (mainScreenConfig.getSaleimagepath() != null) {
            CommonUtils.loadImageWithGlide(getContext(), mainScreenConfig.getSaleimagepath(), imgHeaderImage, false);
        }
        if (mainScreenConfig.getOfferImages().size() == OFFER_IMAGES_COUNT) {
            CommonUtils.loadImageWithGlide(getContext(), mainScreenConfig.getOfferImages().get(0).getImagepath(), offerImage1, false);
            CommonUtils.loadImageWithGlide(getContext(), mainScreenConfig.getOfferImages().get(1).getImagepath(), offerImage2, false);
            CommonUtils.loadImageWithGlide(getContext(), mainScreenConfig.getOfferImages().get(2).getImagepath(), offerImage3, false);
            CommonUtils.loadImageWithGlide(getContext(), mainScreenConfig.getOfferImages().get(3).getImagepath(), offerImage4, false);
        }
    }

    private void checkNetworkAndFetchData() {
        if (NetworkCheck.isConnect(getContext())) {
            int PAGE_NO = 0;
            viewModel.setResponseListener(responseListener);
            viewModel.fetchData("1", 10, PAGE_NO, "");
        } else {
            setNoInternetLayout(true);
            Log.d(TAG, "onRefresh: NO INTERNET CONNECTION");
        }
    }

    private final CategoryResponseListener responseListener = new CategoryResponseListener() {
        @Override
        public void onSuccess(List<Category> category) {
            setNoInternetLayout(false);
            categoryImagesAdapter.updateList(category, true);
            Log.d(TAG, "onSuccess: productSize " + category.size());
        }

        @Override
        public void onFailure(int errorCode, String errorMessage) {
            Log.d(TAG, "onFailure: errorCode" + errorCode + " errorMessage " + errorMessage);
            setNoInternetLayout(false);
        }
    };

    private final LoadMoreItems loadMoreItems = () -> {
        Log.d(TAG, "loadMoreItems: ");
    };

    private final ProductClickListener suggestedProductClickListener = new ProductClickListener() {
        @Override
        public void onProductClick(Category categoryCategory) {
            Log.d(TAG, "onProductClick: " + categoryCategory);
        }

        @Override
        public void onSubProductClick(SubCategory product) {
            Log.d(TAG, "onSubProductClick: " + product);
        }
    };

    @Override
    public void onDestroy() {
        if (runnable != null) handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    private final DebouncedOnClickListener offerClickListener = new DebouncedOnClickListener(150) {
        @Override
        public void onDebouncedClick(View v) {
            Log.d(TAG, "onDebouncedClick: viewId " + v.getId());
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

    private void startAutoSlider(final int count) {
        runnable = () -> {
            int pos = vpHeaderImages.getCurrentItem();
            pos = pos + 1;
            if (pos >= count) pos = 0;
            vpHeaderImages.setCurrentItem(pos, true);
            handler.postDelayed(runnable, 4500);
        };
        handler.postDelayed(runnable, 5000);
    }

    private void setCurrentIndicator(int position) {
        if (getContext() == null) return;
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

    //set No internet layout to visible and hide the main layout
    private void setNoInternetLayout(boolean visible) {
        swipeRefreshLayout.setRefreshing(false);
        srlNoInternet.setRefreshing(false);
        if (visible) {
            srlNoInternet.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        } else {
            srlNoInternet.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    private List<SubCategory> getDummyData() {
        List<SubCategory> list = new ArrayList<>();
        //String thumbNail = "http://bloomapp.in/images/product/product_image_3.png";
        String thumbNail = "/images/product/product_image_3.png";
        for (int i = 0; i < 12; i++) {
            SubCategory subCategory = new SubCategory(1, "", "MockData", thumbNail, ""
                    , "", "", "", "");
            list.add(subCategory);
        }
        SubCategory dummyItem = new SubCategory(-1, "", "", thumbNail, ""
                , "", "", "", "");
        list.add(dummyItem);
        return list;
    }

}