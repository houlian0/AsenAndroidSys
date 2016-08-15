package com.asen.android.lib.base.core.gps.bean;

/**
 * λ�õ�ַ������Ϣ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public class LocationInfo {

    /**
     * ��ַ
     */
    private String address;

    /**
     * ����
     */
    private String city;

    /**
     * ���캯��
     *
     * @param address ��ַ
     * @param city    ����
     */
    public LocationInfo(String address, String city) {
        this.address = address;
        this.city = city;
    }

    /**
     * ��ȡ��ַ��Ϣ
     *
     * @return ��ַ��Ϣ
     */
    public String getAddress() {
        return address;
    }

    /**
     * ���õ�ַ��Ϣ
     *
     * @param address ��ַ��Ϣ
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * ��ȡ������Ϣ
     *
     * @return ������Ϣ
     */
    public String getCity() {
        return city;
    }

    /**
     * ���ó�����Ϣ
     *
     * @param city ������Ϣ
     */
    public void setCity(String city) {
        this.city = city;
    }

}