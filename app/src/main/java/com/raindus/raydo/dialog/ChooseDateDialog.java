package com.raindus.raydo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.DatePicker;

import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/3/13.
 */

public class ChooseDateDialog extends BaseDialog {

    private DatePicker mDatePicker;
    private OnChooseDateCallback mOnChooseDateCallback;

    public ChooseDateDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_choose_date);
        initView();
    }

    private void initView() {
        findViewById(R.id.choose_date_negative).setOnClickListener(this);
        findViewById(R.id.choose_date_positive).setOnClickListener(this);

        mDatePicker = findViewById(R.id.choose_date_picker);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_date_negative:
                break;
            case R.id.choose_date_positive:
                if (mOnChooseDateCallback != null)
                    mOnChooseDateCallback.onChooseDate(mDatePicker.getYear(),
                            mDatePicker.getMonth()+1, mDatePicker.getDayOfMonth());
                break;
        }
        dismiss();
    }

    public void setOnChooseDateCallback(OnChooseDateCallback callback) {
        mOnChooseDateCallback = callback;
    }

    public interface OnChooseDateCallback {
        void onChooseDate(int year, int month, int day);
    }
}
