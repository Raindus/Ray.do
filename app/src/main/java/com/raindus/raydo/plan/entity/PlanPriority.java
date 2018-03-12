package com.raindus.raydo.plan.entity;

import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/3/10.
 */

public enum PlanPriority {
    High(3, R.drawable.plan_priority_high),
    Middle(2, R.drawable.plan_priority_middle),
    Low(1, R.drawable.plan_priority_low),
    None(0, R.drawable.plan_priority_none);

    private final int mLevel;
    private final int mIcon;

    PlanPriority(int level, int icon) {
        mLevel = level;
        mIcon = icon;
    }

    public int getLevel() {
        return mLevel;
    }

    public int getIcon() {
        return mIcon;
    }

    public static PlanPriority getPriority(int level) {
        switch (level) {
            case 3:
                return High;
            case 2:
                return Middle;
            case 1:
                return Low;
            case 0:
            default:
                return None;
        }
    }

    public static PlanPriority getDefault() {
        return None;
    }
}
