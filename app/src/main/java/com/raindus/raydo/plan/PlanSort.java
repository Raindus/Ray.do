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
    public static final int SORT_BY_PRIORITY = 3;
    public static final int SORT_BY_TAG = 4;

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
}
