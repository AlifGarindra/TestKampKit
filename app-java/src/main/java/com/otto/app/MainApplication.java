package com.otto.app;

import android.app.Application;
import android.util.Log;

import otto.com.sdk.SDKManager;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKManager sdkman = SDKManager.Companion.getInstance(this);
        Log.e("XXXXXX", sdkman.getX());
        sdkman.setX("NNNNN");

    }
}
