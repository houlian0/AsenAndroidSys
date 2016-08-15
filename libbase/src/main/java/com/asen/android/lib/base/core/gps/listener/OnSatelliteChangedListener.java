package com.asen.android.lib.base.core.gps.listener;

import android.location.GpsSatellite;
import android.location.LocationManager;

import java.util.List;

/**
 * Simple to Introduction
 * GPS״̬�����ӿ�
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
     * @param gpsStatus     GSP״̬
     * @param gpsSatellites ����Ϊnull
     */
    void satelliteChanged(int gpsStatus, List<GpsSatellite> gpsSatellites);
}