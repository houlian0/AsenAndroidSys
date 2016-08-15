package com.asen.android.lib.base.core.gps.extension;

import android.location.Location;

import com.asen.android.lib.base.core.gps.GpsLocation;
import com.asen.android.lib.base.core.gps.GpsLocationMain;
import com.asen.android.lib.base.core.gps.GpsInfoType;
import com.asen.android.lib.base.core.gps.bean.LocationInfo;

/**
 * ��չ��������λ����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public abstract class BaseExtensionLocation implements IExtensionLocation {

    /**
     * GPS��λ������
     */
    protected GpsLocation mGpsLocation;

    /**
     * ��ȡGPS��λ������
     *
     * @return GPS��λ������
     */
    public GpsLocation getGpsLocation() {
        return mGpsLocation;
    }

    /**
     * ����GPS��λ������
     *
     * @param gpsLocation GPS��λ������
     */
    public void setGpsLocation(GpsLocation gpsLocation) {
        mGpsLocation = gpsLocation;
    }

    @Override
    public final void refreshLocation(Location location) {
        ((GpsLocationMain) mGpsLocation).refreshLocation(GpsInfoType.TYPE_EXTENSION, location);
    }

    @Override
    public final void refreshLocationInfo(double lon, double lat, LocationInfo locationInfo) {
        ((GpsLocationMain) mGpsLocation).refreshLocationInfo(lon, lat, locationInfo);
    }

}