package com.raindus.raydo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.raindus.raydo.R;
import com.raindus.raydo.tomato.TomatoLayer;

/**
 * Created by Raindus on 2018/4/13.
 */

public class NavBarView extends View {

    private static final long STATIC_TIME = 3_000L;
    private static final int STATIC_MSG = 1;

    // 视图宽高
    private int mWidth = -1;
    private int mHeight = -1;

    // 滚动的viewPager的数目
    private int mPagerSize = TomatoLayer.LAYER_SIZE;

    private Paint mCurPaint;
    private Paint mBgPaint;

    private RectF mCurRectF;
    private RectF mBgRectF;

    private float rx = 15, ry = 15;

    private float mBgPadding = 2;
    private float mCurPadding = 8;

    private float mCurItemWidth;
    private float mCurItemOffset;

    private int mPosition = 0;
    private float mPositionOffset = 0;

    private boolean mIsStatic = false;

    public NavBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCurPaint = new Paint();
        mCurPaint.setAntiAlias(true);
        mCurPaint.setStyle(Paint.Style.FILL);
        mCurPaint.setColor(Color.WHITE);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setColor(getResources().getColor(R.color.mid_transparent));
        mBgPaint.setStrokeWidth(3f);

        mHandler.sendEmptyMessageDelayed(STATIC_MSG, STATIC_TIME);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mBgRectF = new RectF(mBgPadding, mBgPadding, (mWidth - mBgPadding), (mHeight - mBgPadding));

        mCurItemWidth = (float) mWidth / 2f - mCurPadding;
        mCurItemOffset = mCurItemWidth / (mPagerSize - 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mIsStatic)
            return;

        float left = mCurPadding + (((float) mPosition + mPositionOffset) * mCurItemOffset);
        float right = left + mCurItemWidth;
        if (right > mWidth - mCurPadding) {
            right = mWidth - mCurPadding;
        }
        mCurRectF = new RectF(left, mCurPadding, right, (mHeight - mCurPadding));

        canvas.drawRoundRect(mBgRectF, rx, ry, mBgPaint);
        canvas.drawRoundRect(mCurRectF, rx, ry, mCurPaint);
    }

    public TomatoLayer.OnLayerScrolledListener mOnLayerScrolledListener = new TomatoLayer.OnLayerScrolledListener() {
        @Override
        public void onLayerScrolled(int position, float positionOffset) {
            mPosition = position;
            mPositionOffset = positionOffset;
            invalidate();
        }
    };

    public TomatoLayer.OnLayerStaticListener mOnLayerStaticListener = new TomatoLayer.OnLayerStaticListener() {
        @Override
        public void onLayerStatic(boolean isStatic) {
            if (mHandler.hasMessages(STATIC_MSG))
                mHandler.removeMessages(STATIC_MSG);

            if (isStatic) {
                mHandler.sendEmptyMessageDelayed(STATIC_MSG, STATIC_TIME);
            } else {
                mIsStatic = false;
                invalidate();
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == STATIC_MSG) {
                mIsStatic = true;
                invalidate();
            }
        }
    };
}
