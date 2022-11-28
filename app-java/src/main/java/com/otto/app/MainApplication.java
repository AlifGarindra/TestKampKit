package com.otto.app;

import android.app.Application;

import otto.com.sdk.SDKManager;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKManager sdkManager = SDKManager.Companion.getInstance(this)
                .setClientKey("myClientKey")
                .build();
//        SDKManager sdkman = SDKManager.Companion.config(config).getInstance(this)
//        Log.e("XXXXXX", sdkman.getX());
//        sdkman.setX("NNNNN");

    }
}
