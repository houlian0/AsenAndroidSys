package com.asen.android.lib.base.core.gps.bean;

/**
 * Simple to Introduction 地图点位
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class MapPoint {

    private double x;

    private double y;

    private double z;

    public MapPoint() {
    }

    public MapPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public MapPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}