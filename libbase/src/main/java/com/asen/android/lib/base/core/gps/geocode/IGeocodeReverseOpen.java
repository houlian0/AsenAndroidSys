package com.asen.android.lib.base.core.gps.geocode;

import com.asen.android.lib.base.core.gps.bean.GpsPoint;
import com.asen.android.lib.base.core.gps.bean.LocationInfo;

/**
 * Simple to Introduction
 * 开放的定位接口
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public interface IGeocodeReverseOpen {

    /**
     * 逆地理编码
     *
     * @param gpsPoint gps点位
     */
    LocationInfo reverseGeocodeOpen(GpsPoint gpsPoint);

}