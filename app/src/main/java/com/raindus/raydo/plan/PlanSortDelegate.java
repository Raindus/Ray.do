package com.raindus.raydo.plan;

import android.app.Application;

import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.plan.entity.PlanEntity;

import java.util.List;

/**
 * Created by Raindus on 2018/3/27.
 */

public class PlanSortDelegate {

    private final Application mApplication;
    private final PlanAdapter mAdapter;

    /**
     * if true = PlanFragment
     * else false = ViewFragment
     */
    private final boolean mToday;

    private boolean mShowComplected = true;
    private int mSortType = PlanSort.SORT_BY_TIME;

    // if mToday = false
    private int mYear = -1;
    private int mMonth = -1;
    private int mDate = -1;

    public PlanSortDelegate(Application application, PlanAdapter adapter) {
        this(application, adapter, true);
    }

    public PlanSortDelegate(Application application, PlanAdapter adapter, boolean isToday) {
        mApplication = application;
        mAdapter = adapter;
        mToday = isToday;
    }

    public String getShowComplectedDescribed() {
        return mShowComplected ? "隐藏已完成" : "显示已完成";
    }

    public void switchShowComplected() {
        mShowComplected = !mShowComplected;
        refresh();
    }

    public void setSortType(int sortType) {
        if (mSortType == sortType)
            return;

        mSortType = sortType;
        refresh();
    }

    public void setQueryDate(int year, int month, int date) {
        mYear = year;
        mMonth = month;
        mDate = date;
        refresh();
    }

    public void refresh() {
        List<PlanEntity> list = null;

        if (mToday)
            list = ObjectBox.PlanEntityBox.queryAll(mApplication, true, mShowComplected);
        else if (mYear == -1 || mMonth == -1 || mDate == -1)
            list = null;
        else
            list = ObjectBox.PlanEntityBox.queryDate(mApplication, mYear, mMonth, mDate, mShowComplected);

        mAdapter.setPlanData(sortByType(list));
    }

    private List<Object> sortByType(List<PlanEntity> plans) {
        switch (mSortType) {
            case PlanSort.SORT_BY_TIME:
                return PlanSort.sortByTime(plans);
            case PlanSort.SORT_BY_STATUS:
                return PlanSort.sortByStatus(plans);
            case PlanSort.SORT_BY_PRIORITY:
                return PlanSort.sortByPriority(plans);
            case PlanSort.SORT_BY_TAG:
                return PlanSort.sortByTag(plans);
            default:
                return null;
        }
    }
}
