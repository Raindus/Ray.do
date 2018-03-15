package com.raindus.raydo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.raindus.raydo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raindus on 2018/3/15.
 */

public class PlanCustomIntervalDialog extends BaseDialog {

    private List<Integer> mDays = new ArrayList<>(364);

    private Spinner mSpTimes;
    private ArrayAdapter<Integer> mAdapterDays;
    private ArrayAdapter<Integer> mAdapterWeeks;
    private ArrayAdapter<Integer> mAdapterMonths;
    private Spinner mSpType;

    private OnCustomIntervalCallback mOnCustomIntervalCallback;

    public PlanCustomIntervalDialog(@NonNull Context context) {
        super(context);

        for (int i = 1; i < 365; i++) {
            mDays.add(i);
        }
        mAdapterDays = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_item, mDays);
        mAdapterWeeks = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_dropdown_item, mDays.subList(0, 52));
        mAdapterMonths = new ArrayAdapter<Integer>(getContext(), android.R.layout.simple_spinner_dropdown_item, mDays.subList(0, 11));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plan_custom_interval);

        initView();
    }

    private void initView() {
        findViewById(R.id.custom_interval_negative).setOnClickListener(this);
        findViewById(R.id.custom_interval_positive).setOnClickListener(this);

        mSpTimes = findViewById(R.id.custom_interval_times);
        mSpType = findViewById(R.id.custom_interval_type);
        mSpType.setOnItemSelectedListener(mSpTypeListener);
    }

    private Spinner.OnItemSelectedListener mSpTypeListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0://日
                    mSpTimes.setAdapter(mAdapterDays);
                    break;
                case 1://周
                    mSpTimes.setAdapter(mAdapterWeeks);
                    break;
                case 2://月
                    mSpTimes.setAdapter(mAdapterMonths);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_interval_negative:
                break;
            case R.id.custom_interval_positive:
                if (mOnCustomIntervalCallback != null)
                    mOnCustomIntervalCallback.onCustomInterval((mSpTimes.getSelectedItemPosition() + 1),
                            mSpType.getSelectedItemPosition() + 1);
                break;
        }
        dismiss();
    }

    public void setOnCustomIntervalCallback(OnCustomIntervalCallback callback) {
        mOnCustomIntervalCallback = callback;
    }

    public interface OnCustomIntervalCallback {
        /**
         * @param times 每隔 times begin -1
         * @param type  1-天，2-周，3-月
         */
        void onCustomInterval(int times, int type);
    }
}
