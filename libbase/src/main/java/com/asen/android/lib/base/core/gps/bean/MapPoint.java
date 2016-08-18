package com.asen.android.lib.base.core.gps.bean;

/**
 * 地图点位（真实地图点位信息）
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class MapPoint {

    /**
     * X坐标
     */
    private double x;

    /**
     * Y坐标
     */
    private double y;

    /**
     * Z坐标
     */
    private double z;

    /**
     * 空构造函数
     */
    public MapPoint() {
    }

    /**
     * 构造函数
     *
     * @param x X坐标
     * @param y Y坐标
     */
    public MapPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 构造函数
     *
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     */
    public MapPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 获取X坐标
     *
     * @return X坐标
     */
    public double getX() {
        return x;
    }

    /**
     * 设置X坐标
     *
     * @param x X坐标
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * 获取Y坐标
     *
     * @return Y坐标
     */
    public double getY() {
        return y;
    }

    /**
     * 设置Y坐标
     *
     * @param y Y坐标
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * 获取Z坐标
     *
     * @return Z坐标
     */
    public double getZ() {
        return z;
    }

    /**
     * 设置Z坐标
     *
     * @param z Z坐标
     */
    public void setZ(double z) {
        this.z = z;
    }

}