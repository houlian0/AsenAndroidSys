package com.asen.android.lib.base.core.gps.listener;


import com.asen.android.lib.base.core.gps.bean.GpsPoint;
import com.asen.android.lib.base.core.gps.bean.MapPoint;

/**
 * Simple to Introduction
 * GPS点位改变接口
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public interface OnLocationChangedListener {

    /**
     * GPS点位改变接口
     *
     * @param gpsPoint gps点
     * @param mapPoint 通过转换方法后的地图点（未设置转换方法时，取gps点部分信息）
     */
    void locationChanged(GpsPoint gpsPoint, MapPoint mapPoint);

}