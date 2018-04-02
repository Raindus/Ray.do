package com.raindus.raydo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.raindus.raydo.R;
import com.raindus.raydo.plan.entity.PlanTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raindus on 2018/3/4.
 */

public class ViewFragment extends BaseFragment {

    private CalendarView mCalendarView;

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
        mCalendarView = view.findViewById(R.id.view_calendar);


        List<Calendar> schemes = new ArrayList<>();
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        schemes.add(PlanTag.Work.getSchemeCalendar(year, month, 3,getContext()));
        schemes.add(PlanTag.Study.getSchemeCalendar(year, month, 6,getContext()));
        schemes.add(PlanTag.Shopping.getSchemeCalendar(year, month, 9,getContext()));
        schemes.add(PlanTag.Sport.getSchemeCalendar(year, month, 13,getContext()));
        schemes.add(PlanTag.Tourism.getSchemeCalendar(year, month, 16,getContext()));
        schemes.add(PlanTag.Entertainment.getSchemeCalendar(year, month, 9,getContext()));
        schemes.add(PlanTag.None.getSchemeCalendar(year, month, 23,getContext()));
        schemes.add(PlanTag.Life.getSchemeCalendar(year, month, 26,getContext()));
        schemes.add(PlanTag.Tourism.getSchemeCalendar(year, month, 29,getContext()));
        mCalendarView.setSchemeDate(schemes);

    }
}
