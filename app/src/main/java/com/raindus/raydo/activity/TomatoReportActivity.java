package com.raindus.raydo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.ui.BarChartView;
import com.raindus.raydo.ui.LineChartView;

public class TomatoReportActivity extends BaseActivity {

    private static final String TEXT_OVER = "比平均值高出%f%";
    private static final String TEXT_PERIOD = "%d:00-%d:00";

    // 基本报告
    private TextView mTvTodayFocusTimes;
    private TextView mTvTodayTomatoNum;
    private TextView mTvTodayFocusTotalTime;

    private TextView mTvWeekFocusTimes;
    private TextView mTvWeekTomatoNum;
    private TextView mTvWeekFocusTotalTime;

    private TextView mTvAllFocusTimes;
    private TextView mTvAllTomatoNum;
    private TextView mTvAllFocusTotalTime;

    // 本周最佳工作日
    private TextView mTvBestWorkday;
    private TextView mTvBestWorkdayOfTime;
    private TextView mTvBestWorkdayOfOver;
    private BarChartView mBarChartWeek;

    // 本周最佳工作时段
    private TextView mTvBestPeriodOfTime;
    private BarChartView mBarChartTime;

    // 最近专注次数
    private int mCurFocusTimes = 0;
    private Button mBtnLatelyFocusTimesOfDay;
    private Button mBtnLatelyFocusTimesOfWeek;
    private Button mBtnLatelyFocusTimesOfMonth;
    private LineChartView mLineCharFocusTimes;

    // 最近番茄收成
    private int mCurTomatoNum = 0;
    private Button mBtnLatelyTomatoNumOfDay;
    private Button mBtnLatelyTomatoNumOfWeek;
    private Button mBtnLatelyTomatoNumOfMonth;
    private LineChartView mLineCharTomatoNum;

