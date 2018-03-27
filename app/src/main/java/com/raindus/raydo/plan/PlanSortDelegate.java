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

    private boolean mShowComplected = true;
    private int mSortType = PlanSort.SORT_BY_TIME;

    public PlanSortDelegate(Application application, PlanAdapter adapter) {
        mApplication = application;
        mAdapter = adapter;
    }

    public String getShowComplectedDescribed() {
        return mShowComplected ? "隐藏已完成" : "显示已完成";
    }

    public void switchShowComplected() {
        mShowComplected = !mShowComplected;
        refresh();
    }

    public int getSortType() {
        return mSortType;
    }

    public void setSortType(int sortType) {
        if (mSortType == sortType)
            return;

        mSortType = sortType;
        refresh();
    }

    public void refresh() {
        List<PlanEntity> list = ObjectBox.PlanEntityBox.queryAll(mApplication, true, mShowComplected);
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
