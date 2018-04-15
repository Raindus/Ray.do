package com.raindus.raydo.plan;

import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.plan.entity.PlanEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Raindus on 2018/3/27.
 */

public class PlanSort {

    public static final int SORT_BY_TIME = 1;
    private static final String[] TIME = {"深夜", "凌晨", "早晨", "上午", "中午", "下午", "傍晚", "晚上"};

    public static final int SORT_BY_STATUS = 2;
    private static final String[] STATUS = {"未完成", "完成中", "已完成"};

    public static final int SORT_BY_PRIORITY = 3;
    private static final String[] PRIORITY = {"无 - 优先级", "低 - 优先级", "中 - 优先级", "高 - 优先级"};

    public static final int SORT_BY_TAG = 4;
    private static final String[] TAG = {"无", "购物", "旅游", "生活", "运动", "娱乐", "学习", "工作"};

    /**
     * 深夜 0-3点
     * 凌晨 3-5点
     * 早晨 5-8点
     * 上午 8-12点
     * 中午 12-14点
     * 下午 14-17点
     * 傍晚 17-19点
     * 晚上 19-23点
     */
    public static List<Object> sortByTime(List<PlanEntity> plans) {
        if (plans == null || plans.size() == 0)
            return null;

        Collections.sort(plans, new PlanSortByTime());

        List<Object> list = new ArrayList<>();
        int titleIndex = -1;

        for (PlanEntity plan : plans) {
            int timeState = DateUtils.getTimeState(plan.getTime().getStartTime());
            if (titleIndex != timeState) {
                titleIndex = timeState;
                list.add(TIME[titleIndex]);
            }
            list.add(plan);
        }
        return list;
    }

    static class PlanSortByTime implements Comparator<PlanEntity> {

        // 时间(不考虑日期) - 优先级 - 完成状态
        @Override
        public int compare(PlanEntity o1, PlanEntity o2) {

            Date olDate = new Date(o1.getTime().getStartTime());
            Date o2Date = new Date(o2.getTime().getStartTime());

            if (olDate.getHours() < o2Date.getHours() ||
                    (olDate.getHours() == o2Date.getHours() && olDate.getMinutes() < o2Date.getMinutes()))
                return -1;
            else if (olDate.getHours() == o2Date.getHours() && olDate.getMinutes() == o2Date.getMinutes()) {
                if (o1.getPriority().getLevel() < o2.getPriority().getLevel())
                    return -1;
                else if (o1.getPriority().getLevel() > o2.getPriority().getLevel())
                    return 1;
                else {
                    if (o1.getStatus().getType() < o2.getStatus().getType())
                        return -1;
                    else if (o1.getStatus().getType() > o2.getStatus().getType())
                        return 1;
                    else
                        return 0;
                }
            } else
                return 1;
        }
    }

    public static List<Object> sortByStatus(List<PlanEntity> plans) {
        if (plans == null || plans.size() == 0)
            return null;

        Collections.sort(plans, new PlanSortByStatus());

        List<Object> list = new ArrayList<>();
        int titleIndex = -1;

        for (PlanEntity plan : plans) {
            int status = plan.getStatus().getType();
            if (titleIndex != status) {
                titleIndex = status;
                list.add(STATUS[titleIndex]);
            }
            list.add(plan);
        }
        return list;
    }

    static class PlanSortByStatus implements Comparator<PlanEntity> {

        //  完成状态 - 时间(不考虑日期) - 优先级
        @Override
        public int compare(PlanEntity o1, PlanEntity o2) {
            if (o1.getStatus().getType() < o2.getStatus().getType())
                return -1;
            else if (o1.getStatus().getType() > o2.getStatus().getType())
                return 1;
            else {
                Date olDate = new Date(o1.getTime().getStartTime());
                Date o2Date = new Date(o2.getTime().getStartTime());

                if (olDate.getHours() < o2Date.getHours() ||
                        (olDate.getHours() == o2Date.getHours() && olDate.getMinutes() < o2Date.getMinutes()))
                    return -1;
                else if (olDate.getHours() == o2Date.getHours() && olDate.getMinutes() == o2Date.getMinutes()) {
                    if (o1.getPriority().getLevel() < o2.getPriority().getLevel())
                        return -1;
                    else if (o1.getPriority().getLevel() > o2.getPriority().getLevel())
                        return 1;
                    else
                        return 0;
                } else
                    return 1;
            }
        }
    }

