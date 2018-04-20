package com.raindus.raydo.common;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.text.DecimalFormat;

/**
 * Created by Raindus on 2018/4/7.
 */

public class Utils {

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    public static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @return 获取设备高度
     */
    public static int getDeviceHeight(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public static String getFloatOnePoint(float t, boolean isMin) {
        final DecimalFormat FloatValueFormat = new DecimalFormat("0.0");
        if (isMin)
            t /= 60f;
        return FloatValueFormat.format(t);
    }

    public static String getFloatNoPoint(float t) {
        final DecimalFormat FloatValueFormat = new DecimalFormat("0");
        return FloatValueFormat.format(t);
    }
}
