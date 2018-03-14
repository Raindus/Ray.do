package com.raindus.raydo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.raindus.raydo.R;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by Raindus on 2018/3/14.
 */

public class MultiselectView extends View implements GestureDetector.OnGestureListener {

    private static final String[] WEEK = {"日", "一", "二", "三", "四", "五", "六"};

    public static final int MODE_WEEK = 1;
    public static final int MODE_MONTH = 2;

    private boolean[] mWeekSelected = new boolean[7];
    private boolean[] mMonthSelected = new boolean[31];

    private Set<Integer> mWeekMustSelected = new ArraySet<Integer>(7);
    private Set<Integer> mMonthMustSelected = new ArraySet<Integer>(31);

    private int mMode = MODE_WEEK;

    private int mRadius;
    private float mTextBaseLine;

    private Paint mSelectedPaint;
    private Paint mSelectedTextPaint;
    private Paint mUnSelectedTextPaint;

    private int mWidth;
    private int mHeight;

    private int mDefaultItemSize;

    private int mItemWidth;
    private int mItemHeight;

    private GestureDetector mDetector;

    private OnItemSelectedListener mOnItemSelectedListener;

    public MultiselectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mDefaultItemSize = (int) (getResources().getDisplayMetrics().density * 50f);
        mDetector = new GestureDetector(context, this);
    }

    private void initPaint() {
        float scale = getResources().getDisplayMetrics().density;
        float textSize = scale * 14;

        mSelectedPaint = new Paint();
        mSelectedPaint.setAntiAlias(true);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mSelectedPaint.setColor(getResources().getColor(R.color.dandongshi));

        mSelectedTextPaint = new Paint();
        mSelectedTextPaint.setAntiAlias(true);
        mSelectedTextPaint.setStyle(Paint.Style.FILL);
        mSelectedTextPaint.setTextAlign(Paint.Align.CENTER);
        mSelectedTextPaint.setColor(Color.WHITE);
        mSelectedTextPaint.setTextSize(textSize);

        mUnSelectedTextPaint = new Paint();
        mUnSelectedTextPaint.setAntiAlias(true);
        mUnSelectedTextPaint.setStyle(Paint.Style.FILL);
        mUnSelectedTextPaint.setTextAlign(Paint.Align.CENTER);
        mUnSelectedTextPaint.setColor(Color.BLACK);
        mUnSelectedTextPaint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // wrap_content 时的默认大小
        int width = mDefaultItemSize * 7;
        int height;
        if (mMode == MODE_WEEK)
            height = mDefaultItemSize;
        else
            height = mDefaultItemSize * 5;

        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT &&
                getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(width, height);
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(width, heightSize);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, height);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mWidth = getWidth();
        mHeight = getHeight();
        initItemDimension();
    }

    private void initItemDimension() {
        mItemWidth = mWidth / 7;
        if (mMode == MODE_WEEK) {
            mItemHeight = mHeight;
        } else if (mMode == MODE_MONTH) {
            mItemHeight = mHeight / 5;
        }
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        Paint.FontMetrics metrics = mSelectedTextPaint.getFontMetrics();
        mTextBaseLine = mItemHeight / 2 - metrics.descent + (metrics.bottom - metrics.top) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mMode == MODE_WEEK) {
            for (int i = 0; i < 7; i++) {
                float cx = (float) (mItemWidth * (i + 0.5));
                float cy = (float) mItemHeight / 2f;

                if (mWeekSelected[i]) {
                    canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
                    canvas.drawText(WEEK[i], cx, mTextBaseLine, mSelectedTextPaint);
                } else {
                    canvas.drawText(WEEK[i], cx, mTextBaseLine, mUnSelectedTextPaint);
                }
            }
        } else if (mMode == MODE_MONTH) {
            for (int i = 0; i < 5; i++) {
                float cy = (float) (mItemHeight * (i + 0.5));
                float baseline = mTextBaseLine + (mItemHeight * i);

                for (int j = 0; j < 7; j++) {
                    float cx = (float) (mItemWidth * (j + 0.5));

                    int index = i * 7 + j;
                    if (mMonthSelected[index]) {
                        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
                        canvas.drawText(String.valueOf(index + 1), cx, baseline, mSelectedTextPaint);
                    } else {
                        canvas.drawText(String.valueOf(index + 1), cx, baseline, mUnSelectedTextPaint);
                    }

                    if (index == 30)
                        return;
                }
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (mMode == MODE_WEEK) {
            int index = (int) (e.getX() / mItemWidth);
            if (mWeekMustSelected.contains(index))
                return false;
            mWeekSelected[index] = !mWeekSelected[index];
            if (mOnItemSelectedListener != null)
                mOnItemSelectedListener.onItemSelected(index, MODE_WEEK);
            invalidate();
        } else if (mMode == MODE_MONTH) {
            int x = (int) (e.getX() / mItemWidth);
            int y = (int) (e.getY() / mItemHeight);
            int index = y * 7 + x;
            if (index >= 31)
                return false;

            if (mMonthMustSelected.contains(index))
                return false;
            mMonthSelected[index] = !mMonthSelected[index];
            if (mOnItemSelectedListener != null)
                mOnItemSelectedListener.onItemSelected(index, MODE_MONTH);
            invalidate();
        }
        return false;
    }

    public void switchMode(int mode) {
        if (mMode == mode)
            return;

        ViewGroup.LayoutParams lp = getLayoutParams();
        if (mode == MODE_WEEK) {
            mMode = MODE_WEEK;
            lp.height = mDefaultItemSize;
            Arrays.fill(mWeekSelected, false);
            autoSelectedMust();
            if (mOnItemSelectedListener != null)
                mOnItemSelectedListener.switchMode(MODE_WEEK);

            setLayoutParams(lp);
            invalidate();
        } else if (mode == MODE_MONTH) {
            mMode = MODE_MONTH;
            lp.height = mDefaultItemSize * 5;
            Arrays.fill(mMonthSelected, false);
            autoSelectedMust();
            if (mOnItemSelectedListener != null)
                mOnItemSelectedListener.switchMode(MODE_MONTH);

            setLayoutParams(lp);
            invalidate();
        }
    }

    private void autoSelectedMust() {
        if (mMode == MODE_WEEK) {
            for (int i : mWeekMustSelected)
                mWeekSelected[i] = true;
        } else if (mMode == MODE_MONTH) {
            for (int i : mMonthMustSelected)
                mMonthSelected[i] = true;
        }
    }

    /**
     * @param index begin 0
     */
    public void setMustSelected(int index, int mode) {
        if (mode == MODE_WEEK) {
            mWeekMustSelected.add(index);
            autoSelectedMust();
        } else if (mode == MODE_MONTH) {
            mMonthMustSelected.add(index);
            autoSelectedMust();
        }
    }

    public int getCurMode() {
        return mMode;
    }

    public boolean[] getCurSelectedArray() {
        if (mMode == MODE_WEEK)
            return mWeekSelected;
        else
            return mMonthSelected;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        mOnItemSelectedListener = listener;
    }

    public interface OnItemSelectedListener {
        void switchMode(int newMode);

        void onItemSelected(int index, int mode);
    }

    //-----
    //no use

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
