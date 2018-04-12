package com.raindus.raydo.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

import com.raindus.raydo.R;
import com.raindus.raydo.tomato.TomatoLayer;
import com.raindus.raydo.tomato.TomatoLayerAdapter;
import com.raindus.raydo.tomato.TomatoMusic;
import com.raindus.raydo.ui.ScrollRelativeLayout;
import com.raindus.raydo.ui.TomatoAnim;

public class TomatoActivity extends BaseActivity implements TomatoLayer.OnLayerChangerListener {

    private FrameLayout mRootView;
    private Button mBtnPoint;

    // 背景
    private ScrollRelativeLayout mContainerView;
    // 蒙层
    private ViewPager mVpMaskLayer;
    private TomatoLayerAdapter mLayerAdapter;
    // 音乐
    private TomatoMusic mTomatoMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato);

        init();
        setEnterAnimation();
        setExitAnimation();
    }

    private void init() {
        mBtnPoint = findViewById(R.id.tomato_begin);
        mRootView = findViewById(R.id.tomato_root_view);
        mContainerView = findViewById(R.id.tomato_container);
        mContainerView.setOnLayerChangerListener(this);

        mVpMaskLayer = findViewById(R.id.tomato_mask_layer);
        mLayerAdapter = new TomatoLayerAdapter(this);
        mVpMaskLayer.setAdapter(mLayerAdapter);
        mVpMaskLayer.addOnPageChangeListener(mContainerView.mOnPageChangeListener);

        mTomatoMusic = new TomatoMusic(this);
    }

    // -------
    // 蒙层切换
    @Override
    public void onLayerSelected(int position) {
        mTomatoMusic.playTomatoMusic(position);
    }

    // -----------------------------------//
    // 过渡动画 //

    // 主界面淡入淡出
    private void containerVisible(final boolean show) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                    animation.setDuration(300);
                    mContainerView.setVisibility(View.VISIBLE);
                    mContainerView.startAnimation(animation);
                    mTomatoMusic.playTomatoMusic(0);
                } else {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
                    animation.setDuration(300);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            animateRevealHide();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    mContainerView.setVisibility(View.INVISIBLE);
                    mContainerView.startAnimation(animation);
                    mTomatoMusic.onDestroy();
                }
            }
        });
    }

    // 入场动画
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setEnterAnimation() {
        Transition transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.motion_tomato);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
        getWindow().setSharedElementEnterTransition(transition);
    }

    // 扩散动画
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealShow() {
        TomatoAnim.animateRevealShow(
                this, mRootView,
                mBtnPoint.getWidth() / 2, R.color.dandongshi,
                new TomatoAnim.OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {

                    }

                    @Override
                    public void onRevealShow() {
                        containerVisible(true);
                    }
                });
    }

    // 退出动画
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setExitAnimation() {
        Fade fade = new Fade();
        fade.setDuration(300);
        getWindow().setReturnTransition(fade);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateRevealHide() {
        TomatoAnim.animateRevealHide(
                this, mRootView,
                mBtnPoint.getWidth() / 2, R.color.dandongshi,
                new TomatoAnim.OnRevealAnimationListener() {
                    @Override
                    public void onRevealHide() {
                        defaultBackPressed();
                    }

                    @Override
                    public void onRevealShow() {
                    }
                });
    }

    // 退出事件
    @Override
    public void onBackPressed() {
        containerVisible(false);
    }

    // 真正回退
    private void defaultBackPressed() {
        super.onBackPressed();
    }

    private Context getContext() {
        return this;
    }
}
