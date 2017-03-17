package com.quaie.myapplication.fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.quaie.myapplication.Main_Activity;
import com.quaie.myapplication.R;
import com.quaie.myapplication.gson.Weather_Day;
import com.quaie.myapplication.gson.Weather_Hour;
import com.quaie.myapplication.utils.HttpUtil;
import com.quaie.myapplication.utils.SpUtils;
import com.quaie.myapplication.utils.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yue on 2017/3/7.
 * 　　　　　　　  ┏┓　 ┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　     ┃
 * 　　　　　　　┃　　　━　    ┃ ++ + + +
 * 　　　　　　 ████━████     ┃++  ++
 * 　　　　　　　┃　　　　　　 ┃ +
 * 　　　　　　　┃　　　┻　　　┃  +  +
 * 　　　　　　　┃　　　　　　 ┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 */

public class Frag_Weather extends Fragment implements View.OnClickListener {


    private static final int SHOW_LINECHAR = 001;
    private static final int SHOW_SUGGEST = 002;

    private LineChartView mLineChartHour;
    private TextView mWeatherWindPowerTv;
    private TextView mWeatherWindDireTv;
    private TextView mWeatherSunTv;
    private TextView mWeatherSdPowerTv;
    private TextView mWeatherUvPowerTv;
    private TextView mWeatherUvSugTv;
    private TextView mWeatherClothesPowerTv;
    private TextView mWeatherClothesTv;
    private TextView mWeatherCarPowerTv;
    private TextView mWeatherCarTv;
    private TextView mWeatherPollutantTv;
    private TextView mWeatherComfortPowerTv;
    private TextView mWeatherComfortTv;
    private Button btn_refresh;

    //风力风向
    private String mWindPower;
    private String mWindDire;
    //日出日落
    private String mSunRS;
    //湿度
    private String mSdPower;
    //紫外线
    private String mUvPower;
    private String mUvSug;
    //衣着
    private String mClothesPower;
    private String mClothesSug;
    //洗车
    private String mWashCar;
    private String mWashCarSug;
    //主要污染物
    private String mPollutant;
    //舒适度
    private String mComfortPower;
    private String mComfortSug;
    //当前温度
    private String mNewTemperature;
    //当前天气状况
    private String mNewWeather;
    //最高气温
    private String mHighTemperature;
    //最低气温
    private String mLowTemperature;
    //标志
    private String mNewIcon;
    //AQI
    private int mNewAqi;
    //日期
    private StringBuilder mNewData;
    //pm2.5
    private String mNewPM25;

    //折线X轴顶部坐标值---时间
    private List<String> mDataX;
    //折线X轴底部坐标值---天气情况
    private List<String> mWeather;
    //温度原始数据
    private List<Float> mHourTemperature;
    //温度处理过的数据
    private List<String> mHourTemperatureAfter;
    //折线数据
    private LineChartData mLineChartData;
    //折线拐点的值
    private List<PointValue> mPointValues;
    //折线X轴顶部的值
    private List<AxisValue> mAxisXValues;
    //折线X轴底部的值
    private List<AxisValue> mAxisXValuesBtm;

    private Main_Activity activity;

    private ProgressDialog mProgressDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_LINECHAR:
                    if (mProgressDialog.isShowing()) {
                        closeProgressDialog();
                    }
                    mLineChartHour.setLineChartData(mLineChartData);
                    mLineChartHour.setVisibility(View.VISIBLE);
                    break;
                case SHOW_SUGGEST:
                    if (mProgressDialog.isShowing()) {
                        closeProgressDialog();
                    }
                    mWeatherWindPowerTv.setText(mWindPower);
                    mWeatherWindDireTv.setText(mWindDire);
                    mWeatherSunTv.setText(mSunRS);
                    mWeatherSdPowerTv.setText(mSdPower);
                    mWeatherUvPowerTv.setText(mUvPower);
                    mWeatherUvSugTv.setText(mUvSug);
                    mWeatherClothesPowerTv.setText(mClothesPower);
                    mWeatherClothesTv.setText(mClothesSug);
                    mWeatherCarPowerTv.setText(mWashCar);
                    mWeatherCarTv.setText(mWashCarSug);
                    mWeatherPollutantTv.setText(mPollutant);
                    mWeatherComfortPowerTv.setText(mComfortPower);
                    mWeatherComfortTv.setText(mComfortSug);

