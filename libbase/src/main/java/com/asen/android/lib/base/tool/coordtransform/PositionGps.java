package com.asen.android.lib.base.tool.coordtransform;

/**
 * ��γ��������Ϣ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public class PositionGps {

    private double wgLat; // γ��

    private double wgLon; // ����

    /**
     * ���캯��
     *
     * @param wgLat γ��
     * @param wgLon ����
     */
    public PositionGps(double wgLat, double wgLon) {
        setWgLat(wgLat);
        setWgLon(wgLon);
    }

    /**
     * ��ȡγ��
     *
     * @return γ��
     */
    public double getWgLat() {
        return wgLat;
    }

    /**
     * ����γ��
     *
     * @param wgLat γ��
     */
    public void setWgLat(double wgLat) {
        this.wgLat = wgLat;
    }

    /**
     * ��ȡ����
     *
     * @return ����
     */
    public double getWgLon() {
        return wgLon;
    }

    /**
     * ���þ���
     *
     * @param wgLon ����
     */
    public void setWgLon(double wgLon) {
        this.wgLon = wgLon;
    }

    @Override
    public String toString() {
        return wgLat + "," + wgLon;
    }

}