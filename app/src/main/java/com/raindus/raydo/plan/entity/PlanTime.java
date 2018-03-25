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
    //最近一次提醒时间
    private long mLastRemindTime = -1;

    // 重复方式 + 具体重复内容 +重复结束日
    private PlanRepeat mRepeat;
    // 最近一次重复时间
    private long mLastRepeatTime = -1;

    public PlanTime() {

    }

    public PlanTime(long startTime, int remindType, long lastRemindTime,
                    int repeatType, long lastRepeatTime, String repeatContent, long closeRepeatTime) {
        mStartTime = new Date(startTime);
        mRemind = PlanRemind.getRemind(remindType);
        mLastRemindTime = lastRemindTime;
        mRepeat = PlanRepeat.getRepeat(repeatType, repeatContent, closeRepeatTime);
        mLastRepeatTime = lastRepeatTime;
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

    public long getLastRemindTime() {
        if (mRemind != PlanRemind.NONE && mLastRemindTime == -1)
            calculateLastRemindTime();
        return mLastRemindTime;
    }

    public void setLastRemindTime(long mLastRemindTime) {
        this.mLastRemindTime = mLastRemindTime;
    }

    private void calculateLastRemindTime() {
        long nextTime = mLastRepeatTime == -1 ? mStartTime.getTime() : mLastRepeatTime;
        switch (mRemind) {
            case NONE:
                mLastRemindTime = -1;
                break;
            case FIVE_IN_MINUTE:
                mLastRemindTime = nextTime - (DateUtils.ONE_MINUTE * 5);
                break;
            case THIRD_IN_MINUTE:
                mLastRemindTime = nextTime - (DateUtils.ONE_MINUTE * 30);
                break;
            case ONE_IN_HOUR:
                mLastRemindTime = nextTime - (DateUtils.ONE_HOUR);
                break;
            case ONE_IN_DAY:
                mLastRemindTime = nextTime - (DateUtils.ONE_DAY);
                break;
            case ONE_IN_WEEK://9点
                mLastRemindTime = nextTime - (DateUtils.ONE_WEEK);
                Date temp = new Date(mLastRemindTime);
                temp.setHours(9);
                temp.setMinutes(0);
                mLastRemindTime = temp.getTime();
                break;
        }
    }

    public long getLastRepeatTime() {
        if (mRepeat != PlanRepeat.NONE && mLastRepeatTime == -1)
            calculateLastRepeatTime();
        return mLastRepeatTime;
    }

    public void setLastRepeatTime(long mLastRepeatTime) {
        this.mLastRepeatTime = mLastRepeatTime;
    }

    private void calculateLastRepeatTime() {
        Date cur = new Date();

        // 已经计算过，不再计算
        if (mLastRepeatTime != -1 && cur.getTime() > mLastRepeatTime)
            return;

        if (cur.before(mStartTime))
            mLastRepeatTime = mStartTime.getTime();
        else {
            switch (mRepeat) {
                case NONE:
                    mLastRepeatTime = -1;
                    break;
                case EVERY_DAY:
                    mLastRepeatTime = new Date(cur.getYear(), cur.getMonth(), cur.getDate(),
                            mStartTime.getHours(), mStartTime.getMinutes()).getTime();
                    if (cur.getHours() < mStartTime.getHours() ||
                            (cur.getHours() == mStartTime.getHours()
                                    && cur.getMinutes() < mStartTime.getMinutes()))
                        mLastRepeatTime += DateUtils.ONE_DAY;
                    break;
                case EVERY_WEEK:
                    mLastRepeatTime = DateUtils.jumpToEveryWeek(mStartTime, cur, mRepeat.getSetFromContent());
                    break;
                case EVERY_MONTH:
                    mLastRepeatTime = DateUtils.jumpToEveryMonth(mStartTime, cur, mRepeat.getSetFromContent());
                    break;
                case EVERY_YEAR:
                    mLastRepeatTime = new Date(mStartTime.getYear() + 1, mStartTime.getMonth(), mStartTime.getDate(),
                            mStartTime.getHours(), mStartTime.getMinutes()).getTime();
                    break;
                case EVERY_INTERVAL:
                    String[] split = mRepeat.getContent().split("_");
                    if (split != null && split[0].equals("interval") && split.length == 3) {
                        int interval = Integer.parseInt(split[1]);
                        int t = Integer.parseInt(split[2]);
                        if (t == PlanRepeat.INTERVAL_TYPE_DAY) {
                            int iDate = DateUtils.intervalDate(mStartTime, cur);
                            mLastRepeatTime = (iDate + 1) * interval * DateUtils.ONE_DAY + mStartTime.getTime();
                            if (cur.getTime() > mLastRepeatTime)
                                mLastRepeatTime += interval * DateUtils.ONE_DAY;
                        } else if (t == PlanRepeat.INTERVAL_TYPE_WEEK) {
                            int iWeek = DateUtils.intervalWeek(mStartTime, cur);
                            mLastRepeatTime = (iWeek + 1) * interval * DateUtils.ONE_WEEK + mStartTime.getTime();
                            if (cur.getTime() > mLastRepeatTime)
                                mLastRepeatTime += interval * DateUtils.ONE_WEEK;
                        } else if (t == PlanRepeat.INTERVAL_TYPE_MONTH) {
                            int iMonth = DateUtils.intervalMonth(mStartTime, cur);
                            mLastRepeatTime = DateUtils.jumpToIntervalMonth((iMonth + 1) * interval, mStartTime);
                            if (cur.getTime() > mLastRepeatTime)
                                mLastRepeatTime = DateUtils.jumpToIntervalMonth((iMonth + 2) * interval, mStartTime);
                        }
                    }
                    break;
            }
        }
        // 结束日
        if (mRepeat.getCloseRepeatTime() != -1 && mLastRepeatTime >= mRepeat.getCloseRepeatTime()) {
            mLastRepeatTime = -1;
        }
    }

    // utils...
    private Date getDate(int year, int month, int day, int hour, int min) {
        return new Date(year - 1900, month - 1, day, hour, min);
    }
}
