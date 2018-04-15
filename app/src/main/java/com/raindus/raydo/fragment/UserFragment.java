package com.raindus.raydo.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/3/4.
 */

public class UserFragment extends BaseFragment {

    private RelativeLayout mRlPlanReport;
    private RelativeLayout mRlFocusReport;
    private RelativeLayout mRlTomatoSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRlPlanReport = view.findViewById(R.id.user_plan_report);
        mRlPlanReport.setOnClickListener(this);
        mRlFocusReport = view.findViewById(R.id.user_focus_report);
        mRlFocusReport.setOnClickListener(this);
        mRlTomatoSetting = view.findViewById(R.id.user_tomato_setting);
        mRlTomatoSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_plan_report:
                break;
            case R.id.user_focus_report:
                break;
            case R.id.user_tomato_setting:
                break;
        }
    }
}
