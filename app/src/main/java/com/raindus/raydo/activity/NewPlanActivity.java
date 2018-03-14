package com.raindus.raydo.activity;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.dialog.PlanPriorityDialog;
import com.raindus.raydo.dialog.PlanTagDialog;
import com.raindus.raydo.dialog.PlanTimeDialog;
import com.raindus.raydo.plan.entity.PlanPriority;
import com.raindus.raydo.plan.entity.PlanTag;

public class NewPlanActivity extends BaseActivity {

    private int activityCloseExitAnimation;

    private TextView mTvTime;
    private ImageButton mIBtnTag;
    private ImageButton mIBtnPriority;

    private PlanTag mTag = PlanTag.getDefault();
    private PlanPriority mPriority = PlanPriority.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);

        initView();
        activeExitAnimation();
    }

    private void initView() {

        findViewById(R.id.new_plan_negative).setOnClickListener(this);
        findViewById(R.id.new_plan_positive).setOnClickListener(this);

        mTvTime = findViewById(R.id.new_plan_time);
        mTvTime.setOnClickListener(this);
        mIBtnTag = findViewById(R.id.new_plan_tag);
        mIBtnTag.setOnClickListener(this);
        mIBtnPriority = findViewById(R.id.new_plan_priority);
        mIBtnPriority.setOnClickListener(this);
    }

    // 有些设备退出无效，激活退出动画
    private void activeExitAnimation() {
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseExitAnimation});
        activityCloseExitAnimation = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, activityCloseExitAnimation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_plan_negative:
                finish();
                break;
            case R.id.new_plan_positive:
                break;
            case R.id.new_plan_time:
                setPlanTime();
                break;
            case R.id.new_plan_tag:
                setPlanTag();
                break;
            case R.id.new_plan_priority:
                setPlanPriority();
                break;
        }
    }

    private void setPlanTime() {
        PlanTimeDialog timeDialog = new PlanTimeDialog(this);
        timeDialog.show();
    }

    private void setPlanTag() {
        PlanTagDialog tagDialog = new PlanTagDialog(this, mTag);
        tagDialog.setOnTagCallback(new PlanTagDialog.OnTagCallback() {
            @Override
            public void onCallback(PlanTag tag) {
                if (mTag != tag) {
                    mTag = tag;
                    mIBtnTag.setImageResource(mTag.getIcon());
                }
            }
        });
        tagDialog.show();
    }

    private void setPlanPriority() {
        PlanPriorityDialog priorityDialog = new PlanPriorityDialog(this, mPriority);
        priorityDialog.setOnPriorityCallback(new PlanPriorityDialog.OnPriorityCallback() {
            @Override
            public void onCallback(PlanPriority priority) {
                if (mPriority != priority) {
                    mPriority = priority;
                    mIBtnPriority.setImageResource(mPriority.getIcon());
                }
            }
        });
        priorityDialog.show();
    }
}
