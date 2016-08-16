package com.asen.android.lib.base.core.network.urlconn.bean;

import java.io.File;

/**
 * ��ֵ���ϴ��ļ�����Ϣ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class HttpFileEntity {

    /**
     * ������
     */
    private String name;

    /**
     * �ļ�File
     */
    private File value;

    /**
     * ���캯��
     *
     * @param name  ������
     * @param value �ļ�File
     */
    public HttpFileEntity(String name, File value) {
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
     * ��ȡ�ļ�File
     *
     * @return �ļ�File
     */
    public File getValue() {
        return value;
    }

    /**
     * �����ļ�File
     *
     * @param value �ļ�File
     */
    public void setValue(File value) {
        this.value = value;
    }

}
