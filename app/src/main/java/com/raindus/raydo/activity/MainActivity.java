package com.raindus.raydo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.raindus.raydo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    private ImageButton mIBtnPlan;
    private ImageButton mIBtnView;
    private ImageButton mIBtnNew;
    private ImageButton mIBtnClock;
    private ImageButton mIBtnUser;

    private int mActiveIndex = R.id.main_action_plan;
    private int[] mActiveRes = {R.drawable.ic_action_plan_active, R.drawable.ic_action_view_active,
            R.drawable.ic_action_clock_active,R.drawable.ic_action_user_active};
    private int[] mInActiveRes = {R.drawable.ic_action_plan_inactive, R.drawable.ic_action_view_inactive,
            R.drawable.ic_action_clock_inactive,R.drawable.ic_action_user_inactive};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView(){

        mIBtnPlan = findViewById(R.id.main_action_plan);
        mIBtnPlan.setOnClickListener(this);
        mIBtnView = findViewById(R.id.main_action_view);
        mIBtnView.setOnClickListener(this);
        mIBtnView.setOnLongClickListener(this);
        mIBtnNew = findViewById(R.id.main_action_new);
        mIBtnNew.setOnClickListener(this);
        mIBtnNew.setOnLongClickListener(this);
        mIBtnClock = findViewById(R.id.main_action_clock);
        mIBtnClock.setOnClickListener(this);
        mIBtnUser = findViewById(R.id.main_action_user);
        mIBtnUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mActiveIndex == view.getId())
            return;

        if (view.getId() == R.id.main_action_new){
            // Add
            return;
        }

        changeNavBarRes();
        switch (view.getId()){
            case R.id.main_action_plan:
                mIBtnPlan.setImageResource(mActiveRes[0]);
                break;
            case R.id.main_action_view:
                mIBtnView.setImageResource(mActiveRes[1]);
                break;
            case R.id.main_action_clock:
                mIBtnClock.setImageResource(mActiveRes[2]);
                break;
            case R.id.main_action_user:
                mIBtnUser.setImageResource(mActiveRes[3]);
                break;
        }
        mActiveIndex = view.getId();
    }

    private void changeNavBarRes(){
        switch (mActiveIndex){
            case R.id.main_action_plan:
                mIBtnPlan.setImageResource(mInActiveRes[0]);
                break;
            case R.id.main_action_view:
                mIBtnView.setImageResource(mInActiveRes[1]);
                break;
            case R.id.main_action_clock:
                mIBtnClock.setImageResource(mInActiveRes[2]);
                break;
            case R.id.main_action_user:
                mIBtnUser.setImageResource(mInActiveRes[3]);
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()){
            case R.id.main_action_plan:
                break;
            case R.id.main_action_view:
                if (mActiveIndex != view.getId())
                    break;
                Toast.makeText(this,"change view",Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_action_new:
                Toast.makeText(this,"yuyin",Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_action_clock:
                break;
            case R.id.main_action_user:
                break;
        }
        return false;
    }
}
