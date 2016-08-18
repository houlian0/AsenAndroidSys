package com.asen.android.lib.base.core.gps.geocode;

import com.asen.android.lib.base.core.gps.bean.GpsPoint;
import com.asen.android.lib.base.core.gps.bean.LocationInfo;

/**
 * 逆地理编码接口定义
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
interface IGeocodeReverse {

    /**
     * 获得地址信息
     *
     * @return 地址信息
     */
    LocationInfo getLocationInfo();

    /**
     * 逆地理编码
     *
     * @param gpsPoint gps点位
     */
    void reverseGeocode(GpsPoint gpsPoint);

}