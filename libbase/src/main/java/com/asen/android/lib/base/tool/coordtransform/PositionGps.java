package com.asen.android.lib.base.tool.coordtransform;

/**
 * Simple to Introduction
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public class PositionGps {


    private double wgLat;
    private double wgLon;

    public PositionGps(double wgLat, double wgLon) {
        setWgLat(wgLat);
        setWgLon(wgLon);
    }

    public double getWgLat() {
        return wgLat;
    }

    public void setWgLat(double wgLat) {
        this.wgLat = wgLat;
    }

    public double getWgLon() {
        return wgLon;
    }

    public void setWgLon(double wgLon) {
        this.wgLon = wgLon;
    }

    @Override
    public String toString() {
        return wgLat + "," + wgLon;
    }
}