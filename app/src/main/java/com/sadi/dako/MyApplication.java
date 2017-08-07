package com.sadi.dako;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
