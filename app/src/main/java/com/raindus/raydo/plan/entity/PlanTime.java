package com.raindus.raydo.plan.entity;

import java.util.Date;

/**
 * Created by Raindus on 2018/3/11.
 */

public class PlanTime {

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
