package com.raindus.raydo.tomato;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raindus on 2018/4/12.
 */

public class TomatoLayerAdapter extends PagerAdapter {

    private List<View> mLayers = new ArrayList();

    public TomatoLayerAdapter(Context context) {

        for (int layer : TomatoLayer.LAYER_COLOR) {
            View view = new View(context);
            view.setBackgroundResource(layer);
            mLayers.add(view);
        }
    }

    @Override
    public int getCount() {
        return mLayers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayers.get(position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mLayers.get(position));
    }

}
