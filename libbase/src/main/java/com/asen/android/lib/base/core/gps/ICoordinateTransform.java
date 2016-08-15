package com.asen.android.lib.base.core.gps;

import com.asen.android.lib.base.core.gps.bean.GpsPoint;
import com.asen.android.lib.base.core.gps.bean.MapPoint;

/**
 * GPS 坐标转换接口
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public interface ICoordinateTransform {

    /**
     * W84坐标 转 地方坐标
     *
     * @param gpsPoint GPS定位信息（W84坐标）
     * @return 地方坐标信息
     */
    MapPoint gpsPoint2MapPoint(GpsPoint gpsPoint);

    /**
     * 地方坐标 转 W84坐标
     *
     * @param mapPoint 地方坐标信息
     * @return GPS定位信息（W84坐标）
     */
    GpsPoint mapPoint2GpsPoint(MapPoint mapPoint);

}