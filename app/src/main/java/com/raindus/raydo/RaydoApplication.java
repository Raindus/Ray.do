package com.raindus.raydo;

import android.app.Application;

import io.objectbox.BoxStore;

/**
 * Created by Raindus on 2018/3/10.
 */

public class RaydoApplication extends Application {

    private static BoxStore mBoxStore;

    @Override
    public void onCreate() {
        super.onCreate();
       // mBoxStore = MyObjectBox.builder().androidContext(this).build();
    }

    public BoxStore getBoxStore(){
        return mBoxStore;
    }
}
