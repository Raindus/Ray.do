package com.raindus.raydo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.raindus.raydo.R;
import com.raindus.raydo.common.Utils;
import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.plan.PlanAdapter;
import com.raindus.raydo.plan.PlanSort;
import com.raindus.raydo.plan.PlanSortDelegate;
import com.raindus.raydo.plan.entity.PlanEntity;
import com.raindus.raydo.plan.entity.PlanTag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Raindus on 2018/3/4.
 */

public class ViewFragment extends BaseFragment implements CalendarView.OnDateSelectedListener, CalendarView.OnMonthChangeListener, PlanAdapter.PlanAdapterListener {

    // calendar
    private CalendarView mCalendarView;

    private TextView mTvDate;
    private TextView mTvLunar;
    private TextView mTvYear;

    // plan
    private RelativeLayout mRlPlanEmpty;
    private RecyclerView mRvPlan;
    private PlanAdapter mPlanAdapter;
    private PlanSortDelegate mPlanSort;
    private LinearLayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.view_more).setOnClickListener(this);
        view.findViewById(R.id.view_more).setOnLongClickListener(this);
        registerForContextMenu(view.findViewById(R.id.view_more));

        mTvDate = view.findViewById(R.id.view_tv_date);
        mTvLunar = view.findViewById(R.id.view_tv_lunar);
        mTvYear = view.findViewById(R.id.view_tv_year);

        mRlPlanEmpty = view.findViewById(R.id.plan_empty);
        mRvPlan = view.findViewById(R.id.view_plan_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRvPlan.setLayoutManager(mLayoutManager);
        mPlanAdapter = new PlanAdapter(getActivity());
        mPlanAdapter.setPlanAdapterListener(this);
        mRvPlan.setAdapter(mPlanAdapter);
        mPlanSort = new PlanSortDelegate(getActivity().getApplication(), mPlanAdapter, false);

        mCalendarView = view.findViewById(R.id.view_calendar);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        ((TextView) view.findViewById(R.id.view_tv_today)).setText(mCalendarView.getCurDay() + "");
        view.findViewById(R.id.view_today).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        mPlanSort.refresh();
        refreshSchemes(mPlanSort.getYear(), mPlanSort.getMonth());
        mCalendarView.update();
        ;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_plan_fragment, menu);
        menu.getItem(0).setTitle(mPlanSort.getShowComplectedDescribed());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_plan_show_status:
                mPlanSort.switchShowComplected();
                break;
            case R.id.menu_plan_sort_time:
                mPlanSort.setSortType(PlanSort.SORT_BY_TIME);
                break;
            case R.id.menu_plan_sort_status:
                mPlanSort.setSortType(PlanSort.SORT_BY_STATUS);
                break;
            case R.id.menu_plan_sort_priority:
                mPlanSort.setSortType(PlanSort.SORT_BY_PRIORITY);
                break;
            case R.id.menu_plan_sort_tag:
                mPlanSort.setSortType(PlanSort.SORT_BY_TAG);
                break;
        }
        return super.onContextItemSelected(item);
    }

    // 更新日历上的标记
    private void refreshSchemes(int year, int month) {
        List<PlanEntity> list = ObjectBox.PlanEntityBox.queryThirdMonth(year, month);
        if (list == null || list.size() == 0)
            return;

        List<Calendar> schemes = new ArrayList<>();
        int y = -1;
        int m = -1;
        int d = -1;
        int num = -1;
        PlanTag tag = PlanTag.None;

        for (PlanEntity entity : list) {
            Date date = new Date(entity.getTime().getStartTime());

            if (num == -1) {
                y = date.getYear() + 1900;
                m = date.getMonth() + 1;
                d = date.getDate();
                num = 1;
                tag = entity.getTag();
                continue;
            }

            if (y == date.getYear() + 1900 && m == date.getMonth() + 1 && d == date.getDate()) {
                num++;
                continue;
            } else {
                if (num == 1) {
                    schemes.add(tag.getSchemeCalendar(y, m, d, getContext()));
                } else {
                    schemes.add(getSchemeCalendar(y, m, d, num));
                }
                num = 1;
                y = date.getYear() + 1900;
                m = date.getMonth() + 1;
                d = date.getDate();
                tag = entity.getTag();
            }
        }

        if (num == 1)
            schemes.add(tag.getSchemeCalendar(y, m, d, getContext()));
        else if (num > 1)
            schemes.add(getSchemeCalendar(y, m, d, num));

        mCalendarView.setSchemeDate(schemes);
    }

    // 标记数量
    private Calendar getSchemeCalendar(int year, int month, int day, int num) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(getResources().getColor(R.color.dandongshi));//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(num + "");
        return calendar;
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean b) {
        if (calendar == null) {
            return;
        }

        mTvDate.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        mTvYear.setText(calendar.getYear() + "");
        mTvLunar.setText(calendar.getLunar());

        mPlanSort.setQueryDate(calendar.getYear(), calendar.getMonth(), calendar.getDay());
    }

    @Override
    public void onMonthChange(int year, int month) {
        refreshSchemes(year, month);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_today:
                mCalendarView.scrollToCurrent();
                break;
            case R.id.view_more:
                getView().findViewById(R.id.view_more).performLongClick();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.view_more) {
            getView().findViewById(R.id.view_more).showContextMenu(0f, Utils.dipToPx(getContext(), 36));
            return true;
        }
        return super.onLongClick(v);
    }

    @Override
    public void onDataChanged(int itemCount) {
        if (itemCount == 0)
            mRlPlanEmpty.setVisibility(View.VISIBLE);
        else
            mRlPlanEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onPlanDeleted() {
        toast("计划已删除");

        //TODO 该控件有待改善，标记无法及时更新...
        mPlanSort.refresh();
        refreshSchemes(mPlanSort.getYear(), mPlanSort.getMonth());
        mCalendarView.update();
    }

}
