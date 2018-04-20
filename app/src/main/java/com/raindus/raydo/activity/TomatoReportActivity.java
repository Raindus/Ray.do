package com.raindus.raydo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.common.Utils;
import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.report.entity.TomatoReportEntity;
import com.raindus.raydo.ui.BarChartView;
import com.raindus.raydo.ui.LineChartView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TomatoReportActivity extends BaseActivity {

    private static final String TEXT_OVER = "比平均值高出%d";
    private static final String TEXT_PERIOD = "%d:00-%d:00";

    private Date mCur;
    // 专注次数 - 番茄收成 - 专注时长
    // 七天前 - > 当天/周/月
    private float[][] mDay;
    private String[] mKeyDay;
    private float[][] mWeek;
    private String[] mKeyWeek;
    private float[][] mMonth;
    private String[] mKeyMonth;

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

        mCur = new Date();
        initView();
        initData();
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

    private void initData() {
        // 总
        TomatoReportEntity all = ObjectBox.TomatoReportEntityBox.queryAll();
        mTvAllFocusTimes.setText(all.focusTimes + "");
        mTvAllTomatoNum.setText(all.tomatoNum + "");
        mTvAllFocusTotalTime.setText(Utils.getFloatOnePoint(all.focusTime, true));

        List<TomatoReportEntity> dayList = ObjectBox.TomatoReportEntityBox.queryLatelySevenDate();
        List<TomatoReportEntity> weekList = ObjectBox.TomatoReportEntityBox.queryLatelySevenWeek();
        List<TomatoReportEntity> monthList = ObjectBox.TomatoReportEntityBox.queryLatelySevenMonth();

        // 时段
        float[] bc = computePeriodSpread(dayList);
        mTvBestPeriodOfTime.setText(computeBestPeriod(bc));
        mBarChartTime.setBarChart(bc, BarChartView.MODE_TIME);

        // 工作日
        float[] lc = computeWorkday(dayList);
        int index = 0;
        float max = lc[0];
        float avg = max;
        for (int i = 1; i < lc.length; i++) {
            if (max < lc[i]) {
                index = i;
                max = lc[i];
            }
            avg += max;
        }
        avg /= 7;
        mTvBestWorkday.setText("星期" + BarChartView.TEXT_WEEK[index]);
        mTvBestWorkdayOfTime.setText(Utils.getFloatOnePoint(max, true) + "h");
        mTvBestWorkdayOfOver.setText(String.format(TEXT_OVER, (int) ((max - avg) / avg * 100)) + "%");
        mBarChartWeek.setBarChart(lc, BarChartView.MODE_WEEK);

        // 其他三项
        computerKey();
        computerDay(dayList);
        computerWeek(weekList);
        computerMonth(monthList);

        // 标题
        mTvTodayFocusTimes.setText(Utils.getFloatNoPoint(mDay[0][6]));
        mTvTodayTomatoNum.setText(Utils.getFloatNoPoint(mDay[1][6]));
        mTvTodayFocusTotalTime.setText(Utils.getFloatOnePoint(mDay[2][6], false));

        mTvWeekFocusTimes.setText(Utils.getFloatNoPoint(mWeek[0][6]));
        mTvWeekTomatoNum.setText(Utils.getFloatNoPoint(mWeek[1][6]));
        mTvWeekFocusTotalTime.setText(Utils.getFloatOnePoint(mWeek[2][6], false));

        setFocusTimesData(mCurFocusTimes);
        setTomatoNumData(mCurTomatoNum);
        setFocusTimeData(mCurFocusTime);
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
        if (flag)
            setFocusTimesData(type);
    }

    private void setFocusTimesData(int type) {
        switch (type) {
            case 0:
                mLineCharFocusTimes.setLineChart(mKeyDay, mDay[0], false);
                break;
            case 1:
                mLineCharFocusTimes.setLineChart(mKeyWeek, mWeek[0], false);
                break;
            case 2:
                mLineCharFocusTimes.setLineChart(mKeyMonth, mMonth[0], false);
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
        if (flag)
            setTomatoNumData(type);
    }

    private void setTomatoNumData(int type) {
        switch (type) {
            case 0:
                mLineCharTomatoNum.setLineChart(mKeyDay, mDay[1], false);
                break;
            case 1:
                mLineCharTomatoNum.setLineChart(mKeyWeek, mWeek[1], false);
                break;
            case 2:
                mLineCharTomatoNum.setLineChart(mKeyMonth, mMonth[1], false);
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
                break;
            case 1:
                mBtnLatelyFocusTimeOfWeek.setTextColor(getColor(flag));
                break;
            case 2:
                mBtnLatelyFocusTimeOfMonth.setTextColor(getColor(flag));
                break;
        }
        if (flag)
            setFocusTimeData(type);
    }

    private void setFocusTimeData(int type) {
        switch (type) {
            case 0:
                mLineCharFocusTime.setLineChart(mKeyDay, mDay[2], true);
                break;
            case 1:
                mLineCharFocusTime.setLineChart(mKeyWeek, mWeek[2], true);
                break;
            case 2:
                mLineCharFocusTime.setLineChart(mKeyMonth, mMonth[2], true);
                break;
        }
    }

    private int getColor(boolean flag) {
        if (flag)
            return getResources().getColor(R.color.dandongshi);
        else
            return getResources().getColor(R.color.tomato_coffee);
    }

    // day 专注次数 - 番茄收成 - 专注时长
    private void computerDay(List<TomatoReportEntity> list) {
        mDay = new float[3][7];
        Arrays.fill(mDay[0], 0);
        Arrays.fill(mDay[1], 0);
        Arrays.fill(mDay[2], 0);
        for (TomatoReportEntity entity : list) {
            // 间隔几天？
            int day = entity.getReportType().intervalToNow(mCur);
            int index = 6 - day;
            mDay[0][index] = entity.focusTimes;
            mDay[1][index] = entity.tomatoNum;
            mDay[2][index] = (float) entity.focusTime / 60f;
        }
    }

    // week 专注次数 - 番茄收成 - 专注时长
    private void computerWeek(List<TomatoReportEntity> list) {
        mWeek = new float[3][7];
        Arrays.fill(mWeek[0], 0);
        Arrays.fill(mWeek[1], 0);
        Arrays.fill(mWeek[2], 0);
        for (TomatoReportEntity entity : list) {
            // 间隔几天？
            int week = entity.getReportType().intervalToNow(mCur);
            int index = 6 - week;
            mWeek[0][index] = entity.focusTimes;
            mWeek[1][index] = entity.tomatoNum;
            mWeek[2][index] = (float) entity.focusTime / 60f;
        }
    }

    // month 专注次数 - 番茄收成 - 专注时长
    private void computerMonth(List<TomatoReportEntity> list) {
        mMonth = new float[3][7];
        Arrays.fill(mMonth[0], 0);
        Arrays.fill(mMonth[1], 0);
        Arrays.fill(mMonth[2], 0);
        for (TomatoReportEntity entity : list) {
            // 间隔几天？
            int month = entity.getReportType().intervalToNow(mCur);
            int index = 6 - month;
            mMonth[0][index] = entity.focusTimes;
            mMonth[1][index] = entity.tomatoNum;
            mMonth[2][index] = (float) entity.focusTime / 60f;
        }
    }

    private void computerKey() {
        mKeyDay = new String[7];
        mKeyWeek = new String[7];
        mKeyMonth = new String[7];

        int lastMonth = mCur.getMonth();

        for (int i = 6; i >= 0; i--) {
            // day
            int date = mCur.getDate() - (6 - i);
            if (date > 0) {
                mKeyDay[i] = String.valueOf(date);
            } else if (mCur.getMonth() == 0) {
                if (date == 0)
                    mKeyDay[i] = String.valueOf("12.31");
                else
                    mKeyDay[i] = String.valueOf(31 - date);
            } else {
                if (date == 0)
                    mKeyDay[i] = String.valueOf(mCur.getMonth() + "." + DateUtils.getDaysOfMonth(mCur.getYear() + 1900, mCur.getMonth()));
                else
                    mKeyDay[i] = String.valueOf(DateUtils.getDaysOfMonth(mCur.getYear() + 1900, mCur.getMonth()) - date);
            }

            // week
            long week = mCur.getTime() - (mCur.getDay() * DateUtils.ONE_DAY) - ((6 - i) * DateUtils.ONE_WEEK);
            Date weekDate = new Date(week);
            if (weekDate.getMonth() == lastMonth) {
                mKeyWeek[i] = String.valueOf(weekDate.getDate());
            } else {
                lastMonth = weekDate.getMonth();
                mKeyWeek[i] = String.valueOf((weekDate.getMonth() + 1) + "." + weekDate.getDate());
            }

            // month
            int month = mCur.getMonth() + 1 - (6 - i);
            if (month > 0) {
                mKeyMonth[i] = String.valueOf(month);
            } else if (month == 0)
                mKeyMonth[i] = String.valueOf(mCur.getYear() + 1899 + "." + 12);
            else
                mKeyMonth[i] = String.valueOf(month + 12);
        }
    }

    private float[] computePeriodSpread(List<TomatoReportEntity> list) {
        float[] period = new float[24];
        Arrays.fill(period, 0);
        for (TomatoReportEntity entity : list) {
            float[] temp = entity.getPeriodSpread();
            for (int i = 0; i < 24; i++) {
                period[i] += temp[i];
            }
        }
        return period;
    }

    private String computeBestPeriod(float[] period) {
        float maxValue = period[0];
        int maxIndex = 0;
        for (int i = 1; i < period.length; i++) {
            if (period[i] > maxValue) {
                maxValue = period[i];
                maxIndex = i;
            }
        }
        int left = maxIndex;
        int right = maxIndex;

        int l = maxIndex - 1;
        while (l >= 0 && period[l] > maxValue / 3) {
            left = l;
            l--;
        }
        int r = maxIndex + 1;
        while (r <= 23 && period[r] > maxValue / 3) {
            right = l;
            l++;
        }
        return String.format(TEXT_PERIOD, left, right + 1);
    }

    private float[] computeWorkday(List<TomatoReportEntity> list) {
        float[] w = new float[7];
        Arrays.fill(w, 0);
        for (TomatoReportEntity entity : list) {
            // 本周
            if (mCur.getDay() - entity.getReportType().intervalToNow(mCur) >= 0) {
                w[new Date(entity.date).getDay()] = entity.focusTime;
            }
        }
        return w;
    }
}
