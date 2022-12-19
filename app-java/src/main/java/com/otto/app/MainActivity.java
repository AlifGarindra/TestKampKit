package com.otto.app;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.otto.app.databinding.ActivityMainBinding;
import com.otto.sdk.shared.interfaces.GeneralListener;
import com.otto.sdk.shared.interfaces.UserInfoListener;
import com.otto.sdk.shared.localData.ErrorStatus;
import com.otto.sdk.shared.localData.GeneralStatus;
import com.otto.sdk.shared.localData.UserInfoStatus;

import otto.com.sdk.SDKManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.e("NNNNN", SDKManager.Companion.getInstance(this).getX());

        SDKManager.Companion.getInstance(this).userInfoListener(new UserInfoListener() {
            @Override
            public void onUserInfo(@NonNull UserInfoStatus userInfo) {

            }
        });

        SDKManager.Companion.getInstance(this).setGeneralListener(new GeneralListener() {

            @Override
            public void onUserProfile(@NonNull UserInfoStatus userInfo) {

            }

            @Override
            public void onOpenPPOB(@NonNull GeneralStatus status) {

            }

            @Override
            public void onClosePPOB(@NonNull GeneralStatus status) {

            }

            @Override
            public void onError(@NonNull ErrorStatus status) {

            }

            @Override
            public void onClientTokenExpired() {

            }

            @Override
            public void onUserAccessTokenExpired() {

            }

            @Override
            public void onAuthCode(@NonNull String authCode) {

            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}