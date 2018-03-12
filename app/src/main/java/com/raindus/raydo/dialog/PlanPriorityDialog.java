package com.raindus.raydo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;

import com.raindus.raydo.R;
import com.raindus.raydo.plan.entity.PlanPriority;

/**
 * Created by Raindus on 2018/3/12.
 */

public class PlanPriorityDialog extends Dialog implements View.OnClickListener {

    private PlanPriority mInitPriority;
    private OnPriorityCallback mOnPriorityCallback;

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
        findViewById(R.id.priority_rl_high).setOnClickListener(this);
        findViewById(R.id.priority_rl_middle).setOnClickListener(this);
        findViewById(R.id.priority_rl_low).setOnClickListener(this);
        findViewById(R.id.priority_rl_none).setOnClickListener(this);
        findViewById(R.id.priority_negative).setOnClickListener(this);
    }

    private void initPriority() {
        if (mInitPriority == null)
            mInitPriority = PlanPriority.getDefault();

        switch (mInitPriority) {
            case High:
                ((RadioButton) findViewById(R.id.priority_rb_high)).setChecked(true);
                break;
            case Middle:
                ((RadioButton) findViewById(R.id.priority_rb_middle)).setChecked(true);
                break;
            case Low:
                ((RadioButton) findViewById(R.id.priority_rb_low)).setChecked(true);
                break;
            case None:
                ((RadioButton) findViewById(R.id.priority_rb_none)).setChecked(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnPriorityCallback == null)
            dismiss();

        switch (v.getId()) {
            case R.id.priority_rl_high:
                mOnPriorityCallback.onCallback(PlanPriority.High);
                break;
            case R.id.priority_rl_middle:
                mOnPriorityCallback.onCallback(PlanPriority.Middle);
                break;
            case R.id.priority_rl_low:
                mOnPriorityCallback.onCallback(PlanPriority.Low);
                break;
            case R.id.priority_rl_none:
                mOnPriorityCallback.onCallback(PlanPriority.None);
                break;
            case R.id.priority_negative:
                mOnPriorityCallback.onCallback(mInitPriority);
                break;
        }
        dismiss();
    }

    public void setOnPriorityCallback(OnPriorityCallback callback) {
        mOnPriorityCallback = callback;
    }

    public interface OnPriorityCallback {
        void onCallback(PlanPriority priority);
    }
}
