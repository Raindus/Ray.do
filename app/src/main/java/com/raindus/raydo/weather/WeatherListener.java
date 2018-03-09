package com.raindus.raydo.weather;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.weather.LocalWeatherLive;

/**
 * Created by Raindus on 2018/3/8.
 */

public interface WeatherListener {

    void  onLocation(AMapLocation aMapLocation);

    void onWeatherLive(LocalWeatherLive weatherLive);
}
