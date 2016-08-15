package com.asen.android.lib.base.core.gps.bean;

import com.asen.android.lib.base.core.gps.GpsInfoType;

/**
 * Simple to Introduction
 * <p/>
 * GPS 点位信息
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class GpsPoint {

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    /**
     * 海拔（W84椭球）
     */
    private double altitude;

    /**
     * 精度
     */
    private float accuracy;

    /**
     * 速度
     */
    private float speed;

    /**
     * 轴度(0-360)
     */
    private float bearing;

    /**
     * 时间
     */
    private long time;

    /**
     * 定位点类型
     */
    private GpsInfoType gpsInfoType;

    public GpsPoint() {
    }

    public GpsPoint(double longitude, double latitude, double altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    public GpsInfoType getGpsInfoType() {
        return gpsInfoType;
    }

    public void setGpsInfoType(GpsInfoType gpsInfoType) {
        this.gpsInfoType = gpsInfoType;
    }

    /**
     * 经度
     */
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * 纬度
     */
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * 海拔（W84椭球）
     */
    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    /**
     * 精度
     */
    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * 速度
     */
    public float getSpeed() {
        return speed;
    }


    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * 轴度(0-360)
     */
    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}