package com.asen.android.lib.base.core.gps.bean;

/**
 * 临时定位信息
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class TmpGpsInfo {

    /**
     * GPS点位信息
     */
    private GpsPoint gpsPoint;

    /**
     * 地图点位信息
     */
    private MapPoint mapPoint;

    /**
     * 构造函数
     *
     * @param gpsPoint GPS点位信息
     * @param mapPoint 地图点位信息
     */
    public TmpGpsInfo(GpsPoint gpsPoint, MapPoint mapPoint) {
        this.gpsPoint = gpsPoint;
        this.mapPoint = mapPoint;
    }

    /**
     * 获取GPS点位信息
     *
     * @return GPS点位信息
     */
    public GpsPoint getGpsPoint() {
        return gpsPoint;
    }

    /**
     * 设置GPS点位信息
     *
     * @param gpsPoint GPS点位信息
     */
    public void setGpsPoint(GpsPoint gpsPoint) {
        this.gpsPoint = gpsPoint;
    }

    /**
     * 获取地图点位信息
     *
     * @return 地图点位信息
     */
    public MapPoint getMapPoint() {
        return mapPoint;
    }

    /**
     * 设置地图点位信息
     *
     * @param mapPoint 地图点位信息
     */
    public void setMapPoint(MapPoint mapPoint) {
        this.mapPoint = mapPoint;
    }

}
