package com.asen.android.lib.base.core.gps.listener;

import com.asen.android.lib.base.core.gps.bean.LocationInfo;

/**
 * Simple to Introduction
 * 地址改变时 监听
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public interface OnAddressChangedListener {

    /**
     * 地址改变时触发
     *
     * @param locationInfo 地址信息
     */
    void addressChanged(LocationInfo locationInfo);

}