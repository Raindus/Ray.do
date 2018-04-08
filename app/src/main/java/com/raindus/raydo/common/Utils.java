package com.raindus.raydo.common;

import android.content.Context;

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
}
