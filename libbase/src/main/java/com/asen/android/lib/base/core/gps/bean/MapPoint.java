package com.asen.android.lib.base.core.gps.bean;

/**
 * ��ͼ��λ����ʵ��ͼ��λ��Ϣ��
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class MapPoint {

    /**
     * X����
     */
    private double x;

    /**
     * Y����
     */
    private double y;

    /**
     * Z����
     */
    private double z;

    /**
     * �չ��캯��
     */
    public MapPoint() {
    }

    /**
     * ���캯��
     *
     * @param x X����
     * @param y Y����
     */
    public MapPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * ���캯��
     *
     * @param x X����
     * @param y Y����
     * @param z Z����
     */
    public MapPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * ��ȡX����
     *
     * @return X����
     */
    public double getX() {
        return x;
    }

    /**
     * ����X����
     *
     * @param x X����
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * ��ȡY����
     *
     * @return Y����
     */
    public double getY() {
        return y;
    }

    /**
     * ����Y����
     *
     * @param y Y����
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * ��ȡZ����
     *
     * @return Z����
     */
    public double getZ() {
        return z;
    }

    /**
     * ����Z����
     *
     * @param z Z����
     */
    public void setZ(double z) {
        this.z = z;
    }

}