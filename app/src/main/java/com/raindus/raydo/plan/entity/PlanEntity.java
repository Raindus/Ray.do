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
    public long id;

    /**
     * 标题
     */
    public String title;

    /**
     * 详细
     */
    public String detail;

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

    public PlanEntity() {

    }

    // 新建计划。
    public PlanEntity(String title, String detail, PlanPriority priority, PlanTag tag, PlanStatus status, PlanTime time) {
        this.title = title;
        this.detail = detail;
        mPriority = priority;
        mTag = tag;
        mStatus = status;
        mTime = time;
    }

    // 导入数据库
    public void refresh() {
        priority = mPriority.getLevel();
        status = mStatus.getType();
        tag = mTag.getType();
        startTime = mTime.getStartTime();
        remindType = mTime.getRemind().getType();
        lastRemindTime = mTime.getLastRemindTime();
        repeatType = mTime.getRepeat().getType();
        repeatContent = mTime.getRepeat().getContent();
        lastRepeatTime = mTime.getLastRepeatTime();
        closeRepeatTime = mTime.getRepeat().getCloseRepeatTime();
    }

    public void setPriority(PlanPriority priority) {
        mPriority = priority;
    }

    public PlanPriority getPriority() {
        if (mPriority == null)
            mPriority = PlanPriority.getPriority(priority);
        return mPriority;
    }

    public void setStatus(PlanStatus status) {
        mStatus = status;
    }

    public PlanStatus getStatus() {
        if (mStatus == null)
            mStatus = PlanStatus.getStatus(status);
        return mStatus;
    }

    public void setTag(PlanTag tag) {
        mTag = tag;
    }

    public PlanTag getTag() {
        if (mTag == null)
            mTag = PlanTag.getTag(tag);
        return mTag;
    }

    public void setTime(PlanTime time) {
        mTime = time;
    }

    public PlanTime getTime() {
        if (mTime == null)
            mTime = new PlanTime(startTime, remindType, lastRemindTime,
                    repeatType, lastRepeatTime, repeatContent, closeRepeatTime);
        return mTime;
    }

}
