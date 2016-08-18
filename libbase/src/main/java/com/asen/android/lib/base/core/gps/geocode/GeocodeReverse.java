package com.asen.android.lib.base.core.gps.geocode;

import com.asen.android.lib.base.core.gps.bean.LocationInfo;
import com.asen.android.lib.base.core.gps.listener.OnAddressChangedListener;

import java.util.List;

/**
 * 逆地理编码抽象类 定义
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public abstract class GeocodeReverse implements IGeocodeReverse {

    protected List<OnAddressChangedListener> mAddressChangedListeners = null; // 地址改变时的监听集合

    protected double mLon = Double.NaN, mLat = Double.NaN;

    protected LocationInfo mLocationInfo; // 定位信息

    @Override
    public LocationInfo getLocationInfo() {
        return mLocationInfo;
    }

    /**
     * 设置地址监听集合
     *
     * @param listeners 地址监听集合
     */
    public void setAddressChangedListeners(List<OnAddressChangedListener> listeners) {
        mAddressChangedListeners = listeners;
    }

    // 刷新地址改变的监听
    protected void refreshAddressChangedListener() {
        if (mAddressChangedListeners != null) {
            for (OnAddressChangedListener listener : mAddressChangedListeners) {
                listener.addressChanged(mLocationInfo);
            }
        }
    }

    // 刷新地址信息
    public void refreshLocationInfo(double lon, double lat, LocationInfo locationInfo) {
        mLon = lon;
        mLat = lat;
        mLocationInfo = locationInfo;
    }

}