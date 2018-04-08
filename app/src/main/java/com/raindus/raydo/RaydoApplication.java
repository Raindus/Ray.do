package com.raindus.raydo;

import android.app.Application;

import com.raindus.raydo.plan.entity.MyObjectBox;

import io.objectbox.BoxStore;

/**
 * Created by Raindus on 2018/3/10.
 */

public class RaydoApplication extends Application {

    private static BoxStore mBoxStore;
    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mBoxStore = MyObjectBox.builder().androidContext(this).build();
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }

    public static Application get() {
        return mApplication;
    }
}
