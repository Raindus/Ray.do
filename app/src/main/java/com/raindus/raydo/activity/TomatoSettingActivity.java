package com.raindus.raydo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.tomato.TomatoParam;

public class TomatoSettingActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    private int[] mParam = new int[5];

    private SeekBar mSbTarget;
    private SeekBar mSbTomatoTime;
    private SeekBar mSbShortTime;
    private SeekBar mSbLongTime;
    private SeekBar mSbLongInterval;

    private TextView mTvTarget;
    private TextView mTvTomatoTime;
    private TextView mTvShortTime;
    private TextView mTvLongTime;
    private TextView mTvLongInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_setting);

        initView();
        initData();
    }

    private void initView() {

        findViewById(R.id.setting_back).setOnClickListener(this);

        mTvTarget = findViewById(R.id.setting_tv_tomato_target);
        mTvTomatoTime = findViewById(R.id.setting_tv_tomato_time);
        mTvShortTime = findViewById(R.id.setting_tv_short_rest);
        mTvLongTime = findViewById(R.id.setting_tv_long_rest);
        mTvLongInterval = findViewById(R.id.setting_tv_long_interval);

        mSbTarget = findViewById(R.id.setting_tomato_target);
        mSbTarget.setOnSeekBarChangeListener(this);
        mSbTarget.setTag(1);
        mSbTomatoTime = findViewById(R.id.setting_tomato_time);
        mSbTomatoTime.setOnSeekBarChangeListener(this);
        mSbTomatoTime.setTag(2);
        mSbShortTime = findViewById(R.id.setting_short_rest);
        mSbShortTime.setOnSeekBarChangeListener(this);
        mSbShortTime.setTag(3);
        mSbLongTime = findViewById(R.id.setting_long_rest);
        mSbLongTime.setOnSeekBarChangeListener(this);
        mSbLongTime.setTag(4);
        mSbLongInterval = findViewById(R.id.setting_long_interval);
        mSbLongInterval.setOnSeekBarChangeListener(this);
        mSbLongInterval.setTag(5);
    }

    private void initData() {
        setData(0, TomatoParam.getTargetTime(), true);
        setData(1, TomatoParam.getTomatoTime(), true);
        setData(2, TomatoParam.getShortRestTime(), true);
        setData(3, TomatoParam.getLongRestTime(), true);
        setData(4, TomatoParam.getLongRestIntervalTimes(), true);
    }

    private void saveData() {
        TomatoParam.setTargetTime(mParam[0]);
        TomatoParam.setTomatoTime(mParam[1]);
        TomatoParam.setShortRestTime(mParam[2]);
        TomatoParam.setLongRestTime(mParam[3]);
        TomatoParam.setLongRestIntervalTimes(mParam[4]);
    }

    private void setData(int index, int progress, boolean seekBar) {
        switch (index) {
            case 0:
                mParam[0] = progress;
                mTvTarget.setText(parseTime(progress));
                if (seekBar)
                    mSbTarget.setProgress(progress);
                break;
            case 1:
                mParam[1] = progress;
                mTvTomatoTime.setText(parseTime(progress));
                if (seekBar)
                    mSbTomatoTime.setProgress(progress);
                break;
            case 2:
                mParam[2] = progress;
                mTvShortTime.setText(parseTime(progress));
                if (seekBar)
                    mSbShortTime.setProgress(progress);
                break;
            case 3:
                mParam[3] = progress;
                mTvLongTime.setText(parseTime(progress));
                if (seekBar)
                    mSbLongTime.setProgress(progress);
                break;
            case 4:
                mParam[4] = progress;
                mTvLongInterval.setText(progress + "个");
                if (seekBar)
                    mSbLongInterval.setProgress(progress);
                break;
        }
    }

    private String parseTime(int min) {
        int h = min / 60;
        int m = min % 60;
        StringBuilder builder = new StringBuilder();
        if (h > 0) {
            builder.append(h).append("时");
        }
        if (m > 0)
            builder.append(m).append("分");
        return builder.toString();
    }

    @Override
    public void onBackPressed() {
        saveData();
        super.onBackPressed();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser)
            return;
        switch ((int) seekBar.getTag()) {
            case 1:
                setData(0, progress + 30, false);
                break;
            case 2:
                setData(1, progress + 5, false);
                break;
            case 3:
                setData(2, progress + 1, false);
                break;
            case 4:
                setData(3, progress + 5, false);
                break;
            case 5:
                setData(4, progress + 1, false);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.setting_back)
            onBackPressed();
    }
}
