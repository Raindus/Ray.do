package com.raindus.raydo.activity;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.raindus.raydo.R;
import com.raindus.raydo.dialog.PlanPriorityDialog;
import com.raindus.raydo.plan.entity.PlanPriority;

public class NewPlanActivity extends BaseActivity {

    private int activityCloseExitAnimation;

    private ImageButton mIBtnNegative;
    private ImageButton mIBtnPositive;

    private ImageButton mIBtnPriority;


    private PlanPriority mPriority = PlanPriority.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);

        initView();
        activeExitAnimation();
    }

    private void initView() {

        mIBtnNegative = findViewById(R.id.new_plan_negative);
        mIBtnNegative.setOnClickListener(this);
        mIBtnPositive = findViewById(R.id.new_plan_positive);
        mIBtnPositive.setOnClickListener(this);

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
            case R.id.new_plan_priority:
                setPlanPriority();
                break;
        }
    }

    private void setPlanPriority() {
        PlanPriorityDialog priorityDialog = new PlanPriorityDialog(this, mPriority);
        priorityDialog.setOnPriorityCallBack(new PlanPriorityDialog.OnPriorityCallBack() {
            @Override
            public void onCallBack(PlanPriority priority) {
                if (mPriority != priority) {
                    mPriority = priority;
                    mIBtnPriority.setImageResource(mPriority.getIcon());
                }
            }
        });
        priorityDialog.show();
    }
}
