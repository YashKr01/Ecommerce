package com.shopping.bloom.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.shopping.bloom.R;
import com.shopping.bloom.databinding.ActivityMainBinding;
import com.shopping.bloom.utils.LoginManager;

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
        if (show) {
            mainView.layoutToolbar.tvTitle.setVisibility(View.GONE);
            mainView.layoutToolbar.etSearch.setVisibility(View.VISIBLE);
        } else {
            mainView.layoutToolbar.etSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_cart) {
            Toast.makeText(this, "Cart clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

//        switch (id){
//            case R.id.menu_settings:
//                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.menu_cart:
//                Toast.makeText(this, "Cart clicked", Toast.LENGTH_SHORT).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
    }
}