package com.raindus.raydo.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Raindus on 2018/3/12.
 */

public class BaseFragment extends Fragment implements View.OnClickListener {

    // fragment to activity
    public void overlay(Class<?> classObj) {
        Intent intent = new Intent(getActivity(), classObj);
        getActivity().startActivity(intent);
    }

    // 带数据
    public void overlay(Class<?> classObj, Bundle params) {
        Intent intent = new Intent(getActivity(), classObj);
        intent.putExtras(params);
        startActivity(intent);
    }

    public void debugLog(String log) {
        Log.d("Raydo", log);
    }

    public void toast(String t) {
        Toast.makeText(getActivity(), t, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }
}
