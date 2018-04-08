package com.raindus.raydo.plan.entity;

import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/3/10.
 */

public enum PlanStatus {
    Completed(2, R.drawable.plan_status_completed, "已完成"),
    Completing(1, R.drawable.plan_status_completing, "进行中"),
    UnCompleted(0, R.drawable.plan_status_uncompleted, "未完成");

    private final int mType;
    private final int mIcon;
    private final String mContent;

    PlanStatus(int type, int icon, String content) {
        mType = type;
        mIcon = icon;
        mContent = content;
    }

    public int getType() {
        return mType;
    }

    public int getIcon() {
        return mIcon;
    }

    public String getContent() {
        return mContent;
    }

    public static PlanStatus getStatus(int type) {
        switch (type) {
            case 2:
                return Completed;
            case 1:
                return Completing;
            case 0:
            default:
                return UnCompleted;
        }
    }

    public static PlanStatus getDefault() {
        return UnCompleted;
    }
}
