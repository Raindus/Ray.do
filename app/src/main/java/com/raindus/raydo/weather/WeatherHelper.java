package com.raindus.raydo.weather;

import android.content.Context;

import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;

/**
 * Created by Raindus on 2018/3/5.
 */

public class WeatherHelper {

    private LocalWeatherLive mWeather;
    private WeatherSearchQuery mQuery;
    private WeatherSearch mSearch;
    private WeatherListener mWeatherListener;

    private WeatherSearch.OnWeatherSearchListener mListener = new WeatherSearch.OnWeatherSearchListener() {
        @Override
        public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int i) {
            if (i== 1000) {
                if (weatherLiveResult != null&&weatherLiveResult.getLiveResult() != null) {
                    mWeather = weatherLiveResult.getLiveResult();
                }
                if (mWeatherListener != null){
                    mWeatherListener.onWeatherLive(weatherLiveResult.getLiveResult());
                }
            }
        }

        @Override
        public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

        }
    };

    public WeatherHelper(WeatherListener listener){
        this.mWeatherListener = listener;
    }

    public void activateWeather(Context context,String city){
        if (mSearch == null) {
            mQuery = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);
            mSearch = new WeatherSearch(context);
            mSearch.setOnWeatherSearchListener(mListener);
            mSearch.setQuery(mQuery);
            mSearch.searchWeatherAsyn();
        }else {
            mSearch.searchWeatherAsyn();
        }
    }

    public LocalWeatherLive getWeather(){
        return mWeather;
    }

}
