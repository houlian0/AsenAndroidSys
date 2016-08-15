package com.asen.android.lib.base.core.gps.bean;

/**
 * ��ʱ��λ��Ϣ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class TmpGpsInfo {

    /**
     * GPS��λ��Ϣ
     */
    private GpsPoint gpsPoint;

    /**
     * ��ͼ��λ��Ϣ
     */
    private MapPoint mapPoint;

    /**
     * ���캯��
     *
     * @param gpsPoint GPS��λ��Ϣ
     * @param mapPoint ��ͼ��λ��Ϣ
     */
    public TmpGpsInfo(GpsPoint gpsPoint, MapPoint mapPoint) {
        this.gpsPoint = gpsPoint;
        this.mapPoint = mapPoint;
    }

    /**
     * ��ȡGPS��λ��Ϣ
     *
     * @return GPS��λ��Ϣ
     */
    public GpsPoint getGpsPoint() {
        return gpsPoint;
    }

    /**
     * ����GPS��λ��Ϣ
     *
     * @param gpsPoint GPS��λ��Ϣ
     */
    public void setGpsPoint(GpsPoint gpsPoint) {
        this.gpsPoint = gpsPoint;
    }

    /**
     * ��ȡ��ͼ��λ��Ϣ
     *
     * @return ��ͼ��λ��Ϣ
     */
    public MapPoint getMapPoint() {
        return mapPoint;
    }

    /**
     * ���õ�ͼ��λ��Ϣ
     *
     * @param mapPoint ��ͼ��λ��Ϣ
     */
    public void setMapPoint(MapPoint mapPoint) {
        this.mapPoint = mapPoint;
    }

}
