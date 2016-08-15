package com.asen.android.lib.base.core.gps.geocode;

import com.asen.android.lib.base.core.gps.bean.GpsPoint;
import com.asen.android.lib.base.core.gps.bean.LocationInfo;

/**
 * ��������ӿڶ���
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
interface IGeocodeReverse {

    /**
     * ��õ�ַ��Ϣ
     *
     * @return ��ַ��Ϣ
     */
    LocationInfo getLocationInfo();

    /**
     * ��������
     *
     * @param gpsPoint gps��λ
     */
    void reverseGeocode(GpsPoint gpsPoint);

}