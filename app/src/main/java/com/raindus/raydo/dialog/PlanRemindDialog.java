package com.raindus.raydo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;

import com.raindus.raydo.R;
import com.raindus.raydo.plan.entity.PlanRemind;

/**
 * Created by Raindus on 2018/3/13.
 */

public class PlanRemindDialog extends BaseDialog {

    private CheckBox mCbNone;
    private CheckBox mCbFiveMin;
    private CheckBox mCbThirdMin;
    private CheckBox mCbOneHour;
    private CheckBox mCbOneDay;
    private CheckBox mCbOneWeek;

    private PlanRemind mRemind;
    private OnRemindCallback mOnRemindCallback;

    public PlanRemindDialog(@NonNull Context context, PlanRemind remind) {
        super(context);
        mRemind = remind;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plan_remind);

        initView();
        if (mRemind == null)
            mRemind = PlanRemind.getDefault();
        checkRemind(mRemind, true);
    }

    private void initView() {
        findViewById(R.id.remind_ll_none).setOnClickListener(this);
        findViewById(R.id.remind_ll_five_min).setOnClickListener(this);
        findViewById(R.id.remind_ll_third_min).setOnClickListener(this);
        findViewById(R.id.remind_ll_one_hour).setOnClickListener(this);
        findViewById(R.id.remind_ll_one_day).setOnClickListener(this);
        findViewById(R.id.remind_ll_one_week).setOnClickListener(this);
        findViewById(R.id.remind_positive).setOnClickListener(this);

        mCbNone = findViewById(R.id.remind_cb_none);
        mCbFiveMin = findViewById(R.id.remind_cb_five_min);
        mCbThirdMin = findViewById(R.id.remind_cb_third_min);
        mCbOneHour = findViewById(R.id.remind_cb_one_hour);
        mCbOneDay = findViewById(R.id.remind_cb_one_day);
        mCbOneWeek = findViewById(R.id.remind_cb_one_week);
    }

    private void checkRemind(PlanRemind remind, boolean checked) {
        switch (remind) {
            case NONE:
                mCbNone.setChecked(checked);
                break;
            case FIVE_IN_MINUTE:
                mCbFiveMin.setChecked(checked);
                break;
            case THIRD_IN_MINUTE:
                mCbThirdMin.setChecked(checked);
                break;
            case ONE_IN_HOUR:
                mCbOneHour.setChecked(checked);
                break;
            case ONE_IN_DAY:
                mCbOneDay.setChecked(checked);
                break;
            case ONE_IN_WEEK:
                mCbOneWeek.setChecked(checked);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remind_ll_none:
                if (mRemind != PlanRemind.NONE) {
                    checkRemind(mRemind, false);
                    mRemind = PlanRemind.NONE;
                    checkRemind(PlanRemind.NONE, true);
                }
                break;
            case R.id.remind_ll_five_min:
                if (mRemind != PlanRemind.FIVE_IN_MINUTE) {
                    checkRemind(mRemind, false);
                    mRemind = PlanRemind.FIVE_IN_MINUTE;
                    checkRemind(PlanRemind.FIVE_IN_MINUTE, true);
                }
                break;
            case R.id.remind_ll_third_min:
                if (mRemind != PlanRemind.THIRD_IN_MINUTE) {
                    checkRemind(mRemind, false);
                    mRemind = PlanRemind.THIRD_IN_MINUTE;
                    checkRemind(PlanRemind.THIRD_IN_MINUTE, true);
                }
                break;
            case R.id.remind_ll_one_hour:
                if (mRemind != PlanRemind.ONE_IN_HOUR) {
                    checkRemind(mRemind, false);
                    mRemind = PlanRemind.ONE_IN_HOUR;
                    checkRemind(PlanRemind.ONE_IN_HOUR, true);
                }
                break;
            case R.id.remind_ll_one_day:
                if (mRemind != PlanRemind.ONE_IN_DAY) {
                    checkRemind(mRemind, false);
                    mRemind = PlanRemind.ONE_IN_DAY;
                    checkRemind(PlanRemind.ONE_IN_DAY, true);
                }
                break;
            case R.id.remind_ll_one_week:
                if (mRemind != PlanRemind.ONE_IN_WEEK) {
                    checkRemind(mRemind, false);
                    mRemind = PlanRemind.ONE_IN_WEEK;
                    checkRemind(PlanRemind.ONE_IN_WEEK, true);
                }
                break;
            case R.id.remind_positive:
                if (mOnRemindCallback != null)
                    mOnRemindCallback.onRemind(mRemind);
                dismiss();
                break;
        }
    }

    public void setOnRemindCallback(OnRemindCallback callback) {
        mOnRemindCallback = callback;
    }

    public interface OnRemindCallback {
        void onRemind(PlanRemind remind);
    }
}
