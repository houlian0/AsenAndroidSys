package com.asen.android.lib.base.core.gps.extension;

import android.location.Location;

/**
 * Simple to Introduction
 * 扩展的持续定位，持续获得定位信息，并主动调用定位主体中的定位方法
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public abstract class ExtensionContinuousLocation extends BaseExtensionLocation {

    @Override
    public final Location getLocation() {
        return null;
    }

}