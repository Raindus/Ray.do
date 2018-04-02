package com.raindus.raydo.dao;

import android.app.Activity;
import android.app.Application;

import com.raindus.raydo.RaydoApplication;
import com.raindus.raydo.common.DateUtils;
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

        public static List<PlanEntity> queryAll(Application application, boolean isToday, boolean showComplected) {
            QueryBuilder<PlanEntity> query = getBox(application).query();
            showToday(query, isToday);
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
