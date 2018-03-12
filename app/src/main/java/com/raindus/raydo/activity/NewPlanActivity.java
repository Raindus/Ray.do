package com.raindus.raydo.activity;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.raindus.raydo.R;

public class NewPlanActivity extends AppCompatActivity {

    private int activityCloseExitAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);

        activeExitAnimation();
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
}
