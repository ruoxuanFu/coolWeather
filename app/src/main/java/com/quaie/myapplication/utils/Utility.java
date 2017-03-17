package com.quaie.myapplication.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.quaie.myapplication.db.City;
import com.quaie.myapplication.db.County;
import com.quaie.myapplication.db.Province;
import com.quaie.myapplication.gson.Weather_Day;
import com.quaie.myapplication.gson.Weather_Hour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yue on 2017/3/6.
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

public class Utility {

    /**
     * 解析省并加入到数据库中
     *
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response) {

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    Province jProvince = new Province();
                    JSONObject jsonObject = allProvinces.getJSONObject(i);
                    jProvince.setProvinceName(jsonObject.getString("name"));
                    jProvince.setProvinceCode(jsonObject.getInt("id"));
                    jProvince.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    /**
     * 解析城市并加入到数据库中
     *
     * @param response
     * @param provinceId
     * @return
     */
    public static boolean handleCityResponse(String response, int provinceId) {

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCity = new JSONArray(response);
                for (int i = 0; i < allCity.length(); i++) {
                    City jCity = new City();
                    JSONObject jsonObject = allCity.getJSONObject(i);
                    jCity.setCityName(jsonObject.getString("name"));
                    jCity.setCityCode(jsonObject.getInt("id"));
                    jCity.setProvinceId(provinceId);
                    jCity.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 解析县市并加入到数据库中
     *
     * @param response
     * @param cityId
     * @return
     */
    public static boolean handleCountyResponse(String response, int cityId) {

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounty = new JSONArray(response);
                for (int i = 0; i < allCounty.length(); i++) {
                    County jCounty = new County();
                    JSONObject jsonObject = allCounty.getJSONObject(i);
                    jCounty.setCountyName(jsonObject.getString("name"));
                    jCounty.setWeatherId(jsonObject.getString("weather_id"));
                    jCounty.setCityId(cityId);
                    jCounty.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析每小时的天气情况
     * @param response
     * @return
     */
    public static Weather_Hour handleHourResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            String weatherContent = jsonObject.toString();
            return new Gson().fromJson(weatherContent, Weather_Hour.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解析当天的天气情况
     * @param response
     * @return
     */
    public static Weather_Day handleDayResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            String weatherContent = jsonObject.toString();
            return new Gson().fromJson(weatherContent, Weather_Day.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
