package com.raindus.raydo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.ArraySet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.common.DateUtils;
import com.raindus.raydo.plan.entity.PlanRepeat;
import com.raindus.raydo.ui.MultiSelectView;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Raindus on 2018/3/15.
 */

public class PlanRepeatDialog extends BaseDialog {

    private RadioButton mRbNone;
    private RadioButton mRbEveryDay;
    private RadioButton mRbEveryWeek;
    private RadioButton mRbEveryMonth;
    private RadioButton mRbEveryYear;
    private RadioButton mRbEveryCustom;
    private RadioButton mRbIntervalCustom;

    private TextView mTvEveryCustom;
    private TextView mTvIntervalCustom;

    private PlanRepeat mRepeat;
    private final Date mDate;

    private int mLastChecked;

    private OnRepeatCallback mOnRepeatCallback;

    public PlanRepeatDialog(@NonNull Context context, PlanRepeat repeat, Date date) {
        super(context);
        mRepeat = repeat;
        mDate = date == null ? new Date() : date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plan_repeat);

        initView();
        if (mRepeat == null)
            mRepeat = PlanRepeat.getDefault();
        mLastChecked = parseType(mRepeat);
        checkRepeat(mLastChecked, true);
    }

    private void initView() {
        findViewById(R.id.repeat_ll_none).setOnClickListener(this);
        findViewById(R.id.repeat_ll_every_day).setOnClickListener(this);
        findViewById(R.id.repeat_ll_every_week).setOnClickListener(this);
        findViewById(R.id.repeat_ll_every_month).setOnClickListener(this);
        findViewById(R.id.repeat_ll_every_year).setOnClickListener(this);
        findViewById(R.id.repeat_ll_every_custom).setOnClickListener(this);
        findViewById(R.id.repeat_ll_interval_custom).setOnClickListener(this);
        findViewById(R.id.repeat_positive).setOnClickListener(this);

        mRbNone = findViewById(R.id.repeat_rb_none);
        mRbEveryDay = findViewById(R.id.repeat_rb_every_day);
        mRbEveryWeek = findViewById(R.id.repeat_rb_every_week);
        mRbEveryMonth = findViewById(R.id.repeat_rb_every_month);
        mRbEveryYear = findViewById(R.id.repeat_rb_every_year);
        mRbEveryCustom = findViewById(R.id.repeat_rb_every_custom);
        mRbIntervalCustom = findViewById(R.id.repeat_rb_interval_custom);

        ((TextView) findViewById(R.id.repeat_tv_every_week))
                .setText("每周（" + DateUtils.getDateWeek(mDate) + "）");
        ((TextView) findViewById(R.id.repeat_tv_every_month))
                .setText("每月（" + mDate.getDate() + "日）");
        ((TextView) findViewById(R.id.repeat_tv_every_year))
                .setText("每年（" + (mDate.getMonth() + 1) + "月" + mDate.getDate() + "日）");

        mTvEveryCustom = findViewById(R.id.repeat_tv_every_custom);
        mTvIntervalCustom = findViewById(R.id.repeat_tv_interval_custom);
    }

    private int parseType(PlanRepeat repeat) {
        switch (repeat) {
            case NONE:
                return 0;
            case EVERY_DAY:
                return 1;
            case EVERY_WEEK:
                if (repeat.isOnlyOneDay())
                    return 2;
                else
                    return 6;
            case EVERY_MONTH:
                if (repeat.isOnlyOneDay())
                    return 3;
                else
                    return 6;
            case EVERY_YEAR:
                return 4;
            case EVERY_INTERVAL:
                return 5;
        }
        return -1;
    }

    private void checkRepeat(int type, boolean checked) {
        switch (type) {
            case 0:
                mRbNone.setChecked(checked);
                break;
            case 1:
                mRbEveryDay.setChecked(checked);
                break;
            case 2:// 每周仅一天
                mRbEveryWeek.setChecked(checked);
                break;
            case 3:// 每月仅一天
                mRbEveryMonth.setChecked(checked);
                break;
            case 4:
                mRbEveryYear.setChecked(checked);
                break;
            case 5:// 自定义间隔
                mRbIntervalCustom.setChecked(checked);
                if (!checked)
                    mTvIntervalCustom.setText("自定义间隔");
                else {
                    setCustomIntervalText();
                }
                break;
            case 6:// 自定义重复
                mRbEveryCustom.setChecked(checked);
                if (!checked)
                    mTvEveryCustom.setText("自定义重复");
                else
                    setCustomRepeatText();
                break;
        }
    }

    private void setCustomIntervalText() {
        String[] split = mRepeat.getContent().split("_");
        if (split != null && split[0].equals("interval") && split.length == 3) {
            int t = Integer.parseInt(split[2]);
            String s = "";
            if (t == PlanRepeat.INTERVAL_TYPE_DAY)
                s = "天";
            else if (t == PlanRepeat.INTERVAL_TYPE_WEEK)
                s = "周";
            else if (t == PlanRepeat.INTERVAL_TYPE_MONTH)
                s = "月";
            mTvIntervalCustom.setText("自定义（每隔" + split[1] + s + "）");
        }
    }

    private void setCustomRepeatText() {
        StringBuilder builder = new StringBuilder();
        Set<Integer> select = mRepeat.getSetFromContent();
        if (select.size() == 0)
            return;

        builder.append("自定义（");
        Iterator<Integer> it = select.iterator();
        if (mRepeat.getType() == 2) {
            if (select.size() == 7) {
                mTvEveryCustom.setText("自定义（每天）");
                return;
            }
            builder.append("每周的");
            while (it.hasNext()) {
                builder.append("周").append(MultiSelectView.WEEK[it.next()]);
                if (it.hasNext())
                    builder.append(",");
            }
        } else if (mRepeat.getType() == 3) {
            if (select.size() == 31) {
                mTvEveryCustom.setText("自定义（每天）");
                return;
            }
            builder.append("每月的");
            while (it.hasNext()) {
                builder.append(it.next()).append("日");
                if (it.hasNext())
                    builder.append(",");
            }
        }
        builder.append("）");
        mTvEveryCustom.setText(builder.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.repeat_positive:
                if (mOnRepeatCallback != null) {
                    switch (mRepeat) {
                        case EVERY_WEEK:
                        case EVERY_MONTH:
                            if (mRepeat.isEveryDay())
                                mRepeat = PlanRepeat.EVERY_DAY;
                            break;
                    }
                    mOnRepeatCallback.onRepeat(mRepeat);
                }
                dismiss();
                break;
            case R.id.repeat_ll_none:
                if (mLastChecked != 0) {
                    checkRepeat(mLastChecked, false);
                    mLastChecked = 0;
                    checkRepeat(mLastChecked, true);
                    mRepeat = PlanRepeat.NONE;
                }
                break;
            case R.id.repeat_ll_every_day:
                if (mLastChecked != 1) {
                    checkRepeat(mLastChecked, false);
                    mLastChecked = 1;
                    checkRepeat(mLastChecked, true);
                    mRepeat = PlanRepeat.EVERY_DAY;
                }
                break;
            case R.id.repeat_ll_every_week:
                if (mLastChecked != 2) {
                    checkRepeat(mLastChecked, false);
                    mLastChecked = 2;
                    checkRepeat(mLastChecked, true);
                    mRepeat = PlanRepeat.EVERY_WEEK;
                    Set<Integer> set = new ArraySet<Integer>();
                    set.add(mDate.getDay());
                    mRepeat.setContent(set, -1, -1);
                }
                break;
            case R.id.repeat_ll_every_month:
                if (mLastChecked != 3) {
                    checkRepeat(mLastChecked, false);
                    mLastChecked = 3;
                    checkRepeat(mLastChecked, true);
                    mRepeat = PlanRepeat.EVERY_MONTH;
                    Set<Integer> set = new ArraySet<Integer>();
                    set.add(mDate.getDate() - 1);
                    mRepeat.setContent(set, -1, -1);
                }
                break;
            case R.id.repeat_ll_every_year:
                if (mLastChecked != 4) {
                    checkRepeat(mLastChecked, false);
                    mLastChecked = 4;
                    checkRepeat(mLastChecked, true);
                    mRepeat = PlanRepeat.EVERY_YEAR;
                }
                break;
            case R.id.repeat_ll_every_custom:
                customRepeat();
                break;
            case R.id.repeat_ll_interval_custom:
                customInterval();
                break;
        }
    }

    public void customRepeat() {
        PlanCustomRepeatDialog dialog = new PlanCustomRepeatDialog(getContext(), 1, 14);
        dialog.setOnCustomRepeatCallback(new PlanCustomRepeatDialog.OnCustomRepeatCallback() {
            @Override
            public void onCustomRepeat(Set<Integer> set, int type) {
                if (type == MultiSelectView.MODE_WEEK)
                    mRepeat = PlanRepeat.EVERY_WEEK;
                else if (type == MultiSelectView.MODE_MONTH)
                    mRepeat = PlanRepeat.EVERY_MONTH;
                else
                    return;

                mRepeat.setContent(set, -1, -1);
                if (mLastChecked != 6) {
                    checkRepeat(mLastChecked, false);
                    mLastChecked = 6;
                }
                checkRepeat(mLastChecked, true);
            }
        });
        dialog.show();
    }

    public void customInterval() {
        PlanCustomIntervalDialog dialog = new PlanCustomIntervalDialog(getContext());
        dialog.setOnCustomIntervalCallback(new PlanCustomIntervalDialog.OnCustomIntervalCallback() {
            @Override
            public void onCustomInterval(int times, int type) {
                mRepeat = PlanRepeat.EVERY_INTERVAL;
                mRepeat.setContent(null, times, type);

                if (mLastChecked != 5) {
                    checkRepeat(mLastChecked, false);
                    mLastChecked = 5;
                }
                checkRepeat(mLastChecked, true);
            }
        });
        dialog.show();
    }

    public void setOnRepeatCallback(OnRepeatCallback callback) {
        mOnRepeatCallback = callback;
    }

    public interface OnRepeatCallback {
        void onRepeat(PlanRepeat repeat);
    }

}
