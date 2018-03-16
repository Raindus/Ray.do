package com.raindus.raydo.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.raindus.raydo.calendar.SimpleMonthView;
import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.plan.entity.PlanRemind;
import com.raindus.raydo.plan.entity.PlanRepeat;
import com.raindus.raydo.plan.entity.PlanTime;

import java.util.Date;
import java.util.List;

/**
 * Created by Raindus on 2018/3/12.
 */

public class PlanTimeDialog extends BaseDialog implements CalendarView.OnMonthChangeListener, CalendarView.OnDateSelectedListener {

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

    private ImageButton mIBtnRemind;
    private ImageButton mIBtnRepeat;

    private PlanTime mPlanTime;
    private final int mMinYear;
    private final int mMinMonth;
    private final int mMaxYear;
    private final int mMaxMonth;


    private List<Calendar> mSchemes;
    private int mYear = -1, mMonth = -1, mDay = -1, mHour = -1, mMin = -1;

    public OnPlanTimeCallback mOnPlanTimeCallback;

    public PlanTimeDialog(@NonNull Context context, PlanTime planTime) {
        super(context);

        Date TODAY = new Date();
        SimpleMonthView.setStartDate(true, TODAY.getYear() + 1900, TODAY.getMonth() + 1, TODAY.getDate());
        mMaxYear = TODAY.getYear() + 1901;
        mMaxMonth = TODAY.getMonth() + 1;

        mPlanTime = planTime == null ? new PlanTime() : planTime;
        if (mPlanTime.getStartTime() == -1) {
            mMinYear = TODAY.getYear() + 1900;
            mMinMonth = TODAY.getMonth() + 1;
        } else {
            Date temp = new Date(mPlanTime.getStartTime());
            mMinYear = temp.getYear() + 1900;
            mMinMonth = temp.getMonth() + 1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plan_time);
        initView();
    }

    private void initView() {
        mCalendarView = findViewById(R.id.time_calendar);
        mCalendarView.setRange(mMinYear, mMinMonth, mMaxYear, mMaxMonth);
        if (mPlanTime.getStartTime() == -1) {
            mCalendarView.scrollToCurrent();
        } else {
            Date temp = new Date(mPlanTime.getStartTime());
            mCalendarView.scrollToCalendar(temp.getYear() + 1900, temp.getMonth() + 1, temp.getDate());
        }
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnMonthChangeListener(this);

        findViewById(R.id.time_negative).setOnClickListener(this);
        findViewById(R.id.time_positive).setOnClickListener(this);

        mTvTitle = findViewById(R.id.time_title);
        mTvTitle.setOnClickListener(this);
        mTvTitle.setText(mCalendarView.getSelectedCalendar().getYear() + " " + mCalendarView.getSelectedCalendar().getMonth() + "月");

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

        mIBtnRemind = findViewById(R.id.time_btn_remind);
        mIBtnRemind.setOnClickListener(this);
        mIBtnRepeat = findViewById(R.id.time_btn_repeat);
        mIBtnRepeat.setOnClickListener(this);
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        mYear = calendar.getYear();
        mMonth = calendar.getMonth();
        mDay = calendar.getDay();

        if (mPlanTime.getRepeat() != PlanRepeat.NONE)
            mTvRepeat.setText(mPlanTime.getRepeat().getDescribe(new Date(mYear - 1900, mMonth - 1, mDay)));

        if (mHour == -1 && mMin == -1)
            mPlanTime.setStartTime(mYear, mMonth, mDay, 0, 0);
        else
            mPlanTime.setStartTime(mYear, mMonth, mDay, mHour, mMin);

        computerSchemes();
    }

    // TODO test
    private void computerSchemes() {
//        mSchemes = new ArrayList<>();
//        int days = DateUtils.getDateNumber(mYear, mMonth);
//        for (int i = mDay + 1; i <= days; i++) {
//            if (mPlanTime.isRepeatDay(mYear, mMonth, i))
//                mSchemes.add(getSchemeCalendar(mYear, mMonth, i));
//        }
//        mCalendarView.setSchemeDate(mSchemes);
//        mCalendarView.update();
    }

