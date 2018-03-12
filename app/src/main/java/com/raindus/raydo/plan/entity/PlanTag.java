package com.raindus.raydo.plan.entity;

/**
 * Created by Raindus on 2018/3/11.
 */

public enum PlanTag {
    Work(7),
    Study(6),
    Entertainment(5),
    Sport(4),
    Life(3),
    Tourism(2),
    Shopping(1),
    None(0);

    private int mType;

    PlanTag(int type){
        mType = type;
    }

    public static PlanTag getTag(int type){
        switch (type){
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

    public static PlanTag getDefault(){
        return None;
    }
}
