package com.raindus.raydo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TimePicker;

import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/3/13.
 */

public class ChooseTimeDialog extends BaseDialog {

    private TimePicker mTimePicker;
    private OnChooseTimeCallback mChooseTimeCallback;

    public ChooseTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_time);

        initView();
    }

    private void initView() {
        findViewById(R.id.choose_time_negative).setOnClickListener(this);
        findViewById(R.id.choose_time_positive).setOnClickListener(this);

        mTimePicker = findViewById(R.id.choose_time_picker);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_time_negative:
                break;
            case R.id.choose_time_positive:
                if (mChooseTimeCallback != null)
                    mChooseTimeCallback.onChooseTime(mTimePicker.getHour(), mTimePicker.getMinute());
                break;
        }
        dismiss();
    }

    public void setOnChooseTimeCallback(OnChooseTimeCallback callback) {
        mChooseTimeCallback = callback;
    }

    public interface OnChooseTimeCallback {
        void onChooseTime(int hour, int min);
    }
}
