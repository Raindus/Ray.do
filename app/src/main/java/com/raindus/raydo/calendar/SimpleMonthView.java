package com.raindus.raydo.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

/**
 * Created by Raindus on 2018/3/14.
 */

public class SimpleMonthView extends MonthView {

    private int mRadius;

    private static boolean isStartDate = false;
    private static int startYear;
    private static int startMonth;
    private static int startDay;

    // 在这之前的日期不可点击，且变灰
    public static void setStartDate(boolean start, int year, int month, int day) {
        isStartDate = start;
        startYear = year;
        startMonth = month;
        startDay = day;
    }

    public SimpleMonthView(Context context) {
        super(context);
    }

    @Override
    protected void onPreviewHook() {
        mSchemeTextPaint.setFakeBoldText(false);
        mCurDayTextPaint.setFakeBoldText(false);
        mCurMonthTextPaint.setFakeBoldText(false);
        mOtherMonthTextPaint.setFakeBoldText(false);
        mSchemeTextPaint.setFakeBoldText(false);

        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else if (isStartDate && calendar.getYear() <= startYear
                && calendar.getMonth() <= startMonth
                && calendar.getDay() < startDay) {

            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

    @Override
    public void onClick(View v) {
        if (isStartDate) {// 在这之前的日期不可点击
            Calendar calendar = getIndex();
            if (calendar != null) {
                if (calendar.getYear() <= startYear
                        && calendar.getMonth() <= startMonth
                        && calendar.getDay() < startDay)
                    return;
            }
        }
        super.onClick(v);
    }

}
