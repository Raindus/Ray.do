package com.raindus.raydo.plan.entity;

import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/3/10.
 */

public enum PlanPriority {
    High(3, R.drawable.plan_priority_high, R.color.p_high),
    Middle(2, R.drawable.plan_priority_middle, R.color.p_middle),
    Low(1, R.drawable.plan_priority_low, R.color.p_low),
    None(0, R.drawable.plan_priority_none, R.color.p_none);

    private final int mLevel;
    private final int mIcon;
    private final int mColor;

    PlanPriority(int level, int icon, int color) {
        mLevel = level;
        mIcon = icon;
        mColor = color;
    }

    public int getLevel() {
        return mLevel;
    }

    public int getIcon() {
        return mIcon;
    }

    public int getColor() {
        return mColor;
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
