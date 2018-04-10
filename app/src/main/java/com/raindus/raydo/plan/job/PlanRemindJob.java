package com.raindus.raydo.plan.job;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.util.support.PersistableBundleCompat;
import com.raindus.raydo.R;
import com.raindus.raydo.RaydoApplication;
import com.raindus.raydo.activity.MainActivity;
import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.plan.entity.PlanEntity;

import java.util.Iterator;
import java.util.List;

/**
 * 计划 通知提醒
 * Created by Raindus on 2018/4/10.
 */

public class PlanRemindJob extends Job {

    public static final String TAG = "plan_remind_tag";
    private static final String TICKER = "您有%d个计划即将开始，快去执行吧(>▽<)";

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {

        PersistableBundleCompat remindPlan = params.getExtras();
        long[] remindIDs = remindPlan.getLongArray("IDs");
        // long remindTime = remindPlan.getLong("remindTime", -1);

        List<PlanEntity> list = ObjectBox.PlanEntityBox.queryByID(RaydoApplication.get(), remindIDs);
        if (list != null && list.size() > 0) {
            String ticker = String.format(TICKER, list.size());
            String title;
            StringBuilder content = new StringBuilder();
            if (list.size() == 1) {
                int endSub;
                if (list.get(0).title.length() > 32)
                    endSub = 32;
                else
                    endSub = list.get(0).title.length();

                title = list.get(0).title.substring(0, endSub);

                if (list.get(0).detail.length() > 32)
                    endSub = 32;
                else
                    endSub = list.get(0).detail.length();

                content.append(list.get(0).detail.substring(0, endSub));
            } else {
                title = String.format(TICKER, list.size());
                Iterator<PlanEntity> it = list.iterator();
                while (it.hasNext()) {
                    PlanEntity entity = it.next();
                    int endSub;
                    if (entity.title.length() > 7)
                        endSub = 7;
                    else
                        endSub = entity.title.length();

                    content.append(entity.title.substring(0, endSub));
                    if (it.hasNext())
                        content.append(",");
                }
            }
            notifyPlan(ticker, title, content.toString());
        }
        // 更新该计划
        for (PlanEntity entity : list)
            entity.refreshRemind();
        ObjectBox.PlanEntityBox.put(RaydoApplication.get(), list);

        // 计算下一次通知时间
        PlanJob.scheduleNextPlanRemindJob();
        return Result.SUCCESS;
    }

    private void notifyPlan(String ticker, String title, String content) {
        PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
                new Intent(getContext(), MainActivity.class), 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(TAG, "PlanRemind", NotificationManager.IMPORTANCE_HIGH);
            getContext().getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }

        Notification notification = new NotificationCompat.Builder(getContext(), TAG)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setTicker(ticker)
                .setContentTitle(title)
                .setContentText(content)
                .setChannelId(TAG)
                .setShowWhen(true)
                .setDefaults(Notification.DEFAULT_ALL)// 默认铃声，震动，指示灯
                .setAutoCancel(true)//点击后取消
                .setPriority(Notification.PRIORITY_HIGH)//高优先级
                .setVisibility(Notification.VISIBILITY_PRIVATE)//任何情况都会显示通知
                .setContentIntent(pi)//设置点击过后跳转的activity
                .build();

        NotificationManagerCompat.from(getContext()).notify(0, notification);
    }

}
