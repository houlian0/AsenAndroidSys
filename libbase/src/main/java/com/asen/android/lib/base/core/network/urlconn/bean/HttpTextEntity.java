package com.asen.android.lib.base.core.network.urlconn.bean;

/**
 * ��ֵ���ϴ���������Ϣ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class HttpTextEntity {

    /**
     * ������
     */
    private String name;

    /**
     * ֵ����
     */
    private String value;

    /**
     * ���캯��
     *
     * @param name  ������
     * @param value ֵ����
     */
    public HttpTextEntity(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * ��ȡ������
     *
     * @return ������
     */
    public String getName() {
        return name;
    }

    /**
     * ���ü�����
     *
     * @param name ������
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * ��ȡֵ����
     *
     * @return ֵ����
     */
    public String getValue() {
        return value;
    }

    /**
     * ����ֵ����
     *
     * @param value ֵ����
     */
    public void setValue(String value) {
        this.value = value;
    }

}
