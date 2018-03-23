package com.raindus.raydo.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Raindus on 2018/3/9.
 */

public class DateUtils {

    public static final long ONE_DAY = 24 * 60 * 60 * 1000;
    public static final long ONE_WEEK = ONE_DAY * 7;

    /**
     * @return eg.2018.03.03
     */
    public static String formatDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date();
        return format.format(date);
    }

    /**
     * @return eg.星期X
     */
    public static String formatDay() {
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        Date date = new Date();
        return format.format(date);
    }

    /**
     * @return eg.周X
     */
    public static String formatDay(Date date) {
        if (date == null)
            date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("EE");
        return format.format(date);
    }

    /**
     * @return 间隔的天数
     */
    public static int intervalDate(Date start, Date end) {
        long interval = end.getTime() - start.getTime();
        int date = (int) (interval / ONE_DAY);
        return date >= 0 ? date : -1;
    }

    /**
     * @return 间隔的周数，需要相同星期X
     */
    public static int intervalWeek(Date start, Date end) {
        if (end.getDay() != start.getDay())
            return -1;

        long interval = end.getTime() - start.getTime();
        int week = (int) (interval / ONE_WEEK);
        return week >= 0 ? week : -1;
    }

    /**
     * @return 间隔的月数，需要相同日期
     */
    public static int intervalMonth(Date start, Date end) {
        if (end.getDate() != start.getDate())
            return -1;

        int month = -1;
        if (start.getYear() < end.getYear()) {
            int sm = start.getMonth();
            int em = 12 * (end.getYear() - start.getYear()) + end.getMonth();
            month = em - sm;
        } else if (start.getYear() == end.getYear())
            month = end.getMonth() - start.getMonth();

        return month >= 0 ? month : -1;
    }

    /**
     * @return 返回该月份的天数
     */
    public static int getDaysOfMonth(int year, int month) {
        boolean isLeapYear = ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0);
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
                return isLeapYear ? 29 : 28;
        }
        return -1;
    }

}
