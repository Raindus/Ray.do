package com.raindus.raydo.plan.entity;

/**
 * Created by Raindus on 2018/3/10.
 */

public enum PlanStatus {
    Completed(2),
    Completing(1),
    UnCompleted(0);

    private int mType;

    PlanStatus(int type){
        mType = type;
    }

    public int getType(){
        return mType;
    }

    public static PlanStatus getStatus(int type){
        switch (type){
            case 2:
                return Completed;
            case 1:
                return Completing;
            case 0:
            default:
                return UnCompleted;
        }
    }

    public static PlanStatus getDefault(){
        return UnCompleted;
    }
}
