package com.asen.android.lib.base.core.gps;

/**
 * Simple to Introduction
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public enum LocationType {

//    static final int TYPE_FIRST = 0x01; // 首次刷新
//
//    static final int TYPE_GPS = 0x02; // GPS 定位
//
//    static final int TYPE_NETWORK = 0x03; // 网络定位
//
//    static final int TYPE_EXTENSION = 0x04; // 扩展性定位

    /**
     * 首次刷新
     */
    TYPE_FIRST,
    /**
     * GPS 定位
     */
    TYPE_GPS,
    /**
     * 网络定位（Android自带的网络定位）
     */
    TYPE_NETWORK,
    /**
     * 扩展性定位（第三方插件定位，自定义）
     */
    TYPE_EXTENSION

}
