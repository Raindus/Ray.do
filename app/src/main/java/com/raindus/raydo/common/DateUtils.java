package com.raindus.raydo.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Raindus on 2018/3/9.
 */

public class DateUtils {

    /**
     * @return eg.2018.03.03
     */
    public static String getTodayDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date();
        return format.format(date);
    }

    /**
     * @return eg.星期X
     */
    public static String getTodayWeek() {
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        Date date = new Date();
        return format.format(date);
    }

}
