package com.raindus.raydo.plan.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

/**
 * Created by Raindus on 2018/3/10.
 */

@Entity
public class PlanEntity {

    @Id
    long id;

    /**
     * 标题
     */
    String title;

    /**
     * 详细
     */
    String detail;

    /**
     * 优先级 {@link PlanPriority}
     */
    int priority;

    @Transient
    PlanPriority mPriority;

    /**
     * 完成状态 {@link PlanStatus}
     */
    int status;

    @Transient
    PlanStatus mStatus;

    /**
     * 标签 {@link PlanTag}
     */
    int tag;

    @Transient
    PlanTag mTag;

    /**
     * 开始时间 {@link PlanTime}
     */
    long startTime;

    /**
     * 提醒
     */
    int remindType;
    /**
     * 最近一次提醒时间
     */
    long lastRemindTime;

    /**
     * 重复
     */
    int repeatType;
    /**
     * 具体重复内容
     */
    String repeatContent;
    /**
     * 最近一次重复时间
     */
    long lastRepeatTime;
    /**
     * 重复结束日
     */
    long closeRepeatTime;

    /**
     * 集合时间-重复-提醒 功能
     */
    @Transient
    PlanTime mTime;
}
