package com.asen.android.lib.base.core.network.urlconn.bean;

import java.io.File;

/**
 * 键值对上传文件的信息
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class HttpFileEntity {

    /**
     * 键名称
     */
    private String name;

    /**
     * 文件File
     */
    private File value;

    /**
     * 构造函数
     *
     * @param name  键名称
     * @param value 文件File
     */
    public HttpFileEntity(String name, File value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * 获取键名称
     *
     * @return 键名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置键名称
     *
     * @param name 键名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取文件File
     *
     * @return 文件File
     */
    public File getValue() {
        return value;
    }

    /**
     * 设置文件File
     *
     * @param value 文件File
     */
    public void setValue(File value) {
        this.value = value;
    }

}
