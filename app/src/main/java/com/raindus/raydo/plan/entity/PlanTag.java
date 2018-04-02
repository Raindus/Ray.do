package com.raindus.raydo.plan.entity;

import android.content.Context;
import android.graphics.Color;

import com.haibin.calendarview.Calendar;
import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/3/11.
 */

public enum PlanTag {
    Work(7, R.drawable.plan_tag_work, R.color.tag_work, "工作"),
    Study(6, R.drawable.plan_tag_study, R.color.tag_study, "学习"),
    Entertainment(5, R.drawable.plan_tag_entertainment, R.color.tag_entertainment, "娱乐"),
    Sport(4, R.drawable.plan_tag_sport, R.color.tag_sport, "运动"),
    Life(3, R.drawable.plan_tag_life, R.color.tag_life, "生活"),
    Tourism(2, R.drawable.plan_tag_tourism, R.color.tag_tourism, "旅游"),
    Shopping(1, R.drawable.plan_tag_shopping, R.color.tag_shopping, "购物"),
    None(0, R.drawable.plan_tag_none, R.color.tag_none, "无");

    private final int mType;
    private final int mIcon;
    private final int mColor;
    private final String mContent;

    PlanTag(int type, int icon, int color, String content) {
        mType = type;
        mIcon = icon;
        mColor = color;
        mContent = content;
    }

    public int getType() {
        return mType;
    }

    public int getIcon() {
        return mIcon;
    }

    /**
     * @return 日历标记
     */
    public Calendar getSchemeCalendar(int year, int month, int day, Context context) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(context.getColor(mColor));//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(mContent);
        return calendar;
    }

    public static PlanTag getTag(int type) {
        switch (type) {
            case 7:
                return Work;
            case 6:
                return Study;
            case 5:
                return Entertainment;
            case 4:
                return Sport;
            case 3:
                return Life;
            case 2:
                return Tourism;
            case 1:
                return Shopping;
            case 0:
            default:
                return None;
        }
    }

    public static PlanTag getDefault() {
        return None;
    }
}
