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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.shopping.bloom.R;
import com.shopping.bloom.adapters.ViewpagerAdapter;
import com.shopping.bloom.firebaseConfig.RemoteConfig;
import com.shopping.bloom.models.MainScreenImageModel;
import com.shopping.bloom.restService.callback.ViewPagerClickListener;

import java.util.List;

public class ShopFragment extends Fragment {

    private static final String TAG = "ShopFragment";

    private ViewPager2 vpHeaderImages;
    List<MainScreenImageModel> mainScreenImageModel;
    ViewpagerAdapter viewpagerAdapter;
    LinearLayout vpIndicator;

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

        mainScreenImageModel = RemoteConfig.getMainScreenConfig(getContext()).getViewpager_image();
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

    }

    private void initViews(View view) {
        vpHeaderImages = view.findViewById(R.id.vpHeaderImages);
        vpIndicator = view.findViewById(R.id.vpIndicator);

        viewpagerAdapter = new ViewpagerAdapter(mainScreenImageModel, getContext(), imageModel -> {
            Log.d(TAG, "initViews: viewpager clicked " + imageModel.getImagepath() +
                    " ID: "+ imageModel.getId());
        });
        setUpIndicator();
    }

    private void loadViewPager() {
        if(!mainScreenImageModel.isEmpty()) {
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

    private void setUpIndicator(){
        ImageView[] indicator = new ImageView[viewpagerAdapter.getItemCount()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8,0,8,0);
        for(int i = 0; i < indicator.length; i++){
            indicator[i] = new ImageView(getContext().getApplicationContext());
            indicator[i].setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(),
                    R.drawable.vp_indicator_inactive));
            indicator[i].setLayoutParams(params);
            vpIndicator.addView(indicator[i]);
        }
    }

    private void setCurrentIndicator(int position){
        int childViewCount = vpIndicator.getChildCount();
        for(int i = 0; i < childViewCount; i++){
            ImageView imageView = (ImageView) vpIndicator.getChildAt(i);
            if(i == position){
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext().getApplicationContext(),
                        R.drawable.vp_indicator_active));
            }else{
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

}