package com.asen.android.lib.base.core.gps.geocode;

import com.asen.android.lib.base.core.gps.listener.OnAddressChangedListener;

import java.util.List;

/**
 * Simple to Introduction
 * 逆地理编码抽象类 定义
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public abstract class GeocodeReverse implements IGeocodeReverse, IGeocodeReverseOpen {

    protected List<OnAddressChangedListener> mAddressChangedListeners = null;

    /**
     * 设置地址监听集合
     *
     * @param listeners 地址监听集合
     */
    public void setAddressChangedListeners(List<OnAddressChangedListener> listeners) {
        mAddressChangedListeners = listeners;
    }

}