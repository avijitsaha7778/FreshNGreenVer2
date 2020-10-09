package com.gios.freshngreen.activities;

import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gios.freshngreen.R;
import com.gios.freshngreen.dialogs.LogoutDialog;
import com.gios.freshngreen.utils.SharedPref;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import ru.nikartm.support.ImageBadgeView;

import static androidx.navigation.Navigation.findNavController;

public class HomeActivity extends AppCompatActivity {
    private SharedPref sharedPref;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;
    public static TextView userName;
    public static TextView screenNameLabel;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private TextView logoutButton;
    private TextView appVersion;
    private ImageView profile;
    private static ImageBadgeView toolbarCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
      /*  if (new SharedPref(this).isLightTheme())
            setTheme(AppCompatDelegate.MODE_NIGHT_NO);
        else
            setTheme(AppCompatDelegate.MODE_NIGHT_YES);*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        manageNavigation();
        setListeners();
    }

    private void manageNavigation() {
        setSupportActionBar(toolbar);

        navController = findNavController(this, R.id.nav_host_fragment);
        navController.getGraph().setStartDestination(R.id.homeFragment);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.homeFragment, R.id.profileFragment, R.id.wishlistFragment,R.id.categoriesFragment,
                        R.id.cartFragment)
                        .setOpenableLayout(drawerLayout)
                        .build();
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        drawerLayout.setScrimColor(getColor(R.color.light));
        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
        });


        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.productListFragment ||
                    destination.getId() == R.id.productDetailsFragment ||
                    destination.getId() == R.id.contactUsFragment  ||
                    destination.getId() == R.id.orderAddressFragment  ||
                    destination.getId() == R.id.orderPaymentFragment  ||
                    destination.getId() == R.id.orderHistoryFragment ) {
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }

            if (destination.getId() == R.id.profileFragment ||
                    destination.getId() == R.id.cartFragment ||
                    destination.getId() == R.id.contactUsFragment ||
                    destination.getId() == R.id.orderAddressFragment ||
                    destination.getId() == R.id.orderPaymentFragment  ||
                    destination.getId() == R.id.orderHistoryFragment ) {
                toolbarCart.setVisibility(View.INVISIBLE);
            } else {
                toolbarCart.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initViews() {
        sharedPref = new SharedPref(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        profile = findViewById(R.id.profile);
        toolbarCart = findViewById(R.id.toolbarCart);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        userName = findViewById(R.id.userName);
        screenNameLabel = findViewById(R.id.screenNameLabel);
        logoutButton = findViewById(R.id.logout_button);
        appVersion = findViewById(R.id.appVersion);
        setAppVersion();
        setUserName(sharedPref.getName());
    }

    private void setAppVersion() {
        try {
            PackageInfo pInfo = HomeActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            appVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListeners() {
        profile.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.profileFragment);
        });

        userName.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.profileFragment);
        });

        findViewById(R.id.navHome).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.homeFragment);
        });

        findViewById(R.id.navCategories).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.categoriesFragment);
        });

        findViewById(R.id.contactUs).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.contactUsFragment);
        });

        findViewById(R.id.rateUs).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            String market_uri = "https://play.google.com/store/apps/details?id=com.gios.freshngreen";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(market_uri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                    .FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });

        toolbarCart.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.cartFragment);
        });

        findViewById(R.id.navWishlist).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.wishlistFragment);
        });

        findViewById(R.id.navCart).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.cartFragment);
        });

        findViewById(R.id.navProfile).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.profileFragment);
        });

        findViewById(R.id.navOrderHistory).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.orderHistoryFragment);
        });

        logoutButton.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            new LogoutDialog().show(getSupportFragmentManager(), LogoutDialog.class.getSimpleName());
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public static void setUserName(String name){
        userName.setText("Hello, " + name);
    }
    public static void setScreenName(String screenName){
        screenNameLabel.setText(screenName);
    }
    public static void setCartValue(int value){
        toolbarCart.setBadgeValue(value);
    }
    public static void removeBadge(){
        toolbarCart.clearBadge();
    }
}
