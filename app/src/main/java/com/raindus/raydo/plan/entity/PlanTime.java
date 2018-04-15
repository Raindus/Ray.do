package com.raindus.raydo.plan.entity;

import com.raindus.raydo.common.DateUtils;

import java.util.Date;
import java.util.Set;

/**
 * Created by Raindus on 2018/3/11.
 */

public class PlanTime {

    // 时间点-开始时间
    private Date mStartTime;

    // 提醒方式
    private PlanRemind mRemind;
    // 提醒时间
    private long mRemindTime = -1;

    // 重复方式 + 具体重复内容 +重复结束日
    private PlanRepeat mRepeat;
    // 重复时间
    private long mRepeatTime = -1;

    public PlanTime() {

    }

    public PlanTime(long startTime, int remindType, long remindTime,
                    int repeatType, long repeatTime, String repeatContent, long closeRepeatTime) {
        mStartTime = new Date(startTime);
        mRemind = PlanRemind.getRemind(remindType);
        mRemindTime = remindTime;
        mRepeat = PlanRepeat.getRepeat(repeatType, repeatContent, closeRepeatTime);
        mRepeatTime = repeatTime;
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

    public long getRemindTime() {
        if (mRemind != PlanRemind.NONE && mRemindTime == -1)
            calculateRemindTime();
        else if (mRemind == PlanRemind.NONE)
            mRemindTime = -1;

        return mRemindTime;
    }

    public void setRemindTime(long mRemindTime) {
        this.mRemindTime = mRemindTime;
    }

    private void calculateRemindTime() {
        long time = mStartTime.getTime();
        switch (mRemind) {
            case NONE:
                mRemindTime = -1;
                break;
            case FIVE_IN_MINUTE:
                mRemindTime = time - (DateUtils.ONE_MINUTE * 5);
                break;
            case THIRD_IN_MINUTE:
                mRemindTime = time - (DateUtils.ONE_MINUTE * 30);
                break;
            case ONE_IN_HOUR:
                mRemindTime = time - (DateUtils.ONE_HOUR);
                break;
            case ONE_IN_DAY:
                mRemindTime = time - (DateUtils.ONE_DAY);
                break;
            case ONE_IN_WEEK://9点
                mRemindTime = time - (DateUtils.ONE_WEEK);
                Date temp = new Date(mRemindTime);
                temp.setHours(9);
                temp.setMinutes(0);
                mRemindTime = temp.getTime();
                break;
        }
    }

    public long getRepeatTime() {
        if (mRepeat != PlanRepeat.NONE && mRepeatTime == -1)
            calculateRepeatTime();
        else if (mRepeat == PlanRepeat.NONE)
            mRepeatTime = -1;
        return mRepeatTime;
    }

    public void setRepeatTime(long mRepeatTime) {
        this.mRepeatTime = mRepeatTime;
    }

    private void calculateRepeatTime() {
        switch (mRepeat) {
            case NONE:
                mRepeatTime = -1;
                break;
            case EVERY_DAY:
                mRepeatTime = mStartTime.getTime() + DateUtils.ONE_DAY;
                break;
            case EVERY_WEEK:
                mRepeatTime = DateUtils.jumpToEveryWeek(mStartTime, mRepeat.getSetFromContent());
                break;
            case EVERY_MONTH:
                mRepeatTime = DateUtils.jumpToEveryMonth(mStartTime, mRepeat.getSetFromContent());
                break;
            case EVERY_YEAR:
                if (mStartTime.getMonth() == 1 && mStartTime.getDate() == 29) {
                    mRepeatTime = new Date(mStartTime.getYear() + 1, mStartTime.getMonth(), 28,
                            mStartTime.getHours(), mStartTime.getMinutes()).getTime();
                } else {
                    mRepeatTime = new Date(mStartTime.getYear() + 1, mStartTime.getMonth(), mStartTime.getDate(),
                            mStartTime.getHours(), mStartTime.getMinutes()).getTime();
                }
                break;
            case EVERY_INTERVAL:
                String[] split = mRepeat.getContent().split("_");
                if (split != null && split[0].equals("interval") && split.length == 3) {
                    int interval = Integer.parseInt(split[1]);
                    int t = Integer.parseInt(split[2]);
                    if (t == PlanRepeat.INTERVAL_TYPE_DAY) {
                        mRepeatTime = mStartTime.getTime() + (interval * DateUtils.ONE_DAY);
                    } else if (t == PlanRepeat.INTERVAL_TYPE_WEEK) {
                        mRepeatTime = mStartTime.getTime() + (interval * DateUtils.ONE_WEEK);
                    } else if (t == PlanRepeat.INTERVAL_TYPE_MONTH) {
                        mRepeatTime = DateUtils.jumpToIntervalMonth(interval, mStartTime);
                    }
                }
                break;
        }
        // 结束日
        if (mRepeat.getCloseRepeatTime() != -1 && mRepeatTime >= mRepeat.getCloseRepeatTime()) {
            mRepeatTime = -1;
        }
    }

    // 生成新对象
    public PlanTime clone() {
        return new PlanTime(mStartTime.getTime(), mRemind.getType(), mRemindTime,
                mRepeat.getType(), mRepeatTime, mRepeat.getContent(), mRepeat.getCloseRepeatTime());
    }

    // 由父计划生成重复计划
    public PlanTime cloneForRepeatPlan() {
        return new PlanTime(mRepeatTime, mRemind.getType(), -1,
                mRepeat.getType(), -1, mRepeat.getContent(), mRepeat.getCloseRepeatTime());
    }

    // utils...
    private Date getDate(int year, int month, int day, int hour, int min) {
        return new Date(year - 1900, month - 1, day, hour, min);
    }
}
