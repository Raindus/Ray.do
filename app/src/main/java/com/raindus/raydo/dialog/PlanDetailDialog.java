package com.raindus.raydo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.plan.entity.PlanEntity;

import java.util.Date;

/**
 * Created by Raindus on 2018/4/7.
 */

public class PlanDetailDialog extends BaseDialog {

    private final PlanEntity mPlanEntity;
    private OnPlanDeleteCallback mOnPlanDeleteCallback;

    public PlanDetailDialog(@NonNull Context context, @NonNull PlanEntity planEntity) {
        super(context, R.style.TransparentDialog);
        mPlanEntity = planEntity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plan_detail);

        initView();
    }

    private void initView() {
        findViewById(R.id.plan_detail_negative).setOnClickListener(this);
        findViewById(R.id.plan_detail_delete).setOnClickListener(this);

        ((TextView) findViewById(R.id.plan_detail_title)).setText(mPlanEntity.title);
        ((TextView) findViewById(R.id.plan_detail_content)).setMovementMethod(ScrollingMovementMethod.getInstance());
        ((TextView) findViewById(R.id.plan_detail_content)).setText(mPlanEntity.detail);

        ((TextView) findViewById(R.id.plan_detail_time))
                .setText(DateUtils.describeOfDate(new Date(mPlanEntity.getTime().getStartTime())));
        ((TextView) findViewById(R.id.plan_detail_remind))
                .setText(mPlanEntity.getTime().getRemind().getContent());
        ((TextView) findViewById(R.id.plan_detail_repeat))
                .setText(mPlanEntity.getTime().getRepeat().getContentDescribe(new Date(mPlanEntity.getTime().getStartTime())));
        ((TextView) findViewById(R.id.plan_detail_end))
                .setText(mPlanEntity.getTime().getRepeat().getCloseRepeatTimeDescribe());

        ((ImageView) findViewById(R.id.plan_detail_status_icon))
                .setImageResource(mPlanEntity.getStatus().getIcon());
        ((TextView) findViewById(R.id.plan_detail_status))
                .setText(mPlanEntity.getStatus().getContent());

        ((ImageView) findViewById(R.id.plan_detail_tag_icon))
                .setImageResource(mPlanEntity.getTag().getIcon());
        ((TextView) findViewById(R.id.plan_detail_tag))
                .setText(mPlanEntity.getTag().getContent());

        ((ImageView) findViewById(R.id.plan_detail_priority_icon))
                .setImageResource(mPlanEntity.getPriority().getIcon());
        ((TextView) findViewById(R.id.plan_detail_priority))
                .setText(mPlanEntity.getPriority().getContent());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.plan_detail_negative:
                break;
            case R.id.plan_detail_delete:
                if (mOnPlanDeleteCallback != null)
                    mOnPlanDeleteCallback.onDelete();
                break;
        }
        dismiss();
    }

    public void setOnPlanDeleteCallback(OnPlanDeleteCallback callback) {
        mOnPlanDeleteCallback = callback;
    }

    public interface OnPlanDeleteCallback {
        void onDelete();
    }
}
