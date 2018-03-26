package com.raindus.raydo.plan.entity;

import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/3/10.
 */

public enum PlanStatus {
    Completed(2, R.drawable.plan_status_completed),
    Completing(1, R.drawable.plan_status_completing),
    UnCompleted(0, R.drawable.plan_status_uncompleted);

    private final int mType;
    private final int mIcon;

    PlanStatus(int type, int icon) {
        mType = type;
        mIcon = icon;
    }

    public int getType() {
        return mType;
    }

    public int getIcon() {
        return mIcon;
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
