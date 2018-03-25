package com.raindus.raydo.activity;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.dialog.PlanPriorityDialog;
import com.raindus.raydo.dialog.PlanTagDialog;
import com.raindus.raydo.dialog.PlanTimeDialog;
import com.raindus.raydo.plan.entity.PlanEntity;
import com.raindus.raydo.plan.entity.PlanPriority;
import com.raindus.raydo.plan.entity.PlanRepeat;
import com.raindus.raydo.plan.entity.PlanStatus;
import com.raindus.raydo.plan.entity.PlanTag;
import com.raindus.raydo.plan.entity.PlanTime;

import java.util.Date;

public class NewPlanActivity extends BaseActivity {

    private static final String CLOSE__REPEAT_TEXT = ",直到%1d年%2d月%3d日";
    private static final String CLOSE__REPEAT_TEXT2 = ",直到%1d月%2d日";

    private int activityCloseExitAnimation;

    private TextView mTvTime;
    private ImageButton mIBtnTag;
    private ImageButton mIBtnPriority;

    private TextView mTvTitle;
    private TextView mTvDetail;

    private PlanTag mTag = PlanTag.getDefault();
    private PlanPriority mPriority = PlanPriority.getDefault();
    private PlanTime mPlanTime;

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

        mTvTitle = findViewById(R.id.new_plan_title);
        mTvDetail = findViewById(R.id.new_plan_detail);
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
                newPlan();
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

    private void newPlan() {
        if (mPlanTime == null) {
            toast("请选择时间");
            return;
        }

        if (TextUtils.isEmpty(mTvTitle.getText())) {
            toast("请输入标题");
            return;
        }

        PlanEntity entity = new PlanEntity(
                mTvTitle.getText().toString(),
                mTvDetail.getText().toString(),
                mPriority, mTag, PlanStatus.getDefault(), mPlanTime);

        // 存入数据库
        ObjectBox.PlanEntityBox.put(getApplication(), entity);
        finish();
    }

    private void setPlanTime() {
        PlanTimeDialog timeDialog = new PlanTimeDialog(this, mPlanTime);
        timeDialog.setOnPlanTimeCallback(new PlanTimeDialog.OnPlanTimeCallback() {
            @Override
            public void onPlanTime(PlanTime planTime) {
                mPlanTime = planTime;
                StringBuilder builder = new StringBuilder();
                builder.append(DateUtils.describeOfDate(new Date(planTime.getStartTime())));
                if (planTime.getRepeat() != PlanRepeat.NONE) {
                    builder.append("<br>").append("<font color=\"#757570\">").append(
                            planTime.getRepeat().getContentDescribe(new Date(planTime.getStartTime())));
                    if (planTime.getRepeat().getCloseRepeatTime() != -1) {
                        Date close = new Date(planTime.getRepeat().getCloseRepeatTime());
                        if (close.getYear() != new Date().getYear())
                            builder.append(String.format(CLOSE__REPEAT_TEXT, close.getYear() + 1900, close.getMonth() + 1, close.getDate()));
                        else
                            builder.append(String.format(CLOSE__REPEAT_TEXT2, close.getMonth() + 1, close.getDate()));
                    }
                    builder.append("</font>");
                }
                mTvTime.setText(Html.fromHtml(builder.toString()));
            }
        });
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
