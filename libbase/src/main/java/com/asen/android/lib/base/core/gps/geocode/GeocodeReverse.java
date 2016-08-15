package com.asen.android.lib.base.core.gps.geocode;

import com.asen.android.lib.base.core.gps.bean.LocationInfo;
import com.asen.android.lib.base.core.gps.listener.OnAddressChangedListener;

import java.util.List;

/**
 * ������������� ����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public abstract class GeocodeReverse implements IGeocodeReverse {

    protected List<OnAddressChangedListener> mAddressChangedListeners = null; // ��ַ�ı�ʱ�ļ�������

    protected double mLon = Double.NaN, mLat = Double.NaN;

    protected LocationInfo mLocationInfo; // ��λ��Ϣ

    @Override
    public LocationInfo getLocationInfo() {
        return mLocationInfo;
    }

    /**
     * ���õ�ַ��������
     *
     * @param listeners ��ַ��������
     */
    public void setAddressChangedListeners(List<OnAddressChangedListener> listeners) {
        mAddressChangedListeners = listeners;
    }

    // ˢ�µ�ַ�ı�ļ���
    protected void refreshAddressChangedListener() {
        if (mAddressChangedListeners != null) {
            for (OnAddressChangedListener listener : mAddressChangedListeners) {
                listener.addressChanged(mLocationInfo);
            }
        }
    }

    // ˢ�µ�ַ��Ϣ
    public void refreshLocationInfo(double lon, double lat, LocationInfo locationInfo) {
        mLon = lon;
        mLat = lat;
        mLocationInfo = locationInfo;
    }

}