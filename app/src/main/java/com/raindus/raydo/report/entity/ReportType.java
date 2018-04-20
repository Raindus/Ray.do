package com.raindus.raydo.report.entity;

import com.raindus.raydo.common.DateUtils;

import java.util.Date;

/**
 * Created by Raindus on 2018/4/19.
 */

public enum ReportType {
    DAY(1),
    WEEK(2),
    MONTH(3),
    ALL(4);

    private final int mType;

    private Date date;

    ReportType(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }

    public static ReportType get(int t) {
        switch (t) {
            case 1:
                return DAY;
            case 2:
                return WEEK;
            case 3:
                return MONTH;
            case 4:
                return ALL;
        }
        return null;
    }

    public void setDate(long time) {
        if (this != ALL)
            date = new Date(time);
    }

    public int intervalToNow(Date cur) {
        switch (this) {
            case MONTH:
                return getIntervalMonth(cur);
            case WEEK:
                return getIntervalWeek(cur);
            case DAY:
                return getIntervalDay(cur);
        }
        return -1;
    }

    public long getNowAgo(Date cur) {
        switch (this) {
            case MONTH:
                return getMonthAgo(cur);
            case WEEK:
                return getWeekAgo(cur);
            case DAY:
                return getDateAgo(cur);
        }
        return -1;
    }

    public long getSevenAgo(Date cur) {
        switch (this) {
            case MONTH:
                return getSevenMonthAgo(cur);
            case WEEK:
                return getSevenWeekAgo(cur);
            case DAY:
                return getSevenDateAgo(cur);
        }
        return -1;
    }

    ////////////////
    ///

    // 相隔几天？
    private int getIntervalDay(Date cur) {
        cur = new Date(cur.getYear(), cur.getMonth(), cur.getDate());
        if (cur.getTime() < date.getTime())
            return 0;

        return (int) ((cur.getTime() - date.getTime()) / DateUtils.ONE_DAY) + 1;
    }

    // 相隔几周？
    private int getIntervalWeek(Date cur) {
        long now = cur.getTime();
        cur = new Date(cur.getYear(), cur.getMonth(), cur.getDate());
        long beginWeek = cur.getTime() - (DateUtils.ONE_DAY * cur.getDay());

        if (now - date.getTime() < now - beginWeek)
            return 0;

        return (int) ((beginWeek - date.getTime()) / DateUtils.ONE_WEEK) + 1;
    }

    // 相隔几月？
    private int getIntervalMonth(Date cur) {
        int c = ((cur.getYear() - date.getYear()) * 12 + cur.getMonth());
        return c - date.getMonth();
    }

    ////////////////
    ///
    // 返回当天
    private long getDateAgo(Date cur) {
        cur = new Date(cur.getYear(), cur.getMonth(), cur.getDate());
        return cur.getTime();
    }

    // 返回当周。 周日为起点
    private long getWeekAgo(Date cur) {
        cur = new Date(cur.getYear(), cur.getMonth(), cur.getDate());
        return cur.getTime() - (cur.getDay() * DateUtils.ONE_DAY);
    }

    // 返回当月第一天。
    private long getMonthAgo(Date cur) {
        return new Date(cur.getYear(), cur.getMonth(), 1).getTime();
    }


    ////////////////
    ///

    // 返回7天前
    private long getSevenDateAgo(Date cur) {
        cur = new Date(cur.getYear(), cur.getMonth(), cur.getDate());

        return cur.getTime() - (6 * DateUtils.ONE_DAY);
    }

    // 返回7周前的(包含该周的)第一天。 周日为起点
    private long getSevenWeekAgo(Date cur) {
        cur = new Date(cur.getYear(), cur.getMonth(), cur.getDate());

        return cur.getTime() - (DateUtils.ONE_WEEK * 6) - (cur.getDay() * DateUtils.ONE_DAY);
    }

    // 返回7月前的(包含该月的)第一天。
    private long getSevenMonthAgo(Date cur) {
        int year = cur.getYear() + 1900;
        int month = cur.getMonth() + 1;

        if (month >= 7) {
            month -= 6;
        } else {
            year -= 1;
            month += 6;
        }
        return new Date(year - 1900, month - 1, 1).getTime();
    }
}
