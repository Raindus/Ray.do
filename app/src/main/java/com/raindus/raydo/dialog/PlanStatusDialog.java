package com.raindus.raydo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;

import com.raindus.raydo.R;
import com.raindus.raydo.plan.entity.PlanStatus;

/**
 * Created by Raindus on 2018/4/9.
 */

public class PlanStatusDialog extends BaseDialog {

    private PlanStatus mPlanStatus;
    private OnStatusCallback mOnStatusCallback;

    public PlanStatusDialog(@NonNull Context context, PlanStatus status) {
        super(context);
        mPlanStatus = status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plan_status);

        initView();
        initStatus();
    }

    private void initView() {
        findViewById(R.id.status_rl_completed).setOnClickListener(this);
        findViewById(R.id.status_rl_uncompleted).setOnClickListener(this);
        findViewById(R.id.status_rl_completing).setOnClickListener(this);
        findViewById(R.id.status_negative).setOnClickListener(this);
    }

    private void initStatus() {
        if (mPlanStatus == null)
            mPlanStatus = PlanStatus.getDefault();

        switch (mPlanStatus) {
            case Completed:
                ((RadioButton) findViewById(R.id.status_rb_completed)).setChecked(true);
                break;
            case Completing:
                ((RadioButton) findViewById(R.id.status_rb_completing)).setChecked(true);
                break;
            case UnCompleted:
                ((RadioButton) findViewById(R.id.status_rb_uncompleted)).setChecked(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnStatusCallback == null)
            dismiss();

        switch (v.getId()) {
            case R.id.status_negative:
                break;
            case R.id.status_rl_completed:
                mOnStatusCallback.onCallback(PlanStatus.Completed);
                break;
            case R.id.status_rl_completing:
                mOnStatusCallback.onCallback(PlanStatus.Completing);
                break;
            case R.id.status_rl_uncompleted:
                mOnStatusCallback.onCallback(PlanStatus.UnCompleted);
                break;
        }
        dismiss();
    }

    public void setOnStatusCallback(OnStatusCallback callback) {
        mOnStatusCallback = callback;
    }

    public interface OnStatusCallback {
        void onCallback(PlanStatus status);
    }
}
