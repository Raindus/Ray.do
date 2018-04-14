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


    public static final String[] MUSIC_DESCRIBE = {"Ray.do", "雨天", "森林", "海洋", "冥想", "咖啡"};

    public interface OnLayerChangerListener {
        void onLayerSelected(int position);
    }

    public interface OnLayerScrolledListener {
        void onLayerScrolled(int position, float positionOffset);
    }

    public interface OnLayerStaticListener {
        void onLayerStatic(boolean isStatic);
    }
}
