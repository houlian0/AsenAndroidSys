package com.asen.android.lib.base.tool.coordtransform;

/**
 * 经纬度坐标信息
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public class PositionGps {

    private double wgLat; // 纬度

    private double wgLon; // 经度

    /**
     * 构造函数
     *
     * @param wgLat 纬度
     * @param wgLon 经度
     */
    public PositionGps(double wgLat, double wgLon) {
        setWgLat(wgLat);
        setWgLon(wgLon);
    }

    /**
     * 获取纬度
     *
     * @return 纬度
     */
    public double getWgLat() {
        return wgLat;
    }

    /**
     * 设置纬度
     *
     * @param wgLat 纬度
     */
    public void setWgLat(double wgLat) {
        this.wgLat = wgLat;
    }

    /**
     * 获取经度
     *
     * @return 经度
     */
    public double getWgLon() {
        return wgLon;
    }

    /**
     * 设置经度
     *
     * @param wgLon 经度
     */
    public void setWgLon(double wgLon) {
        this.wgLon = wgLon;
    }

    @Override
    public String toString() {
        return wgLat + "," + wgLon;
    }

}