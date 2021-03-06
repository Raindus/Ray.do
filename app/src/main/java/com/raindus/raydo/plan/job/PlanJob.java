package com.raindus.raydo.plan.job;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.raindus.raydo.RaydoApplication;
import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.plan.entity.PlanEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 更新计划 提醒/重复 时间
 * Created by Raindus on 2018/4/10.
 */

public class PlanJob extends Job {

    public static final String TAG = "plan_tag";

    // TODO jobschedule 超过100 闪退

    // 半天执行一次
    public static void schedulePlanJob() {
        new JobRequest.Builder(PlanJob.TAG)
                .setPeriodic(DateUtils.ONE_DAY / 2)
                .setUpdateCurrent(false)
                .build()
                .schedule();
    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {

        // 更新向下重复
        updateRepeat();
        scheduleNextPlanRemindJob();
        return Result.SUCCESS;
    }

    private void updateRepeat() {
        List<PlanEntity> list = ObjectBox.PlanEntityBox.queryRepeat();
        for (PlanEntity entity : list) {
            entity.creteRepeatPlanEntity();
        }
    }

    // 获取最近一次提醒任务。
    public static void scheduleNextPlanRemindJob() {
        Date cur = new Date();
        // 明天截至日期
        long endTime = DateUtils.getDateTime(cur.getYear() + 1900,
                cur.getMonth() + 1, cur.getDate() + 1, false);

        // 今明两天需要提醒的计划,已按提醒时间升序排序
        List<PlanEntity> list = ObjectBox.PlanEntityBox.queryByRemindTime(endTime);
        if (list == null || list.size() == 0)
            return;

        List<Long> ids = new ArrayList<>();
        long remindTime = -1;

        for (PlanEntity entity : list) {

            if (entity.remindTime < cur.getTime())
                continue;

            if (remindTime == -1) {
                remindTime = entity.remindTime;
                ids.add(entity.id);
                continue;
            } else if (entity.remindTime < remindTime + DateUtils.ONE_HOUR) {
                ids.add(entity.id);
                continue;
            } else {
                break;
            }
        }

        long[] remindIDs = new long[ids.size()];
        for (int i = 0; i < ids.size(); i++)
            remindIDs[i] = ids.get(i);

        PersistableBundleCompat remindPlan = new PersistableBundleCompat();
        remindPlan.putLongArray("IDs", remindIDs);
        remindPlan.putLong("remindTime", remindTime);

        long exact = remindTime - new Date().getTime();
        if (exact <= 0)
            return;

        new JobRequest.Builder(PlanRemindJob.TAG)
                .setExact(exact)// 提醒时间
                .setExtras(remindPlan)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }
}
