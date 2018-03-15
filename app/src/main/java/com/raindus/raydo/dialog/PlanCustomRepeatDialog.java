package com.raindus.raydo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.ui.MultiSelectView;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Raindus on 2018/3/15.
 */

public class PlanCustomRepeatDialog extends BaseDialog {

    private Spinner mSpinner;
    private MultiSelectView mMultiSelectView;
    private TextView mTvContent;

    private final int mWeek;
    private final int mMonth;

    private OnCustomRepeatCallback mOnCustomRepeatCallback;

    /**
     * @param week  begin 0 -> 6
     * @param month begin 0 -> 30
     */
    public PlanCustomRepeatDialog(@NonNull Context context, int week, int month) {
        super(context);
        mWeek = week;
        mMonth = month;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plan_custom_repeat);

        initView();
    }

    private void initView() {
        findViewById(R.id.custom_repeat_negative).setOnClickListener(this);
        findViewById(R.id.custom_repeat_positive).setOnClickListener(this);

        mMultiSelectView = findViewById(R.id.custom_repeat_select);
        mMultiSelectView.setOnItemSelectedListener(mSelectListener);
        mMultiSelectView.setMustSelected(mWeek, MultiSelectView.MODE_WEEK);
        mMultiSelectView.setMustSelected(mMonth, MultiSelectView.MODE_MONTH);
        mTvContent = findViewById(R.id.custom_repeat_content);
        changeContent(mMultiSelectView.getCurMode());
        mSpinner = findViewById(R.id.custom_repeat_spinner);
        mSpinner.setOnItemSelectedListener(mSpinnerListener);
    }

    private Spinner.OnItemSelectedListener mSpinnerListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                mMultiSelectView.switchMode(MultiSelectView.MODE_WEEK);
            } else if (position == 1) {
                mMultiSelectView.switchMode(MultiSelectView.MODE_MONTH);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private MultiSelectView.OnItemSelectedListener mSelectListener = new MultiSelectView.OnItemSelectedListener() {
        @Override
        public void switchMode(int newMode) {
            changeContent(newMode);
        }

        @Override
        public void onItemSelected(int index, int mode) {
            changeContent(mode);
        }
    };

    private void changeContent(int mode) {
        StringBuilder builder = new StringBuilder();
        Set<Integer> select = mMultiSelectView.getCurSelectedArray();
        if (select.size() == 0)
            return;

        Iterator<Integer> it = select.iterator();
        if (mode == MultiSelectView.MODE_WEEK) {
            if (select.size() == 7) {
                mTvContent.setText("每天");
                return;
            }
            builder.append("每周的");
            while (it.hasNext()) {
                builder.append("周").append(MultiSelectView.WEEK[it.next()]);
                if (it.hasNext())
                    builder.append(",");
            }
        } else if (mode == MultiSelectView.MODE_MONTH) {
            if (select.size() == 31) {
                mTvContent.setText("每天");
                return;
            }
            builder.append("每月的");
            while (it.hasNext()) {
                builder.append(it.next() + 1).append("日");
                if (it.hasNext())
                    builder.append(",");
            }
        }
        mTvContent.setText(builder.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_repeat_negative:
                break;
            case R.id.custom_repeat_positive:
                if (mOnCustomRepeatCallback != null)
                    mOnCustomRepeatCallback.onCustomRepeat(mMultiSelectView.getCurSelectedArray()
                            , mMultiSelectView.getCurMode());
                break;
        }
        dismiss();
    }

    public void setOnCustomRepeatCallback(OnCustomRepeatCallback callback) {
        mOnCustomRepeatCallback = callback;
    }

    public interface OnCustomRepeatCallback {
        void onCustomRepeat(Set<Integer> set, int type);
    }
}
