package com.raindus.raydo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/4/9.
 */

public class PlanContentDialog extends BaseDialog {

    private String mInitContent;
    private EditText mEtContent;

    private OnContentCallback mOnContentCallback;

    public PlanContentDialog(@NonNull Context context, String content) {
        super(context);
        mInitContent = content;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plan_content);

        findViewById(R.id.plan_content_negative).setOnClickListener(this);
        findViewById(R.id.plan_content_positive).setOnClickListener(this);
        mEtContent = findViewById(R.id.content_et_text);
        if (mInitContent != null)
            mEtContent.setText(mInitContent);
    }

    @Override
    public void onClick(View v) {
        if (mOnContentCallback == null) {
            dismiss();
            return;
        }

        switch (v.getId()) {
            case R.id.plan_content_negative:
                break;
            case R.id.plan_content_positive:
                mOnContentCallback.onCallback(mEtContent.getText().toString());
                break;
        }
        dismiss();
    }

    public void setOnContentCallback(OnContentCallback callback) {
        mOnContentCallback = callback;
    }

    public interface OnContentCallback {
        void onCallback(String content);
    }
}
