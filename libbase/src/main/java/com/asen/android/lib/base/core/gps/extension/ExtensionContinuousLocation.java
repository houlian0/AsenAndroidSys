package com.asen.android.lib.base.core.gps.extension;

import android.location.Location;

/**
 * Simple to Introduction
 * 扩展的持续定位，持续获得定位信息，并主动调用定位主体中的定位方法
 * 定位主体在调用start后，当前类中只要坐标信息改变，就调用{@link IExtensionLocation#refreshLocation(Location)}方法，进行刷新定位信息
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public abstract class ExtensionContinuousLocation extends BaseExtensionLocation {

    @Override
    public final Location getLocation() {
        return null;
    }

}