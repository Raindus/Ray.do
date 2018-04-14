package com.raindus.raydo.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.raindus.raydo.R;
import com.raindus.raydo.common.Utils;

/**
 * Created by Raindus on 2018/4/13.
 */

public class TomatoClockView extends View {

    private int mWidth;
    private int mHeight;

    // 圆心坐标
    private float cx, cy;
    // 时钟半径
    private float mClockRadius;
    // 时钟框阴影
    private Paint mShadeClockPaint;
    // 时钟框进度
    private Paint mClockPaint;
    private Path mClockPath;
    private PathMeasure mClockPathMeasure;
    // 时钟文字
    private Paint mClockTextPaint;
    private float mStartX;
    private float mBaseLine;


    private boolean mIsMusicPlay = true;

    public TomatoClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initAnim();
    }

    private void initPaint() {
        mShadeClockPaint = new Paint();
        mShadeClockPaint.setAntiAlias(true);
        mShadeClockPaint.setStyle(Paint.Style.STROKE);
        mShadeClockPaint.setStrokeWidth(8f);
        mShadeClockPaint.setColor(getResources().getColor(R.color.mid_transparent));

        mClockPaint = new Paint();
        mClockPaint.setAntiAlias(true);
        mClockPaint.setStyle(Paint.Style.STROKE);
        mClockPaint.setStrokeWidth(12f);
        mClockPaint.setColor(Color.WHITE);

        mClockTextPaint = new Paint();
        mClockTextPaint.setAntiAlias(true);
        mClockTextPaint.setTypeface(Typeface.SANS_SERIF);
        mClockTextPaint.setTextSize(Utils.dipToPx(getContext(), 48));
        mClockTextPaint.setColor(Color.WHITE);
    }

    private void initAnim() {
        mMusicPaint = new Paint();
        mMusicPaint.setAntiAlias(true);
        mMusicPaint.setStyle(Paint.Style.STROKE);
        mMusicPaint.setStrokeWidth(4f);
        mMusicPaint.setColor(getResources().getColor(R.color.mid_transparent));

        mMusicAnimator = ValueAnimator.ofFloat(0, 1);
        mMusicAnimator.setDuration(3000);
        mMusicAnimator.setInterpolator(new LinearInterpolator());
        mMusicAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mMusicAnimator.setRepeatMode(ValueAnimator.RESTART);
        mMusicAnimator.addUpdateListener(mMusicAnimatorUpdateListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        cx = (float) w / 2f;
        cy = (float) h / 2f;
        mClockRadius = Math.min(cx, cy) * (5f / 7f);

        mClockPath = new Path();
        mClockPath.addArc(cx - mClockRadius, cy - mClockRadius,
                cx + mClockRadius, cy + mClockRadius,
                -90, 360);
        mClockPathMeasure = new PathMeasure(mClockPath, true);

        mStartX = cx - Utils.dipToPx(getContext(), 56);
        mBaseLine = cy + Utils.dipToPx(getContext(), 20);

        MusicRadius = Math.min(cx, cy) * (2f / 7f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(cx, cy, mClockRadius, mShadeClockPaint);

        Path tempClockPath = new Path();
        boolean success = mClockPathMeasure.getSegment(0,
                mClockPathMeasure.getLength() * mClockFraction, tempClockPath, true);
        if (success)
            canvas.drawPath(tempClockPath, mClockPaint);

        canvas.drawText(mClockTime, mStartX, mBaseLine, mClockTextPaint);

        if (mIsMusicPlay) {
            for (int i = 0; i < MUSIC_ANIM_NUM; i++) {
                if (!mMusicShow[i] || mMusicRadius[i] <= mClockRadius + Utils.dipToPx(getContext(), 2))
                    continue;
                mMusicPaint.setAlpha(mMusicAlpha[i]);
                canvas.drawCircle(cx, cy, mMusicRadius[i], mMusicPaint);
            }
        }
    }

    // -------------------------
    // 音乐波纹动画 //

    private ValueAnimator mMusicAnimator;
    private Paint mMusicPaint;
    private final int MUSIC_ANIM_NUM = 3;
    private boolean[] mMusicShow = new boolean[MUSIC_ANIM_NUM];
    private float MusicRadius;
    private float[] mMusicRadius = new float[MUSIC_ANIM_NUM];
    private final float MusicAlpha = 255f;
    private int[] mMusicAlpha = new int[MUSIC_ANIM_NUM];

    private ValueAnimator.AnimatorUpdateListener mMusicAnimatorUpdateListener
            = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float percent = (float) animation.getAnimatedValue();

            for (int i = 0; i < MUSIC_ANIM_NUM; i++) {
                float split = ((float) i / (float) MUSIC_ANIM_NUM);
                if (!mMusicShow[i] && percent > split) {
                    mMusicShow[i] = true;
                }
                setMusicParam(percent, i);
            }
            invalidate();
        }
    };

    private void setMusicParam(float percent, int i) {
        float split = ((float) i / (float) MUSIC_ANIM_NUM);
        float pos = percent - split;
        if (pos < 0)
            pos = 1 + pos;

        mMusicAlpha[i] = (int) ((1f - pos) * MusicAlpha);
        if (mMusicAlpha[i] > MusicAlpha)
            mMusicAlpha[i] = (int) MusicAlpha;

        mMusicRadius[i] = mClockRadius + pos * MusicRadius;
    }


    public void playMusic() {
        mIsMusicPlay = true;

        for (int i = 0; i < MUSIC_ANIM_NUM; i++)
            mMusicShow[i] = false;

        mMusicAnimator.start();
    }

    public void stopMusic() {
        if (mMusicAnimator.isStarted())
            mMusicAnimator.cancel();
        mIsMusicPlay = false;
        invalidate();
    }

    // -------------------------
    // 倒计时功能
    private String mClockTime = "";
    private float mClockFraction = 0;

    // 开始倒计时
    public void startTiming(String time, int status) {
        mClockTime = time;
        mClockFraction = 0;
        invalidate();
    }

    // 计时中
    public void onTiming(String time, float fraction) {
        mClockTime = time;
        mClockFraction = fraction;
        invalidate();
    }
}
