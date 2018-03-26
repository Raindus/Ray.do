package com.raindus.raydo.dao;

import android.app.Activity;
import android.app.Application;

import com.raindus.raydo.RaydoApplication;
import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.plan.entity.PlanEntity;
import com.raindus.raydo.plan.entity.PlanEntity_;

import java.util.Date;
import java.util.List;

import io.objectbox.Box;

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

        public static List<PlanEntity> queryAll(Application application, boolean isToday) {
            if (!isToday)
                return getBox(application).query().build().find();
            else {
                long startTime = DateUtils.getTodayTime(true);
                long endTime = DateUtils.getTodayTime(false);
                return getBox(application).query()
                        .between(PlanEntity_.startTime, startTime, endTime)
                        .or()
                        .between(PlanEntity_.lastRepeatTime, startTime, endTime)
                        .build().find();
            }
        }

    }


}
