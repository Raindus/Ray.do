package com.raindus.raydo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.raindus.raydo.R;
import com.raindus.raydo.plan.entity.PlanPriority;

/**
 * Created by Raindus on 2018/3/12.
 */

public class PlanPriorityDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout mRlHigh;
    private RelativeLayout mRlMiddle;
    private RelativeLayout mRlLow;
    private RelativeLayout mRlNone;

    private RadioButton mRbHigh;
    private RadioButton mRbMiddle;
    private RadioButton mRbLow;
    private RadioButton mRbNone;

    private Button mBtnNegative;

    private PlanPriority mInitPriority;
    private OnPriorityCallBack mOnPriorityCallBack;

    public PlanPriorityDialog(@NonNull Context context, PlanPriority priority) {
        super(context);
        mInitPriority = priority;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plan_priority);

        initView();
        initPriority();
    }

    private void initView() {
        mRlHigh = findViewById(R.id.priority_rl_high);
        mRlHigh.setOnClickListener(this);
        mRlMiddle = findViewById(R.id.priority_rl_middle);
        mRlMiddle.setOnClickListener(this);
        mRlLow = findViewById(R.id.priority_rl_low);
        mRlLow.setOnClickListener(this);
        mRlNone = findViewById(R.id.priority_rl_none);
        mRlNone.setOnClickListener(this);

        mRbHigh = findViewById(R.id.priority_rb_high);
        mRbMiddle = findViewById(R.id.priority_rb_middle);
        mRbLow = findViewById(R.id.priority_rb_low);
        mRbNone = findViewById(R.id.priority_rb_none);

        mBtnNegative = findViewById(R.id.priority_negative);
        mBtnNegative.setOnClickListener(this);
    }

    private void initPriority() {
        if (mInitPriority == null)
            mInitPriority = PlanPriority.getDefault();

        switch (mInitPriority) {
            case High:
                mRbHigh.setChecked(true);
                break;
            case Middle:
                mRbMiddle.setChecked(true);
                break;
            case Low:
                mRbLow.setChecked(true);
                break;
            case None:
                mRbNone.setChecked(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnPriorityCallBack == null)
            dismiss();

        switch (v.getId()) {
            case R.id.priority_rl_high:
                mOnPriorityCallBack.onCallBack(PlanPriority.High);
                break;
            case R.id.priority_rl_middle:
                mOnPriorityCallBack.onCallBack(PlanPriority.Middle);
                break;
            case R.id.priority_rl_low:
                mOnPriorityCallBack.onCallBack(PlanPriority.Low);
                break;
            case R.id.priority_rl_none:
                mOnPriorityCallBack.onCallBack(PlanPriority.None);
                break;
            case R.id.priority_negative:
                mOnPriorityCallBack.onCallBack(mInitPriority);
                break;
        }
        dismiss();
    }

    public void setOnPriorityCallBack(OnPriorityCallBack callBack) {
        mOnPriorityCallBack = callBack;
    }

    public interface OnPriorityCallBack {
        void onCallBack(PlanPriority priority);
    }
}
