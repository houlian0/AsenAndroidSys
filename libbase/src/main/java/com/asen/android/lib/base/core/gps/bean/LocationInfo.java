package com.asen.android.lib.base.core.gps.bean;

/**
 * 位置地址城市信息
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class LocationInfo {

    /**
     * 地址
     */
    private String address;

    /**
     * 城市
     */
    private String city;

    /**
     * 构造函数
     *
     * @param address 地址
     * @param city    城市
     */
    public LocationInfo(String address, String city) {
        this.address = address;
        this.city = city;
    }

    /**
     * 获取地址信息
     *
     * @return 地址信息
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址信息
     *
     * @param address 地址信息
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取城市信息
     *
     * @return 城市信息
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市信息
     *
     * @param city 城市信息
     */
    public void setCity(String city) {
        this.city = city;
    }

}