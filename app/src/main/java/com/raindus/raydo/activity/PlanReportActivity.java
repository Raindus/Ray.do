package com.raindus.raydo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.report.entity.TomatoReportEntity;
import com.raindus.raydo.ui.BarChartView;
import com.raindus.raydo.ui.LineChartView;

import java.util.Arrays;
import java.util.List;

public class PlanReportActivity extends BaseActivity {

    private static final String TEXT_OVER = "比平均值高出%f%";
    private static final String TEXT_PERIOD = "%d:00-%d:00";

    // 基本报告
    private TextView mTvTodayCompleted;
    private TextView mTvTodayPlanNum;

    private TextView mTvWeekCompleted;
    private TextView mTvWeekPlanNum;

    private TextView mTvAllCompleted;
    private TextView mTvAllPlanNum;

    // 本周最佳工作日
    private TextView mTvBestWorkday;
    private TextView mTvBestWorkdayOfTime;
    private TextView mTvBestWorkdayOfOver;
    private BarChartView mBarChartWeek;

    // 本周最佳工作时段
    private TextView mTvBestPeriodOfTime;
    private BarChartView mBarChartTime;

    // 最近计划数目
    private int mCurPlanNum = 0;
    private Button mBtnLatelyPlanNumOfDay;
    private Button mBtnLatelyPlanNumOfWeek;
    private Button mBtnLatelyPlanNumOfMonth;
    private LineChartView mLineCharPlanNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_report);

        initView();
    }

    private void initView() {
        findViewById(R.id.report_back).setOnClickListener(this);

        // 基本报告
        mTvTodayCompleted = findViewById(R.id.plan_report_tv_today_completed);
        mTvTodayPlanNum = findViewById(R.id.plan_report_tv_today_number);

        mTvWeekCompleted = findViewById(R.id.plan_report_tv_week_completed);
        mTvWeekPlanNum = findViewById(R.id.plan_report_tv_week_number);

        mTvAllCompleted = findViewById(R.id.plan_report_tv_all_completed);
        mTvAllPlanNum = findViewById(R.id.plan_report_tv_all_number);

        // 本周最佳工作日
        mTvBestWorkday = findViewById(R.id.plan_report_tv_best_workday);
        mTvBestWorkdayOfTime = findViewById(R.id.plan_report_tv_best_workday_time);
        mTvBestWorkdayOfOver = findViewById(R.id.plan_report_tv_best_workday_over);
        mBarChartWeek = findViewById(R.id.plan_report_bc_week);

        // 本周最佳工作时段
        mTvBestPeriodOfTime = findViewById(R.id.plan_report_tv_best_time);
        mBarChartTime = findViewById(R.id.plan_report_bc_time);

        // 最近计划数目
        mBtnLatelyPlanNumOfDay = findViewById(R.id.plan_report_btn_day);
        mBtnLatelyPlanNumOfDay.setOnClickListener(this);
        mBtnLatelyPlanNumOfWeek = findViewById(R.id.plan_report_btn_week);
        mBtnLatelyPlanNumOfWeek.setOnClickListener(this);
        mBtnLatelyPlanNumOfMonth = findViewById(R.id.plan_report_btn_month);
        mBtnLatelyPlanNumOfMonth.setOnClickListener(this);
        mLineCharPlanNum = findViewById(R.id.plan_report_lc);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.report_back:
                finish();
                break;
            case R.id.plan_report_btn_day:
                setPlanNum(0);
                break;
            case R.id.plan_report_btn_week:
                setPlanNum(1);
                break;
            case R.id.plan_report_btn_month:
                setPlanNum(2);
                break;
        }
    }

    private void setPlanNum(int type) {
        if (mCurPlanNum == type)
            return;

        switchPlanNum(mCurPlanNum, false);
        mCurPlanNum = type;
        switchPlanNum(mCurPlanNum, true);
    }

    private void switchPlanNum(int type, boolean flag) {
        switch (type) {
            case 0:
                mBtnLatelyPlanNumOfDay.setTextColor(getColor(flag));
                //TODO if (flag)
                break;
            case 1:
                mBtnLatelyPlanNumOfWeek.setTextColor(getColor(flag));
                break;
            case 2:
                mBtnLatelyPlanNumOfMonth.setTextColor(getColor(flag));
                break;
        }
    }

    private int getColor(boolean flag) {
        if (flag)
            return getResources().getColor(R.color.dandongshi);
        else
            return getResources().getColor(R.color.tomato_coffee);
    }

}
