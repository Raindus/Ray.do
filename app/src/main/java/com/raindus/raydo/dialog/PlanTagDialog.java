package com.raindus.raydo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;

import com.raindus.raydo.R;
import com.raindus.raydo.plan.entity.PlanTag;

/**
 * Created by Raindus on 2018/3/12.
 */

public class PlanTagDialog extends BaseDialog {

    private PlanTag mInitTag;
    private OnTagCallback mOnTagCallback;

    public PlanTagDialog(@NonNull Context context, PlanTag tag) {
        super(context);
        mInitTag = tag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plan_tag);

        initView();
        initTag();
    }

    private void initView() {
        findViewById(R.id.tag_rl_work).setOnClickListener(this);
        findViewById(R.id.tag_rl_study).setOnClickListener(this);
        findViewById(R.id.tag_rl_entertainment).setOnClickListener(this);
        findViewById(R.id.tag_rl_sport).setOnClickListener(this);
        findViewById(R.id.tag_rl_life).setOnClickListener(this);
        findViewById(R.id.tag_rl_tourism).setOnClickListener(this);
        findViewById(R.id.tag_rl_shopping).setOnClickListener(this);
        findViewById(R.id.tag_rl_none).setOnClickListener(this);
        findViewById(R.id.tag_negative).setOnClickListener(this);
    }

    private void initTag() {
        if (mInitTag == null)
            mInitTag = PlanTag.getDefault();

        switch (mInitTag) {
            case Work:
                ((RadioButton) findViewById(R.id.tag_rb_work)).setChecked(true);
                break;
            case Study:
                ((RadioButton) findViewById(R.id.tag_rb_study)).setChecked(true);
                break;
            case Entertainment:
                ((RadioButton) findViewById(R.id.tag_rb_entertainment)).setChecked(true);
                break;
            case Sport:
                ((RadioButton) findViewById(R.id.tag_rb_sport)).setChecked(true);
                break;
            case Life:
                ((RadioButton) findViewById(R.id.tag_rb_life)).setChecked(true);
                break;
            case Tourism:
                ((RadioButton) findViewById(R.id.tag_rb_tourism)).setChecked(true);
                break;
            case Shopping:
                ((RadioButton) findViewById(R.id.tag_rb_shopping)).setChecked(true);
                break;
            case None:
                ((RadioButton) findViewById(R.id.tag_rb_none)).setChecked(true);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnTagCallback == null)
            dismiss();
        switch (v.getId()) {
            case R.id.tag_rl_work:
                mOnTagCallback.onCallback(PlanTag.Work);
                break;
            case R.id.tag_rl_study:
                mOnTagCallback.onCallback(PlanTag.Study);
                break;
            case R.id.tag_rl_entertainment:
                mOnTagCallback.onCallback(PlanTag.Entertainment);
                break;
            case R.id.tag_rl_sport:
                mOnTagCallback.onCallback(PlanTag.Sport);
                break;
            case R.id.tag_rl_life:
                mOnTagCallback.onCallback(PlanTag.Life);
                break;
            case R.id.tag_rl_tourism:
                mOnTagCallback.onCallback(PlanTag.Tourism);
                break;
            case R.id.tag_rl_shopping:
                mOnTagCallback.onCallback(PlanTag.Shopping);
                break;
            case R.id.tag_rl_none:
                mOnTagCallback.onCallback(PlanTag.None);
                break;
            case R.id.tag_negative:
                break;
        }
        dismiss();
    }

    public void setOnTagCallback(OnTagCallback callback) {
        mOnTagCallback = callback;
    }

    public interface OnTagCallback {
        void onCallback(PlanTag tag);
    }
}
