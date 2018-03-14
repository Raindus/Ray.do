package com.raindus.raydo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.raindus.raydo.R;
import com.raindus.raydo.plan.entity.PlanTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raindus on 2018/3/12.
 */

public class PlanTimeDialog extends BaseDialog {

    private CalendarView mCalendarView;
    private TextView mTvTitle;

    private LinearLayout mLlEnd;

    private ImageView mIvTime;
    private ImageView mIvRemind;
    private ImageView mIvRepeat;

    private TextView mTvTime;
    private TextView mTvRemind;
    private TextView mTvRepeat;
    private TextView mTvEnd;

    private ImageButton mIBtnTime;
    private ImageButton mIBtnRemind;
    private ImageButton mIBtnRepeat;

    public PlanTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plan_time);

        initView();
    }

    private void initView() {
        mCalendarView = findViewById(R.id.time_calendar);

        List<Calendar> schemes = new ArrayList<>();
        schemes.add(getSchemeCalendar(2018, 3, 21));
        mCalendarView.setSchemeDate(schemes);

        mCalendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {

            public void onMonthChange(int year, int month) {
                debugLog(year + "__" + month);
            }
        });

        findViewById(R.id.time_negative).setOnClickListener(this);
        findViewById(R.id.time_positive).setOnClickListener(this);

        mTvTitle = findViewById(R.id.time_title);
        mTvTitle.setOnClickListener(this);

        findViewById(R.id.time_ll_time).setOnClickListener(this);
        findViewById(R.id.time_ll_remind).setOnClickListener(this);
        findViewById(R.id.time_ll_repeat).setOnClickListener(this);
        mLlEnd = findViewById(R.id.time_ll_end);
        mLlEnd.setOnClickListener(this);

        mIvTime = findViewById(R.id.time_iv_time);
        mIvRemind = findViewById(R.id.time_iv_remind);
        mIvRepeat = findViewById(R.id.time_iv_repeat);

        mTvTime = findViewById(R.id.time_tv_time);
        mTvRemind = findViewById(R.id.time_tv_remind);
        mTvRepeat = findViewById(R.id.time_tv_repeat);
        mTvEnd = findViewById(R.id.time_tv_end);

        mIBtnTime = findViewById(R.id.time_btn_time);
        mIBtnTime.setOnClickListener(this);
        mIBtnRemind = findViewById(R.id.time_btn_remind);
        mIBtnRemind.setOnClickListener(this);
        mIBtnRepeat = findViewById(R.id.time_btn_repeat);
        mIBtnRepeat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_negative:
                break;
            case R.id.time_positive:
                break;
            case R.id.time_title:
                break;
            case R.id.time_ll_time:
                chooseTime();
                break;
            case R.id.time_ll_remind:
                planRemind();
                break;
            case R.id.time_ll_repeat:
                break;
            case R.id.time_ll_end:
                chooseEndDate();
                break;
            case R.id.time_btn_time:
                break;
            case R.id.time_btn_remind:
                break;
            case R.id.time_btn_repeat:
                break;
        }
    }

    private void chooseTime() {
        ChooseTimeDialog dialog = new ChooseTimeDialog(getContext());
        dialog.setOnChooseTimeCallback(new ChooseTimeDialog.OnChooseTimeCallback() {
            @Override
            public void onChooseTime(int hour, int min) {
                //TODO
            }
        });
        dialog.show();
    }

    private void planRemind() {
        PlanRemindDialog remindDialog = new PlanRemindDialog(getContext(), null);
        remindDialog.setOnRemindCallback(new PlanRemindDialog.OnRemindCallback() {
            @Override
            public void onRemind(PlanTime.Remind remind) {
                //TODO
            }
        });
        remindDialog.show();
    }

    private void chooseEndDate() {
        ChooseDateDialog dateDialog = new ChooseDateDialog(getContext());
        dateDialog.setOnChooseDateCallback(new ChooseDateDialog.OnChooseDateCallback() {
            @Override
            public void onChooseDate(int year, int month, int day) {
                //TODO month-1
            }
        });
        dateDialog.show();
    }

    private Calendar getSchemeCalendar(int year, int month, int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        return calendar;
    }
}
