package com.asen.android.lib.base.core.gps.listener;

import android.location.GpsSatellite;
import android.location.LocationManager;

import java.util.List;

/**
 * Simple to Introduction
 * GPS状态监听接口
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public interface OnSatelliteChangedListener {

    /**
     * When this method is called, the client should call
     * {@link LocationManager#getGpsStatus} to get additional
     * status information.
     *
     * @param gpsStatus     GSP状态
     * @param gpsSatellites 可能为null
     */
    void satelliteChanged(int gpsStatus, List<GpsSatellite> gpsSatellites);
}