package com.raindus.raydo.dao;

import com.raindus.raydo.RaydoApplication;
import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.plan.entity.PlanEntity;
import com.raindus.raydo.plan.entity.PlanEntity_;
import com.raindus.raydo.plan.entity.PlanRemind;
import com.raindus.raydo.plan.entity.PlanRepeat;
import com.raindus.raydo.plan.entity.PlanStatus;
import com.raindus.raydo.plan.job.PlanJob;
import com.raindus.raydo.report.entity.PlanReportEntity;
import com.raindus.raydo.report.entity.ReportType;
import com.raindus.raydo.report.entity.TomatoReportEntity;
import com.raindus.raydo.report.entity.TomatoReportEntity_;
import com.raindus.raydo.tomato.TomatoEntity;
import com.raindus.raydo.tomato.TomatoEntity_;

import java.util.ArrayList;
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
        public static Box<PlanEntity> getBox() {
            return ((RaydoApplication) RaydoApplication.get()).getBoxStore().boxFor(PlanEntity.class);
        }

        // 添加 or 更新
        public static long put(PlanEntity entity) {
            entity.refresh();
            return getBox().put(entity);
        }

        // 添加 or 更新 并检查任务提醒
        public static long putAndRemind(PlanEntity entity) {
            entity.refresh();
            long id = getBox().put(entity);

            if (entity.remindType != PlanRemind.NONE.getType()) {
                Date cur = new Date();
                // 明天截至日期
                long endTime = DateUtils.getDateTime(cur.getYear() + 1900,
                        cur.getMonth() + 1, cur.getDate() + 1, false);

                // 是否需要检查任务提醒
                if (entity.remindTime != -1 && entity.remindTime < endTime)
                    PlanJob.scheduleNextPlanRemindJob();
            }
            return id;
        }

        // 删除
        public static void delete(PlanEntity... entity) {
            getBox().remove(entity);
        }

        // 关键字搜索 只搜索用户自建任务
        public static List<PlanEntity> queryKeyword(String... keywords) {
            QueryBuilder<PlanEntity> query = getBox().query();

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
            query.and().equal(PlanEntity_.fromId, -1);
            return query.order(PlanEntity_.startTime).build().find();
        }

        // 指定日期 三个月 用于日历上显示标注
        // 重复任务没有中间的具体日期
        public static List<PlanEntity> queryThirdMonth(int year, int month) {
            QueryBuilder<PlanEntity> query = getBox().query();

            // 指定日期 三个月
            long startTime = DateUtils.getThirdMonthTime(year, month, true);
            long endTime = DateUtils.getThirdMonthTime(year, month, false);
            return query.between(PlanEntity_.startTime, startTime, endTime)
                    .order(PlanEntity_.startTime)
                    .build().find();
        }

        // 全部 or 今日
        public static List<PlanEntity> queryAll(boolean isToday, boolean showComplected) {
            QueryBuilder<PlanEntity> query = getBox().query();
            showToday(query, isToday);
            showComplected(query, showComplected);
            return query.build().find();
        }

        // 显示指定日期
        public static List<PlanEntity> queryDate(int year, int month, int date, boolean showComplected) {
            QueryBuilder<PlanEntity> query = getBox().query();

            // 指定日期
            long startTime = DateUtils.getDateTime(year, month, date, true);
            long endTime = DateUtils.getDateTime(year, month, date, false);
            query.between(PlanEntity_.startTime, startTime, endTime);

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
                return query.between(PlanEntity_.startTime, startTime, endTime);
            }
        }

        // 在time之前的提醒任务
        public static List<PlanEntity> queryByRemindTime(long time) {

            QueryBuilder<PlanEntity> query = getBox().query();
            return query.notEqual(PlanEntity_.remindType, PlanRemind.NONE.getType())
                    .less(PlanEntity_.remindTime, time)
                    .order(PlanEntity_.remindTime)
                    .build().find();
        }

        public static List<PlanEntity> queryByID(long... ids) {
            QueryBuilder<PlanEntity> query = getBox().query();
            return query.in(PlanEntity_.id, ids)
                    .build().find();
        }

        // 需要重复 且 未向下绑定
        public static List<PlanEntity> queryRepeat() {
            QueryBuilder<PlanEntity> query = getBox().query();
            return query.notEqual(PlanEntity_.repeatType, PlanRepeat.NONE.getType())
                    .equal(PlanEntity_.bindId, -1)
                    .build().find();
        }
    }

    public static class PlanReportEntityBox {

        public static Box<PlanReportEntity> getBox() {
            return ((RaydoApplication) RaydoApplication.get()).getBoxStore().boxFor(PlanReportEntity.class);
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

        // 查询今天的番茄钟
        public static List<TomatoEntity> queryToday() {
            QueryBuilder<TomatoEntity> query = getBox().query();

            long startTime = DateUtils.getTodayTime(true);
            long endTime = DateUtils.getTodayTime(false);

            return query.between(TomatoEntity_.startTime, startTime, endTime).build().find();
        }

    }

    public static class TomatoReportEntityBox {

        public static Box<TomatoReportEntity> getBox() {
            return ((RaydoApplication) RaydoApplication.get()).getBoxStore().boxFor(TomatoReportEntity.class);
        }

        // 添加 or 更新
        public static long put(TomatoReportEntity entity) {
            return getBox().put(entity);
        }

        // 天 - 周 - 月 - 日
        public static List<TomatoReportEntity> queryNeedUpdateNow() {
            Date cur = new Date();
            List<TomatoReportEntity> list = new ArrayList<>();
            List<TomatoReportEntity> temp;

            // 当天
            temp = getBox().query()
                    .equal(TomatoReportEntity_.type, ReportType.DAY.getType())
                    .between(TomatoReportEntity_.date, ReportType.DAY.getNowAgo(cur), cur.getTime())
                    .build().find();
            if (temp == null || temp.size() == 0)
                list.add(new TomatoReportEntity(ReportType.DAY, cur.getTime()));
            else
                list.add(temp.get(0));

            // 当周
            temp = getBox().query()
                    .equal(TomatoReportEntity_.type, ReportType.WEEK.getType())
                    .between(TomatoReportEntity_.date, ReportType.WEEK.getNowAgo(cur), cur.getTime())
                    .build().find();
            if (temp == null || temp.size() == 0)
                list.add(new TomatoReportEntity(ReportType.WEEK, cur.getTime()));
            else
                list.add(temp.get(0));

            // 当月
            temp = getBox().query()
                    .equal(TomatoReportEntity_.type, ReportType.MONTH.getType())
                    .between(TomatoReportEntity_.date, ReportType.MONTH.getNowAgo(cur), cur.getTime())
                    .build().find();
            if (temp == null || temp.size() == 0)
                list.add(new TomatoReportEntity(ReportType.MONTH, cur.getTime()));
            else
                list.add(temp.get(0));

            list.add(queryAll());
            return list;
        }

        // 最近7天，以排序,最近一天在上面
        public static List<TomatoReportEntity> queryLatelySevenDate() {
            Date cur = new Date();

            return getBox().query()
                    .equal(TomatoReportEntity_.type, ReportType.DAY.getType())
                    .between(TomatoReportEntity_.date, ReportType.DAY.getSevenAgo(cur), cur.getTime())
                    .orderDesc(TomatoReportEntity_.date)
                    .build().find();
        }

        // 最近7周，以排序,最近一周在上面
        public static List<TomatoReportEntity> queryLatelySevenWeek() {
            Date cur = new Date();

            return getBox().query()
                    .equal(TomatoReportEntity_.type, ReportType.WEEK.getType())
                    .between(TomatoReportEntity_.date, ReportType.WEEK.getSevenAgo(cur), cur.getTime())
                    .orderDesc(TomatoReportEntity_.date)
                    .build().find();
        }

        // 最近7个月，以排序,最近一月在上面
        public static List<TomatoReportEntity> queryLatelySevenMonth() {
            Date cur = new Date();

            return getBox().query()
                    .equal(TomatoReportEntity_.type, ReportType.MONTH.getType())
                    .between(TomatoReportEntity_.date, ReportType.MONTH.getSevenAgo(cur), cur.getTime())
                    .orderDesc(TomatoReportEntity_.date)
                    .build().find();


        }

        // type = all 只有一个
        public static TomatoReportEntity queryAll() {
            List<TomatoReportEntity> list =
                    getBox().query()
                            .equal(TomatoReportEntity_.type, ReportType.ALL.getType())
                            .build().find();

            if (list == null || list.size() == 0)
                return new TomatoReportEntity(ReportType.ALL, new Date().getTime());
            else
                return list.get(0);
        }

    }

}
