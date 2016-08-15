package com.asen.android.lib.base.core.gps.extension;

import android.location.Location;

/**
 * ��չ��λ�ӿ�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public interface IExtensionLocation {

    /**
     * ��ʼ��λ
     */
    void start();

    /**
     * ������λ
     */
    void stop();

    /**
     * ��ö�λ��Ϣ
     *
     * @return ��λ��Ϣ
     */
    Location getLocation();

    /**
     * ˢ�¶�λ��Ϣ
     */
    void refreshLocation(Location location);

}