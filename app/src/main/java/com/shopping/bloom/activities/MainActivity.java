package com.shopping.bloom.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.shopping.bloom.R;
import com.shopping.bloom.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private ActivityMainBinding mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainView.getRoot();
        setContentView(view);

        NavController navController = Navigation.findNavController(this, R.id.home_fragment);
        NavigationUI.setupWithNavController(mainView.bottomNavigationView, navController);

        navController.addOnDestinationChangedListener(destinationChangedListener);
        setSupportActionBar(mainView.layoutToolbar.topAppBar);
    }

    /*Customize the toolbar according to the fragment*/
    private final NavController.OnDestinationChangedListener destinationChangedListener =
            (controller, destination, arguments) -> {

                Log.d(TAG, "onDestinationChanged: current fragment Id " + destination.getId());
                //show the searchBar if current fragment is shopFragment or category Fragment
                Boolean searchBarVisible = (destination.getId() == R.id.shopFragment) ||
                        (destination.getId() == R.id.categoryFragment);
                showSearchBar(searchBarVisible);
                if (destination.getId() == R.id.newTrendFragment) {
                    setTitle(getString(R.string.new_));
                }

                if (destination.getId() == R.id.profileFragment) {
                    setTitle("");
                    mainView.layoutToolbar.imgMail.setVisibility(View.GONE);
                    mainView.layoutToolbar.imgFavourites.setVisibility(View.GONE);
                } else {
                    mainView.layoutToolbar.imgMail.setVisibility(View.VISIBLE);
                    mainView.layoutToolbar.imgFavourites.setVisibility(View.VISIBLE);
                }
            };


    private void setTitle(String title) {
        //Hide the search box and update the
        showSearchBar(false);
        mainView.layoutToolbar.tvTitle.setText(title);
        mainView.layoutToolbar.tvTitle.setVisibility(View.VISIBLE);
    }

    private void showSearchBar(Boolean show) {
        if(show) {
            mainView.layoutToolbar.tvTitle.setVisibility(View.GONE);
            mainView.layoutToolbar.etSearch.setVisibility(View.VISIBLE);
        } else {
            mainView.layoutToolbar.etSearch.setVisibility(View.GONE);
        }
    }

}