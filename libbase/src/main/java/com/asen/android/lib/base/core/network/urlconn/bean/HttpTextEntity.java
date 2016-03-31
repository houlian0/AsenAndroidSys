package com.asen.android.lib.base.core.network.urlconn.bean;

/**
 * Simple to Introduction
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class HttpTextEntity {

    private String name;

    private String value;

    public HttpTextEntity(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
