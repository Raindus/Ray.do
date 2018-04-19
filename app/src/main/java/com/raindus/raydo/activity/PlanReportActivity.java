package com.raindus.raydo.activity;

import android.os.Bundle;
import android.view.View;

import com.raindus.raydo.R;

public class PlanReportActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_report);

        initView();
    }

    private void initView() {
        findViewById(R.id.report_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.report_back)
            finish();
    }
}
