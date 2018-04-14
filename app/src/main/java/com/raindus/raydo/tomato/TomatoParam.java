package com.raindus.raydo.tomato;

import android.content.Context;
import android.content.SharedPreferences;

import com.raindus.raydo.RaydoApplication;

/**
 * Created by Raindus on 2018/4/14.
 */

public class TomatoParam {

    private static final String TOMATO_NAME = "tomato";

    private static final String KEY_TARGET_TIME = "targetTime";
    private static final String KEY_TOMATO_TIME = "tomatoTime";
    private static final String KEY_SHORT_REST_TIME = "shortRestTime";
    private static final String KEY_LONG_REST_TIME = "longRestTime";
    private static final String KEY_LONG_REST_INTERVAL_TIMES = "longRestIntervalTimes";

    private static final int DEFAULT_TARGET_TIME = 60;
    private static final int DEFAULT_TOMATO_TIME = 25;
    private static final int DEFAULT_SHORT_REST_TIME = 5;
    private static final int DEFAULT_LONG_REST_TIME = 15;
    private static final int DEFAULT_LONG_REST_INTERVAL_TIMES = 4;

    public static SharedPreferences TomatoShared() {
        return RaydoApplication.get().getSharedPreferences(TOMATO_NAME, Context.MODE_PRIVATE);
    }

    public static void setTargetTime(int time) {
        TomatoShared().edit().putInt(KEY_TARGET_TIME, time).apply();
    }

    public static int getTargetTime() {
        return TomatoShared().getInt(KEY_TARGET_TIME, DEFAULT_TARGET_TIME);
    }

    public static void setTomatoTime(int time) {
        TomatoShared().edit().putInt(KEY_TOMATO_TIME, time).apply();
    }

    public static int getTomatoTime() {
        return TomatoShared().getInt(KEY_TOMATO_TIME, DEFAULT_TOMATO_TIME);
    }

    public static void setShortRestTime(int time) {
        TomatoShared().edit().putInt(KEY_SHORT_REST_TIME, time).apply();
    }

    public static int getShortRestTime() {
        return TomatoShared().getInt(KEY_SHORT_REST_TIME, DEFAULT_SHORT_REST_TIME);
    }

    public static void setLongRestTime(int time) {
        TomatoShared().edit().putInt(KEY_LONG_REST_TIME, time).apply();
    }

    public static int getLongRestTime() {
        return TomatoShared().getInt(KEY_LONG_REST_TIME, DEFAULT_LONG_REST_TIME);
    }

    public static void setLongRestIntervalTimes(int time) {
        TomatoShared().edit().putInt(KEY_LONG_REST_INTERVAL_TIMES, time).apply();
    }

    public static int getLongRestIntervalTimes() {
        return TomatoShared().getInt(KEY_LONG_REST_INTERVAL_TIMES, DEFAULT_LONG_REST_INTERVAL_TIMES);
    }

}
