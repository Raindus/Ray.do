package com.raindus.raydo.tomato;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.raindus.raydo.R;
import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.report.entity.TomatoReportEntity;

import java.util.Date;

/**
 * Created by Raindus on 2018/4/13.
 */

public class TomatoDelegate implements View.OnClickListener {

    private static final int MSG_TIMING = 1;
    private static final long TIME_TIMING = 1000L;

    public static final String[] STATUS_DESCRIBE = {"番茄钟", "短休息", "长休息"};
    public static final int STATUS_TOMATO = 0;
    public static final int STATUS_SHORT_REST = 1;
    public static final int STATUS_LONG_REST = 2;

    private int mCurStatus;
    private int mCurTotalTime;
    private int mTiming;

    private TomatoEntity mTomato;

    private Button mBtnPauseOrSkip;
    private LinearLayout mLlControlPanel;
    private Button mBtnContinue;
    private Button mBtnQuit;

    private OnTomatoListener mOnTomatoListener = mDefaultListener;

    public TomatoDelegate(Window window) {
        mBtnPauseOrSkip = window.findViewById(R.id.tomato_clock_control);
        mBtnPauseOrSkip.setOnClickListener(this);

        mLlControlPanel = window.findViewById(R.id.tomato_clock_control_layout);
        mBtnContinue = window.findViewById(R.id.tomato_clock_continue);
        mBtnContinue.setOnClickListener(this);
        mBtnQuit = window.findViewById(R.id.tomato_clock_quit);
        mBtnQuit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tomato_clock_control:
                if (mCurStatus == STATUS_TOMATO) {
                    mBtnPauseOrSkip.setVisibility(View.INVISIBLE);
                    mLlControlPanel.setVisibility(View.VISIBLE);
                    onPause();
                } else {
                    onSkipRest();
                }
                break;
            case R.id.tomato_clock_continue:
                mLlControlPanel.setVisibility(View.GONE);
                mBtnPauseOrSkip.setVisibility(View.VISIBLE);
                onContinue();
                break;
            case R.id.tomato_clock_quit:
                onQuit();
                break;
        }
    }

    private String parseTime() {
        int time = mCurTotalTime - mTiming;
        int min = time / 60;
        int sec = time % 60;
        StringBuilder builder = new StringBuilder();
        if (min < 10)
            builder.append("0");
        builder.append(min).append(":");
        if (sec < 10)
            builder.append("0");
        builder.append(sec);
        return builder.toString();
    }

    private float parseFraction() {
        return (float) mTiming / (float) mCurTotalTime;
    }

    private void nextStatus() {
        if (mCurStatus == STATUS_TOMATO) {
            mTomato.tomatoNum++;
            if (mTomato.tomatoNum % mTomato.longRestIntervalNum == 0) {
                mCurStatus = STATUS_LONG_REST;
                mCurTotalTime = mTomato.longRestTime * 60;
            } else {
                mCurStatus = STATUS_SHORT_REST;
                mCurTotalTime = mTomato.shortRestTime * 60;
            }
            mBtnPauseOrSkip.setText("跳过");
        } else {//long or short rest
            mCurStatus = STATUS_TOMATO;
            mCurTotalTime = mTomato.tomatoTime * 60;
            mBtnPauseOrSkip.setText("暂停");
        }
        mTiming = 0;
    }

    public void onStart() {
        mTomato = new TomatoEntity(TomatoParam.getTomatoTime(), TomatoParam.getShortRestTime(),
                TomatoParam.getLongRestTime(), TomatoParam.getLongRestIntervalTimes());
        mCurStatus = STATUS_TOMATO;
        mCurTotalTime = mTomato.tomatoTime * 60;
        mTiming = 0;

        mOnTomatoListener.onStart(parseTime(), mCurStatus);
        mHandler.sendEmptyMessageDelayed(MSG_TIMING, TIME_TIMING);
    }

    private void onSkipRest() {
        if (mHandler.hasMessages(MSG_TIMING))
            mHandler.removeMessages(MSG_TIMING);

        nextStatus();
        mOnTomatoListener.onStatusChanged(mCurStatus);
        mOnTomatoListener.onTiming(parseTime(), parseFraction());
        mHandler.sendEmptyMessageDelayed(MSG_TIMING, TIME_TIMING * 3);
    }

    public void onPause() {
        if (mHandler != null && mHandler.hasMessages(MSG_TIMING))
            mHandler.removeMessages(MSG_TIMING);
    }

    public void onContinue() {
        if (mTomato == null)
            return;
        if (!mHandler.hasMessages(MSG_TIMING))
            mHandler.sendEmptyMessageDelayed(MSG_TIMING, TIME_TIMING);
    }

    private void onQuit() {
        mOnTomatoListener.onQuit();
    }

    public void saveTomato() {
        // 退出计时
        mHandler.removeMessages(MSG_TIMING);
        mHandler = null;

        if (mTomato != null && mTomato.tomatoNum > 0) {
            mTomato.endTime = new Date().getTime();
            ObjectBox.TomatoEntityBox.put(mTomato);
            TomatoReportEntity.update(mTomato);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TIMING:
                    mTiming++;
                    if (mTiming > mCurTotalTime) {// 下一状态
                        nextStatus();
                        mOnTomatoListener.onStatusChanged(mCurStatus);
                        mHandler.sendEmptyMessageDelayed(MSG_TIMING, TIME_TIMING * 3);
                    } else {
                        mHandler.sendEmptyMessageDelayed(MSG_TIMING, TIME_TIMING);
                    }
                    mOnTomatoListener.onTiming(parseTime(), parseFraction());
                    break;
            }
        }
    };

    public void setOnTomatoListener(OnTomatoListener listener) {
        mOnTomatoListener = listener;
    }

    public interface OnTomatoListener {
        void onStart(String time, int status);

        void onStatusChanged(int status);

        void onTiming(String time, float fraction);

        void onQuit();
    }

    // 空实现
    private static OnTomatoListener mDefaultListener = new OnTomatoListener() {
        @Override
        public void onStart(String time, int status) {

        }

        @Override
        public void onStatusChanged(int status) {

        }

        @Override
        public void onTiming(String time, float fraction) {

        }

        @Override
        public void onQuit() {

        }
    };
}
