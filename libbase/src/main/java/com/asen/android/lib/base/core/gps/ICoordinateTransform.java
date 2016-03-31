package com.asen.android.lib.base.core.gps;

import com.asen.android.lib.base.core.gps.bean.GpsPoint;
import com.asen.android.lib.base.core.gps.bean.MapPoint;

/**
 * Simple to Introduction
 * GPS 坐标转换接口
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public interface ICoordinateTransform {

    /**
     * W84坐标 转 地方坐标
     *
     * @param gpsPoint
     * @return
     */
    MapPoint gpsPoint2MapPoint(GpsPoint gpsPoint);

    /**
     * @param mapPoint
     * @return
     */
    GpsPoint mapPoint2GpsPoint(MapPoint mapPoint);

}