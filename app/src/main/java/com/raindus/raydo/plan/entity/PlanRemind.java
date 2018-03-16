package com.raindus.raydo.plan.entity;

/**
 * Created by Raindus on 2018/3/15.
 */

public enum PlanRemind {
    NONE(0, "无"),
    FIVE_IN_MINUTE(1, "提前 5 分钟"),
    THIRD_IN_MINUTE(2, "提前 30 分钟"),
    ONE_IN_HOUR(3, "提前 1 小时"),
    ONE_IN_DAY(4, "提前 1 天"),
    ONE_IN_WEEK(5, "提前 1 周（09:00）");// 9点

    private final int mType;
    private final String mContent;

    PlanRemind(int type, String content) {
        mType = type;
        mContent = content;
    }

    public int getType() {
        return mType;
    }

    public String getContent() {
        return mContent;
    }

    public static PlanRemind getRemind(int type) {
        switch (type) {
            case 1:
                return FIVE_IN_MINUTE;
            case 2:
                return THIRD_IN_MINUTE;
            case 3:
                return ONE_IN_HOUR;
            case 4:
                return ONE_IN_DAY;
            case 5:
                return ONE_IN_WEEK;
            case 0:
            default:
                return NONE;
        }
    }

    public static PlanRemind getDefault() {
        return NONE;
    }
}
