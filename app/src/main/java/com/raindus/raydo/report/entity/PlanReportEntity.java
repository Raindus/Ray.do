package com.raindus.raydo.report.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

/**
 * Created by Raindus on 2018/4/19.
 */
@Entity
public class PlanReportEntity {

    @Id
    public long id;

    /**
     * 日/周/月/总
     */
    public int type;

    @Transient
    private ReportType mType;

    /**
     * 日期
     */
    public long date;

    /**
     * 计划数目
     */
    public int planNumber;

    /**
     * 已完成计划数目
     */
    public int completedNum;

    /**
     * 进行中计划数目
     */
    public int completingNum;

    /**
     * 未完成计划数目
     */
    public int unCompletedNum;

    /**
     * 计划时段分布 根据计划的开始时间
     * e.g. 34_4_4 .. 下标对应 0，1，2点...
     */
    public String periodSpread;

    public PlanReportEntity() {

    }
}
