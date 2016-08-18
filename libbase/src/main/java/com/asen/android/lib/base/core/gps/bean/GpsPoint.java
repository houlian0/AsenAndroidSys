package com.asen.android.lib.base.core.gps.bean;

import com.asen.android.lib.base.core.gps.GpsInfoType;

/**
 * GPS 点位信息
 *
 * @author Asen
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

    /**
     * 空构造函数
     */
    public GpsPoint() {
    }

    /**
     * 构造函数
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param altitude  高程
     */
    public GpsPoint(double longitude, double latitude, double altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    /**
     * 获取gps信息的类型
     *
     * @return gps信息的类型
     */
    public GpsInfoType getGpsInfoType() {
        return gpsInfoType;
    }

    /**
     * 设置gps信息的类型
     *
     * @param gpsInfoType gps信息的类型
     */
    public void setGpsInfoType(GpsInfoType gpsInfoType) {
        this.gpsInfoType = gpsInfoType;
    }

    /**
     * 获取经度
     *
     * @return 经度
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * 设置经度
     *
     * @param longitude 经度
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * 获取纬度
     *
     * @return 纬度
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * 设置纬度
     *
     * @param latitude 纬度
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * 获取海拔高度（W84椭球）
     *
     * @return 海拔高度
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * 设置海拔高度
     *
     * @param altitude 海拔高度
     */
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    /**
     * 获取精度
     *
     * @return 精度
     */
    public float getAccuracy() {
        return accuracy;
    }

    /**
     * 设置精度
     *
     * @param accuracy 精度
     */
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * 获取速度
     *
     * @return 速度
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * 设置速度
     *
     * @param speed 速度
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * 获取轴度(0-360)
     *
     * @return 轴度
     */
    public float getBearing() {
        return bearing;
    }

    /**
     * 设置轴度
     *
     * @param bearing 轴度
     */
    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    /**
     * 获取定位点获取时间
     *
     * @return 定位点获取时间
     */
    public long getTime() {
        return time;
    }

    /**
     * 设置定位点获取时间
     *
     * @param time 定位点获取时间
     */
    public void setTime(long time) {
        this.time = time;
    }

}