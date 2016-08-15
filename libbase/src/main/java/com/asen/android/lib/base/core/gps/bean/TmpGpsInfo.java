package com.asen.android.lib.base.core.gps.bean;

/**
 * Simple to Introduction
 * 临时定位信息
 *
 * @ProjectName: Gisinfo Gas Hetian
 * @Description:
 * @Author: Asen
 * @CreateDate: 2015-11-04
 * @Time: 17:19
 * @Version: [v1.0]
 */
public class TmpGpsInfo {

    private GpsPoint gpsPoint;

    private MapPoint mapPoint;

    public TmpGpsInfo(GpsPoint gpsPoint, MapPoint mapPoint) {
        this.gpsPoint = gpsPoint;
        this.mapPoint = mapPoint;
    }

    public GpsPoint getGpsPoint() {
        return gpsPoint;
    }

    public void setGpsPoint(GpsPoint gpsPoint) {
        this.gpsPoint = gpsPoint;
    }

    public MapPoint getMapPoint() {
        return mapPoint;
    }

    public void setMapPoint(MapPoint mapPoint) {
        this.mapPoint = mapPoint;
    }

}
