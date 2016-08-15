package com.asen.android.lib.base.core.gps;

import com.asen.android.lib.base.core.gps.bean.GpsPoint;
import com.asen.android.lib.base.core.gps.bean.MapPoint;

/**
 * GPS ����ת���ӿ�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public interface ICoordinateTransform {

    /**
     * W84���� ת �ط�����
     *
     * @param gpsPoint GPS��λ��Ϣ��W84���꣩
     * @return �ط�������Ϣ
     */
    MapPoint gpsPoint2MapPoint(GpsPoint gpsPoint);

    /**
     * �ط����� ת W84����
     *
     * @param mapPoint �ط�������Ϣ
     * @return GPS��λ��Ϣ��W84���꣩
     */
    GpsPoint mapPoint2GpsPoint(MapPoint mapPoint);

}