package com.asen.android.lib.base.core.gps.listener;


import com.asen.android.lib.base.core.gps.bean.GpsPoint;
import com.asen.android.lib.base.core.gps.bean.MapPoint;

/**
 * GPS��λ�ı�ӿ�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public interface OnLocationChangedListener {

    /**
     * GPS��λ�ı�ӿ�
     *
     * @param gpsPoint gps��
     * @param mapPoint ͨ��ת��������ĵ�ͼ�㣨δ����ת������ʱ��ȡgps�㲿����Ϣ��
     */
    void locationChanged(GpsPoint gpsPoint, MapPoint mapPoint);

}