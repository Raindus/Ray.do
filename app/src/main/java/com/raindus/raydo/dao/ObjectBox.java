package com.raindus.raydo.dao;

import android.app.Activity;
import android.app.Application;

import com.raindus.raydo.RaydoApplication;
import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.plan.PlanSort;
import com.raindus.raydo.plan.entity.PlanEntity;
import com.raindus.raydo.plan.entity.PlanEntity_;
import com.raindus.raydo.plan.entity.PlanStatus;

import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.QueryBuilder;

/**
 * Created by Raindus on 2018/3/25.
 */

public class ObjectBox {

    public static class PlanEntityBox {

        // 获取操作实例
        public static Box<PlanEntity> getBox(Application application) {
            return ((RaydoApplication) application).getBoxStore().boxFor(PlanEntity.class);
        }

        // 添加 or 更新
        public static long put(Application application, PlanEntity entity) {
            entity.refresh();
            return getBox(application).put(entity);
        }

        // 指定日期 三个月 用于日历上显示标注
        // 重复任务没有中间的具体日期
        public static List<PlanEntity> queryThirdMonth(Application application, int year, int month) {
            QueryBuilder<PlanEntity> query = getBox(application).query();

            // 指定日期 三个月
            long startTime = DateUtils.getThirdMonthTime(year, month, true);
            long endTime = DateUtils.getThirdMonthTime(year, month, false);
            return query.between(PlanEntity_.startTime, startTime, endTime)
                    .or()
                    .between(PlanEntity_.lastRepeatTime, startTime, endTime)
                    .sort(new PlanSort.PlanSortByDate())
                    .build().find();
        }

        // 全部 or 今日
        public static List<PlanEntity> queryAll(Application application, boolean isToday, boolean showComplected) {
            QueryBuilder<PlanEntity> query = getBox(application).query();
            showToday(query, isToday);
            showComplected(query, showComplected);
            return query.build().find();
        }

        // 显示指定日期
        public static List<PlanEntity> queryDate(Application application, int year, int month, int date, boolean showComplected) {
            QueryBuilder<PlanEntity> query = getBox(application).query();

            // 指定日期
            long startTime = DateUtils.getDateTime(year, month, date, true);
            long endTime = DateUtils.getDateTime(year, month, date, false);
            query.between(PlanEntity_.startTime, startTime, endTime)
                    .or()
                    .between(PlanEntity_.lastRepeatTime, startTime, endTime);

            showComplected(query, showComplected);
            return query.build().find();
        }


        // 显示 已完成 状态
        private static QueryBuilder<PlanEntity> showComplected(QueryBuilder<PlanEntity> query, boolean showComplected) {
            if (showComplected)
                return query;
            else
                return query.notEqual(PlanEntity_.status, PlanStatus.Completed.getType());
        }

        // 只显示 今日的计划
        private static QueryBuilder<PlanEntity> showToday(QueryBuilder<PlanEntity> query, boolean isToday) {
            if (!isToday)
                return query;
            else {
                long startTime = DateUtils.getTodayTime(true);
                long endTime = DateUtils.getTodayTime(false);

                return query.between(PlanEntity_.startTime, startTime, endTime)
                        .or()
                        .between(PlanEntity_.lastRepeatTime, startTime, endTime);
            }
        }

    }


}
