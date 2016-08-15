package com.asen.android.lib.base.core.gps.bean;

import com.asen.android.lib.base.core.gps.GpsInfoType;

/**
 * GPS ��λ��Ϣ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class GpsPoint {

    /**
     * ����
     */
    private double longitude;

    /**
     * γ��
     */
    private double latitude;

    /**
     * ���Σ�W84����
     */
    private double altitude;

    /**
     * ����
     */
    private float accuracy;

    /**
     * �ٶ�
     */
    private float speed;

    /**
     * ���(0-360)
     */
    private float bearing;

    /**
     * ʱ��
     */
    private long time;

    /**
     * ��λ������
     */
    private GpsInfoType gpsInfoType;

    /**
     * �չ��캯��
     */
    public GpsPoint() {
    }

    /**
     * ���캯��
     *
     * @param longitude ����
     * @param latitude  γ��
     * @param altitude  �߳�
     */
    public GpsPoint(double longitude, double latitude, double altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    /**
     * ��ȡgps��Ϣ������
     *
     * @return gps��Ϣ������
     */
    public GpsInfoType getGpsInfoType() {
        return gpsInfoType;
    }

    /**
     * ����gps��Ϣ������
     *
     * @param gpsInfoType gps��Ϣ������
     */
    public void setGpsInfoType(GpsInfoType gpsInfoType) {
        this.gpsInfoType = gpsInfoType;
    }

    /**
     * ��ȡ����
     *
     * @return ����
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * ���þ���
     *
     * @param longitude ����
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * ��ȡγ��
     *
     * @return γ��
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * ����γ��
     *
     * @param latitude γ��
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * ��ȡ���θ߶ȣ�W84����
     *
     * @return ���θ߶�
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * ���ú��θ߶�
     *
     * @param altitude ���θ߶�
     */
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    /**
     * ��ȡ����
     *
     * @return ����
     */
    public float getAccuracy() {
        return accuracy;
    }

    /**
     * ���þ���
     *
     * @param accuracy ����
     */
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * ��ȡ�ٶ�
     *
     * @return �ٶ�
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * �����ٶ�
     *
     * @param speed �ٶ�
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * ��ȡ���(0-360)
     *
     * @return ���
     */
    public float getBearing() {
        return bearing;
    }

    /**
     * �������
     *
     * @param bearing ���
     */
    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    /**
     * ��ȡ��λ���ȡʱ��
     *
     * @return ��λ���ȡʱ��
     */
    public long getTime() {
        return time;
    }

    /**
     * ���ö�λ���ȡʱ��
     *
     * @param time ��λ���ȡʱ��
     */
    public void setTime(long time) {
        this.time = time;
    }

}