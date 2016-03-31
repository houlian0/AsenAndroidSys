package com.asen.android.lib.base.core.gps.bean;

/**
 * Simple to Introduction
 * 位置信息
 *
 * @author ASEN
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

    public LocationInfo(String address, String city) {
        this.address = address;
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}