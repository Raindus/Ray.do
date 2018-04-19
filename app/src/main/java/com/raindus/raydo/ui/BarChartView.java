package com.raindus.raydo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.raindus.raydo.R;
import com.raindus.raydo.common.Utils;

import java.util.Arrays;

/**
 * Created by Raindus on 2018/4/19.
 */

public class BarChartView extends View {

    public static final int MODE_WEEK = 1; // 日-六
    public static final int MODE_TIME = 2; // 0-24

    public static final int SIZE_WEEK = 7;
    public static final int SIZE_TIME = 24;

    public static final String[] TEXT_WEEK = {"日", "一", "二", "三", "四", "五", "六"};
    // 下标就是时间
    public static final int[] TEXT_TIME = {2, 5, 8, 11, 14, 17, 20, 23};

    private int mCurMode = MODE_WEEK;

    // 横坐标定位
    private float[] mWeekLocateX = new float[SIZE_WEEK];
    private float[] mTimeLocateX = new float[SIZE_TIME];
    // 最小纵坐标 在底部
    private float mMinY = -1;
    // 最大纵坐标，在顶部
    private float mMaxY = -1;
    // 圆柱 圆角半径
    private float mBarRoundRadius;
    // 圆柱长度 /2
    private float mHalfOfWidth;

    // 灰色 底部 圆柱
    private Paint mFootPaint;
    // 底部字体
    private Paint mFootTextPaint;
    // 底部字体 基准线
    private float mFootTextBaseLine;

