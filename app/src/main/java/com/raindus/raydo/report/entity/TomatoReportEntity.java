package com.raindus.raydo.report.entity;

import android.text.TextUtils;

import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.tomato.TomatoEntity;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;

/**
 * Created by Raindus on 2018/4/19.
 */

@Entity
public class TomatoReportEntity {

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
     * 专注次数
     */
    public int focusTimes = 0;

    /**
     * 收获番茄总数
     */
    public int tomatoNum = 0;

    /**
     * 专注时长，单位分钟
     */
    public int focusTime = 0;

    /**
     * 根据 番茄表 的开始结束时间的覆盖，单位h
     * e.g. 1.52_2_4_ .....   对应的时间段分别是 0，1，2，3点。。。
     */
    public String periodSpread;


    public TomatoReportEntity() {

    }

    public TomatoReportEntity(ReportType t, long curTime) {
        type = t.getType();
        mType = t;
        date = curTime;
    }

    public ReportType getReportType() {
        if (mType == null) {
            mType = ReportType.get(type);
            mType.setDate(date);
        }
        return mType;
    }

    public static void update(TomatoEntity tomato) {
        List<TomatoReportEntity> list = ObjectBox.TomatoReportEntityBox.queryNeedUpdateNow();

        for (TomatoReportEntity report : list) {
            report.focusTimes++;
            report.tomatoNum += tomato.tomatoNum;
            report.focusTime += (tomato.tomatoNum * tomato.tomatoTime);
            report.periodSpread = updatePeriodSpread(report.periodSpread, tomato);
            ObjectBox.TomatoReportEntityBox.put(report);
        }
    }

    public float[] getPeriodSpread() {
        float[] period = new float[24];
        if (TextUtils.isEmpty(periodSpread)) {
            Arrays.fill(period, 0);
        } else {
            String[] split = periodSpread.split("_");
            for (int i = 0; i < 24; i++) {
                //TODO 异常
                period[i] = Float.parseFloat(split[i]);
            }
        }
        return period;
    }

    private static String updatePeriodSpread(String old, TomatoEntity tomato) {
        float[] period = new float[24];
        if (TextUtils.isEmpty(old)) {
            Arrays.fill(period, 0);
        } else {
            String[] split = old.split("_");
            for (int i = 0; i < 24; i++) {
                //TODO 异常
                period[i] = Float.parseFloat(split[i]);
            }
        }

        Date start = new Date(tomato.startTime);
        Date end = new Date(tomato.endTime);
        // TODO 先考虑跨度不大于1天的
        int startHour = start.getHours();
        int startMin = start.getMinutes();
        int endHour = end.getHours();
        int endMin = end.getMinutes();
        if (start.getDate() != end.getDate()) {
            startHour = 0;
            startMin = 0;
        }

        for (int i = 0; i < 24; i++) {
            if (startHour < i && endHour > i) {
                period[i] += 1f;
            } else if (startHour == i) {
                period[i] += (60f - startMin) / 60f;
            } else if (endHour == i) {
                period[i] += (float) endMin / 60f;
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 24; i++) {
            builder.append(period[i]);
            if (i != 23)
                builder.append("_");
        }
        return builder.toString();
    }
}
