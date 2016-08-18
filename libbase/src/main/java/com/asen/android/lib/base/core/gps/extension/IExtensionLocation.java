package com.asen.android.lib.base.core.gps.extension;

import android.location.Location;

import com.asen.android.lib.base.core.gps.bean.LocationInfo;

/**
 * 扩展定位接口
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public interface IExtensionLocation {

    /**
     * 开始定位
     */
    void start();

    /**
     * 结束定位
     */
    void stop();

    /**
     * 获得定位信息
     *
     * @return 定位信息
     */
    Location getLocation();

    /**
     * 刷新位置信息
     *
     * @param location GPS位置信息
     */
    void refreshLocation(Location location);

    /**
     * 刷新定位地址信息
     *
     * @param lon          经度
     * @param lat          纬度
     * @param locationInfo 地址信息
     */
    void refreshLocationInfo(double lon, double lat, LocationInfo locationInfo);

}