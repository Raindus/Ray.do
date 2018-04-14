package com.raindus.raydo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;

import com.raindus.raydo.R;
import com.raindus.raydo.common.Utils;

/**
 * Created by Raindus on 2018/4/14.
 */

public class ProgressCircleView extends View {

    // 圆心坐标
    private float cx, cy;
    // 半径
    private float mRadius;
    // 阴影
    private Paint mShadePaint;
    // 进度
    private Paint mPaint;
    private Path mPath;
    private PathMeasure mPathMeasure;

    private Paint mTextPaint;
    private Paint mCompleteTextPaint;
    private float mBaseLine;
    private float mCompleteBaseLine;

    public ProgressCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mShadePaint = new Paint();
        mShadePaint.setAntiAlias(true);
        mShadePaint.setStyle(Paint.Style.STROKE);
        mShadePaint.setStrokeWidth(16f);
        mShadePaint.setColor(getResources().getColor(R.color.mid_transparent));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(16f);
        mPaint.setColor(getResources().getColor(R.color.dandongshi));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface(Typeface.SANS_SERIF);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(Utils.dipToPx(getContext(), 16));
        mTextPaint.setColor(getResources().getColor(R.color.tomato_coffee));

        mCompleteTextPaint = new Paint();
        mCompleteTextPaint.setAntiAlias(true);
        mCompleteTextPaint.setTextAlign(Paint.Align.CENTER);
        mCompleteTextPaint.setTypeface(Typeface.SANS_SERIF);
        mCompleteTextPaint.setColor(getResources().getColor(R.color.dandongshi));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cx = (float) w / 2f;
        cy = (float) h / 2f;
        mRadius = Math.min(cx, cy) - Utils.dipToPx(getContext(), 16);

        mPath = new Path();
        mPath.addArc(cx - mRadius, cy - mRadius,
                cx + mRadius, cy + mRadius,
                -90, 360);
        mPathMeasure = new PathMeasure(mPath, true);

        mBaseLine = cy + Utils.dipToPx(getContext(), 65);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(cx, cy, mRadius, mShadePaint);

        Path tempClockPath = new Path();
        boolean success = mPathMeasure.getSegment(0,
                mPathMeasure.getLength() * mFraction, tempClockPath, true);
        if (success)
            canvas.drawPath(tempClockPath, mPaint);

        if (mIsComplete) {
            mCompleteTextPaint.setFakeBoldText(false);
            mCompleteTextPaint.setTextSize(Utils.dipToPx(getContext(), 32));
            mCompleteBaseLine = cy + Utils.dipToPx(getContext(), 15);
        } else {
            mCompleteTextPaint.setFakeBoldText(true);
            mCompleteTextPaint.setTextSize(Utils.dipToPx(getContext(), 48));
            mCompleteBaseLine = cy + Utils.dipToPx(getContext(), 15);
            canvas.drawText(mText, cx, mBaseLine, mTextPaint);
        }
        canvas.drawText(mCompleteText, cx, mCompleteBaseLine, mCompleteTextPaint);
    }

    private float mFraction = 0;
    private boolean mIsComplete = false;
    private String mCompleteText = "0";
    private String mText = "目标60分钟";
    private final String TEXT = "目标%d分钟";

    public void setFraction(int target, int real) {
        mFraction = (float) real / (float) target;
        if (mFraction >= 1) {
            mFraction = 1;
            mIsComplete = true;
            mCompleteText = "目标达成";
        } else {
            mIsComplete = false;
            mCompleteText = String.valueOf((int) ((mFraction + 0.005f) * 100)) + "%";
            mText = String.format(TEXT, target);
        }
        invalidate();
    }
}
