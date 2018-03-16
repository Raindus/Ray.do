package com.raindus.raydo.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Raindus on 2018/3/9.
 */

public class DateUtils {

    public static String getNewPlanTimeDescribed(long time){
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 yyyy EE");
        Date date = new Date(time);
        return format.format(date);
    }

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

    /**
     * @return eg.周X
     */
    public static String getDateWeek(Date date) {
        if (date == null)
            date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("EE");
        return format.format(date);
    }

    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    public static int intervalDay(Date start, Date test) {
        long interval = test.getTime() - start.getTime();
        int day = (int) (interval / ONE_DAY);
        return day >= 0 ? day : -1;
    }

    public static int intervalWeek(Date start, Date test) {
        if (test.getDay() != start.getDay())
            return -1;

        long interval = test.getTime() - start.getTime();
        int week = (int) (interval / ONE_DAY / 7);
        return week >= 0 ? week : -1;
    }

    public static int intervalMonth(Date start, Date test) {
        if (test.getDate() != start.getDate())
            return -1;

        int month = -1;
        if (start.getYear() < test.getYear()) {
            int sm = start.getMonth();
            int em = 12 * (test.getYear() - start.getYear()) + test.getMonth();
            month = em - sm;
        } else if (start.getYear() == test.getYear())
            month = test.getMonth() - start.getMonth();

        return month >= 0 ? month : -1;
    }

    public static int getDateNumber(int year, int month) {
        boolean isRun = ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return isRun ? 29 : 28;
        }
        return 30;
    }

}
