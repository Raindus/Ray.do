package com.raindus.raydo.plan.entity;

import java.util.Date;

/**
 * Created by Raindus on 2018/3/11.
 */

public class PlanTime {

    public class Remind {
        public static final int NONE = 0;
        public static final int FIVE_IN_MINUTE = 1;
        public static final int THIRD_IN_MINUTE = 2;
        public static final int ONE_IN_HOUR = 3;
        public static final int ONE_IN_DAY = 4;
        public static final int ONE_IN_WEEK = 5;
    }

    public class Repeat {
        public static final int NONE = 0;
        public static final int EVERY_DAY = 1;
        /**
         * e.g. 0_1_2_3_4_5_6
         * 每周1-6天：必须有一天包含当前计划的开始星期X
         */
        public static final int EVERY_WEEK = 2;
        /**
         * e.g. 1_2_3_4_5 ...
         * 每月1-[(28-31)-1]天：必须包含当前计划的X号
         */
        public static final int EVERY_MONTH = 3;
        /**
         * 每年公历这一天
         */
        public static final int EVERY_YEAR = 4;
        /**
         * 间隔X天/周/月
         * e.g. day/week/month_x
         */
        public static final int EVERY_INTERVAL = 5;
    }

    // 时间点-开始时间
    private Date mStartTime;
    // 提醒方式
    private int mRemindType;
    //最近一次提醒时间
    private long mLastRemindTime;

    // 重复方式
    private int mRepeatType;
    // 具体重复内容
    private String mRepeatContent;
    // 最近一次重复时间
    private long mLastRepeatTime;
    // 重复结束日
    private long mCloseRepeatTime;

    public PlanTime() {

    }

    public void setStartTime(int year, int month, int day, int hour, int min) {
        mStartTime = getDate(year, month, day, hour, min);
    }

    public long getStartTime() {
        return mStartTime == null ? -1 : mStartTime.getTime();
    }


    // utils...
    public Date getDate(int year, int month, int day, int hour, int min) {
        return new Date(year - 1900, month - 1, day, hour, min);
    }

//    private long calculationRemindTime(){
//        long cur = System.currentTimeMillis();
//
//    }
}