    // 数值百分比画笔
    private Paint mPaint;
    // 实际数值
    private float[] mWeekValue = new float[SIZE_WEEK];
    private float[] mTimeValue = new float[SIZE_TIME];
    // 百分比
    private float[] mWeekRate = new float[SIZE_WEEK];
    private float[] mTimeRate = new float[SIZE_TIME];
    // 实际坐标
    private float[] mWeekLocateY = new float[SIZE_WEEK];
    private float[] mTimeLocateY = new float[SIZE_TIME];
    // 0 则不绘制VALUE
    private boolean mYValueIsZero = true;

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BarChartView);
        mCurMode = a.getInt(R.styleable.BarChartView_mode, mCurMode);
        initPaint();
    }

    private void initPaint() {
        mBarRoundRadius = Utils.dipToPx(getContext(), 12f);

        mFootPaint = new Paint();
        mFootPaint.setAntiAlias(true);
        mFootPaint.setColor(getResources().getColor(R.color.mid_transparent));

        mFootTextPaint = new Paint();
        mFootTextPaint.setAntiAlias(true);
        mFootTextPaint.setTextAlign(Paint.Align.CENTER);
        mFootTextPaint.setColor(getResources().getColor(R.color.tomato_coffee));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.dandongshi));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        if (mCurMode == MODE_WEEK) {
            setBarChart(mWeekValue, MODE_WEEK);
        } else if (mCurMode == MODE_TIME) {
            setBarChart(mTimeValue, MODE_TIME);
        }
    }

    private void initSize() {

        mMaxY = getHeight() * 0.12f;
        mMinY = getHeight() * 0.72f;

        if (mCurMode == MODE_WEEK) {
            mHalfOfWidth = Utils.dipToPx(getContext(), 9f);
            mWeekLocateX[0] = getWidth() * 0.1f;
            mWeekLocateX[6] = getWidth() * 0.9f;
            for (int i = 1; i < SIZE_WEEK - 1; i++) {
                mWeekLocateX[i] = getWidth() * (0.1f + (0.8f / 6 * i));
            }

            mFootTextBaseLine = getHeight() * 0.86f;
        } else if (mCurMode == MODE_TIME) {
            mHalfOfWidth = Utils.dipToPx(getContext(), 3.3f);
            mTimeLocateX[0] = getWidth() * 0.05f;
            mTimeLocateX[23] = getWidth() * 0.95f;
            for (int i = 1; i < SIZE_TIME - 1; i++) {
                mTimeLocateX[i] = getWidth() * (0.05f + (0.9f / 23 * i));
            }

            mFootTextBaseLine = getHeight() * 0.84f;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mCurMode == MODE_WEEK) {
            drawWeek(canvas);
        } else if (mCurMode == MODE_TIME) {
            drawTime(canvas);
        }

    }

    private void drawWeek(Canvas canvas) {

        mFootTextPaint.setTextSize(Utils.dipToPx(getContext(), 16f));
        for (int i = 0; i < SIZE_WEEK; i++) {
            canvas.drawRoundRect(mWeekLocateX[i] - mHalfOfWidth, mMaxY,
                    mWeekLocateX[i] + mHalfOfWidth, mMinY,
                    mBarRoundRadius, mBarRoundRadius, mFootPaint);
            canvas.drawText(TEXT_WEEK[i], mWeekLocateX[i], mFootTextBaseLine, mFootTextPaint);
        }

        if (!mYValueIsZero) {
            for (int i = 0; i < SIZE_WEEK; i++) {
                canvas.drawRoundRect(mWeekLocateX[i] - mHalfOfWidth, mWeekLocateY[i],
                        mWeekLocateX[i] + mHalfOfWidth, mMinY,
                        mBarRoundRadius, mBarRoundRadius, mPaint);
            }
        }
    }

    private void drawTime(Canvas canvas) {

        for (int i = 0; i < SIZE_TIME; i++) {
            canvas.drawRoundRect(mTimeLocateX[i] - mHalfOfWidth, mMaxY,
                    mTimeLocateX[i] + mHalfOfWidth, mMinY,
                    mBarRoundRadius, mBarRoundRadius, mFootPaint);
        }
        mFootTextPaint.setTextSize(Utils.dipToPx(getContext(), 12f));
        for (int i : TEXT_TIME) {
            canvas.drawText(String.valueOf(i), mTimeLocateX[i], mFootTextBaseLine, mFootTextPaint);
        }

        if (!mYValueIsZero) {
            for (int i = 0; i < SIZE_TIME; i++) {
                canvas.drawRoundRect(mTimeLocateX[i] - mHalfOfWidth, mTimeLocateY[i],
                        mTimeLocateX[i] + mHalfOfWidth, mMinY,
                        mBarRoundRadius, mBarRoundRadius, mPaint);
            }
        }

    }

    public void setBarChart(float[] mYValue, int mode) {
        mCurMode = mode;
        initSize();

        // 寻找最大数值
        float maxValue = mYValue[0];
        float maxIndex = 0;
        for (int i = 1; i < mYValue.length; i++) {
            if (mYValue[i] > maxValue) {
                maxValue = mYValue[i];
                maxIndex = i;
            }
        }

        if (maxValue == 0 || mMaxY == -1 || mMinY == -1) {
            mYValueIsZero = true;
            Arrays.fill(mWeekRate, 0f);
            Arrays.fill(mWeekLocateY, 0f);
            Arrays.fill(mTimeRate, 0f);
            Arrays.fill(mTimeLocateY, 0f);
            invalidate();
            return;
        }

        mYValueIsZero = false;
        if (mCurMode == MODE_WEEK) {
            mWeekValue = mYValue;
            for (int i = 0; i < SIZE_WEEK; i++) {
                if (i == maxIndex) {
                    mWeekRate[i] = 1f;
                    mWeekLocateY[i] = mMaxY;
                } else {
                    mWeekRate[i] = mYValue[i] / maxValue;
                    mWeekLocateY[i] = mMinY - ((mMinY - mMaxY) * mWeekRate[i]);
                }
            }
        } else if (mCurMode == MODE_TIME) {
            mTimeValue = mYValue;
            for (int i = 0; i < SIZE_TIME; i++) {
                if (i == maxIndex) {
                    mTimeRate[i] = 1f;
                    mTimeLocateY[i] = mMaxY;
                } else {
                    mTimeRate[i] = mYValue[i] / maxValue;
                    mTimeLocateY[i] = mMinY - ((mMinY - mMaxY) * mTimeRate[i]);
                }
            }
        }
        invalidate();
    }
}
