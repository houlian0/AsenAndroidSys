package com.asen.android.lib.base.core.network.urlconn.bean;

/**
 * 键值对上传参数的信息
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class HttpTextEntity {

    /**
     * 键名称
     */
    private String name;

    /**
     * 值内容
     */
    private String value;

    /**
     * 构造函数
     *
     * @param name  键名称
     * @param value 值内容
     */
    public HttpTextEntity(String name, String value) {
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
     * 获取值内容
     *
     * @return 值内容
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置值内容
     *
     * @param value 值内容
     */
    public void setValue(String value) {
        this.value = value;
    }

}
