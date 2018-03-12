package com.raindus.raydo.plan.entity;

/**
 * Created by Raindus on 2018/3/10.
 */

public enum PlanPriority {
    High(3),
    Middle(2),
    Low(1),
    None(0);

    private int mLevel;

    PlanPriority(int level){
        mLevel = level;
    }

    public int getLevel(){
        return mLevel;
    }

    public static PlanPriority getPriority(int level){
        switch (level){
            case 3:
                return High;
            case 2:
                return Middle;
            case 1 :
                return Low;
            case 0:
            default:
                    return None;
        }
    }

    public static PlanPriority getDefault(){
        return None;
    }
}
