package com.quaie.myapplication.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.quaie.myapplication.Main_Activity;
import com.quaie.myapplication.R;
import com.quaie.myapplication.custom.NestedListView;
import com.quaie.myapplication.db.City;
import com.quaie.myapplication.db.County;
import com.quaie.myapplication.db.Province;
import com.quaie.myapplication.utils.HttpUtil;
import com.quaie.myapplication.utils.SpUtils;
import com.quaie.myapplication.utils.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class Frag_City extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog mProgressDialog;
    private NestedListView fc_list_view;
    private TextView tv_info;
    private Button btn_back;

    private ArrayAdapter<String> mAdapter;
    private List<String> mData;

    /**
     * 省列表
     */
    private List<Province> mProvinceList;

    /**
     * 市列表
     */
    private List<City> mCityList;

    /**
     * 县列表
     */
    private List<County> mCountyList;

    /**
     * 选中的省
     */
    private Province mSelectedProvince;

    /**
     * 选中的市
     */
    private City mSelectedCity;

    /**
     * 选中的级别
     */
    private int mSelectedLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_city, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        fc_list_view = (NestedListView) view.findViewById(R.id.fc_list_view);
        tv_info = (TextView) view.findViewById(R.id.tv_info);
        btn_back = (Button) view.findViewById(R.id.btn_back);
    }

    private void initData() {
        mData = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mData);
        fc_list_view.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fc_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSelectedLevel == LEVEL_PROVINCE) {
                    mSelectedProvince = mProvinceList.get(position);
                    queryCity();
                } else if (mSelectedLevel == LEVEL_CITY) {
                    mSelectedCity = mCityList.get(position);
                    queryCounty();
                } else if (mSelectedLevel == LEVEL_COUNTY) {
                    //Log.e("Frag_City", "mData = " + mData.get(position));

                    SpUtils.put(getContext(), "Select_County", mData.get(position));
                    Main_Activity activity = (Main_Activity) getActivity();
                    activity.drawer.closeDrawers();
                    Frag_Weather frag_weather = new Frag_Weather();
                    activity.replaceFragment(frag_weather);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.closeOkHttpRequest();
                if (mSelectedLevel == LEVEL_COUNTY) {
                    queryCity();
                } else if (mSelectedLevel == LEVEL_CITY) {
                    queryProvince();
                }
            }
        });

        queryProvince();
    }


    private void queryProvince() {
        tv_info.setText("中国");
        btn_back.setVisibility(View.GONE);
        mProvinceList = DataSupport.findAll(Province.class);
        if (mProvinceList.size() > 0) {
            mData.clear();
            for (Province province : mProvinceList) {
                mData.add(province.getProvinceName());
                //Log.e("Frag_City", "ProvinceName = " + province.getProvinceName());

            }
            mAdapter.notifyDataSetChanged();
            mSelectedLevel = LEVEL_PROVINCE;
        } else {
            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");
        }
    }

    private void queryCity() {
        tv_info.setText(mSelectedProvince.getProvinceName());
        btn_back.setVisibility(View.VISIBLE);
        mCityList = DataSupport
                .where("provinceid = ?", String.valueOf(mSelectedProvince.getId()))
                .find(City.class);
        if (mCityList.size() > 0) {
            mData.clear();
            for (City city : mCityList) {
                mData.add(city.getCityName());
                //Log.e("Frag_City", "CityName = " + city.getCityName());
            }
            mAdapter.notifyDataSetChanged();
            mSelectedLevel = LEVEL_CITY;
        } else {
            int provinceCode = mSelectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    private void queryCounty() {
        tv_info.setText(mSelectedCity.getCityName());
        btn_back.setVisibility(View.VISIBLE);
        mCountyList = DataSupport
                .where("cityid = ?", String.valueOf(mSelectedCity.getId()))
                .find(County.class);
        if (mCountyList.size() > 0) {
            mData.clear();
            for (County county : mCountyList) {
                mData.add(county.getCountyName());
                //Log.e("Frag_City", "CountyName = " + county.getCountyName());
            }
            mAdapter.notifyDataSetChanged();
            mSelectedLevel = LEVEL_COUNTY;
        } else {
            int provinceCode = mSelectedProvince.getProvinceCode();
            int cityCode = mSelectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询数据
     *
     * @param address
     * @param code
     */
    private void queryFromServer(String address, final String code) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequestWithGet(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(code)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(code)) {
                    result = Utility.handleCityResponse(responseText, mSelectedProvince.getId());
                } else if ("county".equals(code)) {
                    result = Utility.handleCountyResponse(responseText, mSelectedCity.getId());
                }
                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(code)) {
                                queryProvince();
                            } else if ("city".equals(code)) {
                                queryCity();
                            } else if ("county".equals(code)) {
                                queryCounty();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), getString(R.string.Load_Fail), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
}