    public static List<Object> sortByPriority(List<PlanEntity> plans) {
        if (plans == null || plans.size() == 0)
            return null;

        Collections.sort(plans, new PlanSortByPriority());

        List<Object> list = new ArrayList<>();
        int titleIndex = -1;

        for (PlanEntity plan : plans) {
            int level = plan.getPriority().getLevel();
            if (titleIndex != level) {
                titleIndex = level;
                list.add(PRIORITY[titleIndex]);
            }
            list.add(plan);
        }
        return list;
    }

    static class PlanSortByPriority implements Comparator<PlanEntity> {

        //  优先级 - 时间(不考虑日期) - 完成状态
        @Override
        public int compare(PlanEntity o1, PlanEntity o2) {
            if (o1.getPriority().getLevel() < o2.getPriority().getLevel())
                return 1;
            else if (o1.getPriority().getLevel() > o2.getPriority().getLevel())
                return -1;
            else {
                Date olDate = new Date(o1.getTime().getStartTime());
                Date o2Date = new Date(o2.getTime().getStartTime());

                if (olDate.getHours() < o2Date.getHours() ||
                        (olDate.getHours() == o2Date.getHours() && olDate.getMinutes() < o2Date.getMinutes()))
                    return -1;
                else if (olDate.getHours() == o2Date.getHours() && olDate.getMinutes() == o2Date.getMinutes()) {
                    if (o1.getStatus().getType() < o2.getStatus().getType())
                        return -1;
                    else if (o1.getStatus().getType() > o2.getStatus().getType())
                        return 1;
                    else
                        return 0;
                } else
                    return 1;
            }
        }
    }

    public static List<Object> sortByTag(List<PlanEntity> plans) {
        if (plans == null || plans.size() == 0)
            return null;

        Collections.sort(plans, new PlanSortByTag());

        List<Object> list = new ArrayList<>();
        int titleIndex = -1;

        for (PlanEntity plan : plans) {
            int tag = plan.getTag().getType();
            if (titleIndex != tag) {
                titleIndex = tag;
                list.add(TAG[titleIndex]);
            }
            list.add(plan);
        }
        return list;
    }

    static class PlanSortByTag implements Comparator<PlanEntity> {

        //  标签 - 时间(不考虑日期) - 优先级 - 完成状态
        @Override
        public int compare(PlanEntity o1, PlanEntity o2) {

            if (o1.getTag().getType() < o2.getTag().getType())
                return 1;
            else if (o1.getTag().getType() > o2.getTag().getType())
                return -1;
            else {
                Date olDate = new Date(o1.getTime().getStartTime());
                Date o2Date = new Date(o2.getTime().getStartTime());

                if (olDate.getHours() < o2Date.getHours() ||
                        (olDate.getHours() == o2Date.getHours() && olDate.getMinutes() < o2Date.getMinutes()))
                    return -1;
                else if (olDate.getHours() == o2Date.getHours() && olDate.getMinutes() == o2Date.getMinutes()) {
                    if (o1.getPriority().getLevel() < o2.getPriority().getLevel())
                        return -1;
                    else if (o1.getPriority().getLevel() > o2.getPriority().getLevel())
                        return 1;
                    else {
                        if (o1.getStatus().getType() < o2.getStatus().getType())
                            return -1;
                        else if (o1.getStatus().getType() > o2.getStatus().getType())
                            return 1;
                        else
                            return 0;
                    }
                } else
                    return 1;
            }
        }
    }
}
