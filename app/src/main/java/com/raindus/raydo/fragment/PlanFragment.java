package com.raindus.raydo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.weather.LocalWeatherLive;
import com.raindus.raydo.R;
import com.raindus.raydo.dao.ObjectBox;
import com.raindus.raydo.plan.PlanAdapter;
import com.raindus.raydo.plan.PlanSort;
import com.raindus.raydo.plan.entity.PlanEntity;
import com.raindus.raydo.ui.ShadeRelativeLayout;
import com.raindus.raydo.weather.LocationHelper;
import com.raindus.raydo.weather.WeatherHelper;
import com.raindus.raydo.weather.WeatherInfo;
import com.raindus.raydo.weather.WeatherListener;
import com.raindus.raydo.common.DateUtils;

import java.util.List;

/**
 * Created by Raindus on 2018/3/4.
 */

public class PlanFragment extends BaseFragment implements PlanAdapter.PlanAdapterListener {

    // TitleBar
    private TextView mTvDate;

    // 天气--
    private ShadeRelativeLayout mWeatherPanel;
    private ImageView mWeatherIcon;
    private TextView mWeatherTemperature;
    private TextView mWeatherType;
    private TextView mWeatherWind;
    private TextView mWeatherWeek;
    private TextView mWeatherHumidity;
    private TextView mWeatherCity;

    private LocationHelper mLocation;
    private WeatherHelper mWeather;
    private WeatherListener mWeatherListener = new WeatherListener() {
        @Override
        public void onLocation(AMapLocation aMapLocation) {
            if (aMapLocation != null)
                mWeather.activateWeather(getActivity().getApplicationContext(), aMapLocation.getAdCode());
        }

        @Override
        public void onWeatherLive(LocalWeatherLive weather) {
            if (weather != null) {
                WeatherInfo info = WeatherInfo.getWeather(weather.getWeather());
                mWeatherPanel.setColorEnd(info.getColor());
                mWeatherIcon.setImageResource(info.getIcon());
                mWeatherTemperature.setText(weather.getTemperature() + "°");
                mWeatherType.setText(weather.getWeather());
                mWeatherWind.setText(weather.getWindDirection() + "风" + weather.getWindPower() + "级");
                mWeatherWeek.setText(DateUtils.formatDay());
                mWeatherHumidity.setText("湿度" + weather.getHumidity() + "%");
                mWeatherCity.setText(weather.getCity());
            } else {
                WeatherInfo info = WeatherInfo.getWeather("default");
                mWeatherPanel.setColorEnd(info.getColor());
                mWeatherIcon.setImageResource(info.getIcon());
                mWeatherTemperature.setText("XX°");
                mWeatherType.setText("XX");
                mWeatherWind.setText("X风x级");
                mWeatherWeek.setText(DateUtils.formatDay());
                mWeatherHumidity.setText("湿度xx%");
                mWeatherCity.setText("XXX");
            }
        }
    };

    // 空任务
    private RelativeLayout mRlPlanEmpty;
    private RecyclerView mRvPlan;
    private PlanAdapter mPlanAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocation = new LocationHelper(mWeatherListener);
        mWeather = new WeatherHelper(mWeatherListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        view.findViewById(R.id.plan_search).setOnClickListener(this);
        view.findViewById(R.id.plan_more).setOnClickListener(this);
        mTvDate = view.findViewById(R.id.plan_date);
        mTvDate.setText(DateUtils.formatDate());

        mWeatherPanel = view.findViewById(R.id.weather_panel);
        mWeatherIcon = view.findViewById(R.id.weather_panel_icon);
        mWeatherTemperature = view.findViewById(R.id.weather_panel_temperature);
        mWeatherType = view.findViewById(R.id.weather_panel_type);
        mWeatherWind = view.findViewById(R.id.weather_panel_wind);
        mWeatherWeek = view.findViewById(R.id.weather_panel_week);
        mWeatherHumidity = view.findViewById(R.id.weather_panel_humidity);
        mWeatherCity = view.findViewById(R.id.weather_panel_city);

        mRlPlanEmpty = view.findViewById(R.id.plan_empty);
        mRvPlan = view.findViewById(R.id.plan_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRvPlan.setLayoutManager(mLayoutManager);
        mPlanAdapter = new PlanAdapter(getActivity());
        mPlanAdapter.setPlanAdapterListener(this);
        mRvPlan.setAdapter(mPlanAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        mLocation.activateLocation(getActivity().getApplicationContext());
        refreshPlanData();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mLocation.deactivateLocation();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plan_search:
                Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.plan_more:
                Toast.makeText(getActivity(), "more", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void refreshPlanData() {
        List<PlanEntity> list = ObjectBox.PlanEntityBox.queryAll(getActivity().getApplication(), true);
        mPlanAdapter.setPlanData(PlanSort.sortByTime(list));
    }

    @Override
    public void onDataChanged(int itemCount) {
        if (itemCount == 0)
            mRlPlanEmpty.setVisibility(View.VISIBLE);
        else
            mRlPlanEmpty.setVisibility(View.GONE);
    }

    @Override
    public void onPlanItemClick(View view, int position) {
        toast(position + "");
    }

    @Override
    public void onPlanItemLongClick(View view, int position) {
        toast(position + "");
    }
}
