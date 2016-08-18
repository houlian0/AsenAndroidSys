package com.asen.android.lib.base.core.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Android原生定位监听
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
class GpsLocationListener implements LocationListener {

    private GpsLocationMain mGpsLocation;

    private GpsInfoType mType;

    GpsLocationListener(GpsLocationMain gpsLocation, GpsInfoType type) {
        mGpsLocation = gpsLocation;
        mType = type;
    }

    @Override
    public void onLocationChanged(Location location) {
        mGpsLocation.refreshLocation(mType, location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}