package com.shopping.bloom.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shopping.bloom.R;
import com.shopping.bloom.firebaseConfig.RemoteConfig;
import com.shopping.bloom.models.MainScreenImageModel;

public class ShopFragment extends Fragment {
    private static final String TAG = "ShopFragment";

    public ShopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

      MainScreenImageModel mainScreenImageModel  =  RemoteConfig.getMainScreenConfig(getContext()).getViewpager_image().get(0) ;
        Log.d(TAG , mainScreenImageModel.getImagepath() + "\n " +  mainScreenImageModel.getId() + "\n " + mainScreenImageModel.getOrder() + "\n "  );
        Log.d(TAG , RemoteConfig.getMainScreenConfig(getContext()).getSaleimagepath() );

        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}