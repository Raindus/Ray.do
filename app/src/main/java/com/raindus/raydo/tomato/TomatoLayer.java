package com.raindus.raydo.tomato;

import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/4/12.
 */

public class TomatoLayer {

    public static final int LAYER_SIZE = 6;

    public static final int[] LAYER_COLOR = {R.color.tomato_raydo, R.color.tomato_rain,
            R.color.tomato_forest, R.color.tomato_ocean,
            R.color.tomato_meditation, R.color.tomato_coffee};

    public interface OnLayerChangerListener {
        void onLayerSelected(int position);
    }
}
