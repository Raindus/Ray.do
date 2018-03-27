package com.raindus.raydo.common;

import com.raindus.raydo.ui.MultiSelectView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Created by Raindus on 2018/3/9.
 */

public class DateUtils {

    public static final long ONE_MINUTE = 60 * 1000;
    public static final long ONE_HOUR = 60 * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final long ONE_WEEK = 7 * ONE_DAY;

    /**
     * @return eg. 12 : 30
     */
    public static String formatTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH : mm");
        Date date = new Date(time);
        return format.format(date);
    }

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
     * 清除具体时间，保留至当天
     */
    public static Date cleanTime(Date date) {
        Date clean = new Date(date.getYear(), date.getMonth(), date.getDate());
        return clean;
    }

    /**
     * @return 间隔的天数
     */
    public static int intervalDate(Date start, Date end) {
        start = cleanTime(start);
        end = cleanTime(end);

        long interval = end.getTime() - start.getTime();
        int date = (int) (interval / ONE_DAY);
        return date >= 0 ? date : -1;
    }

    /**
     * @return 间隔的周数，需要相同星期X
     */
    public static int intervalWeek(Date start, Date end) {
        start = cleanTime(start);
        end = cleanTime(end);

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
        start = cleanTime(start);
        end = cleanTime(end);

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
     * @return 跳跃至每周X的时间
     */
    public static long jumpToEveryWeek(Date start, Date cur, Set<Integer> week) {
        int nextDay = -1;
        int jumpDay;
        long curTime = new Date(cur.getYear(), cur.getMonth(), cur.getDate(), start.getHours(), start.getMinutes()).getTime();

        if (week.contains(cur.getDay())) {//当天
            if (cur.getHours() < start.getHours() ||
                    (cur.getHours() == start.getHours()
                            && cur.getMinutes() < start.getMinutes()))
                return curTime;
        }

        for (int day : week) {
            if (nextDay == -1) {
                nextDay = day;
                continue;
            }
            if (day > cur.getDay()) {
                nextDay = day;
                break;
            }
        }

        if (nextDay > cur.getDay())
            jumpDay = nextDay - cur.getDay();
        else
            jumpDay = 7 + nextDay - cur.getDay();

        return curTime + jumpDay * ONE_DAY;
    }

    /**
     * @return 跳跃至每月X的时间 1-31
     */
    public static long jumpToEveryMonth(Date start, Date cur, Set<Integer> month) {
        int nextDay = -1;

        if (month.contains(cur.getDate())) {//当天
            if (cur.getHours() < start.getHours() ||
                    (cur.getHours() == start.getHours()
                            && cur.getMinutes() < start.getMinutes()))
                return new Date(cur.getYear(), cur.getMonth(), cur.getDate(), start.getHours(), start.getMinutes()).getTime();
        }

        for (int day : month) {
            if (nextDay == -1) {
                nextDay = day;
                continue;
            }
            if (day > cur.getDate()) {
                nextDay = day;
                break;
            }
        }

        int y;
        int m;
        if (nextDay > cur.getDate()) {
            int lastDay = getDaysOfMonth(cur.getYear() + 1900, cur.getMonth() + 1);
            if (nextDay > lastDay) {
                nextDay = lastDay;
            }
            y = cur.getYear();
            m = cur.getMonth();
        } else {
            if (cur.getMonth() == 11) {//12月
                y = cur.getYear() + 1;
                m = 0;
            } else {
                y = cur.getYear();
                m = cur.getMonth() + 1;
            }
            int lastDay = getDaysOfMonth(y + 1900, m + 1);
            if (nextDay > lastDay) {
                nextDay = lastDay;
            }
        }

        return new Date(y, m, nextDay, start.getHours(), start.getMinutes()).getTime();
    }

    /**
     * @return 跳跃至间隔的时间
     */
    public static long jumpToIntervalMonth(int interval, Date start) {
        int year = start.getYear() + 1900;
        int month = start.getMonth() + 1;
        int date = start.getDate();

        month = (month + interval) % 12;
        year += (month + interval) / 12;
        if (month == 0) {
            month = 12;
            year--;
        }

        int lastDay = getDaysOfMonth(year, month);
        if (date > lastDay) {
            date = lastDay;
        }
        return new Date(year - 1900, month - 1, date, start.getHours(), start.getMinutes()).getTime();
    }

    // 新建任务页
    public static String describeOfDate(Date date) {
        Date cur = new Date();
        StringBuilder builder = new StringBuilder();
        int intervalDate = intervalDate(cur, date);
        boolean overPlus = true;

        if (intervalDate == 0) {
            builder.append("今天");
        } else if (intervalDate == 1) {
            builder.append("明天");
        } else if (intervalDate == 2) {
            builder.append("后天");
        } else if (intervalDate <= (6 - cur.getDay())) {
            builder.append("这周" + MultiSelectView.WEEK[date.getDay()]);
        } else if (intervalDate <= (13 - cur.getDay())) {
            builder.append("下周" + MultiSelectView.WEEK[date.getDay()]);
        }

        if (builder.length() != 0) {
            builder.append(",");
            overPlus = false;
        }

        if (cur.getYear() == date.getYear()) {
            builder.append((date.getMonth() + 1) + "月" + date.getDate() + "日");
        } else {
            builder.append((date.getYear() + 1900) + "年" + (date.getMonth() + 1) + "月" + date.getDate() + "日");
        }

        builder.append(",").append(describeOfTime(date.getHours(), date.getMinutes()));

        if (overPlus) {
            builder.append(",").append("剩余" + intervalDate + "天");
        }
        return builder.toString();
    }

    public static String describeOfTime(int hour, int min) {
        StringBuilder builder = new StringBuilder();
        if (hour > 11) {
            builder.append("下午 ");
            if (hour < 22)
                builder.append("0");
            builder.append(hour - 12).append(":");
            if (min < 10)
                builder.append("0");
            builder.append(min);
        } else {
            builder.append("上午 ");
            if (hour < 10)
                builder.append("0");
            builder.append(hour).append(":");
            if (min < 10)
                builder.append("0");
            builder.append(min);
        }
        return builder.toString();
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

    // true = begin; false = end
    public static long getTodayTime(boolean beginOrEnd) {
        Date cur = new Date();
        long time = new Date(cur.getYear(), cur.getMonth(), cur.getDate()).getTime();
        return beginOrEnd ? time : time + ONE_DAY - 1;
    }

    /**
     * 深夜 0-3点
     * 凌晨 3-5点
     * 早晨 5-8点
     * 上午 8-12点
     * 中午 12-14点
     * 下午 14-17点
     * 傍晚 17-19点
     * 晚上 19-23点
     */
    public static int getTimeState(long time) {
        int hour = new Date(time).getHours();
        if (hour < 3)
            return 0;
        else if (hour < 5)
            return 1;
        else if (hour < 8)
            return 2;
        else if (hour < 12)
            return 3;
        else if (hour < 14)
            return 4;
        else if (hour < 17)
            return 5;
        else if (hour < 19)
            return 6;
        else
            return 7;
    }

}
