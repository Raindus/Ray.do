package com.raindus.raydo.plan.entity;

import android.text.TextUtils;
import android.util.ArraySet;

import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.ui.MultiSelectView;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Raindus on 2018/3/15.
 */

public enum PlanRepeat {
    NONE(0),
    EVERY_DAY(1),
    /**
     * e.g. 0_1_2_3_4_5_6
     * 每周1-6天：必须有一天包含当前计划的开始星期X
     */
    EVERY_WEEK(2),
    /**
     * e.g. 1_2_3_4_5 ...
     * 每月1-[(28-31)-1]天：必须包含当前计划的X号
     */
    EVERY_MONTH(3),
    /**
     * 每年公历这一天
     */
    EVERY_YEAR(4),
    /**
     * 间隔X天/周/月
     * e.g. day/week/month_x
     */
    EVERY_INTERVAL(5);

    public static final int INTERVAL_TYPE_DAY = 1;//(1-364)
    public static final int INTERVAL_TYPE_WEEK = 2;//(1-52)
    public static final int INTERVAL_TYPE_MONTH = 3;//(1-11)

    private final int mType;
    private String mContent;
    // 重复结束日
    private long mCloseRepeatTime = -1;

    PlanRepeat(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }

    public String getContent() {
        if (TextUtils.isEmpty(mContent))
            setContent(null, -1, -1);
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    /**
     * @param set           week & month else can be bull
     * @param intervalTimes only in interval
     * @param intervalType  only in interval
     */
    public void setContent(Set<Integer> set, int intervalTimes, int intervalType) {
        StringBuilder builder = new StringBuilder();
        switch (this) {
            case NONE:
                builder.append("none");
                break;
            case EVERY_DAY:
                builder.append("day");
                break;
            case EVERY_WEEK://begin-0,end-6
                builder.append("week");
                if (set != null) {
                    for (int i : set) {
                        if (i >= 0 && i <= 6)
                            builder.append("_").append(i);
                    }
                }
                break;
            case EVERY_MONTH://begin-0,end-30,,but save+1
                builder.append("month");
                if (set != null) {
                    for (int i : set) {
                        if (i >= 0 && i <= 30)
                            builder.append("_").append(i + 1);
                    }
                }
                break;
            case EVERY_YEAR:
                builder.append("year");
                break;
            case EVERY_INTERVAL:
                builder.append("interval");
                switch (intervalType) {
                    case INTERVAL_TYPE_DAY:
                        if (intervalTimes >= 1 && intervalTimes < 365)
                            builder.append("_").append(intervalTimes).append("_").append(INTERVAL_TYPE_DAY);
                        break;
                    case INTERVAL_TYPE_WEEK:
                        if (intervalTimes >= 1 && intervalTimes < 53)
                            builder.append("_").append(intervalTimes).append("_").append(INTERVAL_TYPE_WEEK);
                        break;
                    case INTERVAL_TYPE_MONTH:
                        if (intervalTimes >= 1 && intervalTimes < 12)
                            builder.append("_").append(intervalTimes).append("_").append(INTERVAL_TYPE_MONTH);
                        break;
                }
                break;
        }
        mContent = builder.toString();
    }

    public String getDescribe(Date date) {
        switch (this) {
            case NONE:
                return "永不";
            case EVERY_DAY:
                return "每天";
            case EVERY_WEEK://begin-0,end-6
                if (isOnlyOneDay())
                    return "每周（" + DateUtils.getDateWeek(date) + "）";
                else {
                    return getCustomRepeatText();
                }
            case EVERY_MONTH://begin-1,end-31
                if (isOnlyOneDay())
                    return "每月（" + date.getDate() + "日）";
                else {
                    return getCustomRepeatText();
                }
            case EVERY_YEAR:
                return "每年（" + (date.getMonth() + 1) + "月" + date.getDate() + "日）";
            case EVERY_INTERVAL:
                return getCustomIntervalText();
        }
        return "";
    }

    private String getCustomIntervalText() {
        String[] split = mContent.split("_");
        if (split != null && split[0].equals("interval") && split.length == 3) {
            int t = Integer.parseInt(split[2]);
            String s = "";
            if (t == PlanRepeat.INTERVAL_TYPE_DAY)
                s = "天";
            else if (t == PlanRepeat.INTERVAL_TYPE_WEEK)
                s = "周";
            else if (t == PlanRepeat.INTERVAL_TYPE_MONTH)
                s = "月";
            return ("自定义（每隔" + split[1] + s + "）");
        }
        return "";
    }

    private String getCustomRepeatText() {
        StringBuilder builder = new StringBuilder();
        Set<Integer> select = getSetFromContent();
        if (select.size() == 0)
            return "";

        builder.append("自定义（");
        Iterator<Integer> it = select.iterator();
        if (getType() == 2) {
            builder.append("每周的");
            while (it.hasNext()) {
                builder.append("周").append(MultiSelectView.WEEK[it.next()]);
                if (it.hasNext())
                    builder.append(",");
            }
        } else if (getType() == 3) {
            builder.append("每月的");
            while (it.hasNext()) {
                builder.append(it.next()).append("日");
                if (it.hasNext())
                    builder.append(",");
            }
        }
        builder.append("）");
        return builder.toString();
    }

    //TODO
    private void parseContent(String content) {
        if (TextUtils.isEmpty(content))
            return;

        switch (this) {
            case NONE:
                break;
            case EVERY_DAY:
                break;
            case EVERY_WEEK://begin-0,end-6
                break;
            case EVERY_MONTH://begin-1,end-31
                break;
            case EVERY_YEAR:
                break;
            case EVERY_INTERVAL:
                break;
        }
    }

    // week or month
    public Set<Integer> getSetFromContent() {
        Set<Integer> set = new ArraySet<>();
        switch (this) {
            case EVERY_WEEK:
            case EVERY_MONTH:
                String[] split = mContent.split("_");
                for (String i : split) {
                    if (i.length() < 3) {
                        try {
                            set.add(Integer.parseInt(i));
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                }
                break;
        }
        return set;
    }

    // week or month
    public boolean isOnlyOneDay() {
        switch (this) {
            case EVERY_WEEK:
                if (mContent.startsWith("week") && mContent.split("_").length == 2)
                    return true;
                break;
            case EVERY_MONTH:
                if (mContent.startsWith("month") && mContent.split("_").length == 2)
                    return true;
                break;
        }
        return false;
    }

    // week or month
    public boolean isEveryDay() {
        switch (this) {
            case EVERY_DAY:
                return true;
            case EVERY_WEEK:
                if (mContent.startsWith("week") && mContent.split("_").length == 8)
                    return true;
                break;
            case EVERY_MONTH:
                if (mContent.startsWith("month") && mContent.split("_").length == 32)
                    return true;
                break;
        }
        return false;
    }

    public long getCloseRepeatTime() {
        return mCloseRepeatTime;
    }

    public void setCloseRepeatTime(long time) {
        mCloseRepeatTime = time;
    }

    public static PlanRepeat getRepeat(int type, String content, int endTime) {
        PlanRepeat repeat;
        switch (type) {
            case 0:
                repeat = NONE;
                break;
            case 1:
                repeat = EVERY_DAY;
                break;
            case 2:
                repeat = EVERY_WEEK;
                break;
            case 3:
                repeat = EVERY_MONTH;
                break;
            case 4:
                repeat = EVERY_YEAR;
                break;
            case 5:
                repeat = EVERY_INTERVAL;
                break;
            default:
                return null;
        }
        repeat.setContent(content);
        repeat.setCloseRepeatTime(endTime);
        return repeat;
    }

    public static PlanRepeat getDefault() {
        return NONE;
    }
}
