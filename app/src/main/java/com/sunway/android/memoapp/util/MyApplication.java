package com.sunway.android.memoapp.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by Mr_RexZ on 6/8/2016.
 */
public class MyApplication extends Application {
    protected static Context mContext;

    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }
}