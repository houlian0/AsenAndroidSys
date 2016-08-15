package com.asen.android.lib.base.core.gps.listener;

import com.asen.android.lib.base.core.gps.bean.LocationInfo;

/**
 * ��ַ�ı�ʱ ����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:11
 */
public interface OnAddressChangedListener {

    /**
     * ��ַ�ı�ʱ����
     *
     * @param locationInfo ��ַ��Ϣ
     */
    void addressChanged(LocationInfo locationInfo);

}