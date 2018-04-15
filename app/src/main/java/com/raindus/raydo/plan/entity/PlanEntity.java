package com.raindus.raydo.plan.entity;

import android.util.Log;

import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.dao.ObjectBox;

import java.util.Date;
import java.util.List;

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
     * 重复计划通过repeatTime设置成startTime,新建一个相同计划产生，
     * 因此该id表示继承的来源id
     */
    public long fromId = -1;

    /**
     * 绑定 由此重复生成的 下个计划id
     */
    public long bindId = -1;

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
    public int remindType;
    /**
     * 提醒时间
     */
    public long remindTime;

    /**
     * 重复
     */
    int repeatType;
    /**
     * 具体重复内容
     */
    String repeatContent;
    /**
     * 重复时间
     */
    public long repeatTime;
    /**
     * 重复结束日
     */
    public long closeRepeatTime;

    /**
     * 集合时间-重复-提醒 功能
     */
    @Transient
    PlanTime mTime;

    public PlanEntity() {
    }

    // 人为新建计划。
    public PlanEntity(String title, String detail, PlanPriority priority, PlanTag tag, PlanStatus status, PlanTime time) {
        this.title = title;
        this.detail = detail;
        mPriority = priority;
        mTag = tag;
        mStatus = status;
        mTime = time;
    }

    // 递归 应用新建重复计划
    public boolean creteRepeatPlanEntity() {
        Date cur = new Date();

        if (bindId != -1 || getTime().getRepeatTime() == -1)
            return false;
        else if (getTime().getRepeatTime() > cur.getTime() + DateUtils.ONE_WEEK + DateUtils.ONE_DAY)// 应用自建时间在8天以内的范围
            return false;

        PlanEntity repeatPlan = new PlanEntity(title, detail, getPriority(), getTag(), getStatus(), getTime().cloneForRepeatPlan());
        // id 双向绑定
        repeatPlan.fromId = id;
        bindId = ObjectBox.PlanEntityBox.put(repeatPlan);
        ObjectBox.PlanEntityBox.put(this);

        // 递归创建重复子计划
        repeatPlan.creteRepeatPlanEntity();
        return true;
    }

    // 递归 更新重复计划
    public boolean updateRepeatPlanEntity(int type) {

        if (bindId == -1)
            return false;

        if (type == 0) {// 时间
            // 向下删除
            deleteBindRepeatPlanEntity();
            // 向下新建
            bindId = -1;
            creteRepeatPlanEntity();
            return true;
        }

        List<PlanEntity> repeatPlan = ObjectBox.PlanEntityBox.queryByID(bindId);
        if (repeatPlan != null && repeatPlan.size() == 1) {
            switch (type) {
                case 1: // 优先级
                    repeatPlan.get(0).setPriority(getPriority());
                    break;
                case 2: // 标签
                    repeatPlan.get(0).setTag(getTag());
                    break;
                case 3: // 详细
                    repeatPlan.get(0).detail = detail;
                    break;
            }
            // 递归保存
            ObjectBox.PlanEntityBox.put(repeatPlan.get(0));
            repeatPlan.get(0).updateRepeatPlanEntity(type);
        } else
            return false;

        return true;
    }

    // 递归删除 双向计划
    public void deleteRepeatPlanEntity() {
        deleteBindRepeatPlanEntity();
        deleteFromRepeatPlanEntity();
    }

    // 向上递归
    private boolean deleteFromRepeatPlanEntity() {
        if (fromId == -1)
            return false;

        List<PlanEntity> repeatPlan = ObjectBox.PlanEntityBox.queryByID(fromId);
        if (repeatPlan != null && repeatPlan.size() == 1) {
            repeatPlan.get(0).deleteFromRepeatPlanEntity();
            ObjectBox.PlanEntityBox.delete(repeatPlan.get(0));
        }
        return true;
    }

    // 向下递归
    private boolean deleteBindRepeatPlanEntity() {
        if (bindId == -1)
            return false;
        List<PlanEntity> repeatPlan = ObjectBox.PlanEntityBox.queryByID(bindId);
        if (repeatPlan != null && repeatPlan.size() == 1) {
            repeatPlan.get(0).deleteBindRepeatPlanEntity();
            ObjectBox.PlanEntityBox.delete(repeatPlan.get(0));
        }
        return true;
    }

    // 导入数据库
    public void refresh() {
        priority = getPriority().getLevel();
        status = getStatus().getType();
        tag = getTag().getType();
        startTime = getTime().getStartTime();
        remindType = getTime().getRemind().getType();
        repeatType = getTime().getRepeat().getType();
        repeatContent = getTime().getRepeat().getContent();
        repeatTime = getTime().getRepeatTime();
        remindTime = getTime().getRemindTime();
        closeRepeatTime = getTime().getRepeat().getCloseRepeatTime();
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
            mTime = new PlanTime(startTime, remindType, remindTime,
                    repeatType, repeatTime, repeatContent, closeRepeatTime);
        return mTime;
    }

}
