package com.asen.android.lib.base.core.gps;

/**
 * 定位信息类型
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public enum GpsInfoType {

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
