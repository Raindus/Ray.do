package com.raindus.raydo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Raindus on 2018/3/12.
 */

public class BaseDialog extends Dialog implements View.OnClickListener {

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public void debugLog(String log) {
        Log.d("Raydo", log);
    }

    public void toast(String t) {
        Toast.makeText(getContext(), t, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }
}
