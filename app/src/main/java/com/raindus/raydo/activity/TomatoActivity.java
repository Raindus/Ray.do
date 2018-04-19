package com.raindus.raydo.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.tomato.TomatoDelegate;
import com.raindus.raydo.tomato.TomatoLayer;
import com.raindus.raydo.tomato.TomatoLayerAdapter;
import com.raindus.raydo.tomato.TomatoMusic;
import com.raindus.raydo.ui.NavBarView;
import com.raindus.raydo.ui.ScrollRelativeLayout;
import com.raindus.raydo.ui.TomatoAnim;
import com.raindus.raydo.ui.TomatoClockView;

public class TomatoActivity extends BaseActivity implements TomatoLayer.OnLayerChangerListener {

    private FrameLayout mRootView;
    private Button mBtnPoint;

    // 背景
    private ScrollRelativeLayout mContainerView;
    // 蒙层
    private ViewPager mVpMaskLayer;
    private TomatoLayerAdapter mLayerAdapter;
    private int mPosition = 0;
    // 音乐
    private TomatoMusic mTomatoMusic;
    // control
    private TomatoClockView mClock;
    private NavBarView mNavBar;

    private TextView mTvTitle;

    private ImageButton mIBtnLight;
    private boolean mLightOn = false;
    private ImageButton mIBtnMusic;
    private boolean mMusicOn = true;

    private TomatoDelegate mTomatoDelegate;

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

        mClock = findViewById(R.id.tomato_clock);
        mNavBar = findViewById(R.id.tomato_nav_bar);
        mContainerView.setOnLayerScrolledListener(mNavBar.mOnLayerScrolledListener);
        mContainerView.setOnLayerStaticListener(mNavBar.mOnLayerStaticListener);

        mTvTitle = findViewById(R.id.tomato_title);

        mIBtnLight = findViewById(R.id.tomato_light);
        mIBtnLight.setOnClickListener(this);
        mIBtnMusic = findViewById(R.id.tomato_music);
        mIBtnMusic.setOnClickListener(this);

        mTomatoDelegate = new TomatoDelegate(getWindow());
        mTomatoDelegate.setOnTomatoListener(mOnTomatoListener);
    }

    // -----------------------------------//
    // 标题 - (蒙层切换 - 状态切换)//

    private void switchTitle(final String title, final int color, boolean sendMsg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                animation.setDuration(300);
                mTvTitle.setText(title);
                mTvTitle.setTextColor(color);
                mTvTitle.startAnimation(animation);
            }
        });
        if (sendMsg) {
            if (mHandler.hasMessages(MSG_RECOVER))
                mHandler.removeMessages(MSG_RECOVER);
            mHandler.sendEmptyMessageDelayed(MSG_RECOVER, RECOVER_TIME);
        }
    }

    private static final long RECOVER_TIME = 3_000L;
    private static final int MSG_RECOVER = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_RECOVER) {
                switchTitle(getText(R.string.app_name).toString(), Color.WHITE, false);
            }
        }
    };

    // -------
    // 蒙层切换
    @Override
    public void onLayerSelected(int position) {
        mPosition = position;
        mTomatoMusic.playTomatoMusic(position);

        switchTitle(TomatoLayer.MUSIC_DESCRIBE[position],
                getResources().getColor(R.color.mid_transparent), true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tomato_light:
                if (mLightOn) {
                    mIBtnLight.setColorFilter(getResources().getColor(R.color.mid_transparent));
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    mIBtnLight.setColorFilter(Color.WHITE);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    toast("屏幕已常亮");
                }
                mLightOn = !mLightOn;
                break;
            case R.id.tomato_music:
                if (mMusicOn) {
                    mIBtnMusic.setColorFilter(getResources().getColor(R.color.mid_transparent));
                    mTomatoMusic.stopTomatoMusic();
                    mClock.stopMusic();
                } else {
                    mIBtnMusic.setColorFilter(Color.WHITE);
                    mTomatoMusic.restartTomatoMusic(mPosition);
                    mClock.playMusic();
                }
                mMusicOn = !mMusicOn;
                break;
        }
    }

    // -----------------------------------//
    // 番茄钟 //


    @Override
    protected void onResume() {
        super.onResume();
        mTomatoDelegate.onContinue();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTomatoDelegate.onPause();
    }

    private TomatoDelegate.OnTomatoListener mOnTomatoListener = new TomatoDelegate.OnTomatoListener() {
        @Override
        public void onStart(String time, int status) {
            mClock.startTiming(time, status);
        }

        @Override
        public void onStatusChanged(int status) {
            mTomatoMusic.playRingtone();
            switchTitle(TomatoDelegate.STATUS_DESCRIBE[status],
                    getResources().getColor(R.color.mid_transparent), true);
        }

        @Override
        public void onTiming(String time, float fraction) {
            mClock.onTiming(time, fraction);
        }

        @Override
        public void onQuit() {
            onBackPressed();
        }
    };


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
                    // 音乐，动画准备
                    mTomatoMusic.playTomatoMusic(0);
                    mClock.playMusic();
                    mTomatoDelegate.onStart();
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
        // 保存番茄数据
        mTomatoDelegate.saveTomato();
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
