package com.quaie.myapplication.gson;

import java.util.List;

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

public class Weather_Hour {

    /**
     * showapi_res_code : 0
     * showapi_res_error :
     * showapi_res_body : {"ret_code":0,"area":"徐汇","hourList":[{"weather_code":"00","time":"201703071600","weather":"晴","temperature":"13"},{"weather_code":"00","time":"201703071700","weather":"晴","temperature":"13"},{"weather_code":"00","time":"201703071800","weather":"晴","temperature":"13"},{"weather_code":"00","time":"201703071900","weather":"晴","temperature":"13"},{"weather_code":"00","time":"201703072000","weather":"晴","temperature":"10"},{"weather_code":"00","time":"201703072100","weather":"晴","temperature":"10"},{"weather_code":"01","time":"201703072200","weather":"多云","temperature":"10"},{"weather_code":"01","time":"201703072300","weather":"多云","temperature":"7"},{"weather_code":"01","time":"201703080000","weather":"多云","temperature":"7"},{"weather_code":"01","time":"201703080100","weather":"多云","temperature":"7"},{"weather_code":"01","time":"201703080200","weather":"多云","temperature":"6"},{"weather_code":"01","time":"201703080300","weather":"多云","temperature":"6"},{"weather_code":"01","time":"201703080400","weather":"多云","temperature":"6"},{"weather_code":"01","time":"201703080500","weather":"多云","temperature":"5"},{"weather_code":"01","time":"201703080600","weather":"多云","temperature":"5"},{"weather_code":"01","time":"201703080700","weather":"多云","temperature":"5"},{"weather_code":"01","time":"201703080800","weather":"多云","temperature":"5"},{"weather_code":"01","time":"201703080900","weather":"多云","temperature":"8"},{"weather_code":"00","time":"201703081000","weather":"晴","temperature":"11"},{"weather_code":"00","time":"201703081100","weather":"晴","temperature":"11"},{"weather_code":"00","time":"201703081200","weather":"晴","temperature":"12"},{"weather_code":"00","time":"201703081300","weather":"晴","temperature":"12"},{"weather_code":"00","time":"201703081400","weather":"晴","temperature":"12"},{"weather_code":"00","time":"201703081500","weather":"晴","temperature":"12"}]}
     */

    private int showapi_res_code;
    private String showapi_res_error;
    private ShowapiResBodyBean showapi_res_body;

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ShowapiResBodyBean getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyBean showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public static class ShowapiResBodyBean {
        /**
         * ret_code : 0
         * area : 徐汇
         * hourList : [{"weather_code":"00","time":"201703071600","weather":"晴","temperature":"13"},{"weather_code":"00","time":"201703071700","weather":"晴","temperature":"13"},{"weather_code":"00","time":"201703071800","weather":"晴","temperature":"13"},{"weather_code":"00","time":"201703071900","weather":"晴","temperature":"13"},{"weather_code":"00","time":"201703072000","weather":"晴","temperature":"10"},{"weather_code":"00","time":"201703072100","weather":"晴","temperature":"10"},{"weather_code":"01","time":"201703072200","weather":"多云","temperature":"10"},{"weather_code":"01","time":"201703072300","weather":"多云","temperature":"7"},{"weather_code":"01","time":"201703080000","weather":"多云","temperature":"7"},{"weather_code":"01","time":"201703080100","weather":"多云","temperature":"7"},{"weather_code":"01","time":"201703080200","weather":"多云","temperature":"6"},{"weather_code":"01","time":"201703080300","weather":"多云","temperature":"6"},{"weather_code":"01","time":"201703080400","weather":"多云","temperature":"6"},{"weather_code":"01","time":"201703080500","weather":"多云","temperature":"5"},{"weather_code":"01","time":"201703080600","weather":"多云","temperature":"5"},{"weather_code":"01","time":"201703080700","weather":"多云","temperature":"5"},{"weather_code":"01","time":"201703080800","weather":"多云","temperature":"5"},{"weather_code":"01","time":"201703080900","weather":"多云","temperature":"8"},{"weather_code":"00","time":"201703081000","weather":"晴","temperature":"11"},{"weather_code":"00","time":"201703081100","weather":"晴","temperature":"11"},{"weather_code":"00","time":"201703081200","weather":"晴","temperature":"12"},{"weather_code":"00","time":"201703081300","weather":"晴","temperature":"12"},{"weather_code":"00","time":"201703081400","weather":"晴","temperature":"12"},{"weather_code":"00","time":"201703081500","weather":"晴","temperature":"12"}]
         */

        private int ret_code;
        private String area;
        private List<HourListBean> hourList;

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public List<HourListBean> getHourList() {
            return hourList;
        }

        public void setHourList(List<HourListBean> hourList) {
            this.hourList = hourList;
        }

        public static class HourListBean {
            /**
             * weather_code : 00
             * time : 201703071600
             * weather : 晴
             * temperature : 13
             */

            private String weather_code;
            private String time;
            private String weather;
            private String temperature;

            public String getWeather_code() {
                return weather_code;
            }

            public void setWeather_code(String weather_code) {
                this.weather_code = weather_code;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getWeather() {
                return weather;
            }

            public void setWeather(String weather) {
                this.weather = weather;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }
        }
    }
}
