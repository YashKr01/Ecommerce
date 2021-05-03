package com.shopping.bloom.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shopping.bloom.R;
import com.shopping.bloom.activities.search.SearchActivity;
import com.shopping.bloom.activities.wishlist.WishListActivity;
import com.shopping.bloom.databinding.ActivityMainBinding;
import com.shopping.bloom.utils.DebouncedOnClickListener;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private ActivityMainBinding mainView;
    private int bottomsheet_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainView = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mainView.getRoot();
        setContentView(view);
        MaterialToolbar toolbar = (mainView.layoutToolbar.topAppBar);
        setSupportActionBar(toolbar);

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(getTopLevelDestinations()).build();
        NavController navController = Navigation.findNavController(this, R.id.home_fragment);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(mainView.bottomNavigationView, navController);

        navController.addOnDestinationChangedListener(destinationChangedListener);
        mainView.bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
            //empty just to stop repeated  api call
        });

        //might use this to handle fragments
       /* mainView.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });
*/

        View includedLayout = findViewById(R.id.layoutToolbar);
        ImageView imgFav = includedLayout.findViewById(R.id.imgFavourites);

        imgFav.setOnClickListener(new DebouncedOnClickListener(200) {
            @Override
            public void onDebouncedClick(View v) {
                startActivity(new Intent(getApplicationContext(), WishListActivity.class));
            }
        });

    }


    /*Customize the toolbar according to the fragment*/
    private final NavController.OnDestinationChangedListener destinationChangedListener =
            (controller, destination, arguments) -> {

                Log.d(TAG, "onDestinationChanged: current fragment Id " + destination.getId());
                //show the searchBar if current fragment is shopFragment or category Fragment
                if (destination.getId() == R.id.shopFragment) {
                    bottomsheet_int = 1;
                } else if (destination.getId() == R.id.categoryFragment) {
                    bottomsheet_int = 2;
                } else if (destination.getId() == R.id.newTrendFragment) {
                    bottomsheet_int = 3;
                } else if (destination.getId() == R.id.profileFragment) {
                    bottomsheet_int = 4;
                }



            /*    Boolean searchBarVisible = (destination.getId() == R.id.shopFragment) ||
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
                }*/

                //Hide the bottom navigation bar if destination fragment not contains in the topLevelDestination
                boolean showBottomNavBar = isTopLevelDestination(destination.getId());
                showBottomNavigationBar(showBottomNavBar);

            };

    private void setTitle(String title) {
        //Hide the search box and update the
        showSearchBar(false);
     /*   mainView.layoutToolbar.tvTitle.setText(title);
        mainView.layoutToolbar.tvTitle.setVisibility(View.VISIBLE);*/
    }

    private void showSearchBar(Boolean show) {
    /*    if (show) {
            mainView.layoutToolbar.tvTitle.setVisibility(View.GONE);
            mainView.layoutToolbar.etSearch.setVisibility(View.VISIBLE);
        } else {
            mainView.layoutToolbar.etSearch.setVisibility(View.GONE);
        }*/
    }

    //Back button on Toolbar will not appear on these fragments
    private Set<Integer> getTopLevelDestinations() {
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.shopFragment);
        topLevelDestinations.add(R.id.categoryFragment);
        topLevelDestinations.add(R.id.newTrendFragment);
        topLevelDestinations.add(R.id.profileFragment);
        return topLevelDestinations;
    }

    private boolean isTopLevelDestination(int id) {
        return getTopLevelDestinations().contains(id);
    }

    private void showBottomNavigationBar(boolean show) {
        long ANIMATION_DURATION = 300L;
        if (show) {
            //show navigation bar with animation
            mainView.bottomNavigationView.animate().setListener(null);
            mainView.bottomNavigationView.clearAnimation();
            mainView.bottomNavigationView.animate().translationY(0f).setDuration(ANIMATION_DURATION);
            mainView.bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            //hide bottom navigation bar with animation
            mainView.bottomNavigationView.clearAnimation();
            mainView.bottomNavigationView.animate()
                    .translationY(mainView.bottomNavigationView.getHeight())
                    .setDuration(ANIMATION_DURATION + 200)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mainView.bottomNavigationView.setVisibility(View.GONE);
                        }
                    });
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        if (bottomsheet_int == 1) {
            inflater.inflate(R.menu.shop_fragment, menu);
            mainView.layoutToolbar.imgFavourites.setVisibility(View.VISIBLE);
            mainView.layoutToolbar.imgMail.setVisibility(View.VISIBLE);
        } else if (bottomsheet_int == 2) {
            inflater.inflate(R.menu.shop_fragment, menu);
            mainView.layoutToolbar.imgFavourites.setVisibility(View.VISIBLE);
            mainView.layoutToolbar.imgMail.setVisibility(View.VISIBLE);
        } else if (bottomsheet_int == 3) {
            inflater.inflate(R.menu.new_fragment_menu, menu);
            mainView.layoutToolbar.imgFavourites.setVisibility(View.VISIBLE);
            mainView.layoutToolbar.imgMail.setVisibility(View.VISIBLE);
        } else if (bottomsheet_int == 4) {
            inflater.inflate(R.menu.profile_fragment_menu, menu);
            mainView.layoutToolbar.imgFavourites.setVisibility(View.GONE);
            mainView.layoutToolbar.imgMail.setVisibility(View.GONE);
        }
        return true;
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
        } else if (id == R.id.action_search) {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}