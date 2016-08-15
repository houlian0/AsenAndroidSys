package com.asen.android.lib.base.core.gps.geocode.tdt;

/**
 * ���ͼ��ַ��Ϣ����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class GeoAddressInfo {

    /**
     * ��ʽ���ĵ�ַ
     */
    private String formattedAddress;

    /**
     * �����·��
     */
    private String road;

    /**
     * �����·���ľ���
     */
    private int roadDistance;

    /**
     * �����POI��
     */
    private String poi;

    /**
     * �������POI��ľ���
     */
    private int poiDistance;

    /**
     * �����POI��ķ�λ��Ϣ
     */
    private String poiPosition;

    /**
     * ����ĵ�ַ
     */
    private String address;

    /**
     * �������ַ�ķ�λ��Ϣ
     */
    private String addressPosition;

    /**
     * �������ַ�ľ���
     */
    private int addressDistance;

    /**
     * ���ڵĳ�����Ϣ
     */
    private String city;

    /**
     * ���캯��
     *
     * @param formattedAddress ��ʽ���ĵ�ַ
     * @param road             �����·��
     * @param roadDistance     �����·���ľ���
     * @param poi              �����POI��
     * @param poiDistance      �������POI��ľ���
     * @param poiPosition      �����POI��ķ�λ��Ϣ
     * @param address          ����ĵ�ַ
     * @param addressPosition  �������ַ�ķ�λ��Ϣ
     * @param addressDistance  �������ַ�ľ���
     * @param city             ���ڵĳ�����Ϣ
     */
    public GeoAddressInfo(String formattedAddress, String road, int roadDistance, String poi, int poiDistance, String poiPosition, String address, String addressPosition, int addressDistance, String city) {
        this.formattedAddress = formattedAddress;
        this.road = road;
        this.roadDistance = roadDistance;
        this.poi = poi;
        this.poiDistance = poiDistance;
        this.poiPosition = poiPosition;
        this.address = address;
        this.addressPosition = addressPosition;
        this.addressDistance = addressDistance;
        this.city = city;
    }

    /**
     * ��ȡ��ʽ���ĵ�ַ
     *
     * @return ��ʽ���ĵ�ַ
     */
    public String getFormattedAddress() {
        return this.formattedAddress;
    }

    /**
     * ���ø�ʽ���ĵ�ַ
     *
     * @param formattedAddress ��ʽ���ĵ�ַ
     */
    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    /**
     * ��ȡ�����·��
     *
     * @return �����·��
     */
    public String getRoad() {
        return this.road;
    }

    /**
     * ���������·��
     *
     * @param road �����·��
     */
    public void setRoad(String road) {
        this.road = road;
    }

    /**
     * ��ȡ�����·���ľ���
     *
     * @return �����·���ľ���
     */
    public int getRoadDistance() {
        return this.roadDistance;
    }

    /**
     * ���������·���ľ���
     *
     * @param roadDistance �����·���ľ���
     */
    public void setRoadDistance(int roadDistance) {
        this.roadDistance = roadDistance;
    }

    /**
     * ��ȡ�����POI��
     *
     * @return �����POI��
     */
    public String getPoi() {
        return this.poi;
    }

    /**
     * ���������POI��
     *
     * @param poi �����POI��
     */
    public void setPoi(String poi) {
        this.poi = poi;
    }

    /**
     * ��ȡ�������POI��ľ���
     *
     * @return �������POI��ľ���
     */
    public int getPoiDistance() {
        return this.poiDistance;
    }

    /**
     * �����������POI��ľ���
     *
     * @param poiDistance �������POI��ľ���
     */
    public void setPoiDistance(int poiDistance) {
        this.poiDistance = poiDistance;
    }

    /**
     * ��ȡ�����POI��ķ�λ��Ϣ
     *
     * @return �����POI��ķ�λ��Ϣ
     */
    public String getPoiPosition() {
        return this.poiPosition;
    }

    /**
     * ���������POI��ķ�λ��Ϣ
     *
     * @param poiPosition �����POI��ķ�λ��Ϣ
     */
    public void setPoiPosition(String poiPosition) {
        this.poiPosition = poiPosition;
    }

    /**
     * ��ȡ����ĵ�ַ
     *
     * @return ����ĵ�ַ
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * ��������ĵ�ַ
     *
     * @param address ����ĵ�ַ
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * ��ȡ�������ַ�ķ�λ��Ϣ
     *
     * @return �������ַ�ķ�λ��Ϣ
     */
    public String getAddressPosition() {
        return this.addressPosition;
    }

    /**
     * �����������ַ�ķ�λ��Ϣ
     *
     * @param addressPosition �������ַ�ķ�λ��Ϣ
     */
    public void setAddressPosition(String addressPosition) {
        this.addressPosition = addressPosition;
    }

    /**
     * ��ȡ�������ַ�ľ���
     *
     * @return �������ַ�ľ���
     */
    public int getAddressDistance() {
        return this.addressDistance;
    }

    /**
     * �����������ַ�ľ���
     *
     * @param addressDistance �������ַ�ľ���
     */
    public void setAddressDistance(int addressDistance) {
        this.addressDistance = addressDistance;
    }

    /**
     * ��ȡ���ڵĳ�����Ϣ
     *
     * @return ���ڵĳ�����Ϣ
     */
    public String getCity() {
        return this.city;
    }

    /**
     * �������ڵĳ�����Ϣ
     *
     * @param city ���ڵĳ�����Ϣ
     */
    public void setCity(String city) {
        this.city = city;
    }
}