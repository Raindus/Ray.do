package com.raindus.raydo.dao;

import android.app.Application;

import com.raindus.raydo.RaydoApplication;
import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.plan.PlanSort;
import com.raindus.raydo.plan.entity.PlanEntity;
import com.raindus.raydo.plan.entity.PlanEntity_;
import com.raindus.raydo.plan.entity.PlanRemind;
import com.raindus.raydo.plan.entity.PlanRepeat;
import com.raindus.raydo.plan.entity.PlanStatus;
import com.raindus.raydo.plan.job.PlanJob;
import com.raindus.raydo.tomato.TomatoEntity;

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

        // 添加 or 更新
        public static void put(Application application, List<PlanEntity> entities) {
            getBox(application).put(entities);
        }

        // 添加 or 更新 并检查任务提醒
        public static long putAndRemind(Application application, PlanEntity entity) {
            entity.refresh();
            long id = getBox(application).put(entity);

            if (entity.remindType != PlanRemind.NONE.getType()) {
                Date cur = new Date();
                // 明天截至日期
                long endTime = DateUtils.getDateTime(cur.getYear() + 1900,
                        cur.getMonth() + 1, cur.getDate() + 1, false);

                // 是否需要检查任务提醒
                if (entity.lastRemindTime != -1 && entity.lastRemindTime < endTime)
                    PlanJob.scheduleNextPlanRemindJob();
            }
            return id;
        }

        // 删除
        public static void delete(Application application, PlanEntity... entity) {
            getBox(application).remove(entity);
        }

        // 关键字搜索
        public static List<PlanEntity> queryKeyword(Application application, String... keywords) {
            QueryBuilder<PlanEntity> query = getBox(application).query();

            boolean addOr = false;

            for (String word : keywords) {
                if (word.isEmpty())
                    continue;

                if (addOr)
                    query.or();

                query.contains(PlanEntity_.title, word).or().contains(PlanEntity_.detail, word);
                addOr = true;
            }
            if (!addOr)
                return null;
            return query.sort(new PlanSort.PlanSortByDate()).build().find();
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

        // 需要提醒
        public static List<PlanEntity> queryRemind(Application application) {
            QueryBuilder<PlanEntity> query = getBox(application).query();
            return query.notEqual(PlanEntity_.remindType, PlanRemind.NONE.getType())
                    .build().find();
        }

        // 在time之前的提醒任务
        public static List<PlanEntity> queryByRemindTime(Application application, long time) {

            QueryBuilder<PlanEntity> query = getBox(application).query();
            return query.notEqual(PlanEntity_.remindType, PlanRemind.NONE.getType())
                    .and()
                    .less(PlanEntity_.lastRemindTime, time)
                    .order(PlanEntity_.lastRemindTime)
                    .build().find();
        }

        public static List<PlanEntity> queryByID(Application application, long... ids) {
            QueryBuilder<PlanEntity> query = getBox(application).query();
            return query.in(PlanEntity_.id, ids)
                    .build().find();
        }

        // 需要重复
        public static List<PlanEntity> queryRepeat(Application application) {
            QueryBuilder<PlanEntity> query = getBox(application).query();
            return query.notEqual(PlanEntity_.repeatType, PlanRepeat.NONE.getType())
                    .build().find();
        }
    }

    public static class TomatoEntityBox {
        // 获取操作实例
        public static Box<TomatoEntity> getBox() {
            return ((RaydoApplication) RaydoApplication.get()).getBoxStore().boxFor(TomatoEntity.class);
        }

        // 添加 or 更新
        public static long put(TomatoEntity entity) {
            return getBox().put(entity);
        }

        public static List<TomatoEntity> query() {
            QueryBuilder<TomatoEntity> query = getBox().query();
            return query.build().find();
        }

    }

}
