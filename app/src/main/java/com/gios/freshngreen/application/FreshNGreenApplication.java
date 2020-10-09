package com.gios.freshngreen.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.razorpay.Checkout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FreshNGreenApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static Context appContext;


    @Override
    public void onCreate() {
        super.onCreate();

        try {
            appContext = getApplicationContext();
            Checkout.preload(getApplicationContext());
        } catch (Exception ignored) {
        }
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    public static Context getAppContext() {
        return appContext;
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}