    // 最近专注时长
    private int mCurFocusTime = 0;
    private Button mBtnLatelyFocusTimeOfDay;
    private Button mBtnLatelyFocusTimeOfWeek;
    private Button mBtnLatelyFocusTimeOfMonth;
    private LineChartView mLineCharFocusTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_report);

        initView();
    }

    private void initView() {
        findViewById(R.id.report_back).setOnClickListener(this);
        // 基本报告
        mTvTodayFocusTimes = findViewById(R.id.tomato_report_tv_today_focus);
        mTvTodayTomatoNum = findViewById(R.id.tomato_report_tv_today_num);
        mTvTodayFocusTotalTime = findViewById(R.id.tomato_report_tv_today_time);

        mTvWeekFocusTimes = findViewById(R.id.tomato_report_tv_week_focus);
        mTvWeekTomatoNum = findViewById(R.id.tomato_report_tv_week_num);
        mTvWeekFocusTotalTime = findViewById(R.id.tomato_report_tv_week_time);

        mTvAllFocusTimes = findViewById(R.id.tomato_report_tv_total_focus);
        mTvAllTomatoNum = findViewById(R.id.tomato_report_tv_total_num);
        mTvAllFocusTotalTime = findViewById(R.id.tomato_report_tv_total_time);

        // 本周最佳工作日
        mTvBestWorkday = findViewById(R.id.tomato_report_tv_best_workday);
        mTvBestWorkdayOfTime = findViewById(R.id.tomato_report_tv_best_workday_time);
        mTvBestWorkdayOfOver = findViewById(R.id.tomato_report_tv_best_workday_over);
        mBarChartWeek = findViewById(R.id.tomato_report_bc_week);

        // 本周最佳工作时段
        mTvBestPeriodOfTime = findViewById(R.id.tomato_report_tv_best_time);
        mBarChartTime = findViewById(R.id.tomato_report_bc_time);

        // 最近专注次数
        mBtnLatelyFocusTimesOfDay = findViewById(R.id.tomato_report_btn_focus_day);
        mBtnLatelyFocusTimesOfDay.setOnClickListener(this);
        mBtnLatelyFocusTimesOfWeek = findViewById(R.id.tomato_report_btn_focus_week);
        mBtnLatelyFocusTimesOfWeek.setOnClickListener(this);
        mBtnLatelyFocusTimesOfMonth = findViewById(R.id.tomato_report_btn_focus_month);
        mBtnLatelyFocusTimesOfMonth.setOnClickListener(this);
        mLineCharFocusTimes = findViewById(R.id.tomato_report_lc_focus);

        // 最近番茄收成
        mBtnLatelyTomatoNumOfDay = findViewById(R.id.tomato_report_btn_crop_day);
        mBtnLatelyTomatoNumOfDay.setOnClickListener(this);
        mBtnLatelyTomatoNumOfWeek = findViewById(R.id.tomato_report_btn_crop_week);
        mBtnLatelyTomatoNumOfWeek.setOnClickListener(this);
        mBtnLatelyTomatoNumOfMonth = findViewById(R.id.tomato_report_btn_crop_month);
        mBtnLatelyTomatoNumOfMonth.setOnClickListener(this);
        mLineCharTomatoNum = findViewById(R.id.tomato_report_lc_crop);

        // 最近专注时长
        mBtnLatelyFocusTimeOfDay = findViewById(R.id.tomato_report_btn_time_day);
        mBtnLatelyFocusTimeOfDay.setOnClickListener(this);
        mBtnLatelyFocusTimeOfWeek = findViewById(R.id.tomato_report_btn_time_week);
        mBtnLatelyFocusTimeOfWeek.setOnClickListener(this);
        mBtnLatelyFocusTimeOfMonth = findViewById(R.id.tomato_report_btn_time_month);
        mBtnLatelyFocusTimeOfMonth.setOnClickListener(this);
        mLineCharFocusTime = findViewById(R.id.tomato_report_lc_time);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.report_back:
                finish();
                break;
            case R.id.tomato_report_btn_focus_day:
                setFocusTimes(0);
                break;
            case R.id.tomato_report_btn_focus_week:
                setFocusTimes(1);
                break;
            case R.id.tomato_report_btn_focus_month:
                setFocusTimes(2);
                break;
            case R.id.tomato_report_btn_crop_day:
                setTomatoNum(0);
                break;
            case R.id.tomato_report_btn_crop_week:
                setTomatoNum(1);
                break;
            case R.id.tomato_report_btn_crop_month:
                setTomatoNum(2);
                break;
            case R.id.tomato_report_btn_time_day:
                setFocusTime(0);
                break;
            case R.id.tomato_report_btn_time_week:
                setFocusTime(1);
                break;
            case R.id.tomato_report_btn_time_month:
                setFocusTime(2);
                break;
        }
    }

    //////////
    ///

    private void setFocusTimes(int type) {
        if (mCurFocusTimes == type)
            return;

        switchFocusTimes(mCurFocusTimes, false);
        mCurFocusTimes = type;
        switchFocusTimes(mCurFocusTimes, true);
    }

    private void switchFocusTimes(int type, boolean flag) {
        switch (type) {
            case 0:
                mBtnLatelyFocusTimesOfDay.setTextColor(getColor(flag));
                //TODO if (flag)
                break;
            case 1:
                mBtnLatelyFocusTimesOfWeek.setTextColor(getColor(flag));
                break;
            case 2:
                mBtnLatelyFocusTimesOfMonth.setTextColor(getColor(flag));
                break;
        }
    }

    //////////
    ///

    private void setTomatoNum(int type) {
        if (mCurTomatoNum == type)
            return;

        switchTomatoNum(mCurTomatoNum, false);
        mCurTomatoNum = type;
        switchTomatoNum(mCurTomatoNum, true);
    }

    private void switchTomatoNum(int type, boolean flag) {
        switch (type) {
            case 0:
                mBtnLatelyTomatoNumOfDay.setTextColor(getColor(flag));
                //TODO if (flag)
                break;
            case 1:
                mBtnLatelyTomatoNumOfWeek.setTextColor(getColor(flag));
                break;
            case 2:
                mBtnLatelyTomatoNumOfMonth.setTextColor(getColor(flag));
                break;
        }
    }

    //////////
    ///

    private void setFocusTime(int type) {
        if (mCurFocusTime == type)
            return;

        switchFocusTime(mCurFocusTime, false);
        mCurFocusTime = type;
        switchFocusTime(mCurFocusTime, true);
    }

    private void switchFocusTime(int type, boolean flag) {
        switch (type) {
            case 0:
                mBtnLatelyFocusTimeOfDay.setTextColor(getColor(flag));
                //TODO if (flag)
                break;
            case 1:
                mBtnLatelyFocusTimeOfWeek.setTextColor(getColor(flag));
                break;
            case 2:
                mBtnLatelyFocusTimeOfMonth.setTextColor(getColor(flag));
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
