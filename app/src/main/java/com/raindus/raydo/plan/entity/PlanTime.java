package com.raindus.raydo.plan.entity;

import com.raindus.raydo.common.DateUtils;

import java.util.Date;

/**
 * Created by Raindus on 2018/3/11.
 */

public class PlanTime {

    // 时间点-开始时间
    private Date mStartTime;

    // 提醒方式
    private PlanRemind mRemind;
    //最近一次提醒时间
    private long mLastRemindTime;

    // 重复方式 + 具体重复内容 +重复结束日
    private PlanRepeat mRepeat;
    // 最近一次重复时间
    private long mLastRepeatTime;

    public PlanTime() {

    }

    /**
     * @param year  1900 - max
     * @param month 1-12
     * @param day   1-31
     * @param hour  0-23
     * @param min   0-59
     */
    public void setStartTime(int year, int month, int day, int hour, int min) {
        mStartTime = getDate(year, month, day, hour, min);
    }

    public long getStartTime() {
        return mStartTime == null ? -1 : mStartTime.getTime();
    }

    public PlanRemind getRemind() {
        if (mRemind == null)
            mRemind = PlanRemind.getDefault();
        return mRemind;
    }

    public void setRemind(PlanRemind mRemind) {
        this.mRemind = mRemind;
    }

    public PlanRepeat getRepeat() {
        if (mRepeat == null)
            mRepeat = PlanRepeat.getDefault();
        return mRepeat;
    }

    public void setRepeat(PlanRepeat mRepeat) {
        this.mRepeat = mRepeat;
    }

    public boolean isRepeatDay(int year, int month, int day) {
        Date date = new Date(year - 1900, month - 1, day);

        Date start = (Date) mStartTime.clone();
        start.setHours(0);
        start.setMinutes(0);
        if (start.getTime() + DateUtils.ONE_DAY > date.getTime())
            return false;

        if (mRepeat.getCloseRepeatTime() != -1) {
            Date end = new Date(mRepeat.getCloseRepeatTime());
            if (end.getTime() + DateUtils.ONE_DAY < date.getTime())
                return false;
        }

        switch (mRepeat) {
            case EVERY_DAY:
                return true;
            case EVERY_WEEK:
                if (mRepeat.isOneDay() && mStartTime.getDay() == date.getDay())
                    return true;
                else {
                    for (int i : mRepeat.getSetFromContent()) {
                        if (i == date.getDay())
                            return true;
                    }
                }
                break;
            case EVERY_MONTH:
                if (mRepeat.isOneDay() && mStartTime.getDate() == day)
                    return true;
                else {
                    for (int i : mRepeat.getSetFromContent()) {
                        if (i == day)
                            return true;
                    }
                }
                break;
            case EVERY_YEAR:
                if (mStartTime.getMonth() + 1 == month && mStartTime.getDate() == day)
                    return true;
            case EVERY_INTERVAL:
                String[] split = mRepeat.getContent().split("_");
                if (split != null && split[0].equals("interval") && split.length == 3) {
                    int interval = Integer.parseInt(split[1]);
                    int t = Integer.parseInt(split[2]);
                    if (t == PlanRepeat.INTERVAL_TYPE_DAY) {
                        int d = DateUtils.intervalDate(start, date);
                        if (d % interval == 0)
                            return true;
                    } else if (t == PlanRepeat.INTERVAL_TYPE_WEEK) {
                        if (mStartTime.getDay() == date.getDay()) {
                            int w = DateUtils.intervalWeek(start, date);
                            if (w % interval == 0)
                                return true;
                        }
                    } else if (t == PlanRepeat.INTERVAL_TYPE_MONTH) {
                        if (mStartTime.getDate() == day) {
                            int m = DateUtils.intervalMonth(start, date);
                            if (m % interval == 0)
                                return true;
                        }
                    }
                }
        }
        return false;
    }

    // utils...
    private Date getDate(int year, int month, int day, int hour, int min) {
        return new Date(year - 1900, month - 1, day, hour, min);
    }

//    private long calculationRemindTime(){
//        long cur = System.currentTimeMillis();
//
//    }
}
