package com.raindus.raydo.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.raindus.raydo.R;
import com.raindus.raydo.activity.TomatoActivity;

/**
 * Created by Raindus on 2018/3/4.
 */

public class ClockFragment extends BaseFragment {

    private Button mBtnBegin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBtnBegin = view.findViewById(R.id.clock_begin_tomato);
        mBtnBegin.setOnClickListener(this);
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