                    activity.mTvNewTemperature.setText(mNewTemperature + "°");
                    activity.mTvAqi.setText(String.valueOf(mNewAqi));
                    activity.mTvDate.setText(mNewData.toString());
                    activity.mTvPm25.setText(mNewPM25);
                    Glide.with(activity).load(mNewIcon).into(activity.mImgNewTemperature);
                    activity.appBarExpandedListener(activity.appBarLayout,
                            SpUtils.get(getContext(), "Select_County", "").toString() + "   " + mNewWeather,
                            SpUtils.get(getContext(), "Select_County", "").toString());
                    activity.mTvHighTemperature.setText(mHighTemperature + "°");
                    activity.mTvLowTemperature.setText(mLowTemperature + "°");
                    activity.mImgAqi.setVisibility(View.VISIBLE);
                    activity.mImgPm25.setVisibility(View.VISIBLE);

                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_weather, container, false);

        initView(view);
        initData();

        activity = (Main_Activity) getActivity();

        return view;
    }

    private void initView(View view) {
        mLineChartHour = (LineChartView) view.findViewById(R.id.line_chart_hour);
        mLineChartHour.setVisibility(View.INVISIBLE);

        mWeatherWindPowerTv = (TextView) view.findViewById(R.id.weather_wind_power_tv);
        mWeatherWindDireTv = (TextView) view.findViewById(R.id.weather_wind_dire_tv);
        mWeatherSunTv = (TextView) view.findViewById(R.id.weather_sun_tv);
        mWeatherSdPowerTv = (TextView) view.findViewById(R.id.weather_sd_power_tv);
        mWeatherUvPowerTv = (TextView) view.findViewById(R.id.weather_uv_power_tv);
        mWeatherUvSugTv = (TextView) view.findViewById(R.id.weather_uv_sug_tv);
        mWeatherClothesPowerTv = (TextView) view.findViewById(R.id.weather_clothes_power_tv);
        mWeatherClothesTv = (TextView) view.findViewById(R.id.weather_clothes_tv);
        mWeatherCarPowerTv = (TextView) view.findViewById(R.id.weather_car_power_tv);
        mWeatherCarTv = (TextView) view.findViewById(R.id.weather_car_tv);
        mWeatherPollutantTv = (TextView) view.findViewById(R.id.weather_pollutant_tv);
        mWeatherComfortPowerTv = (TextView) view.findViewById(R.id.weather_comfort_power_tv);
        mWeatherComfortTv = (TextView) view.findViewById(R.id.weather_comfort_tv);
        btn_refresh = (Button) view.findViewById(R.id.btn_refresh);

        btn_refresh.setOnClickListener(this);
    }

    private void initData() {
        mDataX = new ArrayList();
        mWeather = new ArrayList<>();
        mHourTemperature = new ArrayList();
        mPointValues = new ArrayList<>();
        mAxisXValues = new ArrayList<>();
        mLineChartData = new LineChartData();
        mAxisXValuesBtm = new ArrayList<>();
        mHourTemperatureAfter = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showProgressDialog();
        requestWeatherWithHour(SpUtils.get(getContext(), "Select_County", "").toString());
        requestWeatherWithDay(SpUtils.get(getContext(), "Select_County", "").toString());
    }

    public void requestWeatherWithDay(String location) {
        String url = "http://route.showapi.com/9-2?showapi_appid=33109&showapi_sign=98e4c95cb6c34441a9f3689190fd761d&needMoreDay=0&needIndex=1&needHourData=0&need3HourForcast=0&" +
                "area=" + location;
        HttpUtil.sendOkHttpRequestWithGet(url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Weather_Day toDay = Utility.handleDayResponse(responseText);

                mWindPower = toDay.getShowapi_res_body().getNow().getWind_power();
                mWindDire = toDay.getShowapi_res_body().getNow().getWind_direction();
                mSunRS = toDay.getShowapi_res_body().getF1().getSun_begin_end();
                mSdPower = toDay.getShowapi_res_body().getNow().getSd();
                mUvPower = toDay.getShowapi_res_body().getF1().getIndex().getUv().getTitle();
                mUvSug = toDay.getShowapi_res_body().getF1().getIndex().getUv().getDesc();
                mClothesPower = toDay.getShowapi_res_body().getF1().getIndex().getClothes().getTitle();
                mClothesSug = toDay.getShowapi_res_body().getF1().getIndex().getClothes().getDesc();
                mWashCar = toDay.getShowapi_res_body().getF1().getIndex().getWash_car().getTitle();
                mWashCarSug = toDay.getShowapi_res_body().getF1().getIndex().getWash_car().getDesc();

                mPollutant = toDay.getShowapi_res_body().getNow().getAqiDetail() == null
                        || toDay.getShowapi_res_body().getNow().getAqiDetail().getPrimary_pollutant() == ""
                        ? "暂无"
                        : toDay.getShowapi_res_body().getNow().getAqiDetail().getPrimary_pollutant();
                mComfortPower = toDay.getShowapi_res_body().getF1().getIndex().getComfort().getTitle();
                mComfortSug = toDay.getShowapi_res_body().getF1().getIndex().getComfort().getDesc();

                mNewTemperature = toDay.getShowapi_res_body().getNow().getTemperature();
                mNewWeather = toDay.getShowapi_res_body().getNow().getWeather();
                mNewIcon = toDay.getShowapi_res_body().getNow().getWeather_pic();
                mNewAqi = toDay.getShowapi_res_body().getNow().getAqi();
                mNewData = new StringBuilder();
                mNewData.append("更新时间: ")
                        .append(toDay.getShowapi_res_body().getTime().toString().substring(4, 6))
                        .append("月")
                        .append(toDay.getShowapi_res_body().getTime().toString().substring(6, 8))
                        .append("日")
                        .append("  ")
                        .append(toDay.getShowapi_res_body().getNow().getTemperature_time());
                mNewPM25 = toDay.getShowapi_res_body().getNow().getAqiDetail() == null
                        || String.valueOf(toDay.getShowapi_res_body().getNow().getAqiDetail().getPm2_5()) == ""
                        ? "暂无"
                        : String.valueOf(toDay.getShowapi_res_body().getNow().getAqiDetail().getPm2_5());
                mHighTemperature = toDay.getShowapi_res_body().getF1().getDay_air_temperature();
                mLowTemperature = toDay.getShowapi_res_body().getF1().getNight_air_temperature();

                mHandler.sendEmptyMessageDelayed(SHOW_SUGGEST, 1000);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getString(R.string.Load_Fail), Toast.LENGTH_SHORT).show();
                        if (mProgressDialog.isShowing()) {
                            closeProgressDialog();
                        }
                    }
                });
            }
        });
    }

    public void requestWeatherWithHour(String location) {
        String url = "http://route.showapi.com/9-8?showapi_appid=33109&showapi_sign=98e4c95cb6c34441a9f3689190fd761d&" +
                "area=" + location;
        HttpUtil.sendOkHttpRequestWithGet(url, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Weather_Hour everyHour = Utility.handleHourResponse(responseText);
                if (everyHour.getShowapi_res_code() == 0) {
                    for (int i = 0; i < 8; i++) {
                        mDataX.add(everyHour.getShowapi_res_body().getHourList().get(i).getTime().subSequence(8, 10) + "时");
                        mHourTemperature.add(Float.valueOf(everyHour.getShowapi_res_body().getHourList().get(i).getTemperature()));
                        mHourTemperatureAfter.add(initPointTemp(mHourTemperature.get(i)));
                        mWeather.add(everyHour.getShowapi_res_body().getHourList().get(i).getWeather());
                    }

                    for (int i = 0; i < mHourTemperature.size(); i++) {
                        //获取坐标点对应的值
                        mPointValues.add(new PointValue(i, mHourTemperature.get(i)).setLabel(mHourTemperatureAfter.get(i)));
                        //获取x轴的标注
                        mAxisXValues.add(new AxisValue(i).setLabel(mDataX.get(i)));
                        mAxisXValuesBtm.add(new AxisValue(i).setLabel(mWeather.get(i)));
                    }
                    //初始化折线图
                    initLineChart();

                    mHandler.sendEmptyMessageDelayed(SHOW_LINECHAR, 1000);
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.Load_Fail), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getString(R.string.Load_Fail), Toast.LENGTH_SHORT).show();
                        if (mProgressDialog.isShowing()) {
                            closeProgressDialog();
                        }
                    }
                });
            }

        });
    }

    private String initPointTemp(float point) {
        String temp = String.valueOf(point);
        if (point >= 10.0) {
            temp = temp.substring(0, 2);
        } else if (point <= -10.0) {
            temp = temp.substring(0, 3);
        } else if (point >= 0 && point < 10) {
            temp = temp.substring(0, 1);
        } else {
            temp = temp.substring(0, 2);
        }
        return temp + "°";
    }

    private void initLineChart() {
        //折线的颜色
        Line line = new Line(mPointValues).setColor(getResources().getColor(R.color.white));
        List<Line> lines = new ArrayList<>();
        //折线图上每个数据点的形状
        line.setShape(ValueShape.CIRCLE);
        //曲线是否平滑，true是曲线，false是折线
        line.setCubic(false);
        //是否填充曲线的面积
        line.setFilled(false);
        //曲线的数据坐标是否加上备注
        line.setHasLabels(true);
        //点击数据坐标提示数据（设置了这个setHasLabels就无效）
        //line.setHasLabelsOnlyForSelected(true);
        //是否用线显示。如果为false则没有曲线只有点显示
        line.setHasLines(true);
        //是否显示圆点,如果为false则没有原点只有点显示（每个数据点都是个大的圆点）
        line.setHasPoints(true);
        //设置折线宽度
        line.setStrokeWidth(1);
        //设置节点半径宽度
        line.setPointRadius(4);

        lines.add(line);

        //X轴顶部坐标---时间
        Axis axisX = new Axis();
        //X坐标轴字体是斜的显示还是直的，true是斜体
        axisX.setHasTiltedLabels(false);
        //设置字体颜色
        axisX.setTextColor(Color.WHITE);
        //设置字体大小
        axisX.setTextSize(12);
        //最多几个X轴坐标之间的距离间隔
        axisX.setMaxLabelChars(0);
        //填充X轴的坐标
        axisX.setValues(mAxisXValues);
        //X轴分割线
        axisX.setHasLines(true);
        //X轴分割线颜色
        axisX.setLineColor(Color.parseColor("#d3d3d3"));

        //X轴底部坐标---天气
        Axis axisXbtm = new Axis();
        //X坐标轴字体是斜的显示还是直的，true是斜体
        axisXbtm.setHasTiltedLabels(false);
        //设置字体颜色
        axisXbtm.setTextColor(Color.WHITE);
        //设置字体大小
        axisXbtm.setTextSize(14);
        //最多几个X轴坐标之间的距离间隔
        axisXbtm.setMaxLabelChars(0);
        //填充X轴的坐标
        axisXbtm.setValues(mAxisXValuesBtm);
        //X轴分割线
        axisXbtm.setHasLines(false);

        //添加线的数据
        mLineChartData.setLines(lines);
        //X轴在底部
        mLineChartData.setAxisXTop(axisX);
        //X轴在底部
        mLineChartData.setAxisXBottom(axisXbtm);
        //设置是否有数据背景
        mLineChartData.setValueLabelBackgroundEnabled(false);
        //设置数据文字大小
        mLineChartData.setValueLabelTextSize(14);
        //设置数据文字样式
        mLineChartData.setValueLabelTypeface(Typeface.DEFAULT);

        //不支持缩放、滑动以及平移
        mLineChartHour.setInteractive(false);
        mLineChartHour.setScrollEnabled(false);

    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.On_Loading));
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void closeProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refresh:

                //折线X轴顶部坐标值---时间
                mDataX.clear();
                //折线X轴底部坐标值---天气情况
                mWeather.clear();
                //温度原始数据
                mHourTemperature.clear();
                //温度处理过的数据
                mHourTemperatureAfter.clear();
                //折线拐点的值
                mPointValues.clear();
                //折线X轴顶部的值
                mAxisXValues.clear();
                //折线X轴底部的值
                mAxisXValuesBtm.clear();

                showProgressDialog();
                requestWeatherWithHour(SpUtils.get(getContext(), "Select_County", "").toString());
                requestWeatherWithDay(SpUtils.get(getContext(), "Select_County", "").toString());
                break;
        }
    }
}
