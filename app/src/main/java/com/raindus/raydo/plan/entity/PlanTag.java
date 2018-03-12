package com.raindus.raydo.plan.entity;

import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/3/11.
 */

public enum PlanTag {
    Work(7, R.drawable.plan_tag_work),
    Study(6, R.drawable.plan_tag_study),
    Entertainment(5, R.drawable.plan_tag_entertainment),
    Sport(4, R.drawable.plan_tag_sport),
    Life(3, R.drawable.plan_tag_life),
    Tourism(2, R.drawable.plan_tag_tourism),
    Shopping(1, R.drawable.plan_tag_shopping),
    None(0, R.drawable.plan_tag_none);

    private final int mType;
    private final int mIcon;

    PlanTag(int type, int icon) {
        mType = type;
        mIcon = icon;
    }

    public int getType() {
        return mType;
    }

    public int getIcon() {
        return mIcon;
    }

    public static PlanTag getTag(int type) {
        switch (type) {
            case 7:
                return Work;
            case 6:
                return Study;
            case 5:
                return Entertainment;
            case 4:
                return Sport;
            case 3:
                return Life;
            case 2:
                return Tourism;
            case 1:
                return Shopping;
            case 0:
            default:
                return None;
        }
    }

    public static PlanTag getDefault() {
        return None;
    }
}
