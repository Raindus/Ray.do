package com.raindus.raydo.weather;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by Raindus on 2018/3/5.
 */

public class LocationHelper {

    private AMapLocation mLastLocation;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private WeatherListener mListener;

    public LocationHelper(WeatherListener listener) {
        this.mListener = listener;
    }

    private AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            mLastLocation = aMapLocation;
            if (mListener != null) {
                mListener.onLocation(aMapLocation);
            }
        }
    };

    public void activateLocation(Context context) {
        if (mLocationClient == null) {
            // 单次高精度定位
            mLocationOption = new AMapLocationClientOption();
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(true);
            mLocationOption.setLocationCacheEnable(true);

            mLocationClient = new AMapLocationClient(context);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.setLocationListener(mLocationListener);
            mLocationClient.startLocation();
        } else {
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    public void deactivateLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
        mLocationOption = null;
    }

    public AMapLocation getLocation() {
        return mLastLocation;
    }

}
