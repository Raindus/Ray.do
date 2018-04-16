package com.raindus.raydo.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.raindus.raydo.R;
import com.raindus.raydo.fragment.ClockFragment;
import com.raindus.raydo.fragment.PlanFragment;
import com.raindus.raydo.fragment.UserFragment;
import com.raindus.raydo.fragment.ViewFragment;

public class MainActivity extends BaseActivity {

    //---
    private final int PERMISSION_CODE_STORAGE = 1;
    private final String[] PERMISSION_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,};
    private final int PERMISSION_CODE_LOCATION = 1 << 1;
    private final String[] PERMISSION_LOCATION = {Manifest.permission.ACCESS_COARSE_LOCATION};
    private final int PERMISSION_CODE_PHONE = 1 << 2;
    private final String[] PERMISSION_PHONE = {Manifest.permission.READ_PHONE_STATE};

    //---
    private LinearLayout mLlNavBar;
    private ImageButton mIBtnPlan;
    private ImageButton mIBtnView;
    private ImageButton mIBtnNew;
    private ImageButton mIBtnClock;
    private ImageButton mIBtnUser;

    private int mActiveIndex = R.id.main_action_plan;
    private int[] mActiveRes = {R.drawable.ic_action_plan_active, R.drawable.ic_action_view_active,
            R.drawable.ic_action_clock_active, R.drawable.ic_action_user_active};
    private int[] mInActiveRes = {R.drawable.ic_action_plan_inactive, R.drawable.ic_action_view_inactive,
            R.drawable.ic_action_clock_inactive, R.drawable.ic_action_user_inactive};

    //---
    private Fragment mFmPlan;
    private Fragment mFmView;
    private Fragment mFmClock;
    private Fragment mFmUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
    }

    private void initView() {
        mLlNavBar = findViewById(R.id.main_nav_bar);
        mIBtnPlan = findViewById(R.id.main_action_plan);
        mIBtnPlan.setOnClickListener(this);
        mIBtnView = findViewById(R.id.main_action_view);
        mIBtnView.setOnClickListener(this);
        mIBtnView.setOnLongClickListener(this);
        mIBtnNew = findViewById(R.id.main_action_new);
        mIBtnNew.setOnClickListener(this);
        mIBtnNew.setOnLongClickListener(this);
        mIBtnClock = findViewById(R.id.main_action_clock);
        mIBtnClock.setOnClickListener(this);
        mIBtnUser = findViewById(R.id.main_action_user);
        mIBtnUser.setOnClickListener(this);

        mFmPlan = new PlanFragment();
        mFmView = new ViewFragment();
        mFmClock = new ClockFragment();
        mFmUser = new UserFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.main_content, mFmPlan)
                .add(R.id.main_content, mFmView)
                .add(R.id.main_content, mFmClock)
                .add(R.id.main_content, mFmUser)
                .hide(mFmView)
                .hide(mFmClock)
                .hide(mFmUser)
                .show(mFmPlan)
                .commit();
    }

    private void switchFragment(int id) {
        switch (id) {
            case R.id.main_action_plan:
                getFragmentManager().beginTransaction().show(mFmPlan).hide(curShowFragment()).commit();
                break;
            case R.id.main_action_view:
                getFragmentManager().beginTransaction().show(mFmView).hide(curShowFragment()).commit();
                break;
            case R.id.main_action_clock:
                getFragmentManager().beginTransaction().show(mFmClock).hide(curShowFragment()).commit();
                break;
            case R.id.main_action_user:
                getFragmentManager().beginTransaction().show(mFmUser).hide(curShowFragment()).commit();
                break;
        }
    }

    private Fragment curShowFragment() {
        switch (mActiveIndex) {
            case R.id.main_action_plan:
                return mFmPlan;
            case R.id.main_action_view:
                return mFmView;
            case R.id.main_action_clock:
                return mFmClock;
            case R.id.main_action_user:
                return mFmUser;
        }
        return null;
    }

    @Override
    public void onClick(View view) {
        if (mActiveIndex == view.getId())
            return;

        if (view.getId() == R.id.main_action_new) {
            overlay(NewPlanActivity.class);
            return;
        }

        if (view.getId() == R.id.main_action_user) {//释放状态栏空间
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (mHandler.hasMessages(NAV_BAR))
                mHandler.removeMessages(NAV_BAR);
            navBarVisible(false);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        changeNavBarRes();
        switchFragment(view.getId());
        switch (view.getId()) {
            case R.id.main_action_plan:
                mIBtnPlan.setImageResource(mActiveRes[0]);
                break;
            case R.id.main_action_view:
                mIBtnView.setImageResource(mActiveRes[1]);
                break;
            case R.id.main_action_clock:
                mIBtnClock.setImageResource(mActiveRes[2]);
                break;
            case R.id.main_action_user:
                mIBtnUser.setImageResource(mActiveRes[3]);
                break;
        }
        mActiveIndex = view.getId();
    }

    private void changeNavBarRes() {
        switch (mActiveIndex) {
            case R.id.main_action_plan:
                mIBtnPlan.setImageResource(mInActiveRes[0]);
                break;
            case R.id.main_action_view:
                mIBtnView.setImageResource(mInActiveRes[1]);
                break;
            case R.id.main_action_clock:
                mIBtnClock.setImageResource(mInActiveRes[2]);
                break;
            case R.id.main_action_user:
                mIBtnUser.setImageResource(mInActiveRes[3]);
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.main_action_plan:
                break;
            case R.id.main_action_view:
                if (mActiveIndex != view.getId())
                    break;
                toast("change view");
                break;
            case R.id.main_action_new:
                toast("voice");
                break;
            case R.id.main_action_clock:
                break;
            case R.id.main_action_user:
                break;
        }
        return false;
    }

    @TargetApi(23)
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, PERMISSION_STORAGE[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION_STORAGE, PERMISSION_CODE_STORAGE);
        } else if (ContextCompat.checkSelfPermission(this, PERMISSION_LOCATION[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION_LOCATION, PERMISSION_CODE_LOCATION);
        } else if (ContextCompat.checkSelfPermission(this, PERMISSION_PHONE[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION_PHONE, PERMISSION_CODE_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        switch (requestCode) {
            case PERMISSION_CODE_STORAGE:
                ActivityCompat.requestPermissions(this, PERMISSION_LOCATION, PERMISSION_CODE_LOCATION);
                break;
            case PERMISSION_CODE_LOCATION:
                ActivityCompat.requestPermissions(this, PERMISSION_PHONE, PERMISSION_CODE_PHONE);
                break;
            case PERMISSION_CODE_PHONE:
                ((PlanFragment) mFmPlan).refreshWeather();
                break;
        }
    }

    // 导航条出现/消失动画
    private void navBarVisible(final boolean show) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                    animation.setDuration(200);
                    mLlNavBar.setVisibility(View.VISIBLE);
                    mLlNavBar.startAnimation(animation);
                } else {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
                    animation.setDuration(200);
                    mLlNavBar.setVisibility(View.INVISIBLE);
                    mLlNavBar.startAnimation(animation);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mActiveIndex == R.id.main_action_user) {
            if (mLlNavBar.getVisibility() == View.INVISIBLE) {
                navBarVisible(true);
                mHandler.sendEmptyMessageDelayed(NAV_BAR, 2000);
                return;
            }
        }
        super.onBackPressed();
    }

    private static final int NAV_BAR = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == NAV_BAR && mActiveIndex == R.id.main_action_user
                    && mLlNavBar.getVisibility() == View.VISIBLE) {
                navBarVisible(false);
            }
        }
    };

    public Context getContext() {
        return this;
    }

    // plan to update

    public void plan2UpdateView() {
        ((ViewFragment) mFmView).onRefresh();
    }

    public void view2UpdatePlan() {
        ((PlanFragment) mFmPlan).onRefresh();
    }
}
