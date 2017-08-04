package com.sadi.dako;

import android.app.Application;

import com.facebook.accountkit.AccountKit;

/**
 * Created by User on 8/2/2017.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AccountKit.initialize(getApplicationContext());
    }
}
