package com.raindus.raydo.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.activity.TomatoActivity;
import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.tomato.TomatoEntity;
import com.raindus.raydo.tomato.TomatoParam;
import com.raindus.raydo.ui.ProgressCircleView;

import java.util.List;

/**
 * Created by Raindus on 2018/3/4.
 */

public class ClockFragment extends BaseFragment {

    private ProgressCircleView mProgressView;

    private TextView mTvFocusTimes;
    private TextView mTvTomatoNum;
    private TextView mTvFocusTime;

    private Button mBtnBegin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressView = view.findViewById(R.id.clock_progress);

        mTvFocusTimes = view.findViewById(R.id.clock_tv_num);
        mTvTomatoNum = view.findViewById(R.id.clock_tv_tomato_num);
        mTvFocusTime = view.findViewById(R.id.clock_tv_time);

        mBtnBegin = view.findViewById(R.id.clock_begin_tomato);
        mBtnBegin.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        initTodayInfo();
    }

    private void initTodayInfo() {
        List<TomatoEntity> list = ObjectBox.TomatoEntityBox.queryToday();

        int focusTimes = 0;
        int tomatoNum = 0;
        int realTime = 0;

        if (list != null) {
            focusTimes = list.size();
            for (TomatoEntity entity : list) {
                tomatoNum += entity.tomatoNum;
                realTime += (entity.tomatoNum * entity.tomatoTime);
            }
        }

        mProgressView.setFraction(TomatoParam.getTargetTime(), realTime);
        mTvFocusTimes.setText(String.valueOf(focusTimes));
        mTvTomatoNum.setText(String.valueOf(tomatoNum));

        StringBuilder builder = new StringBuilder();
        int hour = realTime / 60;
        int min = realTime % 60;
        if (hour != 0) {
            builder.append(hour).append("h");
        }
        builder.append(min).append("min");
        mTvFocusTime.setText(builder.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clock_begin_tomato:
                overlayTomatoAnim();
                return;
        }
    }

    private void overlayTomatoAnim() {
        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(getActivity(), mBtnBegin, mBtnBegin.getTransitionName());
        startActivity(new Intent(getActivity(), TomatoActivity.class), options.toBundle());
    }
}