    @Override
    public void onMonthChange(int year, int month) {
        mTvTitle.setText(year + " " + month + "月");

        if (year > mCalendarView.getCurYear() || (year == mCalendarView.getCurYear() && month > mCalendarView.getCurMonth())) {
            Drawable dra = getContext().getResources().getDrawable(R.drawable.ic_action_left);
            dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
            mTvTitle.setCompoundDrawables(null, null, dra, null);
        } else {
            mTvTitle.setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.time_negative:
                SimpleMonthView.setStartDate(false, 0, 0, 0);
                dismiss();
                break;
            case R.id.time_positive:
                setPositive();
                break;
            case R.id.time_title:
                int year = mCalendarView.getCurYear();
                int month = mCalendarView.getCurMonth();
                int sYear = mCalendarView.getSelectedCalendar().getYear();
                int sMonth = mCalendarView.getSelectedCalendar().getMonth();
                if (sYear > year || (sYear == year && sMonth > month))
                    mCalendarView.scrollToCurrent();
                break;
            case R.id.time_ll_time:
                chooseTime();
                break;
            case R.id.time_ll_remind:
                planRemind();
                break;
            case R.id.time_ll_repeat:
                planRepeat();
                break;
            case R.id.time_ll_end:
                chooseEndDate();
                break;
            case R.id.time_btn_remind:
                mPlanTime.setRemind(PlanRemind.NONE);
                mIvRemind.setImageResource(R.drawable.time_remind_inactive);
                mTvRemind.setText("");
                mIBtnRemind.setVisibility(View.INVISIBLE);
                break;
            case R.id.time_btn_repeat:
                mPlanTime.setRepeat(PlanRepeat.NONE);
                mIvRepeat.setImageResource(R.drawable.time_repeat_inactive);
                mTvRepeat.setText("");
                mIBtnRepeat.setVisibility(View.INVISIBLE);
                mLlEnd.setVisibility(View.GONE);
                mTvEnd.setText("永不结束");
                mPlanTime.getRepeat().setCloseRepeatTime(-1);
                computerSchemes();
                break;
        }
    }

    private void setPositive() {
        if (mHour == -1 && mMin == -1) {
            toast("请设置时间");
            return;
        }

        if (mOnPlanTimeCallback != null)
            mOnPlanTimeCallback.onPlanTime(mPlanTime);

        SimpleMonthView.setStartDate(false, 0, 0, 0);
        dismiss();
    }

    private void chooseTime() {
        ChooseTimeDialog dialog = new ChooseTimeDialog(getContext());
        dialog.setOnChooseTimeCallback(new ChooseTimeDialog.OnChooseTimeCallback() {
            @Override
            public void onChooseTime(int hour, int min) {
                if (mHour == -1 && mMin == -1)
                    mIvTime.setImageResource(R.drawable.time_active);
                StringBuilder builder = new StringBuilder();
                if (hour > 11) {
                    builder.append("下午 ");
                    if (hour < 22)
                        builder.append("0");
                    builder.append(hour - 12).append(":");
                    if (min < 10)
                        builder.append("0");
                    builder.append(min);
                } else {
                    builder.append("上午 ");
                    if (hour < 10)
                        builder.append("0");
                    builder.append(hour).append(":");
                    if (min < 10)
                        builder.append("0");
                    builder.append(min);
                }
                mTvTime.setText(builder.toString());
                mHour = hour;
                mMin = min;
            }
        });
        dialog.show();
    }


    private void planRemind() {
        PlanRemindDialog remindDialog = new PlanRemindDialog(getContext(), mPlanTime.getRemind());
        remindDialog.setOnRemindCallback(new PlanRemindDialog.OnRemindCallback() {
            @Override
            public void onRemind(PlanRemind remind) {
                mPlanTime.setRemind(remind);
                if (remind == PlanRemind.NONE) {
                    mIvRemind.setImageResource(R.drawable.time_remind_inactive);
                    mTvRemind.setText("");
                    mIBtnRemind.setVisibility(View.INVISIBLE);
                } else {
                    mIvRemind.setImageResource(R.drawable.time_remind_active);
                    mTvRemind.setText(remind.getContent());
                    mIBtnRemind.setVisibility(View.VISIBLE);
                }
            }
        });
        remindDialog.show();
    }

    private void planRepeat() {
        final Date date = new Date(mYear - 1900, mMonth - 1, mDay);
        PlanRepeatDialog repeatDialog = new PlanRepeatDialog(getContext(), mPlanTime.getRepeat(), date);
        repeatDialog.setOnRepeatCallback(new PlanRepeatDialog.OnRepeatCallback() {
            @Override
            public void onRepeat(PlanRepeat repeat) {
                mPlanTime.setRepeat(repeat);
                if (repeat == PlanRepeat.NONE) {
                    mIvRepeat.setImageResource(R.drawable.time_repeat_inactive);
                    mTvRepeat.setText("");
                    mIBtnRepeat.setVisibility(View.INVISIBLE);
                    mLlEnd.setVisibility(View.GONE);
                } else {
                    mIvRepeat.setImageResource(R.drawable.time_repeat_active);
                    mTvRepeat.setText(repeat.getDescribe(date));
                    mIBtnRepeat.setVisibility(View.VISIBLE);
                    mLlEnd.setVisibility(View.VISIBLE);
                }
                computerSchemes();
            }
        });
        repeatDialog.show();
    }

    private void chooseEndDate() {
        ChooseDateDialog dateDialog = new ChooseDateDialog(getContext());
        dateDialog.setOnChooseDateCallback(new ChooseDateDialog.OnChooseDateCallback() {
            @Override
            public void onChooseDate(int year, int month, int day) {
                mTvEnd.setText(year + "-" + month + "-" + day + " 结束");
                mPlanTime.getRepeat().setCloseRepeatTime(new Date(year - 1900, month - 1, day).getTime());
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

    public void setOnPlanTimeCallback(OnPlanTimeCallback callback) {
        mOnPlanTimeCallback = callback;
    }

    public interface OnPlanTimeCallback {
        void onPlanTime(PlanTime planTime);
    }
}
