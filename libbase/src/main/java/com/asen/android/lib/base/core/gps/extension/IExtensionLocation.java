package com.asen.android.lib.base.core.gps.extension;

import android.location.Location;

/**
 * Simple to Introduction
 * 扩展定位接口
 *
 * @author ASEN
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
     * @return
     */
    Location getLocation();

    /**
     * 刷新定位信息
     */
    void refreshLocation(Location location);

}