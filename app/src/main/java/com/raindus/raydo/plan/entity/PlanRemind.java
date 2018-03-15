package com.raindus.raydo.plan.entity;

/**
 * Created by Raindus on 2018/3/15.
 */

public enum PlanRemind {
    NONE(0),
    FIVE_IN_MINUTE(1),
    THIRD_IN_MINUTE(2),
    ONE_IN_HOUR(3),
    ONE_IN_DAY(4),
    ONE_IN_WEEK(5);// 9点

    private final int mType;

    PlanRemind(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }

    public static PlanRemind getDefault() {
        return NONE;
    }
}
