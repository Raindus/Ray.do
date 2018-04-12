package com.raindus.raydo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.raindus.raydo.R;
import com.raindus.raydo.tomato.TomatoLayer;

/**
 * Created by Raindus on 2018/4/12.
 */

public class ScrollRelativeLayout extends RelativeLayout {

    // 视图宽高
    private int mWidth = -1;
    private int mHeight = -1;

    // 滚动的viewPager的数目
    private int mPagerSize = TomatoLayer.LAYER_SIZE;

    // 滚动图
    private Bitmap mScrollBg;
    private int mScrollBgWidth = -1;
    private int mScrollBgHeight = -1;
    // 滚动图相比于视图的缩放比(适应高度)
    private float mScrollBgScale;
    // 图片滚动的偏移量，需要长图
    private int mScrollBgOffset = -1;
    // 适应高度后的图片宽
    private int mScrollBgScaleWidth = -1;


    // 绘制滚动图的区域
    private Rect mSrcRect;
    // 图片绘制在整个视图
    private Rect mDstRect;
    private Paint mPaint = new Paint();

    public ScrollRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScrollBackground(R.drawable.tomato_bg);
    }

    // 设置滚动图
    public void setScrollBackground(int resId) {
        if (mScrollBg != null)
            mScrollBg.recycle();

        mScrollBg = BitmapFactory.decodeResource(getResources(), resId);
        if (mScrollBg != null) {
            mScrollBgWidth = mScrollBg.getWidth();
            mScrollBgHeight = mScrollBg.getHeight();
            setScrollBgOffset();
        } else {
            mScrollBgWidth = -1;
            mScrollBgHeight = -1;
        }
    }

    private void setScrollBgOffset() {
        if (mScrollBgWidth == -1 || mScrollBgHeight == -1 || mWidth == -1 || mHeight == -1)
            return;

        mScrollBgScale = (float) mScrollBgHeight / (float) mHeight;
        mScrollBgScaleWidth = (int) ((float) mWidth * mScrollBgScale);
        mScrollBgOffset = (int) ((float) (mScrollBgWidth - mScrollBgScaleWidth) / (float) (mPagerSize - 1) + 0.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mDstRect = new Rect(0, 0, w, h);
        setScrollBgOffset();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mScrollBg, mSrcRect, mDstRect, mPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mScrollBg != null)
            mScrollBg.recycle();
    }

    /**
     * p0-p1 --> : 0-0  -> 0-100 (1-0) 属于本页
     * p1-p0 <-- : 0-100 (1-0) -> 0-0  属于上一页
     *
     * @param position       当前所在页 0-5
     * @param positionOffset 偏移百分比 0-100%
     */
    private void setSrcRect(int position, float positionOffset) {

        int left = (int) (((float) position + positionOffset) * (float) mScrollBgOffset + 0.5f);
        int right = left + mScrollBgScaleWidth;
        if (right > mScrollBgWidth) {
            right = mScrollBgWidth;
            left = mScrollBgWidth - mScrollBgScaleWidth;
        }

        mSrcRect = new Rect(left, 0, right, mScrollBgHeight);
        invalidate();
    }

    private TomatoLayer.OnLayerChangerListener mOnLayerChangerListener;

    public void setOnLayerChangerListener(TomatoLayer.OnLayerChangerListener listener) {
        mOnLayerChangerListener = listener;
    }

    // 跟随viewpager滚动
    public ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            setSrcRect(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            if (mOnLayerChangerListener != null)
                mOnLayerChangerListener.onLayerSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


}
