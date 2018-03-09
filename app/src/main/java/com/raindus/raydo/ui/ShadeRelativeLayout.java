package com.raindus.raydo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/3/9.
 */

public class ShadeRelativeLayout extends RelativeLayout {

    private int mWidth;
    private int mHeight;

    private int mColorBg = getResources().getColor(R.color.w_background);
    private int mColorStart = getResources().getColor(R.color.dandongshi);
    private int mColorEnd = getResources().getColor(R.color.w_sunny);

    private Path mPath;
    private Path mShadePath;
    private Paint mPaint;
    private Paint mShadePaint;

    public ShadeRelativeLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mWidth = getWidth();
        mHeight = getHeight();
        init();
    }

    private void init() {
        mPaint = new Paint();
        RadialGradient gradient = new RadialGradient(
                0, 0, mWidth, new int[]{mColorStart, mColorEnd}, null, Shader.TileMode.CLAMP);
        mPaint.setShader(gradient);
        mPaint.setShadowLayer(5, 3, 3, mColorBg);

        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo((int) (0.66 * mWidth), 0);
        mPath.lineTo((int) (0.73 * mWidth), mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();

        mShadePaint = new Paint();
        mShadePaint.setColor(getResources().getColor(R.color.light_transparent));

        mShadePath = new Path();
        mShadePath.moveTo(0, (int) (0.65 * mHeight));
        mShadePath.lineTo((int) (0.705 * mWidth), (int) (0.65 * mHeight));
        mShadePath.lineTo((int) (0.723 * mWidth), (int) (0.9 * mHeight));
        mShadePath.lineTo(0, (int) (0.9 * mHeight));
        mShadePath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(mColorBg);
        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(mShadePath, mShadePaint);
    }

    public void setColorEnd(int color) {
        mColorEnd = color;
        invalidate();
    }
}
