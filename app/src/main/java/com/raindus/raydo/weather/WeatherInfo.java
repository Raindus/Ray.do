package com.raindus.raydo.weather;

import com.raindus.raydo.R;

/**
 * Created by Raindus on 2018/3/8.
 */

public enum WeatherInfo {

    SUNNY("晴", R.drawable.w_sunny, R.color.w_sunny),
    OVERCAST("阴", R.drawable.w_overcast, R.color.w_overcast),
    CLOUDY("多云", R.drawable.w_cloudy, R.color.w_cloudy),
    LIGHT_RAIN("小雨", R.drawable.w_light_rain, R.color.w_light_rain),
    MODERATE_RAIN("中雨", R.drawable.w_moderate_rain, R.color.w_moderate_rain),
    HEAVY_RAIN("大雨", R.drawable.w_heavy_rain, R.color.w_heavy_rain),
    RAINSTORM("暴雨", R.drawable.w_rainstorm, R.color.w_rainstorms),
    RAIN_SHOWER("阵雨", R.drawable.w_rain_shower, R.color.w_moderate_rain),
    THUNDERSTORMS("雷阵雨", R.drawable.w_thunderstorms, R.color.w_thunderstorms),
    LIGHT_SNOW("小雪", R.drawable.w_light_snow, R.color.w_snow),
    MODERATE_SNOW("中雪", R.drawable.w_moderate_snow, R.color.w_snow),
    HEAVY_SNOW("大雪", R.drawable.w_heavy_snow, R.color.w_snow),
    BLIZZARD("暴雪", R.drawable.w_blizzard, R.color.w_snow),
    SLEET("雨夹雪", R.drawable.w_sleet, R.color.w_snow),
    FOG("雾", R.drawable.w_fog, R.color.w_fog),
    HAZE("霾", R.drawable.w_haze, R.color.w_fog),
    BLOWING_SAND("扬沙", R.drawable.w_blowing_sand, R.color.w_sand),
    DEFAULT("默认", R.drawable.w_default, R.color.dandongshi);

    private String mType;
    private int mIcon;
    private int mColor;

    WeatherInfo(String type, int icon, int color) {
        mType = type;
        mIcon = icon;
        mColor = color;
    }

    public int getIcon() {
        return mIcon;
    }

    public int getColor() {
        return mColor;
    }

    public static WeatherInfo getWeather(String type) {
        switch (type) {
            case "晴":
                return SUNNY;
            case "阴":
                return OVERCAST;
            case "多云":
                return CLOUDY;
            case "小雨":
                return LIGHT_RAIN;
            case "中雨":
                return MODERATE_RAIN;
            case "大雨":
                return HEAVY_RAIN;
            case "暴雨":
                return RAINSTORM;
            case "阵雨":
                return RAIN_SHOWER;
            case "雷阵雨":
                return THUNDERSTORMS;
            case "小雪":
                return LIGHT_SNOW;
            case "中雪":
                return MODERATE_SNOW;
            case "大雪":
                return HEAVY_SNOW;
            case "暴雪":
                return BLIZZARD;
            case "雨夹雪":
                return SLEET;
            case "雾":
                return FOG;
            case "霾":
                return HAZE;
            case "扬沙":
                return BLOWING_SAND;
            default:
                return DEFAULT;
        }
    }
}
