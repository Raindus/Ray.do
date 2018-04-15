package com.raindus.raydo.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raindus.raydo.R;
import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.plan.PlanAdapter;
import com.raindus.raydo.plan.entity.PlanEntity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Raindus on 2018/4/5.
 */

public class QueryActivity extends BaseActivity implements PlanAdapter.PlanAdapterListener {

    private static final String QUERY_NUM = "共找到%d条计划";

    private ImageButton mBtnClean;
    private EditText mEtKeyword;

    private TextView mTvNum;
    private RelativeLayout mRlEmpty;
    private RecyclerView mRvPlan;
    private PlanAdapter mPlanAdapter;
    private LinearLayoutManager mLayoutManager;

    private String[] mQuerySplit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        initView();
    }

    private void initView() {
        findViewById(R.id.query_back).setOnClickListener(this);
        mBtnClean = findViewById(R.id.query_clean);
        mBtnClean.setOnClickListener(this);

        mEtKeyword = findViewById(R.id.query_keyword);
        mEtKeyword.addTextChangedListener(mQueryWatch);

        mTvNum = findViewById(R.id.query_num);
        mRlEmpty = findViewById(R.id.query_empty);

        mRvPlan = findViewById(R.id.query_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRvPlan.setLayoutManager(mLayoutManager);
        mPlanAdapter = new PlanAdapter(this);
        mPlanAdapter.setPlanAdapterListener(this);
        mRvPlan.setAdapter(mPlanAdapter);
    }

    private TextWatcher mQueryWatch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                mPlanAdapter.setPlanData(null);
                mTvNum.setText("");
                mBtnClean.setVisibility(View.INVISIBLE);
                mRlEmpty.setVisibility(View.GONE);
                return;
            } else
                mBtnClean.setVisibility(View.VISIBLE);

            String text = s.toString().trim();
            mQuerySplit = text.split(" ");
            List<PlanEntity> list = ObjectBox.PlanEntityBox.queryKeyword(mQuerySplit);

            if (list == null)
                mPlanAdapter.setPlanData(null);
            else
                mPlanAdapter.setPlanData(Arrays.asList(list.toArray(new Object[0])));
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.query_back:
                finish();
                break;
            case R.id.query_clean:
                mEtKeyword.setText("");
                mBtnClean.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onDataChanged(int itemCount) {
        if (itemCount == 0) {
            mRlEmpty.setVisibility(View.VISIBLE);
            mTvNum.setText("");
        } else {
            mRlEmpty.setVisibility(View.GONE);
            mTvNum.setText(String.format(QUERY_NUM, itemCount));
        }
    }

    @Override
    public void onPlanDeleted() {
        toast("计划已删除");
        List<PlanEntity> list = ObjectBox.PlanEntityBox.queryKeyword(mQuerySplit);
        if (list == null)
            mPlanAdapter.setPlanData(null);
        else
            mPlanAdapter.setPlanData(Arrays.asList(list.toArray(new Object[0])));
    }
}
