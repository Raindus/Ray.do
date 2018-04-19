package com.raindus.raydo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.raindus.raydo.R;
import com.raindus.raydo.common.Utils;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by Raindus on 2018/4/16.
 */

public class LineChartView extends View {

    private final DecimalFormat FloatValueFormat = new DecimalFormat(".0");
    private final DecimalFormat IntValueFormat = new DecimalFormat("");
    private boolean mIsFloat = true;

    // 折线图横坐标个数
    private final int NUMBER = 7;

    // 横坐标内容描述
    private String[] mXKey;
    // 纵坐标数值
    private float[] mYValue;
    // 0 则不绘制value折现和圆点
    private boolean mYValueIsZero = true;
    // 纵坐标比率(0-1)
    private float[] mYRate = new float[NUMBER];

    // 底部内容描述文字画笔
    private Paint mKeyPaint;
    // 数值文字画笔
    private Paint mValuePaint;
    // 底部内容描述文字画笔 基准线
    private float mKeyBaseLine;
    // 数值文字画笔 基准线
    private float mValueBaseLine;

    // 值的折线图
    private Path mValuePath;
    // 值的纵坐标
    private float[] mLocateY = new float[NUMBER];

    // 定位小圆点与连线画笔
    private Paint mPaint;
    // 定位小圆点半径
    private float mPointRadius;

    // 最小纵坐标 在底部，定位小圆点所在直线
    private float mMinY = -1;
    // 最大纵坐标，在顶部，及数值最高所在直线
    private float mMaxY = -1;

    // 各点 定位 横坐标
    private float[] mLocateX = new float[NUMBER];

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPointRadius = Utils.dipToPx(getContext(), 2.5f);
        mValueBaseLine = Utils.dipToPx(getContext(), 6f);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        // 连线宽度
        mPaint.setStrokeWidth(Utils.dipToPx(getContext(), 0.8f));
        mPaint.setColor(getResources().getColor(R.color.dandongshi));

        mKeyPaint = new Paint();
        mKeyPaint.setAntiAlias(true);
        mKeyPaint.setTextAlign(Paint.Align.CENTER);
        mKeyPaint.setTextSize(Utils.dipToPx(getContext(), 12f));
        mKeyPaint.setColor(getResources().getColor(R.color.tomato_coffee));

        mValuePaint = new Paint();
        mValuePaint.setAntiAlias(true);
        mValuePaint.setTextAlign(Paint.Align.CENTER);
        mValuePaint.setTextSize(Utils.dipToPx(getContext(), 12f));
        mValuePaint.setColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mMaxY = h * 0.2f;
        mMinY = h * 0.8f;
        mLocateX[0] = w * 0.05f;
        mLocateX[6] = w * 0.95f;
        for (int i = 1; i < NUMBER - 1; i++) {
            mLocateX[i] = w * (0.05f + (0.9f / 6 * i));
        }

        mKeyBaseLine = h * 0.92f;

        if (mXKey != null && mYValue != null)
            setLineChart(mXKey, mYValue, mIsFloat);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 横坐标
        canvas.drawLine(mLocateX[0], mMinY, mLocateX[6], mMinY, mPaint);
        // 横坐标圆点 ,底部描述
        for (int i = 0; i < NUMBER; i++) {
            canvas.drawCircle(mLocateX[i], mMinY, mPointRadius, mPaint);
            if (mXKey != null)
                canvas.drawText(mXKey[i], mLocateX[i], mKeyBaseLine, mKeyPaint);
        }

        if (!mYValueIsZero) {// 不为0 绘制值折线
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(mValuePath, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            for (int i = 0; i < NUMBER; i++) {
                canvas.drawCircle(mLocateX[i], mLocateY[i], mPointRadius, mPaint);
                canvas.drawText(mIsFloat ? FloatValueFormat.format(mYValue[i]) : IntValueFormat.format(mYValue[i]),
                        mLocateX[i], mLocateY[i] - mValueBaseLine, mValuePaint);
            }
        } else { // 不绘制折线
            for (int i = 0; i < NUMBER; i++) {
                canvas.drawText("0", mLocateX[i], mMinY - mValueBaseLine, mValuePaint);
            }
        }

    }

    // size is 7
    public void setLineChart(String[] key, float[] value, boolean isFloat) {
        mXKey = key;
        mYValue = value;
        mIsFloat = isFloat;

        float maxValue = mYValue[0];
        float maxIndex = 0;
        for (int i = 1; i < NUMBER; i++) {
            if (mYValue[i] > maxValue) {
                maxValue = mYValue[i];
                maxIndex = i;
            }
        }

        if (maxValue == 0 || mMaxY == -1 || mMinY == -1) {
            mYValueIsZero = true;
            Arrays.fill(mYRate, 0f);
            Arrays.fill(mLocateY, 0f);
            invalidate();
            return;
        }
        mYValueIsZero = false;
        // 计算折线显示纵坐标高度
        // 计算实际纵坐标 （0.1-0.8）
        mValuePath = new Path();
        for (int i = 0; i < NUMBER; i++) {
            if (i == maxIndex) {
                mYRate[i] = 1f;
                mLocateY[i] = mMaxY;
            } else {
                mYRate[i] = mYValue[i] / maxValue;
                mLocateY[i] = mMinY - ((mMinY - mMaxY) * mYRate[i]);
            }

            if (i == 0) {
                mValuePath.moveTo(mLocateX[i], mLocateY[i]);
            } else {
                mValuePath.lineTo(mLocateX[i], mLocateY[i]);
            }
        }

        invalidate();
    }
}
