package com.asen.android.lib.base.core.gps.extension;

import android.location.Location;

/**
 * Simple to Introduction
 * ��չ�ĳ�����λ��������ö�λ��Ϣ�����������ö�λ�����еĶ�λ����
 * ��λ�����ڵ���start�󣬵�ǰ����ֻҪ������Ϣ�ı䣬�͵���{@link IExtensionLocation#refreshLocation(Location)}����������ˢ�¶�λ��Ϣ
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